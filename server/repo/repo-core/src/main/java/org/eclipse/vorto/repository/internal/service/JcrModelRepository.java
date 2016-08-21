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
package org.eclipse.vorto.repository.internal.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.http.model.ModelType;
import org.eclipse.vorto.repository.internal.service.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.internal.service.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.internal.service.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.internal.service.validation.ModelReferencesValidation;
import org.eclipse.vorto.repository.model.ModelEMFResource;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.CheckinMessage;
import org.eclipse.vorto.repository.service.FatalModelRepositoryException;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.ModelNotFoundException;
import org.eclipse.vorto.repository.service.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.service.UserRepository;
import org.eclipse.vorto.repository.validation.IModelValidator;
import org.eclipse.vorto.repository.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * Implementation of the repository using JCR
 * 
 * @author Alexander Edelmann
 *
 */
@Service
public class JcrModelRepository implements IModelRepository {

	@Autowired
	private Session session;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelSearchUtil modelSearchUtil;

	@Autowired
	private INotificationService notificationService;
	
	private List<IModelValidator> validators = new LinkedList<IModelValidator>();

	private static Logger logger = Logger.getLogger(JcrModelRepository.class);

	@Override
	public List<ModelResource> search(String queryExpression) {
		if (queryExpression == null || queryExpression.isEmpty()) {
			queryExpression = "*";
		}
		try {
			List<ModelResource> modelResources = new ArrayList<>();

			Query query = modelSearchUtil.createQueryFromExpression(session, queryExpression);

			logger.debug("Searching repository with expression " + query.getStatement());
			QueryResult result = query.execute();
			RowIterator rowIterator = result.getRows();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.nextRow();
				Node currentNode = row.getNode();
				if (currentNode.hasProperty("vorto:type") && !isMappingNode(currentNode)) {
					try {
						modelResources.add(createModelResource(currentNode));
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

	private boolean isMappingNode(Node node) throws RepositoryException {
		return node.hasProperty("vorto:type")
				&& ModelType.valueOf(node.getProperty("vorto:type").getString()) == ModelType.Mapping;
	}

	private ModelResource createModelResource(Node node) throws RepositoryException {
		ModelResource resource = new ModelResource(ModelId.fromPath(node.getParent().getPath()),
				ModelType.valueOf(node.getProperty("vorto:type").getString()));
		resource.setDescription(node.getProperty("vorto:description").getString());
		resource.setDisplayName(node.getProperty("vorto:displayname").getString());
		resource.setCreationDate(node.getProperty("jcr:created").getDate().getTime());
		if (node.hasProperty("vorto:author")) {
			resource.setAuthor(node.getProperty("vorto:author").getString());
		}

		if (node.hasProperty("vorto:references")) {
			Value[] referenceValues = node.getProperty("vorto:references").getValues();
			if (referenceValues != null) {
				ModelReferencesHelper referenceHelper = new ModelReferencesHelper();
				for (Value referValue : referenceValues) {
					String nodeUuid = referValue.getString();
					Node referencedNode = session.getNodeByIdentifier(nodeUuid);
					referenceHelper.addModelReference(
							ModelId.fromPath(referencedNode.getParent().getPath()).getPrettyFormat());
				}
				resource.setReferences(referenceHelper.getReferences());
			}
		}

		PropertyIterator propIter = node.getReferences();
		while (propIter.hasNext()) {
			Property prop = propIter.nextProperty();
			Node referencedByFileNode = prop.getParent();
			final ModelId referencedById = ModelId.fromPath(referencedByFileNode.getParent().getPath());
			resource.getReferencedBy().add(referencedById);
		}

		return resource;
	}

	@Override
	public byte[] getModelContent(ModelId modelId, ContentType contentType) {
		try {
			Node folderNode = session.getNode(modelId.getFullPath());
			Node fileNode = (Node) folderNode.getNodes(modelId.getName() + "*").next();
			Node fileItem = (Node) fileNode.getPrimaryItem();
			InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();

			if (contentType == ContentType.XMI) {
				ModelEMFResource resource = (ModelEMFResource) ModelParserFactory.getParser(fileNode.getName())
						.parse(is);
				return resource.toXMI();
			} else {
				return IOUtils.toByteArray(is);
			}
		} catch (PathNotFoundException e) {
			throw new ModelNotFoundException("Could not find model with the given model id", e);
		} catch (Exception e) {
			throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
		}
	}

	@Override
	public UploadModelResult upload(byte[] content, String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return UploadModelResult.invalid(new ValidationException("Filename is invalid", null));
		}
		
		try {
			ModelResource resource = ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(content));
			
			List<ValidationException> validationExceptions = new ArrayList<ValidationException>();
			for (IModelValidator validator : validators) {
				try {
					validator.validate(resource);
				} catch(ValidationException validationException) {
					validationExceptions.add(validationException);
				}
			}
			
			if (validationExceptions.size() <= 0) {
				return UploadModelResult.valid(createUploadHandle(resource.getId(), content, fileName), resource);
			} else {
				return UploadModelResult.invalid(validationExceptions.toArray(new ValidationException[validationExceptions.size()]));
			}
		} catch(ValidationException e) {
			return UploadModelResult.invalid(e);
		}
	}

	@Override
	public void checkin(String handleId, final String author) {
		String absoluteHandleId = new File(FilenameUtils.normalize(getDefaultExtractDirectory() + "/" + handleId)).getAbsolutePath();
		final ModelResource resource = parseModelResource(absoluteHandleId);
		checkinModel(absoluteHandleId, resource, author);			
	}
	
	private static String getDefaultExtractDirectory() {
		return FilenameUtils.normalize(FileUtils.getTempDirectory().getPath() + "/vorto", true);
	}
	
	private String createUploadHandle(ModelId id, byte[] content, String fileName) {
		try {
			File tmpDirectory = new File(getDefaultExtractDirectory());
			if (!tmpDirectory.exists()) {
				tmpDirectory.mkdirs();
			}
			File file = new File(FilenameUtils.normalize(getDefaultExtractDirectory() + "/" + StringUtils.getFilename(fileName)));
			IOUtils.write(content, new FileOutputStream(file));
			logger.debug("Created temporary file for upload : " + file.getName());
			return file.getName();
		} catch (IOException e) {
			throw new RuntimeException("Could not create temporary file for uploaded model", e);
		}
	}

	private ModelResource parseModelResource(String handleId) {
		InputStream contentAsStream;
		File uploadedFile;
		try {
			uploadedFile = new File(handleId);
			logger.info("Found temporary file for handleId : " + uploadedFile.getName());
			contentAsStream = new FileInputStream(uploadedFile);
		} catch (FileNotFoundException e) {
			throw new ModelNotFoundException("Could not find uploaded model with the specified handle" + handleId, e);
		}
			final ModelResource resource = ModelParserFactory.getParser(uploadedFile.getName()).parse(contentAsStream);
		return resource;
		}
			
	private void checkinModel(String handleId, ModelResource resource, String author) {
		final File uploadedFile = new File(handleId);
		byte[] content;
		try {
			content = IOUtils.toByteArray(new FileInputStream(uploadedFile));
			logger.info("Checkin for " + resource.getId().getPrettyFormat() + " was approved. Proceeding with checkin...");
			Node folderNode = createNodeForModelId(resource.getId());
			Node fileNode = folderNode.addNode(resource.getId().getName() + resource.getModelType().getExtension(),
					"nt:file");
			fileNode.addMixin("vorto:meta");
			fileNode.addMixin("mix:referenceable");
			fileNode.setProperty("vorto:author", author);
			Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
			Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(content));
			contentNode.setProperty("jcr:data", binary);
			session.save();
			logger.info("Checkin successful." + handleId);
			FileUtils.deleteQuietly(uploadedFile);
			// Email Notification
			notifyWatchers(resource, author);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FatalModelRepositoryException("Problem checking in uploaded model" + handleId, e);
		}
		}

	private void notifyWatchers(ModelResource resource, String author) {
		resource.setAuthor(author);
		for (User recipient : userRepository.findAll()) {
			if (recipient.getHasWatchOnRepository()) {
				Map<String, Object> context = new HashMap<>(2);
				context.put("user", recipient);
				context.put("model", resource);
				notificationService.sendNotification(new CheckinMessage(recipient, resource));
			}
		}
	}

	private Node createNodeForModelId(ModelId id) throws RepositoryException {
		StringBuilder pathBuilder = new StringBuilder();
		Iterator<String> modelIdIterator = id.iterator();
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

		return rootNode.getNode(id.getFullPath().substring(1));
	}

	@Override
	public ModelResource getById(ModelId modelId) {
		try {
			Node folderNode = session.getNode(modelId.getFullPath());

			Node modelFileNode = folderNode.getNodes("*.type | *.fbmodel | *.infomodel | *.mapping").nextNode();

			ModelResource modelResource = createModelResource(modelFileNode);
			if (modelResource.getModelType() == ModelType.InformationModel) {
				NodeIterator imageNodeIterator = folderNode.getNodes("img.png*");
				if (imageNodeIterator.hasNext()) {
					modelResource.setHasImage(true);
				}
				for (ModelId referencedById : modelResource.getReferencedBy()) {
					ModelEMFResource emfResource = getEMFResource(referencedById);
					modelResource.addTargetPlatform(emfResource.getTargetPlatform());
				}
			}
			return modelResource;
		} catch (PathNotFoundException e) {
			return null;
		} catch (RepositoryException e) {
			throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository", e);
		}
	}

	@Override
	public void removeModel(ModelId modelId) {
		try {
			ModelResource modelResource = getById(modelId);
			if (!modelResource.getReferencedBy().isEmpty()) {
				throw new ModelReferentialIntegrityException(
						"Cannot remove model because it is referenced by other model(s)",
						modelResource.getReferencedBy());
			}
			Item item = session.getItem(modelId.getFullPath());
			item.remove();
			session.save();
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Problem occured removing the model", e);
		}
	}

	@PostConstruct
	public void createValidators() {
		this.validators.add(new DuplicateModelValidation(this));
		this.validators.add(new ModelReferencesValidation(this));
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public List<ModelResource> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform) {
		List<ModelResource> mappingResources = new ArrayList<>();
		ModelResource modelResource = getById(modelId);
		if (modelResource != null) {
			for (ModelId referenceeModelId : modelResource.getReferencedBy()) {
				ModelResource referenceeModelResources = getById(referenceeModelId);
				if (referenceeModelResources.getModelType() == ModelType.Mapping
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

	private boolean isTargetPlatformMapping(ModelResource model, String targetPlatform) {
		try {
			ModelEMFResource emfResource = getEMFResource(model.getId());
			return emfResource.matchesTargetPlatform(targetPlatform);
		} catch (Exception e) {
			throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
		}
	}

	private ModelEMFResource getEMFResource(ModelId modelId) {
		try {
			Node folderNode = session.getNode(modelId.getFullPath());
			Node fileNode = (Node) folderNode.getNodes().next();
			Node fileItem = (Node) fileNode.getPrimaryItem();
			InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();
			return (ModelEMFResource) ModelParserFactory.getParser(fileNode.getName()).parse(is);
		} catch (Exception e) {
			throw new FatalModelRepositoryException("Something went wrong accessing the repository", e);
		}
	}

	@Override
	public void addModelImage(ModelId modelId, byte[] imageContent) {
		try {
			Node modelFolderNode = session.getNode(modelId.getFullPath());
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
	public byte[] getModelImage(ModelId modelId) {
		try {
			Node folderNode = session.getNode(modelId.getFullPath());
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
	
	public ModelSearchUtil getModelSearchUtil() {
		return modelSearchUtil;
	}

	public void setModelSearchUtil(ModelSearchUtil modelSearchUtil) {
		this.modelSearchUtil = modelSearchUtil;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public INotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(INotificationService notificationService) {
		this.notificationService = notificationService;
	}
}
