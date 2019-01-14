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
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.core.security.SpringSecurityCredentials;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.modeshape.jcr.security.SimplePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the repository using JCR
 * 
 * @author Alexander Edelmann
 *
 */
@Service("modelRepository")
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
    String queryExpression = expression;
    if (queryExpression == null || queryExpression.isEmpty()) {
      queryExpression = "*";
    }
    try {
      List<ModelInfo> modelResources = new ArrayList<>();

      Session session = getSession();

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

      session.logout();
      return modelResources;
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Could not create query manager", e);
    }
  }

  public Session getSession() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      return repository.login(new SpringSecurityCredentials(authentication));
    } catch (RepositoryException ex) {
      throw new FatalModelRepositoryException("Cannot create repository session for user", ex);
    }
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
  public ModelFileContent getModelContent(ModelId modelId) {
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = getSession().getNode(modelIdHelper.getFullPath());
      Node fileNode = (Node) folderNode.getNodes(FILE_NODES).next();
      Node fileItem = (Node) fileNode.getPrimaryItem();
      InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();

      final String fileContent = IOUtils.toString(is);
      ModelResource resource = (ModelResource) modelParserFactory.getParser(fileNode.getName())
          .parse(IOUtils.toInputStream(fileContent));
      return new ModelFileContent(resource.getModel(), fileNode.getName(), fileContent.getBytes());

    } catch (PathNotFoundException e) {
      throw new ModelNotFoundException("Could not find model with the given model id", e);
    } catch (Exception e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
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

  public ModelInfo save(ModelId modelId, byte[] content, String fileName,
      IUserContext userContext) {
    Objects.requireNonNull(content);
    Objects.requireNonNull(modelId);

    ModelResource modelInfo = (ModelResource) modelParserFactory
        .getParser("model" + ModelType.fromFileName(fileName).getExtension())
        .parse(new ByteArrayInputStream(content));

    logger.info("Saving " + modelId.toString() + " as " + fileName + " in Repository");

    org.modeshape.jcr.api.Session session = (org.modeshape.jcr.api.Session) getSession();
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
          throw new FatalModelRepositoryException("Problem indexing new node for search" + modelId,
              null);
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
          throw new FatalModelRepositoryException("Problem indexing new node for search" + modelId,
              null);
        }
      }

      session.save();
      logger.info("Model was saved successful");
      return modelInfo;
    } catch (Exception e) {
      logger.error("Error checking in model", e);
      throw new FatalModelRepositoryException("Problem checking in uploaded model" + modelId, e);
    } finally {
      session.logout();
    }
  }

  @Override
  public ModelInfo getById(ModelId modelId) {
    final Session session = getSession();

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
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
    } finally {
      session.logout();
    }
  }

  private ModelInfo createModelResource(Node fileNode) throws RepositoryException {
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
            Node referencedNode = getSession().getNodeByIdentifier(nodeUuid);
            ModelId referenceModelId = ModelIdHelper.fromPath(referencedNode.getPath());
            if (hasPermission(referenceModelId,Permission.READ)) {
              referenceHelper.addModelReference(referenceModelId.getPrettyFormat());
            }
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
          if (hasPermission(referencedById,Permission.READ)) {
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
    final Session session = getSession();

    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      Node modelFileNode = folderNode.getNodes(FILE_NODES).nextNode();
      return createMinimalModelInfo(modelFileNode);
    } catch (PathNotFoundException e) {
      return null;
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
    } finally {
      session.logout();
    }
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

  public ModelResource getEMFResource(ModelId modelId) {
    final Session session = getSession();

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
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    } finally {
      session.logout();
    }
  }

  @Override
  public void removeModel(ModelId modelId) {
    final Session session = getSession();

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
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new FatalModelRepositoryException("Problem occured removing the model", e);
    } finally {
      session.logout();
    }
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
    final Session session = getSession();

    try {
      Node folderNode = createNodeForModelId(session, modelId);
      Node fileNode = folderNode.getNodes(FILE_NODES).hasNext()
          ? folderNode.getNodes(FILE_NODES).nextNode() : null;
      fileNode.addMixin("mix:lastModified");
      nodeConsumer.accept(fileNode);
      session.save();
      return modelId;
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new FatalModelRepositoryException("Problem occured removing the model", e);
    } finally {
      session.logout();
    }
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
    final Session session = getSession();

    try {
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
    } catch (PathNotFoundException e) {
      throw new ModelNotFoundException("Could not find model with the given model id", e);
    } catch (Exception e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    } finally {
      session.logout();
    }
  }

  @Override
  public Optional<FileContent> getFileContent(ModelId modelId, Optional<String> fileName) {
    final Session session = getSession();

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
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    } finally {
      session.logout();
    }
  }

  @Override
  public void attachFile(ModelId modelId, FileContent fileContent, IUserContext userContext,
      Tag... tags) throws AttachmentException {

    if (Arrays.asList(tags).stream().filter(tag -> tag.equals(Attachment.TAG_IMPORTED))
        .collect(Collectors.toList()).isEmpty()) {
      attachmentValidator.validateAttachment(fileContent, modelId);
    }

    final Session session = getSession();

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
    } catch (PathNotFoundException e) {
      throw new ModelNotFoundException("Model with ID " + modelId + " not found");
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    } finally {
      session.logout();
    }
  }

  @Override
  public List<Attachment> getAttachments(ModelId modelId) {
    final Session session = getSession();

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
            final List<Value> tags = Arrays.asList(fileNode.getProperty("vorto:tags").getValues());
            attachment
                .setTags(tags.stream().map(value -> createTag(value)).collect(Collectors.toList()));
          }
          attachments.add(attachment);
        }
        return attachments;
      }
      return Collections.emptyList();
    } catch (PathNotFoundException e) {
      throw new ModelNotFoundException("Could not find model with specified ID", e);
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        throw new NotAuthorizedException(modelId, e);
      }
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    } finally {
      session.logout();
    }
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
    final Session session = getSession();

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
    } finally {
      session.logout();
    }
  }

  public boolean deleteAttachment(ModelId modelId, String fileName) {
    if (getAttachments(modelId).stream()
        .anyMatch(attachment -> (attachment.getTags().contains(Attachment.TAG_IMPORTED)
            && attachment.getFilename().equals(fileName)))) {
      return false;
    }
    final Session session = getSession();

    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());

      if (modelFolderNode.hasNode("attachments")) {
        Node attachmentFolderNode = modelFolderNode.getNode("attachments");
        if (attachmentFolderNode.hasNode(fileName)) {
          Node attachmentNode = attachmentFolderNode.getNode(fileName);
          attachmentNode.remove();
          session.save();
          session.logout();
          return true;
        }
      }
      session.logout();
      return false;
    } catch (PathNotFoundException e) {
      return false;
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    } finally {
      session.logout();
    }
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

      ModelFileContent existingModelContent = this.getModelContent(existingId);
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
    final Session session = getSession();
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());
      return repoDiagnostics.diagnose(folderNode);
    } catch (RepositoryException ex) {
      throw new FatalModelRepositoryException("Diagnostics failed", ex);
    } finally {
      session.logout();
    }
  }

  private <Result> Result doInRootNode(Function<Node, Result> fn) {
    final Session session = getSession();

    try {
      Node node = session.getRootNode();
      return fn.apply(node);
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException(e.getMessage(), e);
    } finally {
      session.logout();
    }
  }

  public void setModelParserFactory(ModelParserFactory modelParserFactory) {
    this.modelParserFactory = modelParserFactory;
  }

  public void setRepositoryDiagnostics(RepositoryDiagnostics repoDiagnostics) {
    this.repoDiagnostics = repoDiagnostics;
  }

  @Override
  public boolean exists(ModelId modelId) {
    final Session session = getSession();

    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      return session.itemExists(modelIdHelper.getFullPath());
    } catch (RepositoryException e) {
      if (e instanceof AccessDeniedException) {
        return true;
      }
      throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
    } finally {
      session.logout();
    }
  }

  @Override
  public Collection<PolicyEntry> getPolicyEntries(ModelId modelId) {
    List<PolicyEntry> policyEntries = new ArrayList<PolicyEntry>();
    final Session session = getSession();
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      final Node folderNode = session.getNode(modelIdHelper.getFullPath());
      
      if (!folderNode.getNodes(FILE_NODES).hasNext()) {
        throw new ModelNotFoundException("Could not find model with ID "+modelId);
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

    } catch (AccessDeniedException ex) {
      throw new NotAuthorizedException(modelId);
    } catch (RepositoryException ex) {
      logger.error("Could not read policies entries of model", ex);
      throw new FatalModelRepositoryException("Problem reading model policy entries", ex);
    } finally {
      session.logout();
    }

    return policyEntries;
  }

  @Override
  public void addPolicyEntry(ModelId modelId, PolicyEntry... newEntries) {
    final Session session = getSession();
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      final Node folderNode = session.getNode(modelIdHelper.getFullPath());
      if (!folderNode.getNodes(FILE_NODES).hasNext()) {
        logger.warn("Cannot add policy entry to model " + modelId);
        session.logout();
        return;
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
        Arrays.asList(newEntries).stream().forEach(entry -> {if (entry.isSame(ace)) {existingEntries.add(ace);}});
      }

      if (!existingEntries.isEmpty()) {
        existingEntries.stream().forEach(ace -> {try {
          _acl.removeAccessControlEntry(ace);
        } catch (Exception e) {
          logger.error("Could not grant user readd permissions for model", e); 
        }});
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
    } catch (AccessDeniedException ex) {
      throw new NotAuthorizedException(modelId);
    } catch (RepositoryException ex) {
      logger.error("Could not grant user readd permissions for model", ex);
      throw new FatalModelRepositoryException("Problem to grant user read permissions for model",
          ex);
    } finally {
      session.logout();
    }
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
      final Session session = getSession();
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
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      } catch (RepositoryException ex) {
        logger.error("Could not remove policy from model", ex);
        throw new FatalModelRepositoryException("Problem to grant user read permissions for model",
            ex);
      } finally {
        session.logout();
      }
    }
  }

  @Override
  public boolean hasPermission(final ModelId modelId, final Permission permission) {
    Session session = getSession();
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      if (permission == Permission.READ) {
        return folderNode.getNodes(FILE_NODES).hasNext(); 
      } else {
        return this.getPolicyEntries(modelId).stream().filter(p -> p.getPrincipalId().equalsIgnoreCase(session.getUserID())).filter(p -> hasPermission(p.getPermission(),permission)).findAny().isPresent();        
      } 
    } catch (PathNotFoundException e) {
      throw new ModelNotFoundException("Could not find model with given ID", e);
    } catch (AccessDeniedException e) {
      return false;     
    } catch (RepositoryException e) {
      throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
    } finally {
      session.logout();
    }
  }

  private boolean hasPermission(Permission userPermission, Permission permission) {
    return userPermission.includes(permission);
  }
}
