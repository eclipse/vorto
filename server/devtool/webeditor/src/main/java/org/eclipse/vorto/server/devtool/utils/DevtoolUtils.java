/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.model.ModelResource;
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.server.devtool.service.impl.editor.DatatypeEditorServiceImpl;
import org.eclipse.vorto.server.devtool.service.impl.editor.FunctionBlockEditorServiceImpl;
import org.eclipse.vorto.server.devtool.service.impl.editor.InformationModelEditorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DevtoolUtils {

	@Autowired
	private InformationModelEditorServiceImpl informationModelEditorServiceImpl;

	@Autowired
	private DatatypeEditorServiceImpl datatypeEditorServiceImpl;

	@Autowired
	private FunctionBlockEditorServiceImpl functionBlockEditorServiceImpl;

	@Autowired
	private IProjectRepositoryService projectRepositoryService;

	@Value("${project.repository.path}")
	private String projectRepositoryPath;

	@Value("${reference.repository}")
	private String referenceRepository;

	public String generateFileContent(ModelResource modelResource) {
		if (modelResource.getModelType() == org.eclipse.vorto.repository.api.ModelType.Datatype) {
			return datatypeEditorServiceImpl.generateFileContent(modelResource);
		} else if (modelResource.getModelType() == org.eclipse.vorto.repository.api.ModelType.Functionblock) {
			return functionBlockEditorServiceImpl.generateFileContent(modelResource);
		} else if (modelResource.getModelType() == org.eclipse.vorto.repository.api.ModelType.InformationModel) {
			return informationModelEditorServiceImpl.generateFileContent(modelResource);
		} else {
			return "";
		}
	}

	public String generateResourceId(ModelResource modelResource, String projectName, String author) {
		return Integer
				.toString(new HashCodeBuilder(17, 37).append(author).append(projectName).append(modelResource.getName())
						.append(modelResource.getNamespace()).append(modelResource.getVersion()).toHashCode())
				+ modelResource.getModelType().getExtension();
	}

	public String getReferencedResourceId(ModelInfo modelInfo) {
		ModelResource modelResource = getModelResource(modelInfo);
		String resourceId = generateResourceId(modelResource, referenceRepository, modelInfo.getAuthor());
		return resourceId;
	}

	public URI getResourceURI(String resourceId) {
		ModelResource modelResource = (ModelResource) projectRepositoryService.createQuery()
				.property(ProjectRepositoryConstants.META_PROPERTY_RESOURCE_ID, resourceId).singleResult();
		return URI.createURI(ProjectRepositoryConstants.DUMMY + modelResource.getPath());
	}

	public ModelType getModelType(String modelType) {
		if (modelType.equalsIgnoreCase(ModelType.Datatype.toString())) {
			return ModelType.Datatype;
		} else if (modelType.equalsIgnoreCase(ModelType.Functionblock.toString())) {
			return ModelType.Functionblock;
		} else if (modelType.equalsIgnoreCase(ModelType.InformationModel.toString())) {
			return ModelType.InformationModel;
		} else if (modelType.equalsIgnoreCase(ModelType.Mapping.toString())) {
			return ModelType.Mapping;
		} else {
			throw new UnsupportedOperationException("Given ModelType is unknown");
		}
	}

	public ModelResource getModelResource(ModelInfo modelInfo) {
		ModelId modelId = modelInfo.getId();
		ModelResource modelResource = new ModelResource();
		modelResource.setName(modelId.getName());
		modelResource.setNamespace(modelId.getNamespace());
		modelResource.setVersion(modelId.getVersion());
		modelResource.setModelType(modelInfo.getType());
		modelResource.setSubType("");
		modelResource.setFileName(getFileName(modelInfo));
		return modelResource;
	}
	
	public ModelReference getModelRefernce(String name, String namespace, String version) {
		ModelReference modelReference = ModelFactory.eINSTANCE.createModelReference();
		modelReference.setImportedNamespace(namespace + "." + name);
		modelReference.setVersion(version);
		return modelReference;
	}

	public String getResourceContentsAsString(String resourceId, ResourceSet resourceSet) {
		org.eclipse.emf.ecore.resource.Resource resource = resourceSet.getResource(getResourceURI(resourceId), true);
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			resource.save(byteArrayOutputStream, null);
			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			return "";
		}
	}

	public Set<String> getVariableNames(EList<FunctionblockProperty> properties) {
		Set<String> variableNames = new HashSet<String>();
		for (FunctionblockProperty property : properties) {
			variableNames.add(property.getName());
		}
		return variableNames;
	}

	public FunctionblockProperty createFunctionblockProperty(FunctionblockModel functionblockModel,
			Set<String> existingVariableNames) {
		FunctionblockProperty functionblockProperty = InformationModelFactory.eINSTANCE.createFunctionblockProperty();
		functionblockProperty.setType(functionblockModel);
		functionblockProperty.setName(generateFunctionBlockVariableName(functionblockModel, existingVariableNames));
		return functionblockProperty;
	}
	
	public String getFileName(ModelResource modelResource) {
		return getFileName(modelResource.getName(), modelResource.getNamespace(), modelResource.getVersion(),
				getModelType(modelResource.getModelType().toString()));
	}

	private String generateFunctionBlockVariableName(FunctionblockModel fbm, Set<String> variableNames) {
		String variableName = fbm.getName().toLowerCase();
		int i = 1;
		while (variableNames.contains(variableName)) {
			variableName = fbm.getName().toLowerCase() + i++;
		}
		return variableName;
	}

	private String getFileName(String name, String namespace, String version, ModelType modelType) {
		return namespace.replace(Constants.DOT, Constants.UNDERSCORE) + Constants.HYPHEN + name + Constants.HYPHEN
				+ version.replace(Constants.DOT, Constants.UNDERSCORE) + modelType.getExtension();
	}

	private String getFileName(ModelInfo modelInfo) {
		ModelId modelId = modelInfo.getId();
		return getFileName(modelId.getName(), modelId.getNamespace(), modelId.getVersion(),
				getModelType(modelInfo.getType().toString()));
	}
}
