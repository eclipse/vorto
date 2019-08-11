/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.jcr.AccessDeniedException;
import javax.jcr.Binary;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.refactor.ChangeSet;
import org.eclipse.vorto.model.refactor.RefactoringTask;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.AttachmentException;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.Tag;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.eclipse.vorto.repository.utils.ModelUtils;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import com.google.common.collect.Lists;

public class ModelRepository extends AbstractRepositoryOperation
    implements IModelRepository, ApplicationEventPublisherAware {

  private static final String VORTO_VISIBILITY = "vorto:visibility";

  private static final String VORTO_TAGS = "vorto:tags";

  private static final String VORTO_REFERENCES = "vorto:references";

  private static final String VORTO_META = "vorto:meta";

  private static final String VORTO_AUTHOR = "vorto:author";

  private static final String VORTO_STATE = "vorto:state";

  private static final String VORTO_DISPLAYNAME = "vorto:displayname";

  private static final String VORTO_NODE_TYPE = "vorto:type";

  private static final String VORTO_DESCRIPTION = "vorto:description";

  private static final String JCR_LAST_MODIFIED_BY = "jcr:lastModifiedBy";

  private static final String JCR_LAST_MODIFIED = "jcr:lastModified";

  private static final String JCR_CREATED = "jcr:created";

  private static final String JCR_DATA = "jcr:data";

  private static final String JCR_CONTENT = "jcr:content";

  private static final String MIX_LAST_MODIFIED = "mix:lastModified";

  private static final String MIX_REFERENCEABLE = "mix:referenceable";

  private static final String NT_FOLDER = "nt:folder";

  private static final String NT_FILE = "nt:file";

  private static final String NT_RESOURCE = "nt:resource";

  private static final String MODE_ACCESS_CONTROLLABLE = "mode:accessControllable";

  private static final String ATTACHMENTS_NODE = "attachments";

  private static Logger logger = Logger.getLogger(ModelRepository.class);

  private IModelRetrievalService modelRetrievalService;

  private ModelSearchUtil modelSearchUtil;

  private AttachmentValidator attachmentValidator;

  private ModelParserFactory modelParserFactory;

  private ApplicationEventPublisher eventPublisher = null;

  private ModelRepositoryFactory repositoryFactory;

  private ITenantService tenantService;

  private IModelPolicyManager policyManager;

  public ModelRepository(ModelSearchUtil modelSearchUtil, AttachmentValidator attachmentValidator,
      ModelParserFactory modelParserFactory, IModelRetrievalService modelRetrievalService,
      ModelRepositoryFactory repositoryFactory, ITenantService tenantService,
      IModelPolicyManager policyManager) {
    this.modelSearchUtil = modelSearchUtil;
    this.attachmentValidator = attachmentValidator;
    this.modelParserFactory = modelParserFactory;
    this.modelRetrievalService = modelRetrievalService;
    this.repositoryFactory = repositoryFactory;
    this.tenantService = tenantService;
    this.policyManager = policyManager;
  }

  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  @Override
  public List<ModelInfo> search(final String expression) {
    return doInSession(session -> {
      String queryExpression = expression;
      if (queryExpression == null || queryExpression.isEmpty()) {
        queryExpression = "*";
      }

      List<ModelInfo> modelResources = new ArrayList<>();
      Query query = modelSearchUtil.createQueryFromExpression(session, queryExpression);

      logger.debug("Searching repository with expression " + query.getStatement());
      QueryResult result = query.execute();
      RowIterator rowIterator = result.getRows();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.nextRow();
        Node currentNode = row.getNode();
        if (currentNode.hasProperty(VORTO_NODE_TYPE)) {
          try {
            modelResources.add(createMinimalModelInfo(currentNode));
          } catch (Exception ex) {
            logger.debug("Error while converting node to a ModelInfo", ex);
          }
        }
      }

      return modelResources;
    });
  }

  private ModelInfo createMinimalModelInfo(Node fileNode) throws RepositoryException {
    Node folderNode = fileNode.getParent();
    ModelInfo resource = new ModelInfo(ModelIdHelper.fromPath(folderNode.getPath()),
        fileNode.getProperty(VORTO_NODE_TYPE).getString());
    resource.setDescription(fileNode.getProperty(VORTO_DESCRIPTION).getString());
    resource.setDisplayName(fileNode.getProperty(VORTO_DISPLAYNAME).getString());
    resource.setCreationDate(fileNode.getProperty(JCR_CREATED).getDate().getTime());
    if (fileNode.hasProperty(JCR_LAST_MODIFIED)) {
      resource.setModificationDate(fileNode.getProperty(JCR_LAST_MODIFIED).getDate().getTime());
    }
    if (fileNode.hasProperty(JCR_LAST_MODIFIED_BY)) {
      resource.setLastModifiedBy(fileNode.getProperty(JCR_LAST_MODIFIED_BY).getString());
    }
    if (fileNode.hasProperty(VORTO_STATE)) {
      resource.setState(fileNode.getProperty(VORTO_STATE).getString());
    }
    if (fileNode.hasProperty(VORTO_AUTHOR)) {
      resource.setAuthor(fileNode.getProperty(VORTO_AUTHOR).getString());
    }
    if (fileNode.hasProperty(VORTO_VISIBILITY)) {
      resource.setVisibility(fileNode.getProperty(VORTO_VISIBILITY).getString());
    } else {
      resource.setVisibility(VISIBILITY_PRIVATE);
    }

    if (resource.getType() == ModelType.InformationModel) {
      resource
          .setHasImage(!this.getAttachmentsByTag(resource.getId(), Attachment.TAG_IMAGE).isEmpty());
    }

    return resource;
  }

  @Override
  public ModelFileContent getModelContent(ModelId modelId, boolean validate) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node folderNode = session.getNode(modelIdHelper.getFullPath());
        Node fileNode = (Node) folderNode.getNodes(FILE_NODES).next();
        Node fileItem = (Node) fileNode.getPrimaryItem();
        InputStream is = fileItem.getProperty(JCR_DATA).getBinary().getStream();

        final String fileContent = IOUtils.toString(is);

        IModelParser parser = modelParserFactory.getParser(fileNode.getName());
        parser.setValidate(validate);

        ModelResource resource = (ModelResource) parser.parse(IOUtils.toInputStream(fileContent));
        return new ModelFileContent(resource.getModel(), fileNode.getName(),
            fileContent.getBytes());
      } catch (IOException e) {
        throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
      }
    });
  }

  private Node createNodeForModelId(Session session, ModelId id) throws RepositoryException {
    ModelIdHelper modelIdHelper = new ModelIdHelper(id);
    StringBuilder pathBuilder = new StringBuilder();
    Iterator<String> modelIdIterator = modelIdHelper.iterator();
    Node rootNode = session.getRootNode();
    while (modelIdIterator.hasNext()) {
      String nextPathFragment = modelIdIterator.next();
      pathBuilder.append(nextPathFragment).append("/");
      try {
        rootNode.getNode(pathBuilder.toString());
      } catch (PathNotFoundException pathNotFound) {
        Node addedNode = rootNode.addNode(pathBuilder.toString(), NT_FOLDER);
        addedNode.setPrimaryType(NT_FOLDER);
      }
    }

    return rootNode.getNode(modelIdHelper.getFullPath().substring(1));
  }

  public ModelInfo save(ModelId modelId, byte[] content, String fileName, IUserContext userContext,
      boolean validate) {
    Objects.requireNonNull(content);
    Objects.requireNonNull(modelId);

    IModelParser parser =
        modelParserFactory.getParser("model" + ModelType.fromFileName(fileName).getExtension());
    parser.setValidate(validate);

    ModelResource modelInfo = (ModelResource) parser.parse(new ByteArrayInputStream(content));

    save(modelInfo, userContext);

    return modelInfo;
  }

  public ModelInfo save(ModelId modelId, byte[] content, String fileName,
      IUserContext userContext) {
    return save(modelId, content, fileName, userContext, true);
  }

  public ModelInfo save(final ModelResource modelInfo, IUserContext userContext) {

    return doInSession(jcrSession -> {
      org.modeshape.jcr.api.Session session = (org.modeshape.jcr.api.Session) jcrSession;

      logger.info("Saving " + modelInfo.toString() + " as " + modelInfo.getFileName()
          + " in Workspace/Tenant: " + session.getWorkspace().getName() + "/"
          + userContext.getTenant());

      try {
        Node folderNode = createNodeForModelId(session, modelInfo.getId());
        folderNode.addMixin(MIX_REFERENCEABLE);
        folderNode.addMixin(VORTO_META);
        folderNode.addMixin(MIX_LAST_MODIFIED);

        NodeIterator nodeIt = folderNode.getNodes(FILE_NODES);
        if (!nodeIt.hasNext()) { // new node
          Node fileNode = folderNode.addNode(modelInfo.getFileName(), NT_FILE);
          fileNode.addMixin(VORTO_META);
          fileNode.setProperty(VORTO_AUTHOR, userContext.getUsername());
          fileNode.setProperty(VORTO_VISIBILITY, VISIBILITY_PRIVATE);
          fileNode.addMixin(MODE_ACCESS_CONTROLLABLE);
          folderNode.addMixin(MIX_LAST_MODIFIED);
          Node contentNode = fileNode.addNode(JCR_CONTENT, NT_RESOURCE);
          Binary binary =
              session.getValueFactory().createBinary(new ByteArrayInputStream(modelInfo.toDSL()));
          Property input = contentNode.setProperty(JCR_DATA, binary);
          boolean success = session.sequence("Vorto Sequencer", input, fileNode);
          if (!success) {
            throw new FatalModelRepositoryException(
                "Problem indexing new node for search" + modelInfo.getId(), null);
          }
        } else { // node already exists, so just update it.
          Node fileNode = nodeIt.nextNode();
          fileNode.addMixin(VORTO_META);
          fileNode.addMixin(MIX_LAST_MODIFIED);
          Node contentNode = fileNode.getNode(JCR_CONTENT);
          Binary binary =
              session.getValueFactory().createBinary(new ByteArrayInputStream(modelInfo.toDSL()));
          Property input = contentNode.setProperty(JCR_DATA, binary);
          boolean success = session.sequence("Vorto Sequencer", input, fileNode);
          if (!success) {
            throw new FatalModelRepositoryException(
                "Problem indexing new node for search" + modelInfo.getId(), null);
          }
        }

        session.save();
        logger.info("Model was saved successful");

        ModelInfo createdModel = getById(modelInfo.getId());

        eventPublisher
            .publishEvent(new AppEvent(this, createdModel, userContext, EventType.MODEL_CREATED));

        return createdModel;
      } catch (Exception e) {
        logger.error("Error checking in model", e);
        throw new FatalModelRepositoryException("Problem saving model " + modelInfo.getId(), e);
      }
    });
  }

  @Override
  public ModelInfo getById(ModelId modelId) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        Node folderNode = session.getNode(modelIdHelper.getFullPath());

        if (!folderNode.getNodes(FILE_NODES).hasNext()) {
          throw new NotAuthorizedException(modelId, null);
        }

        Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

        ModelInfo modelResource = createModelResource(fileNode);

        if (!getAttachmentsByTag(modelId, Attachment.TAG_IMAGE).isEmpty()) {
          modelResource.setHasImage(true);
        }

        if (!getAttachmentsByTag(modelId, Attachment.TAG_IMPORTED).isEmpty()) {
          modelResource.setImported(true);
        }

        return modelResource;
      } catch (PathNotFoundException e) {
        return null;
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  private ModelInfo createModelResource(Node fileNode) throws RepositoryException {
    final Node folderNode = fileNode.getParent();

    ModelInfo resource = createMinimalModelInfo(fileNode);
    resource.setFileName(fileNode.getName());

    if (folderNode.hasProperty(VORTO_REFERENCES)) {
      Value[] referenceValues = null;
      try {
        referenceValues = folderNode.getProperty(VORTO_REFERENCES).getValues();
      } catch (Exception ex) {
        referenceValues = new Value[] {folderNode.getProperty(VORTO_REFERENCES).getValue()};
      }

      if (referenceValues != null) {
        ModelReferencesHelper referenceHelper = new ModelReferencesHelper();
        for (Value referValue : referenceValues) {
          referenceHelper.addModelReference(referValue.getString());
        }
        resource.setReferences(referenceHelper.getReferences());
      }
    }

    // only add platform mapping info for non-mapping models
    if (resource.getType() != ModelType.Mapping) {
      Map<String, List<ModelInfo>> referencingModels =
          modelRetrievalService.getModelsReferencing(resource.getId());

      for (Map.Entry<String, List<ModelInfo>> entry : referencingModels.entrySet()) {
        for (ModelInfo modelInfo : entry.getValue()) {
          resource.getReferencedBy().add(modelInfo.getId());
          if (modelInfo.getType() == ModelType.Mapping) {

            try {
              ModelResource emfResource = this.repositoryFactory.getRepositoryByModel(modelInfo.getId()).getEMFResource(modelInfo.getId());
              if (emfResource != null) {
                resource.addPlatformMapping(emfResource.getTargetPlatform(),
                    modelInfo.getId());
              }
            } catch (ValidationException e) {
              logger.warn("Stored Vorto Model is corrupt: " + modelInfo.getId().getPrettyFormat(),
                  e);
            } catch (Exception e) {
              logger.warn("Error while getting a platform mapping", e);
            }
          }
        }
      }
    }
    return resource;
  }


  private ModelInfo getBasicInfo(ModelId modelId) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        Node folderNode = session.getNode(modelIdHelper.getFullPath());

        Node modelFileNode = folderNode.getNodes(FILE_NODES).nextNode();
        return createMinimalModelInfo(modelFileNode);
      } catch (PathNotFoundException e) {
        return null;
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @Override
  public List<ModelInfo> getModelsReferencing(ModelId modelId) {
    return doInSession(session -> {
      List<ModelInfo> referencingModels = Lists.newArrayList();
      QueryManager queryManager = session.getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(
          "SELECT * FROM [vorto:meta] WHERE [vorto:references] = '" + modelId.toString() + "'",
          Query.JCR_SQL2);

      QueryResult result = query.execute();
      RowIterator rowIterator = result.getRows();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.nextRow();
        Node currentNode = row.getNode();
        try {
          referencingModels
              .add(createMinimalModelInfo(currentNode.getNodes(FILE_NODES).nextNode()));
        } catch (Exception ex) {
          logger.error("Error while converting node to a ModelId", ex);
        }
      }

      return referencingModels;
    });
  }

  @Override
  public List<ModelInfo> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform,
      Optional<String> version) {
    logger.info("Fetching mapping models for model ID " + modelId.getPrettyFormat() + " and key "
        + targetPlatform);
    List<ModelInfo> mappingResources = new ArrayList<>();
    ModelInfo modelResource = getById(modelId);
    if (modelResource != null) {
      for (ModelId referenceeModelId : modelResource.getReferencedBy()) {
        ModelResource referenceeModelResource = this.repositoryFactory
            .getRepositoryByModel(referenceeModelId).getEMFResource(referenceeModelId);
        if (referenceeModelResource.getType() == ModelType.Mapping
            && isTargetPlatformMapping(referenceeModelResource, targetPlatform)) {
          if (version.isPresent()
              && !referenceeModelResource.getId().getVersion().equals(version.get())) {
            continue;
          }
          mappingResources.add(referenceeModelResource);
        }
      }
      for (ModelId referencedModelId : modelResource.getReferences()) {

        mappingResources.addAll(this.repositoryFactory.getRepositoryByModel(referencedModelId)
            .getMappingModelsForTargetPlatform(referencedModelId, targetPlatform, version));
      }
    }
    return mappingResources;
  }

  private boolean isTargetPlatformMapping(ModelResource emfResource, String targetPlatform) {
    try {
      return emfResource.matchesTargetPlatform(targetPlatform);
    } catch (FatalModelRepositoryException e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
  }

  @Override
  public ModelResource getEMFResource(ModelId modelId) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node folderNode = session.getNode(modelIdHelper.getFullPath());
        if (!folderNode.getNodes(FILE_NODES).hasNext()) {
          logger.warn("Folder Node :" + folderNode
              + " does not have any files as children. Cannot load EMF Model.");
          return null;
        }
        Node fileNode = (Node) folderNode.getNodes(FILE_NODES).next();
        Node fileItem = (Node) fileNode.getPrimaryItem();
        InputStream is = fileItem.getProperty(JCR_DATA).getBinary().getStream();
        return (ModelResource) modelParserFactory.getParser(fileNode.getName()).parse(is);
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @Override
  public void removeModel(ModelId modelId) {
    doInSession(session -> {
      try {
        ModelInfo modelResource = this.getById(modelId);
        if (modelResource == null) {
          throw new ModelNotFoundException("Cannot find '" + modelId.getPrettyFormat() + "' in '"
              + session.getWorkspace().getName() + "'");
        }

        if (modelResource.getReferencedBy() != null && !modelResource.getReferencedBy().isEmpty()) {
          throw new ModelReferentialIntegrityException(
              "Cannot remove model because it is referenced by other model(s)",
              modelResource.getReferencedBy());
        }
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Item item = session.getItem(modelIdHelper.getFullPath());
        item.remove();
        session.save();

        eventPublisher.publishEvent(new AppEvent(this, modelId, null, EventType.MODEL_DELETED));

        return null;
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @Override
  public ModelInfo updateMeta(ModelInfo model) {
    updateProperty(model.getId(), fileNode -> {
      fileNode.setProperty(VORTO_AUTHOR, model.getAuthor());
      fileNode.setProperty(VORTO_STATE, model.getState());
    });

    return model;
  }

  @Override
  public ModelId updateState(ModelId modelId, String state) {
    return updateProperty(modelId, node -> node.setProperty(VORTO_STATE, state));
  }

  @Override
  public ModelId updateVisibility(ModelId modelId, String visibility) {
    return updateProperty(modelId, node -> node.setProperty(VORTO_VISIBILITY, visibility));
  }

  private ModelId updateProperty(ModelId modelId, NodeConsumer nodeConsumer) {
    return doInSession(session -> {
      try {
        Node folderNode = createNodeForModelId(session, modelId);
        Node fileNode =
            folderNode.getNodes(FILE_NODES).hasNext() ? folderNode.getNodes(FILE_NODES).nextNode()
                : null;
        fileNode.addMixin(MIX_LAST_MODIFIED);
        nodeConsumer.accept(fileNode);
        session.save();

        eventPublisher
            .publishEvent(new AppEvent(this, getBasicInfo(modelId), null, EventType.MODEL_UPDATED));

        return modelId;
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @FunctionalInterface
  private interface NodeConsumer {
    void accept(Node node) throws RepositoryException;
  }

  public ModelSearchUtil getModelSearchUtil() {
    return modelSearchUtil;
  }

  public void setModelSearchUtil(ModelSearchUtil modelSearchUtil) {
    this.modelSearchUtil = modelSearchUtil;
  }

  @Override
  public void addFileContent(ModelId modelId, FileContent fileContent) {
    doInSession(session -> {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      Node contentNode = null;

      if (folderNode.hasNode(fileContent.getFileName())) {
        Node fileNode = (Node) folderNode.getNode(fileContent.getFileName());
        contentNode = (Node) fileNode.getPrimaryItem();
      } else {
        Node fileNode = folderNode.addNode(fileContent.getFileName(), NT_FILE);
        contentNode = fileNode.addNode(JCR_CONTENT, NT_RESOURCE);
      }

      Binary binary = session.getValueFactory()
          .createBinary(new ByteArrayInputStream(fileContent.getContent()));
      contentNode.setProperty(JCR_DATA, binary);
      session.save();

      return null;
    });
  }

  @Override
  public Optional<FileContent> getFileContent(ModelId modelId, Optional<String> fileName) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        Node folderNode = session.getNode(modelIdHelper.getFullPath());

        Node fileNode;
        if (fileName.isPresent()) {
          fileNode = (Node) folderNode.getNode(fileName.get());
        } else {
          if (!folderNode.getNodes(FILE_NODES).hasNext()) {
            throw new NotAuthorizedException(modelId);
          }
          fileNode = (Node) folderNode.getNodes(FILE_NODES).next();
        }

        Node fileItem = (Node) fileNode.getPrimaryItem();
        InputStream is = fileItem.getProperty(JCR_DATA).getBinary().getStream();

        final String fileContent = IOUtils.toString(is);
        return Optional.of(new FileContent(fileNode.getName(), fileContent.getBytes()));

      } catch (PathNotFoundException e) {
        return Optional.empty();
      } catch (IOException e) {
        throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @Override
  public void attachFile(ModelId modelId, FileContent fileContent, IUserContext userContext,
      Tag... tags) throws AttachmentException {

    if (Arrays.asList(tags).stream().filter(tag -> tag.equals(Attachment.TAG_IMPORTED))
        .collect(Collectors.toList()).isEmpty()) {
      attachmentValidator.validateAttachment(fileContent, modelId);
    }

    doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());

        Node attachmentFolderNode = null;
        if (!modelFolderNode.hasNode(ATTACHMENTS_NODE)) {
          attachmentFolderNode = modelFolderNode.addNode(ATTACHMENTS_NODE, NT_FOLDER);
        } else {
          attachmentFolderNode = modelFolderNode.getNode(ATTACHMENTS_NODE);
        }

        String[] tagIds = Arrays.asList(tags).stream().map(t -> t.getId())
            .collect(Collectors.toList()).toArray(new String[tags.length]);

        Node contentNode = null;
        if (attachmentFolderNode.hasNode(fileContent.getFileName())) {
          Node attachmentNode = (Node) attachmentFolderNode.getNode(fileContent.getFileName());
          attachmentNode.addMixin(VORTO_META);
          attachmentNode.setProperty(VORTO_TAGS, tagIds, PropertyType.STRING);
          contentNode = (Node) attachmentNode.getPrimaryItem();
        } else {
          Node attachmentNode = attachmentFolderNode.addNode(fileContent.getFileName(), NT_FILE);
          attachmentNode.addMixin(VORTO_META);
          attachmentNode.setProperty(VORTO_TAGS, tagIds, PropertyType.STRING);
          contentNode = attachmentNode.addNode(JCR_CONTENT, NT_RESOURCE);
        }

        Binary binary = session.getValueFactory()
            .createBinary(new ByteArrayInputStream(fileContent.getContent()));
        contentNode.setProperty(JCR_DATA, binary);
        session.save();

        eventPublisher.publishEvent(
            new AppEvent(this, getById(modelId), userContext, EventType.MODEL_UPDATED));
        return null;
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @Override
  public List<Attachment> getAttachments(ModelId modelId) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());

        if (modelFolderNode.hasNode(ATTACHMENTS_NODE)) {
          Node attachmentFolderNode = modelFolderNode.getNode(ATTACHMENTS_NODE);
          List<Attachment> attachments = new ArrayList<Attachment>();
          NodeIterator nodeIt = attachmentFolderNode.getNodes();
          while (nodeIt.hasNext()) {
            Node fileNode = (Node) nodeIt.next();
            Attachment attachment = Attachment.newInstance(modelId, fileNode.getName());
            if (fileNode.hasProperty(VORTO_TAGS)) {
              final List<Value> tags = Arrays.asList(fileNode.getProperty(VORTO_TAGS).getValues());
              attachment.setTags(
                  tags.stream().map(value -> createTag(value)).collect(Collectors.toList()));
            }
            attachments.add(attachment);
          }
          return attachments;
        }
        return Collections.emptyList();
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  private Tag createTag(Value tagValue) {
    try {
      if (tagValue.getString().equals(Attachment.TAG_DOCUMENTATION.getId())) {
        return Attachment.TAG_DOCUMENTATION;
      } else if (tagValue.getString().equals(Attachment.TAG_IMAGE.getId())) {
        return Attachment.TAG_IMAGE;
      } else if (tagValue.getString().equals(Attachment.TAG_IMPORTED.getId())) {
        return Attachment.TAG_IMPORTED;
      } else {
        return null;
      }
    } catch (RepositoryException ex) {
      return null;
    }
  }

  @Override
  public List<Attachment> getAttachmentsByTag(final ModelId modelId, final Tag tag) {
    return getAttachments(modelId).stream().filter(attachment -> attachment.getTags().contains(tag))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<FileContent> getAttachmentContent(ModelId modelId, String fileName) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());

        if (modelFolderNode.hasNode(ATTACHMENTS_NODE)) {
          Node attachmentFolderNode = modelFolderNode.getNode(ATTACHMENTS_NODE);
          if (attachmentFolderNode.hasNode(fileName)) {
            Node attachment = (Node) attachmentFolderNode.getNode(fileName).getPrimaryItem();
            return Optional.of(new FileContent(fileName,
                IOUtils.toByteArray(attachment.getProperty(JCR_DATA).getBinary().getStream())));
          }
        }
        return Optional.empty();
      } catch (PathNotFoundException e) {
        return Optional.empty();
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId);
      } catch (IOException | RepositoryException e) {
        throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
      }
    });
  }

  public boolean deleteAttachment(ModelId modelId, String fileName) {
    if (getAttachments(modelId).stream()
        .anyMatch(attachment -> (attachment.getTags().contains(Attachment.TAG_IMPORTED)
            && attachment.getFilename().equals(fileName)))) {
      return false;
    }

    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());

        if (modelFolderNode.hasNode(ATTACHMENTS_NODE)) {
          Node attachmentFolderNode = modelFolderNode.getNode(ATTACHMENTS_NODE);
          if (attachmentFolderNode.hasNode(fileName)) {
            Node attachmentNode = attachmentFolderNode.getNode(fileName);
            attachmentNode.remove();
            session.save();
            return true;
          }
        }
        return false;
      } catch (PathNotFoundException e) {
        return false;
      }
    });
  }

  public ModelResource createVersion(ModelId existingId, String newVersion, IUserContext user) {
    ModelInfo existingModel = this.getById(existingId);
    if (existingModel == null) {
      throw new ModelNotFoundException("Model could not be found");

    } else if (existingId.getVersion().equals(newVersion)) {
      throw new ModelAlreadyExistsException();
    } else {
      ModelId newModelId = ModelId.newVersion(existingId, newVersion);
      if (this.exists(newModelId)) {
        throw new ModelAlreadyExistsException();
      }

      ModelFileContent existingModelContent = this.getModelContent(existingId, false);
      Model model = existingModelContent.getModel();
      model.setVersion(newVersion);
      ModelResource resource = new ModelResource(model);
      try {
        this.save(newModelId, resource.toDSL(),
            existingId.getName() + existingModel.getType().getExtension(), user);
      } catch (Exception e) {
        throw new FatalModelRepositoryException(e.getMessage(), e);
      }

      return resource;
    }
  }

  public void setModelParserFactory(ModelParserFactory modelParserFactory) {
    this.modelParserFactory = modelParserFactory;
  }

  @Override
  public boolean exists(ModelId modelId) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        return session.itemExists(modelIdHelper.getFullPath());
      } catch (AccessDeniedException e) {
        return true;
      }
    });
  }

  @Override
  public String getTenantId() {
    return doInSession(session -> {
      return session.getWorkspace().getName();
    });
  }

  @Override
  public ModelInfo rename(ModelId oldModelId, ModelId newModelId, IUserContext user) {
    final Tenant tenant = this.tenantService.getTenant(getTenantId()).get();

    if (getById(newModelId) != null) {
      throw new ModelAlreadyExistsException();
    } else if (!newModelId.getNamespace().startsWith(tenant.getDefaultNamespace())) {
      throw new NewNamespacesNotSupersetException();
    }

    ModelInfo oldModel = getById(oldModelId);

    ChangeSet changeSet = refactorModelWithNewId(oldModel, newModelId);

    saveChangeSetIntoRepository(changeSet, user);

    ModelInfo newModel = getById(newModelId);

    newModel = copy(oldModel, newModel, user);

    removeModel(oldModel.getId());

    return newModel;

  }

  /**
   * copies merely attachments and policies and workflow meta data from the source model to the target model
   * 
   * @param sourceModel source model to copy
   * @param targetModel target model to add data from source model
   * @param user
   * @return new model with all new meta data
   */
  private ModelInfo copy(ModelInfo sourceModel, ModelInfo targetModel, IUserContext user) {
    updateState(targetModel.getId(), sourceModel.getState());
    targetModel.setState(sourceModel.getState());

    // Copy all attachments over to new node
    this.getAttachments(sourceModel.getId()).forEach(oldAttachment -> {
      Optional<FileContent> fileContent =
          this.getAttachmentContent(sourceModel.getId(), oldAttachment.getFilename());
      this.attachFile(targetModel.getId(), fileContent.get(), user,
          oldAttachment.getTags().toArray(new Tag[oldAttachment.getTags().size()]));
    });

    // Copy all policies over to new node
    this.policyManager.copyPolicyEntries(sourceModel.getId(), targetModel.getId());

    return targetModel;
  }

  /**
   * saves the given changeset in a sorted way into the repository in the context of the given user
   * @param changeSet
   * @param user
   */
  private void saveChangeSetIntoRepository(ChangeSet changeSet, IUserContext user) {
    DependencyManager dm = new DependencyManager();

    changeSet.getChanges().stream().forEach(model -> {
      dm.addResource(new ModelResource(model));
    });

    dm.getSorted().forEach(sortedModel -> {
      save((ModelResource) sortedModel, user);
    });

  }

  private ChangeSet refactorModelWithNewId(ModelInfo oldModel, ModelId newModelId) {
    IModelWorkspace workspace = createWorkspaceFromModelAndReferences(oldModel.getId());

    ChangeSet changeSet = RefactoringTask.from(workspace)
        .toModelId(ModelUtils.toEMFModelId(oldModel.getId(), oldModel.getType()),
            ModelUtils.toEMFModelId(newModelId, oldModel.getType()))
        .execute();

    return changeSet;
  }

  private IModelWorkspace createWorkspaceFromModelAndReferences(ModelId modelId) {
    ModelWorkspaceReader reader = IModelWorkspace.newReader();
    ModelResource resource = getEMFResource(modelId);
    reader.addFile(new ByteArrayInputStream(resource.toDSL()), resource.getType());

    getModelsReferencing(modelId).forEach(reference -> {
      ModelResource referencingModel = getEMFResource(reference.getId());
      reader.addFile(new ByteArrayInputStream(referencingModel.toDSL()),
          referencingModel.getType());
    });
    return reader.read();
  }
}
