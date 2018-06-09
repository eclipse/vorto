/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.core.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.jcr.Binary;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IModelContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.TypeImportValidation;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the repository using JCR
 * 
 * @author Alexander Edelmann
 *
 */
@Service
public class JcrModelRepository implements IModelRepository {

	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;
	@Autowired
	private Session session;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ModelSearchUtil modelSearchUtil;

	@Autowired
	private ITemporaryStorage uploadStorage;

	private List<IModelValidator> validators = new LinkedList<IModelValidator>();

	private static Logger logger = Logger.getLogger(JcrModelRepository.class);

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
				if (currentNode.hasProperty("vorto:type")) {
					try {
						modelResources.add(createMinimalModelInfo(currentNode));
					} catch (Exception ex) {
						// model is corrupt ,ignoring....
					}
				}
			}

			return modelResources;
		} catch (RepositoryException e) {
			throw new RuntimeException("Could not create query manager", e);
		}
	}

	private ModelInfo createMinimalModelInfo(Node node) throws RepositoryException {
		ModelInfo resource = new ModelInfo(ModelIdHelper.fromPath(node.getParent().getPath()),
				ModelType.valueOf(node.getProperty("vorto:type").getString()));
		resource.setDescription(node.getProperty("vorto:description").getString());
		resource.setDisplayName(node.getProperty("vorto:displayname").getString());
		resource.setCreationDate(node.getProperty("jcr:created").getDate().getTime());
		if (node.hasProperty("vorto:state")) {
			resource.setState(node.getProperty("vorto:state").getString());
		} 
		if (node.hasProperty("vorto:author")) {
			resource.setAuthor(node.getProperty("vorto:author").getString());
		}

		NodeIterator imageNodeIterator = node.getParent().getNodes("img.png*");
		if (imageNodeIterator.hasNext()) {
			resource.setHasImage(true);
		}

		return resource;
	}

	private ModelInfo createModelResource(Node node) throws RepositoryException {
		ModelInfo resource = createMinimalModelInfo(node);

		if (node.hasProperty("vorto:references")) {
			Value[] referenceValues = null;
			try {
				referenceValues = node.getProperty("vorto:references").getValues();
			} catch (Exception ex) {
				referenceValues = new Value[] { node.getProperty("vorto:references").getValue() };
			}

			if (referenceValues != null) {
				ModelReferencesHelper referenceHelper = new ModelReferencesHelper();
				for (Value referValue : referenceValues) {
					String nodeUuid = referValue.getString();
					Node referencedNode = session.getNodeByIdentifier(nodeUuid);
					referenceHelper.addModelReference(
							ModelIdHelper.fromPath(referencedNode.getParent().getPath()).getPrettyFormat());
				}
				resource.setReferences(referenceHelper.getReferences());
			}
		}

		PropertyIterator propIter = node.getReferences();
		while (propIter.hasNext()) {
			Property prop = propIter.nextProperty();
			Node referencedByFileNode = prop.getParent();
			final ModelId referencedById = ModelIdHelper.fromPath(referencedByFileNode.getParent().getPath());
			resource.getReferencedBy().add(referencedById);

			if (referencedByFileNode.getName().endsWith(ModelType.Mapping.getExtension())) {
				ModelEMFResource emfResource = getEMFResource(referencedById);
				resource.addPlatformMapping(emfResource.getTargetPlatform(), referencedById);
			}
		}

		return resource;
	}

	@Override
	public IModelContent getModelContent(ModelId modelId, ContentType contentType) {
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
			Node folderNode = session.getNode(modelIdHelper.getFullPath());
			Node fileNode = (Node) folderNode.getNodes(modelId.getName() + "*").next();
			Node fileItem = (Node) fileNode.getPrimaryItem();
			InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();

			ModelEMFResource resource = (ModelEMFResource) ModelParserFactory.getParser(fileNode.getName()).parse(is);

			if (contentType == ContentType.XMI) {
				return new DefaultModelContent(resource.getModel(), contentType, resource.toXMI());
			} else {
				return new DefaultModelContent(resource.getModel(), contentType, resource.toDSL());
			}
		} catch (PathNotFoundException e) {
			throw new ModelNotFoundException("Could not find model with the given model id", e);
		} catch (Exception e) {
			throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
		}
	}

	@Override
	public UploadModelResult upload(byte[] content, String fileName, IUserContext userContext) {

		try {
			ModelInfo resource = ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(content));

			List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
			for (IModelValidator validator : validators) {
				try {
					validator.validate(resource, InvocationContext.create(userContext));
				} catch (ValidationException validationException) {
					validationExceptions.add(validationException);
				}
			}

			if (validationExceptions.size() <= 0) {
				return UploadModelResult.valid(createUploadHandle(content, resource.getType()), resource);
			} else {
				return UploadModelResultFactory
						.create(validationExceptions.toArray(new ValidationException[validationExceptions.size()]));
			}
		} catch (ValidationException e) {
			return UploadModelResultFactory.invalid(e);
		}
	}

	private String createUploadHandle(byte[] content, ModelType type) {
		final String handleId = UUID.randomUUID().toString() + type.getExtension();
		return this.uploadStorage.store(handleId, content, TTL_TEMP_STORAGE_INSECONDS).getKey();
	}

	@Override
	public ModelInfo checkin(String handleId, IUserContext userContext) {
		StorageItem uploadedItem = this.uploadStorage.get(handleId);

		if (uploadedItem == null) {
			throw new IllegalArgumentException("No model found for handleId '" + handleId + "'");
		}

		final ModelInfo resource = ModelParserFactory.getParser(handleId)
				.parse(new ByteArrayInputStream((byte[]) uploadedItem.getValue()));

		try {
			Node folderNode = createNodeForModelId(resource.getId());
			Node fileNode = folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").hasNext()
					? folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").nextNode() : null;
			if (fileNode == null) {
				fileNode = folderNode.addNode(resource.getId().getName() + resource.getType().getExtension(),
						"nt:file");
				fileNode.addMixin("vorto:meta");
				fileNode.addMixin("mix:referenceable");
				fileNode.setProperty("vorto:author", userContext.getHashedUsername());
				Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
				Binary binary = session.getValueFactory()
						.createBinary(new ByteArrayInputStream((byte[]) uploadedItem.getValue()));
				contentNode.setProperty("jcr:data", binary);
			} else {
				fileNode.setProperty("vorto:author", userContext.getHashedUsername());
				Node contentNode = fileNode.getNode("jcr:content");
				Binary binary = session.getValueFactory()
						.createBinary(new ByteArrayInputStream((byte[]) uploadedItem.getValue()));
				contentNode.setProperty("jcr:data", binary);
			}

			session.save();
			logger.info("Checkin successful");
			this.uploadStorage.remove(handleId);
		} catch (Exception e) {
			logger.error("Error checking in model", e);
			throw new FatalModelRepositoryException("Problem checking in uploaded model" + resource.getId(), e);
		}

		return resource;
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

	@Override
	public ModelInfo getById(ModelId modelId) {
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

			Node folderNode = session.getNode(modelIdHelper.getFullPath());

			Node modelFileNode = folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").nextNode();

			ModelInfo modelResource = createModelResource(modelFileNode);
			if (modelResource.getType() == ModelType.InformationModel) {
				NodeIterator imageNodeIterator = folderNode.getNodes("img.png*");
				if (imageNodeIterator.hasNext()) {
					modelResource.setHasImage(true);
				}
			}
			return modelResource;
		} catch (PathNotFoundException e) {
			return null;
		} catch (RepositoryException e) {
			throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
		}
	}

	@PostConstruct
	public void createValidators() {
		this.validators.add(new DuplicateModelValidation(this, userRepository));
		this.validators.add(new ModelReferencesValidation(this));
		this.validators.add(new TypeImportValidation());
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public List<ModelInfo> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform) {
		List<ModelInfo> mappingResources = new ArrayList<>();
		ModelInfo modelResource = getById(modelId);
		if (modelResource != null) {
			for (ModelId referenceeModelId : modelResource.getReferencedBy()) {
				ModelInfo referenceeModelResources = getById(referenceeModelId);
				if (referenceeModelResources.getType() == ModelType.Mapping
						&& isTargetPlatformMapping(referenceeModelResources, targetPlatform)) {
					mappingResources.add(referenceeModelResources);
				}
			}
			for (ModelId referencedModelId : modelResource.getReferences()) {
				mappingResources.addAll(getMappingModelsForTargetPlatform(referencedModelId, targetPlatform));
			}
		}
		return mappingResources;
	}

	private boolean isTargetPlatformMapping(ModelInfo model, String targetPlatform) {
		try {
			ModelEMFResource emfResource = getEMFResource(model.getId());
			return emfResource.matchesTargetPlatform(targetPlatform);
		} catch (Exception e) {
			throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
		}
	}

	public ModelEMFResource getEMFResource(ModelId modelId) {
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

			Node folderNode = session.getNode(modelIdHelper.getFullPath());
			Node fileNode = (Node) folderNode.getNodes().next();
			Node fileItem = (Node) fileNode.getPrimaryItem();
			InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();
			return (ModelEMFResource) ModelParserFactory.getParser(fileNode.getName()).parse(is);
		} catch (Exception e) {
			throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
		}
	}

	public void addModelImage(ModelId modelId, byte[] imageContent) {
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
			Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());
			Node contentNode = null;
			if (modelFolderNode.hasNode("img.png")) {
				Node imageNode = (Node) modelFolderNode.getNode("img.png");
				contentNode = (Node) imageNode.getPrimaryItem();
			} else {
				Node imageNode = modelFolderNode.addNode("img.png", "nt:file");
				contentNode = imageNode.addNode("jcr:content", "nt:resource");
			}

			Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(imageContent));
			contentNode.setProperty("jcr:data", binary);
			session.save();
		} catch (PathNotFoundException e) {
			throw new ModelNotFoundException("Problem when trying to add image to model", e);
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Something severe went wrong when accessing the repository", e);
		}
	}
	
	@Override
	public void removeModelImage(ModelId modelId) {
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
			Node modelFolderNode = session.getNode(modelIdHelper.getFullPath());
			if (modelFolderNode.hasNode("img.png")) {
				Node imageNode = (Node) modelFolderNode.getNode("img.png");
				imageNode.remove();
				session.save();
			}
		} catch (PathNotFoundException e) {
			throw new ModelNotFoundException("Problem when trying to remove image to model", e);
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Something severe went wrong when accessing the repository", e);
		}
	}

	@Override
	public byte[] getModelImage(ModelId modelId) {
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

			Node folderNode = session.getNode(modelIdHelper.getFullPath());
			if (folderNode.hasNode("img.png")) {
				Node imageNode = folderNode.getNode("img.png");
				Node fileItem = (Node) imageNode.getPrimaryItem();
				InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();
				return IOUtils.toByteArray(is);
			}
		} catch (PathNotFoundException e) {
			throw new ModelNotFoundException("Problem when trying to retrieve image for model", e);
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Something severe went wrong when accessing the repository", e);
		} catch (IOException e) {
			throw new FatalModelRepositoryException("Something severe went wrong when trying to read image content", e);
		}
		return null;
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
			Item item = session.getItem(modelIdHelper.getFullPath());
			item.remove();
			session.save();
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Problem occured removing the model", e);
		}
	}

	@Override
	public ModelInfo updateMeta(ModelInfo model) {
		try {
			Node folderNode = createNodeForModelId(model.getId());
			Node fileNode = folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").hasNext()
					? folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").nextNode() : null;
			fileNode.setProperty("vorto:author", model.getAuthor());
			fileNode.setProperty("vorto:state", model.getState());
			
			session.save();

			return model;
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Problem occured removing the model", e);
		}
	}

	public void saveModel(ModelEMFResource resource) {
		try {
			Node folderNode = createNodeForModelId(resource.getId());
			Node fileNode = folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").hasNext()
					? folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").nextNode() : null;
			Node contentNode = fileNode.getNode("jcr:content");
			Binary binary = session.getValueFactory()
					.createBinary(new ByteArrayInputStream(resource.toDSL()));
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
	
	public ITemporaryStorage getUploadStorage() {
		return uploadStorage;
	}

	public void setUploadStorage(ITemporaryStorage uploadStorage) {
		this.uploadStorage = uploadStorage;
	}

}
