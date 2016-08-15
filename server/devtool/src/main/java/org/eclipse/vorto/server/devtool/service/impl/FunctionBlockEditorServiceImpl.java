package org.eclipse.vorto.server.devtool.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.server.devtool.service.IFunctionBlockEditorService;
import org.eclipse.vorto.server.devtool.utils.InformationModelEditorReferenceLinker;
import org.eclipse.vorto.server.devtool.utils.InformationModelEditorRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionBlockEditorServiceImpl implements IFunctionBlockEditorService {

	@Autowired
	InformationModelEditorRestClient informationModelEditorRestClient;

	@Autowired
	InformationModelEditorReferenceLinker infomrationModelRefernceLinker;

	@Override
	public String linkDatatypeToFunctionBlock(String functionBlockResourceId, ModelId datatypeModelId,
			ResourceSet resourceSet, Set<String> referencedResourceSet) {
		infomrationModelRefernceLinker.linkDataTypeToFunctionBlock(functionBlockResourceId, datatypeModelId,
				resourceSet, referencedResourceSet);
		Resource functionBlockResource = resourceSet.getResource(URI.createURI(functionBlockResourceId), true);
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			functionBlockResource.save(byteArrayOutputStream, null);
			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<ModelResource> searchDataTypeByExpression(String expression) {
		return searchModelByExpressionAndValidate(expression, org.eclipse.vorto.http.model.ModelType.Datatype);
	}

	private List<ModelResource> searchModelByExpressionAndValidate(String expression,
			org.eclipse.vorto.http.model.ModelType modelType) {
		List<ModelResource> resourceList = informationModelEditorRestClient.searchByExpression(expression);
		ArrayList<ModelResource> modelResourceList = new ArrayList<ModelResource>();
		for (ModelResource modelResource : resourceList) {
			if (modelResource.getModelType().equals(modelType)) {
				modelResourceList.add(modelResource);
			}
		}
		return modelResourceList;
	}
}
