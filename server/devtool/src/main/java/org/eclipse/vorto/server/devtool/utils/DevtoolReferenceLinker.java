/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.http.model.ModelId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevtoolReferenceLinker {

	@Autowired
	DevtoolRestClient informationModelEditorRestClient;

	public void linkFunctionBlockToInfoModel(String infoModelResourceId, ModelId functionBlockModelId,
			ResourceSet resourceSet, Set<String> referencedResourceSet) {
		if (!containsResource(infoModelResourceId, resourceSet)) {
			throw new RuntimeException("No resource with resourceId : " + infoModelResourceId);
		}
		ModelType modelType = informationModelEditorRestClient.getModelType(functionBlockModelId);
		if (!modelType.equals(ModelType.Functionblock)) {
			throw new RuntimeException("No FunctionBlock [" + functionBlockModelId.toString() + "]");
		}

		linkReferenceToModel(infoModelResourceId, functionBlockModelId, resourceSet, referencedResourceSet);

		Resource targetResource = resourceSet.getResource(URI.createURI(infoModelResourceId), true);
		InformationModel informationModel = (InformationModel) targetResource.getContents().get(0);
		Resource referencedResource = resourceSet.getResource(getURI(functionBlockModelId), true);
		EObject eObject = referencedResource.getContents().get(0);
		FunctionblockModel funtionblockModel = (FunctionblockModel) eObject;
		informationModel.getProperties().add(
				createFunctionblockProperty(funtionblockModel, getVariableNames(informationModel.getProperties())));
		referencedResource.getContents().add(eObject);
	}

	public void linkDataTypeToFunctionBlock(String functionBlockResourceId, ModelId datatypeModelId,
			ResourceSet resourceSet, Set<String> referencedResourceSet) {
		if (!containsResource(functionBlockResourceId, resourceSet)) {
			throw new RuntimeException("No resource with resourceId : " + functionBlockResourceId);
		}
		ModelType modelType = informationModelEditorRestClient.getModelType(datatypeModelId);
		if (!modelType.equals(ModelType.Datatype)) {
			throw new RuntimeException("No DataType [" + datatypeModelId.toString() + "]");
		}
		
		linkReferenceToModel(functionBlockResourceId, datatypeModelId, resourceSet, referencedResourceSet);
	}

	private void linkReferenceToModel(String modelResourceId, ModelId referenceModelId, ResourceSet resourceSet,
			Set<String> referencedResourceSet) {

		ModelType referenceModelType = informationModelEditorRestClient.getModelType(referenceModelId);
		String fileName = getFileName(referenceModelId);
		URI uri = getURI(referenceModelId);

		Resource targetResource = resourceSet.getResource(URI.createURI(modelResourceId), true);
		Model model = (Model) targetResource.getContents().get(0);

		ModelReference modelReference = new org.eclipse.vorto.core.api.model.model.ModelId(referenceModelType,
				referenceModelId.getName(), referenceModelId.getNamespace(), referenceModelId.getVersion())
						.asModelReference();

		if (!containsModelReference(model, modelReference)) {
			Resource resource = null;
			EObject eObject = null;
			if (!referencedResourceSet.contains(fileName)) {
				String fileContents = informationModelEditorRestClient.getModelFile(referenceModelId);
				try {
					resource = resourceSet.createResource(uri);
					resource.load(new ByteArrayInputStream(fileContents.getBytes((StandardCharsets.UTF_8))),
							resourceSet.getLoadOptions());
					referencedResourceSet.add(fileName);
					System.out.println(resourceSet);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			resource = resourceSet.getResource(uri, true);
			eObject = resource.getContents().get(0);
			targetResource.getContents().add(eObject);
			model.getReferences().add(modelReference);
			resource.getContents().add(eObject);
		}
	}

	private boolean containsResource(String resourceId, ResourceSet resourceSet) {
		URI uri = URI.createURI(resourceId);
		Resource resource = resourceSet.getResource(uri, true);
		if (resource == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean containsModelReference(Model model, ModelReference reference) {
		for (ModelReference ref : model.getReferences()) {
			if (ref.getImportedNamespace().equals(reference.getImportedNamespace())) {
				return true;
			}
		}
		return false;
	}

	private Set<String> getVariableNames(EList<FunctionblockProperty> properties) {
		Set<String> variableNames = new HashSet<String>();
		for (FunctionblockProperty property : properties) {
			variableNames.add(property.getName());
		}
		return variableNames;
	}

	private FunctionblockProperty createFunctionblockProperty(FunctionblockModel functionblockModel,
			Set<String> existingVariableNames) {
		FunctionblockProperty functionblockProperty = InformationModelFactory.eINSTANCE.createFunctionblockProperty();
		functionblockProperty.setType(functionblockModel);
		functionblockProperty.setName(generateFunctionBlockVariableName(functionblockModel, existingVariableNames));
		return functionblockProperty;
	}

	private String generateFunctionBlockVariableName(FunctionblockModel fbm, Set<String> variableNames) {
		String variableName = fbm.getName().toLowerCase();
		int i = 0;
		while (variableNames.contains(variableName)) {
			variableName = fbm.getName().toLowerCase() + ++i;
		}
		return variableName;
	}

	private String getFileName(ModelId modelId) {
		ModelType modelType = informationModelEditorRestClient.getModelType(modelId);
		return modelId.getNamespace().replace(".", "_") + "_" + modelId.getName() + "_"
				+ modelId.getVersion().replace(".", "_") + modelType.getExtension();
	}

	private URI getURI(ModelId modelId) {
		URI uri = URI.createURI("fake:/" + getFileName(modelId));
		return uri;
	}
}
