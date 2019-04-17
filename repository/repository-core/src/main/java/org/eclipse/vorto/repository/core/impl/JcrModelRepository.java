/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.jcr.AccessDeniedException;
import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.AttachmentException;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.Tag;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.core.security.SpringSecurityCredentials;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.modeshape.jcr.security.SimplePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Implementation of the repository using JCR
 * 
 * @author Alexander Edelmann
 *
 */
// @Service("modelRepository")
public class JcrModelRepository implements IModelRepository, IDiagnostics, IModelPolicyManager {

  private static final String FILE_NODES = "*.type | *.fbmodel | *.infomodel | *.mapping ";

  private static Logger logger = Logger.getLogger(JcrModelRepository.class);

  private static final String VORTO_NODE_TYPE = "vorto:type";

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private ModelSearchUtil modelSearchUtil;

  @Autowired
  private AttachmentValidator attachmentValidator;

  @Autowired
  private ModelParserFactory modelParserFactory;

  @Autowired
  private RepositoryDiagnostics repoDiagnostics;

  @Autowired
  private Repository repository;

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
    resource.setDescription(fileNode.getProperty("vorto:description").getString());
    resource.setDisplayName(fileNode.getProperty("vorto:displayname").getString());
    resource.setCreationDate(fileNode.getProperty("jcr:created").getDate().getTime());
    if (fileNode.hasProperty("jcr:lastModified")) {
      resource.setModificationDate(fileNode.getProperty("jcr:lastModified").getDate().getTime());
    }
    if (fileNode.hasProperty("vorto:state")) {
      resource.setState(fileNode.getProperty("vorto:state").getString());
    }
    if (fileNode.hasProperty("vorto:author")) {
      resource.setAuthor(fileNode.getProperty("vorto:author").getString());
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
        InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();

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
        Node addedNode = rootNode.addNode(pathBuilder.toString(), "nt:folder");
        addedNode.setPrimaryType("nt:folder");
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

    logger.info("Saving " + modelId.toString() + " as " + fileName + " in Repository");

    return doInSession(jcrSession -> {
      org.modeshape.jcr.api.Session session = (org.modeshape.jcr.api.Session) jcrSession;
      try {
        Node folderNode = createNodeForModelId(session, modelId);
        folderNode.addMixin("mix:referenceable");
        folderNode.addMixin("vorto:meta");
        folderNode.addMixin("mix:lastModified");

        NodeIterator nodeIt = folderNode.getNodes(FILE_NODES);
        if (!nodeIt.hasNext()) { // new node
          Node fileNode = folderNode.addNode(fileName, "nt:file");
          fileNode.addMixin("vorto:meta");
          fileNode.setProperty("vorto:author", userContext.getUsername());
          fileNode.addMixin("mode:accessControllable");
          folderNode.addMixin("mix:lastModified");
          Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
          Binary binary =
              session.getValueFactory().createBinary(new ByteArrayInputStream(modelInfo.toDSL()));
          Property input = contentNode.setProperty("jcr:data", binary);
          boolean success = session.sequence("Vorto Sequencer", input, fileNode);
          if (!success) {
            throw new FatalModelRepositoryException(
                "Problem indexing new node for search" + modelId, null);
          }
        } else { // node already exists, so just update it.
          Node fileNode = nodeIt.nextNode();
          fileNode.addMixin("vorto:meta");
          fileNode.setProperty("vorto:author", userContext.getUsername());
          fileNode.addMixin("mix:lastModified");
          Node contentNode = fileNode.getNode("jcr:content");
          Binary binary =
              session.getValueFactory().createBinary(new ByteArrayInputStream(modelInfo.toDSL()));
          Property input = contentNode.setProperty("jcr:data", binary);
          boolean success = session.sequence("Vorto Sequencer", input, fileNode);
          if (!success) {
            throw new FatalModelRepositoryException(
                "Problem indexing new node for search" + modelId, null);
          }
        }

        session.save();
        logger.info("Model was saved successful");
        return modelInfo;
      } catch (IOException e) {
        logger.error("Error checking in model", e);
        throw new FatalModelRepositoryException("Problem checking in uploaded model" + modelId, e);
      }
    });
  }

  public ModelInfo save(ModelId modelId, byte[] content, String fileName,
      IUserContext userContext) {
    return save(modelId, content, fileName, userContext, true);
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

        ModelInfo modelResource = createModelResource(session, fileNode);

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

  private ModelInfo createModelResource(Session session, Node fileNode) throws RepositoryException {
    final Node folderNode = fileNode.getParent();

    ModelInfo resource = createMinimalModelInfo(fileNode);
    resource.setFileName(fileNode.getName());

    if (folderNode.hasProperty("vorto:references")) {
      Value[] referenceValues = null;
      try {
        referenceValues = folderNode.getProperty("vorto:references").getValues();
      } catch (Exception ex) {
        referenceValues = new Value[] {folderNode.getProperty("vorto:references").getValue()};
      }

      if (referenceValues != null) {
        ModelReferencesHelper referenceHelper = new ModelReferencesHelper();
        for (Value referValue : referenceValues) {
          String nodeUuid = referValue.getString();
          try {
            Node referencedNode = session.getNodeByIdentifier(nodeUuid);
            ModelId referenceModelId = ModelIdHelper.fromPath(referencedNode.getPath());
            referenceHelper.addModelReference(referenceModelId.getPrettyFormat());
          } catch (ItemNotFoundException itemNotFound) {
            logger.error("Referential Integrity Problem ----->>>>>>> Broken reference of model "
                + resource.getId(), itemNotFound);
          }
        }
        resource.setReferences(referenceHelper.getReferences());
      }
    }

    if (resource.getType() != ModelType.Mapping) { // only add platform
                                                   // mapping info for
                                                   // non-mapping models
      PropertyIterator propIter = folderNode.getReferences();
      while (propIter.hasNext()) {
        Property prop = propIter.nextProperty();
        try {
          Node referencedFolder = prop.getParent();
          final ModelId referencedById = ModelIdHelper.fromPath(referencedFolder.getPath());
          if (hasPermission(referencedById, Permission.READ)) {
            resource.getReferencedBy().add(referencedById);

            if (referencedFolder.getProperty(VORTO_NODE_TYPE).getString()
                .equals(ModelType.Mapping.name())) {
              try {
                ModelResource emfResource = getEMFResource(referencedById);
                if (emfResource != null) {
                  resource.addPlatformMapping(emfResource.getTargetPlatform(), referencedById);
                }
              } catch (ValidationException validationEx) {
                logger.warn("Stored Vorto Model is corrupt: " + referencedById.getPrettyFormat(),
                    validationEx);
              } catch (Exception e) {
                logger.warn("Error while getting a platform mapping", e);
              }
            }
          }
        } catch (Exception e) {
          logger.warn("A reference has gone stale. Please remove this reference. : ", e);
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
        if (currentNode.hasProperty(VORTO_NODE_TYPE)) {
          try {
            referencingModels.add(createMinimalModelInfo(currentNode));
          } catch (Exception ex) {
            logger.debug("Error while converting node to a ModelId", ex);
          }
        }
      }

      return referencingModels;
    });
  }

  @Override
  public List<ModelInfo> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform) {
    logger.info("Fetching mapping models for model ID " + modelId.getPrettyFormat() + " and key "
        + targetPlatform);
    List<ModelInfo> mappingResources = new ArrayList<>();
    ModelInfo modelResource = getById(modelId);
    if (modelResource != null) {
      for (ModelId referenceeModelId : modelResource.getReferencedBy()) {
        ModelInfo referenceeModelResources = getBasicInfo(referenceeModelId);
        if (referenceeModelResources.getType() == ModelType.Mapping
            && isTargetPlatformMapping(referenceeModelResources, targetPlatform)) {
          mappingResources.add(referenceeModelResources);
        }
      }
      for (ModelId referencedModelId : modelResource.getReferences()) {
        mappingResources
            .addAll(getMappingModelsForTargetPlatform(referencedModelId, targetPlatform));
      }
    }
    return mappingResources;
  }

  private boolean isTargetPlatformMapping(ModelInfo model, String targetPlatform) {
    try {
      ModelResource emfResource = getEMFResource(model.getId());
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
        InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();
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
        if (!modelResource.getReferencedBy().isEmpty()) {
          throw new ModelReferentialIntegrityException(
              "Cannot remove model because it is referenced by other model(s)",
              modelResource.getReferencedBy());
        }
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Item item = session.getItem(modelIdHelper.getFullPath());
        item.remove();
        session.save();
        return null;
      } catch (AccessDeniedException e) {
        throw new NotAuthorizedException(modelId, e);
      }
    });
  }

  @Override
  public ModelInfo updateMeta(ModelInfo model) {
    updateProperty(model.getId(), fileNode -> {
      fileNode.setProperty("vorto:author", model.getAuthor());
      fileNode.setProperty("vorto:state", model.getState());
    });

    return model;
  }

  public ModelId updateState(ModelId modelId, String state) {
    return updateProperty(modelId, node -> node.setProperty("vorto:state", state));
  }

  private ModelId updateProperty(ModelId modelId, NodeConsumer nodeConsumer) {
    return doInSession(session -> {
      try {
        Node folderNode = createNodeForModelId(session, modelId);
        Node fileNode =
            folderNode.getNodes(FILE_NODES).hasNext() ? folderNode.getNodes(FILE_NODES).nextNode()
                : null;
        fileNode.addMixin("mix:lastModified");
        nodeConsumer.accept(fileNode);
        session.save();
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

  public IUserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(IUserRepository userRepository) {
    this.userRepository = userRepository;
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
        Node fileNode = folderNode.addNode(fileContent.getFileName(), "nt:file");
        contentNode = fileNode.addNode("jcr:content", "nt:resource");
      }

      Binary binary = session.getValueFactory()
          .createBinary(new ByteArrayInputStream(fileContent.getContent()));
      contentNode.setProperty("jcr:data", binary);
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
        InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();

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
        if (!modelFolderNode.hasNode("attachments")) {
          attachmentFolderNode = modelFolderNode.addNode("attachments", "nt:folder");
        } else {
          attachmentFolderNode = modelFolderNode.getNode("attachments");
        }

        String[] tagIds = Arrays.asList(tags).stream().map(t -> t.getId())
            .collect(Collectors.toList()).toArray(new String[tags.length]);

        Node contentNode = null;
        if (attachmentFolderNode.hasNode(fileContent.getFileName())) {
          Node attachmentNode = (Node) attachmentFolderNode.getNode(fileContent.getFileName());
          attachmentNode.addMixin("vorto:meta");
          attachmentNode.setProperty("vorto:tags", tagIds, PropertyType.STRING);
          contentNode = (Node) attachmentNode.getPrimaryItem();
        } else {
          Node attachmentNode = attachmentFolderNode.addNode(fileContent.getFileName(), "nt:file");
          attachmentNode.addMixin("vorto:meta");
          attachmentNode.setProperty("vorto:tags", tagIds, PropertyType.STRING);
          contentNode = attachmentNode.addNode("jcr:content", "nt:resource");
        }

        Binary binary = session.getValueFactory()
            .createBinary(new ByteArrayInputStream(fileContent.getContent()));
        contentNode.setProperty("jcr:data", binary);
        session.save();

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

        if (modelFolderNode.hasNode("attachments")) {
          Node attachmentFolderNode = modelFolderNode.getNode("attachments");
          List<Attachment> attachments = new ArrayList<Attachment>();
          NodeIterator nodeIt = attachmentFolderNode.getNodes();
          while (nodeIt.hasNext()) {
            Node fileNode = (Node) nodeIt.next();
            Attachment attachment = Attachment.newInstance(modelId, fileNode.getName());
            if (fileNode.hasProperty("vorto:tags")) {
              final List<Value> tags =
                  Arrays.asList(fileNode.getProperty("vorto:tags").getValues());
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

        if (modelFolderNode.hasNode("attachments")) {
          Node attachmentFolderNode = modelFolderNode.getNode("attachments");
          if (attachmentFolderNode.hasNode(fileName)) {
            Node attachment = (Node) attachmentFolderNode.getNode(fileName).getPrimaryItem();
            return Optional.of(new FileContent(fileName,
                IOUtils.toByteArray(attachment.getProperty("jcr:data").getBinary().getStream())));
          }
        }
        return Optional.empty();
      } catch (PathNotFoundException e) {
        return Optional.empty();
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

        if (modelFolderNode.hasNode("attachments")) {
          Node attachmentFolderNode = modelFolderNode.getNode("attachments");
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

    } else if (existingId.getVersion() == newVersion) {
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
      } catch (IOException e) {
        throw new FatalModelRepositoryException(e.getMessage(), e);
      }

      return resource;
    }
  }

  public Collection<Diagnostic> diagnoseAllModels() {
    return doInRootNode(repoDiagnostics::diagnose);
  }

  public Collection<Diagnostic> diagnoseModel(ModelId modelId) {
    return doInSession(session -> {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());
      return repoDiagnostics.diagnose(folderNode);
    });
  }

  private <Result> Result doInRootNode(Function<Node, Result> fn) {
    return doInSession(session -> {
      Node node = session.getRootNode();
      return fn.apply(node);
    });
  }

  public void setModelParserFactory(ModelParserFactory modelParserFactory) {
    this.modelParserFactory = modelParserFactory;
  }

  public void setRepositoryDiagnostics(RepositoryDiagnostics repoDiagnostics) {
    this.repoDiagnostics = repoDiagnostics;
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
  public Collection<PolicyEntry> getPolicyEntries(ModelId modelId) {
    return doInSession(session -> {
      List<PolicyEntry> policyEntries = new ArrayList<PolicyEntry>();

      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        final Node folderNode = session.getNode(modelIdHelper.getFullPath());

        if (!folderNode.getNodes(FILE_NODES).hasNext()) {
          throw new ModelNotFoundException("Could not find model with ID " + modelId);
        }
        Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

        AccessControlManager acm = session.getAccessControlManager();

        AccessControlList acl = null;
        AccessControlPolicyIterator it = acm.getApplicablePolicies(fileNode.getPath());
        if (it.hasNext()) {
          acl = (AccessControlList) it.nextAccessControlPolicy();
        } else {
          acl = (AccessControlList) acm.getPolicies(fileNode.getPath())[0];
        }

        for (AccessControlEntry entry : acl.getAccessControlEntries()) {
          PolicyEntry policy = PolicyEntry.of(entry);
          if (!policy.isAdminPolicy()) {
            policyEntries.add(policy);
          }
        }

        return policyEntries;
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      }
    });
  }

  @Override
  public void addPolicyEntry(ModelId modelId, PolicyEntry... newEntries) {
    doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        final Node folderNode = session.getNode(modelIdHelper.getFullPath());
        if (!folderNode.getNodes(FILE_NODES).hasNext()) {
          logger.warn("Cannot add policy entry to model " + modelId);
          session.logout();
          return null;
        }
        Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

        AccessControlManager acm = session.getAccessControlManager();

        AccessControlList acl = null;
        AccessControlPolicyIterator it = acm.getApplicablePolicies(fileNode.getPath());
        if (it.hasNext()) {
          acl = (AccessControlList) it.nextAccessControlPolicy();
        } else {
          acl = (AccessControlList) acm.getPolicies(fileNode.getPath())[0];
        }

        final AccessControlList _acl = acl;

        List<AccessControlEntry> existingEntries = new ArrayList<>();
        for (AccessControlEntry ace : acl.getAccessControlEntries()) {
          Arrays.asList(newEntries).stream().forEach(entry -> {
            if (entry.isSame(ace)) {
              existingEntries.add(ace);
            }
          });
        }

        if (!existingEntries.isEmpty()) {
          existingEntries.stream().forEach(ace -> {
            try {
              _acl.removeAccessControlEntry(ace);
            } catch (Exception e) {
              logger.error("Could not grant user readd permissions for model", e);
            }
          });
        }

        for (PolicyEntry newEntry : newEntries) {
          String[] privileges = createPrivileges(newEntry);
          Privilege[] permissions = new Privilege[privileges.length];
          for (int i = 0; i < privileges.length; i++) {
            permissions[i] = acm.privilegeFromName(privileges[i]);
          }
          if (privileges.length > 0) {
            _acl.addAccessControlEntry(SimplePrincipal.newInstance(newEntry.toACEPrincipal()),
                permissions);
          }
        }

        acm.setPolicy(fileNode.getPath(), _acl);
        session.save();
        return null;
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      }
    });
  }

  private String[] createPrivileges(PolicyEntry newEntry) {
    Set<String> result = new HashSet<>();
    if (newEntry.getPermission() == Permission.READ) {
      result.add(Privilege.JCR_READ);
      result.add(Privilege.JCR_READ_ACCESS_CONTROL);
    } else if (newEntry.getPermission() == Permission.MODIFY) {
      result.add(Privilege.JCR_READ);
      result.add(Privilege.JCR_READ_ACCESS_CONTROL);
      result.add(Privilege.JCR_WRITE);
    } else if (newEntry.getPermission() == Permission.FULL_ACCESS) {
      result.add(Privilege.JCR_ALL);
    }

    return result.toArray(new String[result.size()]);
  }

  @Override
  public void removePolicyEntry(ModelId modelId, PolicyEntry entryToRemove) {
    entryToRemove.setPermission(null);
    this.addPolicyEntry(modelId, entryToRemove);

    if (this.getPolicyEntries(modelId).isEmpty()) {
      doInSession(session -> {
        try {
          ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

          final Node folderNode = session.getNode(modelIdHelper.getFullPath());
          Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

          AccessControlManager acm = session.getAccessControlManager();

          AccessControlList acl = null;
          AccessControlPolicyIterator it = acm.getApplicablePolicies(fileNode.getPath());
          if (it.hasNext()) {
            acl = (AccessControlList) it.nextAccessControlPolicy();
          } else {
            acl = (AccessControlList) acm.getPolicies(fileNode.getPath())[0];
          }

          acm.removePolicy(fileNode.getPath(), acl);
          session.save();

          return null;
        } catch (AccessDeniedException ex) {
          throw new NotAuthorizedException(modelId);
        }
      });
    }
  }

  @Override
  public boolean hasPermission(final ModelId modelId, final Permission permission) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        Node folderNode = session.getNode(modelIdHelper.getFullPath());

        if (permission == Permission.READ) {
          return folderNode.getNodes(FILE_NODES).hasNext();
        } else {
          return this.getPolicyEntries(modelId).stream()
              .filter(p -> p.getPrincipalId().equalsIgnoreCase(session.getUserID()))
              .filter(p -> hasPermission(p.getPermission(), permission)).findAny().isPresent();
        }
      } catch (AccessDeniedException e) {
        return false;
      }
    });
  }

  private boolean hasPermission(Permission userPermission, Permission permission) {
    return userPermission.includes(permission);
  }

  @Override
  public byte[] backup() {
    return doInSession(session -> {
      try {
        return backupRepository(session);
      } catch (IOException e) {
        logger.error("Exception while making a backup", e);
        throw new FatalModelRepositoryException(
            "Something went wrong while making a backup of the system.", e);
      }
    });
  }

  private byte[] backupRepository(Session session) throws RepositoryException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    session.exportSystemView("/", baos, false, false);
    baos.close();
    return baos.toByteArray();
  }

  @Override
  public void restore(byte[] data) {
    doInSession(session -> {
      byte[] oldData = null;
      try {
        oldData = backupRepository(session);

        logger.info("Attempting to restore backup");
        session.getWorkspace().importXML("/", new ByteArrayInputStream(data),
            ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
        logger.info("Restored backup succesfully");

      } catch (RepositoryException | IOException e) {
        logger.error("Backup failed. Will try to revert the restoration with previous data.", e);
        try {
          logger.info("Reverting to old data.");
          session.getWorkspace().importXML("/", new ByteArrayInputStream(oldData),
              ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
          logger.info("Reverted the restoration succesfully");
        } catch (RepositoryException | IOException ex) {
          logger.error("Revert of restoration unsuccesfull", ex);
        }
        throw e;
      }
      return null;
    });
  }

  public <ReturnType> ReturnType doInSession(SessionFunction<ReturnType> fn) {
    Session session = null;
    try {
      session = repository.login(getCredentialSource(), "playground");
      return fn.apply(session);
    } catch (PathNotFoundException e) {
      logger.error(e);
      throw new ModelNotFoundException(e);
    } catch (RepositoryException ex) {
      logger.error(ex);
      throw new FatalModelRepositoryException("Cannot create repository session for user", ex);
    } catch (Exception ex) {
      logger.error("Unexception exception", ex);
      throw new FatalModelRepositoryException("Unexpected exception while operating on repository.",
          ex);
    } finally {
      if (session != null) {
        session.logout();
      }
    }
  }

  protected Credentials getCredentialSource() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return new SpringSecurityCredentials(authentication, Sets.newHashSet(Role.SYS_ADMIN));
  }

  @FunctionalInterface
  public interface SessionFunction<K> {
    K apply(Session session) throws Exception;
  }

  public void setRepository(Repository repository) {
    this.repository = repository;
  }
}
