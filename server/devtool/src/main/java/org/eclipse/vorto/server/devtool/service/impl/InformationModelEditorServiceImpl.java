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
import org.eclipse.vorto.server.devtool.service.IInformationModelEditorService;
import org.eclipse.vorto.server.devtool.utils.DevtoolReferenceLinker;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InformationModelEditorServiceImpl implements IInformationModelEditorService {

	@Autowired
	DevtoolRestClient devtoolRestClient;

	@Autowired
	DevtoolReferenceLinker devtoolReferenceLinker;

	@Override
	public String linkFunctionBlockToInformationModel(String infoModelResourceId, ModelId functionBlockModelId,
			ResourceSet resourceSet, Set<String> referencedResourceSet) {
		devtoolReferenceLinker.linkFunctionBlockToInfoModel(infoModelResourceId, functionBlockModelId,
				resourceSet, referencedResourceSet);
		Resource infoModelResource = resourceSet.getResource(URI.createURI(infoModelResourceId), true);
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			infoModelResource.save(byteArrayOutputStream, null);
			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<ModelResource> searchFunctionBlockByExpression(String expression) {
		return searchModelByExpressionAndValidate(expression, org.eclipse.vorto.http.model.ModelType.Functionblock);
	}

	private List<ModelResource> searchModelByExpressionAndValidate(String expression,
			org.eclipse.vorto.http.model.ModelType modelType) {
		List<ModelResource> resourceList = devtoolRestClient.searchByExpression(expression);
		ArrayList<ModelResource> modelResourceList = new ArrayList<ModelResource>();
		for (ModelResource modelResource : resourceList) {
			if (modelResource.getModelType().equals(modelType)) {
				modelResourceList.add(modelResource);
			}
		}
		return modelResourceList;
	}
}
