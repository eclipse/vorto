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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.jcr.Binary;
import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
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
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.Tag;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.modeshape.jcr.api.JcrTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the repository using JCR
 * 
 * @author Alexander Edelmann
 *
 */
@Service
public class JcrModelRepository implements IModelRepository, IDiagnostics {

  private static final String FILE_NODES = "*.type | *.fbmodel | *.infomodel | *.mapping ";

  private static Logger logger = Logger.getLogger(JcrModelRepository.class);

  @Autowired
  private Session session;

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
  
  private static final JcrTools jcrTools = new JcrTools();

  @Override
  public List<ModelInfo> search(final String expression) {
    String queryExpression = expression;
    if (queryExpression == null || queryExpression.isEmpty()) {
      queryExpression = "*";
    }
    try {
      List<ModelInfo> modelResources = new ArrayList<>();

      Query query = modelSearchUtil.createQueryFromExpression(session, queryExpression);

      logger.debug("Searching repository with expression " + query.getStatement());
      QueryResult result = query.execute();
      RowIterator rowIterator = result.getRows();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.nextRow();
        Node currentNode = row.getNode();
        try {
          if (currentNode.hasProperty("vorto:type")) {
            modelResources.add(createMinimalModelInfo(currentNode));
          }
        } catch (Exception ex) {
          logger.debug("Error while converting node to a ModelInfo", ex);
        }
      }

      return modelResources;
    } catch (RepositoryException e) {
      throw new RuntimeException("Could not create query manager", e);
    }
  }

  private ModelInfo createMinimalModelInfo(Node node) throws RepositoryException {

    ModelInfo resource = new ModelInfo(ModelIdHelper.fromPath(node.getParent().getPath()),
        node.getProperty("vorto:type").getString());
    resource.setDescription(node.getProperty("vorto:description").getString());
    resource.setDisplayName(node.getProperty("vorto:displayname").getString());
    resource.setCreationDate(node.getProperty("jcr:created").getDate().getTime());
    if (node.hasProperty("jcr:lastModified")) {
      resource.setModificationDate(node.getProperty("jcr:lastModified").getDate().getTime());
    }
    if (node.hasProperty("vorto:state")) {
      resource.setState(node.getProperty("vorto:state").getString());
    }
    if (node.hasProperty("vorto:author")) {
      resource.setAuthor(node.getProperty("vorto:author").getString());
    }

    if (!getAttachmentsByTag(resource.getId(), Attachment.TAG_IMAGE).isEmpty()) {
      resource.setHasImage(true);
    }

    return resource;
  }

  private ModelInfo createModelResource(Node node) throws RepositoryException {
    ModelInfo resource = createMinimalModelInfo(node);
    resource.setFileName(node.getName());

    if (node.hasProperty("vorto:references")) {
      Value[] referenceValues = null;
      try {
        referenceValues = node.getProperty("vorto:references").getValues();
      } catch (Exception ex) {
        referenceValues = new Value[] {node.getProperty("vorto:references").getValue()};
      }

      if (referenceValues != null) {
        ModelReferencesHelper referenceHelper = new ModelReferencesHelper();
        for (Value referValue : referenceValues) {
          String nodeUuid = referValue.getString();
          try {
            Node referencedNode = session.getNodeByIdentifier(nodeUuid);
            referenceHelper.addModelReference(
                ModelIdHelper.fromPath(referencedNode.getParent().getPath()).getPrettyFormat());
          } catch (ItemNotFoundException itemNotFound) {
            logger.error("Referential Integrity Problem ----->>>>>>> Broken reference of model "
                + resource.getId(), itemNotFound);
          }
        }
        resource.setReferences(referenceHelper.getReferences());
      }
    }

    if (resource.getType() != ModelType.Mapping) { // only add platform mapping info for non-mapping
                                                   // models
      PropertyIterator propIter = node.getReferences();
      while (propIter.hasNext()) {
        Property prop = propIter.nextProperty();
        try {
          Node referencedByFileNode = prop.getParent();
          final ModelId referencedById =
              ModelIdHelper.fromPath(referencedByFileNode.getParent().getPath());
          resource.getReferencedBy().add(referencedById);

          if (referencedByFileNode.getName().endsWith(ModelType.Mapping.getExtension())) {
            try {
              ModelResource emfResource = getEMFResource(referencedById);
              resource.addPlatformMapping(emfResource.getTargetPlatform(), referencedById);
            } catch (ValidationException validationEx) {
              logger.warn("Stored Vorto Model is corrupt: " + referencedById.getPrettyFormat(),
                  validationEx);
            } catch (Exception e) {
              logger.warn("Error while getting a platform mapping", e);
            }
          }
        } catch (Exception e) {
          logger.warn("A reference has gone stale. Please remove this reference. : ", e);
        }
      }
    }
    return resource;
  }

  @Override
  public ModelFileContent getModelContent(ModelId modelId) {
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());
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

  private Node createNodeForModelId(ModelId id) throws RepositoryException {
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

    logger.info("Saving " + modelId.toString() + " as " + fileName + " to Repo");

    try {

      Node folderNode = createNodeForModelId(modelId);
      NodeIterator nodeIt = folderNode.getNodes(FILE_NODES);
      if (!nodeIt.hasNext()) { // new node
        Node fileNode = folderNode.addNode(fileName, "nt:file");
        fileNode.addMixin("vorto:meta");
        fileNode.addMixin("mix:referenceable");
        fileNode.addMixin("mix:lastModified");
        fileNode.setProperty("vorto:author", userContext.getUsername());
        Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
        Binary binary =
            session.getValueFactory().createBinary(new ByteArrayInputStream(modelInfo.toDSL()));
        contentNode.setProperty("jcr:data", binary);
      } else { // node already exists.
        Node fileNode = nodeIt.nextNode();
        fileNode.addMixin("mix:lastModified");
        fileNode.setProperty("vorto:author", userContext.getUsername());
        Node contentNode = fileNode.getNode("jcr:content");
        Binary binary =
            session.getValueFactory().createBinary(new ByteArrayInputStream(modelInfo.toDSL()));
        contentNode.setProperty("jcr:data", binary);
      }

      session.save();
      logger.info("Model was saved successful");
      return modelInfo;
    } catch (Exception e) {
      logger.error("Error checking in model", e);
      throw new FatalModelRepositoryException("Problem checking in uploaded model" + modelId, e);
    }
  }

  @Override
  public ModelInfo getById(ModelId modelId) {
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      Node modelFileNode = folderNode.getNodes(FILE_NODES).nextNode();

      ModelInfo modelResource = createModelResource(modelFileNode);

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
      throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
    }
  }

  private ModelInfo getBasicById(ModelId modelId) {
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      Node modelFileNode = folderNode.getNodes(FILE_NODES).nextNode();

      return createMinimalModelInfo(modelFileNode);
    } catch (PathNotFoundException e) {
      return null;
    } catch (RepositoryException e) {
      throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
    }
  }

  public void setSession(Session session) {
    this.session = session;
  }

  @Override
  public List<ModelInfo> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform) {
    logger.info("Fetching mapping models for model ID " + modelId.getPrettyFormat() + " and key "
        + targetPlatform);
    List<ModelInfo> mappingResources = new ArrayList<>();
    ModelInfo modelResource = getById(modelId);
    if (modelResource != null) {
      for (ModelId referenceeModelId : modelResource.getReferencedBy()) {
        ModelInfo referenceeModelResources = getBasicById(referenceeModelId);
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
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      Node folderNode = session.getNode(modelIdHelper.getFullPath());
      Node fileNode = (Node) folderNode.getNodes().next();
      Node fileItem = (Node) fileNode.getPrimaryItem();
      InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();
      return (ModelResource) modelParserFactory.getParser(fileNode.getName()).parse(is);
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
  }

  @Override
  public void removeModel(ModelId modelId) {
    try {
      ModelInfo modelResource = this.getById(modelId);
      if (!modelResource.getReferencedBy().isEmpty()) {
        throw new ModelReferentialIntegrityException(
            "Cannot remove model because it is referenced by other model(s)",
            modelResource.getReferencedBy());
      }
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      jcrTools.removeAllChildren(session, modelIdHelper.getFullPath());
      Item item = session.getItem(modelIdHelper.getFullPath());
      item.remove();
      session.save();
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Problem occured removing the model", e);
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
    try {
      Node folderNode = createNodeForModelId(modelId);
      Node fileNode = folderNode.getNodes(FILE_NODES).hasNext()
          ? folderNode.getNodes(FILE_NODES).nextNode() : null;
      fileNode.addMixin("mix:lastModified");
      nodeConsumer.accept(fileNode);
      session.save();

      return modelId;
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Problem occured removing the model", e);
    }
  }

  @FunctionalInterface
  private interface NodeConsumer {
    void accept(Node node) throws RepositoryException;
  }

  public void saveModel(ModelResource resource) {
    try {
      Node folderNode = createNodeForModelId(resource.getId());
      Node fileNode = folderNode.getNodes(FILE_NODES).hasNext()
          ? folderNode.getNodes(FILE_NODES).nextNode() : null;
      Node contentNode = fileNode.getNode("jcr:content");
      Binary binary =
          session.getValueFactory().createBinary(new ByteArrayInputStream(resource.toDSL()));
      contentNode.setProperty("jcr:data", binary);
      session.save();
    } catch (Exception e) {
      throw new FatalModelRepositoryException("Problem occured removing the model", e);
    }
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
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      Node contentNode = null;

      if (folderNode.hasNode(fileContent.getFileName())) {
        Node imageNode = (Node) folderNode.getNode(fileContent.getFileName());
        contentNode = (Node) imageNode.getPrimaryItem();
      } else {
        Node imageNode = folderNode.addNode(fileContent.getFileName(), "nt:file");
        contentNode = imageNode.addNode("jcr:content", "nt:resource");
      }

      Binary binary = session.getValueFactory()
          .createBinary(new ByteArrayInputStream(fileContent.getContent()));
      contentNode.setProperty("jcr:data", binary);
      session.save();

    } catch (PathNotFoundException e) {
      throw new ModelNotFoundException("Could not find model with the given model id", e);
    } catch (Exception e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
  }

  @Override
  public Optional<FileContent> getFileContent(ModelId modelId, Optional<String> fileName) {
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

      Node folderNode = session.getNode(modelIdHelper.getFullPath());

      Node fileNode;
      if (fileName.isPresent()) {
        fileNode = (Node) folderNode.getNode(fileName.get());
      } else {
        fileNode = (Node) folderNode.getNodes(FILE_NODES).next();
      }

      Node fileItem = (Node) fileNode.getPrimaryItem();
      InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();

      final String fileContent = IOUtils.toString(is);
      return Optional.of(new FileContent(fileNode.getName(), fileContent.getBytes()));

    } catch (PathNotFoundException e) {
      return Optional.empty();
    } catch (Exception e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
  }

  @Override
  public void attachFile(ModelId modelId, FileContent fileContent, IUserContext userContext,
      Tag... tags) throws AttachmentException {

    if (Arrays.asList(tags).stream().filter(tag -> tag.equals(Attachment.TAG_IMPORTED))
        .collect(Collectors.toList()).isEmpty()) {
      attachmentValidator.validateAttachment(fileContent, modelId);
    }

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
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
  }

  @Override
  public List<Attachment> getAttachments(ModelId modelId) {
    ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
    Node modelFolderNode = null;
    try {
      modelFolderNode = session.getNode(modelIdHelper.getFullPath());
    } catch (RepositoryException e) {
      throw new ModelNotFoundException("Model with ID " + modelId + " not found");
    }
    
    try {
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
      return Collections.emptyList();
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
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
  }

  public boolean deleteAttachment(ModelId modelId, String fileName) {
    if (getAttachments(modelId).stream()
        .anyMatch(attachment -> (attachment.getTags().contains(Attachment.TAG_IMPORTED)
            && attachment.getFilename().equals(fileName)))) {
      return false;
    }
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
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
    }
  }

  public Session getSession() {
    return this.session;
  }

  public ModelResource createVersion(ModelId existingId, String newVersion, IUserContext user) {
    ModelInfo existingModel = this.getById(existingId);
    if (existingModel == null) {
      throw new ModelNotFoundException("Model could not be found");

    } else if (existingId.getVersion() == newVersion) {
      throw new ModelAlreadyExistsException();
    } else {
      ModelId newModelId = ModelId.newVersion(existingId, newVersion);
      if (this.getById(newModelId) != null) {
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
    try {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());
      return repoDiagnostics.diagnose(folderNode);
    } catch (RepositoryException ex) {
      throw new FatalModelRepositoryException("Diagnostics failed", ex);
    }
  }

  private <Result> Result doInRootNode(Function<Node, Result> fn) {
    try {
      Node node = getSession().getRootNode();
      return fn.apply(node);
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException(e.getMessage(), e);
    }
  }

  public void setModelParserFactory(ModelParserFactory modelParserFactory) {
    this.modelParserFactory = modelParserFactory;
  }

  public void setRepositoryDiagnostics(RepositoryDiagnostics repoDiagnostics) {
    this.repoDiagnostics = repoDiagnostics;
  }
}
