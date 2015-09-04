/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.internal.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.Binary;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.governance.GovernanceResult;
import org.eclipse.vorto.repository.governance.IGovernance;
import org.eclipse.vorto.repository.governance.IGovernanceCallback;
import org.eclipse.vorto.repository.internal.governance.AlwaysApproveGovernance;
import org.eclipse.vorto.repository.internal.service.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.internal.service.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.internal.service.validation.ModelReferencesValidation;
import org.eclipse.vorto.repository.model.ModelEMFResource;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.model.ModelUploadHandle;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.service.FatalModelRepositoryException;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.ModelNotFoundException;
import org.eclipse.vorto.repository.service.ModelReferentialIntegrityException;
import org.eclipse.vorto.repository.service.ModelRepositoryException;
import org.eclipse.vorto.repository.validation.IModelValidator;
import org.eclipse.vorto.repository.validation.ValidationException;
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

	@Autowired
	private Session session;

	@Autowired(required = false)
	private IGovernance governance = new AlwaysApproveGovernance();
	
	private List<IModelValidator> validators = new LinkedList<IModelValidator>();
	
	private File tmpDirectory = null;
	
	private static Logger logger = Logger.getLogger(JcrModelRepository.class);
	
	@Override
	public List<ModelResource> search(String queryExpression) {
		if (queryExpression == null || queryExpression.isEmpty()) {
			queryExpression = "*";
		}
		
		try {
			List<ModelResource> modelResources = new ArrayList<>();
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(queryExpression, org.modeshape.jcr.api.query.Query.FULL_TEXT_SEARCH);
			logger.debug("Searching repository with expression "+query.getStatement());
			QueryResult result = query.execute();
			RowIterator rowIterator = result.getRows();
			
			while (rowIterator.hasNext()) {
				Row row = rowIterator.nextRow();
				Node currentNode = row.getNode();
				if (currentNode.hasProperty("vorto:type")) {
					modelResources.add(createModelResource(currentNode));
				}
			}
			
			return modelResources;
		} catch (RepositoryException e) {
			throw new RuntimeException("Could not create query manager", e);
		}
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
					referenceHelper.addModelReference(ModelId.fromPath(referencedNode.getParent().getPath()).getPrettyFormat());
				}
				resource.setReferences(referenceHelper.getReferences());
			}	
		}
		
		PropertyIterator propIter = node.getReferences();
		while (propIter.hasNext()) {
			Property prop = propIter.nextProperty();
			Node referencedByFileNode = prop.getParent();
			resource.getReferencedBy().add(ModelId.fromPath(referencedByFileNode.getParent().getPath()));		
		}
		
		return resource;
	}

	@Override
	public byte[] getModelContent(ModelId modelId,ContentType contentType) {
		try {
			Node folderNode = session.getNode(modelId.getFullPath());
			Node fileNode = (Node)folderNode.getNodes().next();
			Node fileItem = (Node) fileNode.getPrimaryItem();
			InputStream is = fileItem.getProperty("jcr:data").getBinary().getStream();
			
			if (contentType == ContentType.XMI) {
				ModelEMFResource resource = (ModelEMFResource)ModelParserFactory.getParser(fileNode.getName()).parse(is);
				return resource.toXMI();
			} else {
				return IOUtils.toByteArray(is);	
			}
		} catch (PathNotFoundException e) {
			throw new ModelNotFoundException("Could not find model with the given model id", e);
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong accessing the repository", e);
		}
	}

	@Override
	public UploadModelResult upload(byte[] content, String fileName) {
		try {
			ModelResource resource = ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(content));
			
			for (IModelValidator validator : validators) {
				validator.validate(resource);
			}	
			return UploadModelResult.valid(createUploadHandle(resource.getId(),content,fileName), resource);
		} catch(ValidationException validationException) {
			return UploadModelResult.invalid(validationException);
		}
	}
	
	private String createUploadHandle(ModelId id, byte[] content,String fileName) {
		try {
			File file = File.createTempFile("vorto",fileName);
			IOUtils.write(content, new FileOutputStream(file));
			logger.debug("Created temporary file for upload : "+file.getName());
			return file.getName();
		} catch (IOException e) {
			throw new RuntimeException("Could not create temporary file for uploaded model",e);
		}
	}

	@Override
	public void checkin(String handleId) {
		InputStream contentAsStream;
		final File uploadedFile;
		try {
			uploadedFile = loadUploadedFile(handleId);
			logger.debug("Found temporary file for handleId : "+uploadedFile.getName());
			contentAsStream = new FileInputStream(uploadedFile);
		} catch (FileNotFoundException e1) {
			throw new ModelNotFoundException("Could not find uploaded model with the specified handle", e1);
		} 
		
		final ModelResource resource = ModelParserFactory.getParser(uploadedFile.getName()).parse(contentAsStream);

		try {
			governance.start(new ModelUploadHandle(resource.getId(), resource.getModelType(), IOUtils.toByteArray(new FileInputStream(uploadedFile))),
					new IGovernanceCallback() {

						@Override
						public void processUploadResult(GovernanceResult uploadResult) {
							if (uploadResult.isApproved()) {
								try {
									logger.debug("Checkin for "+uploadResult.getHandle().getModelId().getPrettyFormat()+" was approved. Proceeding with checkin...");
									Node folderNode = createNodeForModelId(uploadResult.getHandle().getModelId());
									Node fileNode = folderNode.addNode(
											uploadResult.getHandle().getModelId().getName() + uploadResult.getHandle().getModelType().getExtension(),
											"nt:file");
									fileNode.addMixin("vorto:meta");
									fileNode.addMixin("mix:referenceable");
									Node contentNode = fileNode.addNode("jcr:content", "nt:resource");
									Binary binary = session.getValueFactory()
											.createBinary(new ByteArrayInputStream(uploadResult.getHandle().getContent()));
									contentNode.setProperty("jcr:data", binary);
									session.save();
									logger.debug("Checkin successful.");
									FileUtils.deleteQuietly(uploadedFile);
								} catch (RepositoryException e) {
									throw new RuntimeException(e);
								}
							} else {
								logger.warn(resource.getId().getPrettyFormat()+" was not approved for checkin");
							}
						}
					});
		} catch (IOException e) {
			throw new RuntimeException("Something went wrong when reading the model content",e);
		}
	}

	private File loadUploadedFile(String handleId) {
		return new File(tmpDirectory,handleId);
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
			Node foundNode = session.getNode(modelId.getFullPath());
			Node fileNode = (Node)foundNode.getNodes().next();
			return createModelResource(fileNode);
		} catch (PathNotFoundException e) {
			return null;
		} catch (RepositoryException e) {
			throw new RuntimeException("Retrieving Content of Resource: Problem accessing repository",e);
		}
	}
	
	@Override
	public void removeModel(ModelId modelId) {
		try {
			ModelResource modelResource = getById(modelId);
			if (!modelResource.getReferencedBy().isEmpty()) {
				throw new ModelReferentialIntegrityException("Cannot remove model because it is referenced by other model(s)",modelResource.getReferencedBy());
			}
			Item item = session.getItem(modelId.getFullPath());
	        item.remove();
	        session.save();
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Problem occured removing the model",e);
		} 
	}
	
	
	@PostConstruct
	public void createValidators() {
		try {
			this.tmpDirectory = File.createTempFile("vorto",null).getParentFile();
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize tmp directory");
		}
		this.validators.add(new DuplicateModelValidation(this));
		this.validators.add(new ModelReferencesValidation(this));
	}
	
	public void setSession(Session session) {
		this.session = session;
	}

}
