package org.eclipse.vorto.server.devtool.service.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class IEditorService {

	@Autowired
	DevtoolRestClient devtoolRestClient;
	
	public abstract String linkModelToResource(String infoModelResourceId, ModelId modelId, ResourceSet resourceSet,
			Set<String> referencedResourceSet);

	public abstract List<ModelResource> searchModelByExpression(String expression);
	
	protected final List<ModelResource> searchModelByExpressionAndValidate(String expression,
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
