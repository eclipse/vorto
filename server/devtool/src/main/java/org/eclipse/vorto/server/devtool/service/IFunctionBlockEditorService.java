package org.eclipse.vorto.server.devtool.service;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;

public interface IFunctionBlockEditorService {

	String linkDatatypeToFunctionBlock(String functionBlockResourceId, ModelId datatypeModelId, ResourceSet resourceSet,
			Set<String> referencedResourceSet);

	List<ModelResource> searchDataTypeByExpression(String expression);
	
}
