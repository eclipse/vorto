package org.eclipse.vorto.server.devtool.service;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;

public interface IInformationModelEditorService {

	String linkFunctionBlockToInformationModel(String infoModelResourceId, ModelId functionBlockModelId, ResourceSet resourceSet,
			Set<String> referencedResourceSet);

	List<ModelResource> searchFunctionBlockByExpression(String expression);
}
