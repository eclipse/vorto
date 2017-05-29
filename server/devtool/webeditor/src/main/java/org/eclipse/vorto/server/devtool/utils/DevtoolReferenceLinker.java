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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevtoolReferenceLinker {

	@Autowired
	private DevtoolUtils devtoolUtils;
	
	public void linkReferenceToResource(String targetResourceId, String referenceResourceId, ResourceSet resourceSet) {
		Resource targetResource = resourceSet.getResource(devtoolUtils.getResourceURI(targetResourceId), true);
		Model targetModel = (Model) targetResource.getContents().get(0);
		
		Resource referenceResource = resourceSet.getResource(devtoolUtils.getResourceURI(referenceResourceId), true);
		Model referenceModel = (Model) referenceResource.getContents().get(0);

		ModelReference modelReference = devtoolUtils.getModelRefernce(referenceModel.getName(), referenceModel.getNamespace(), referenceModel.getVersion());
		
		if (!containsModelReference(targetModel, modelReference)) {			
			URI uri = devtoolUtils.getResourceURI(referenceResourceId);
			Resource resource = resourceSet.getResource(uri, true);
			EObject eObject = resource.getContents().get(0);
			targetResource.getContents().add(eObject);
			targetModel.getReferences().add(modelReference);
			resource.getContents().add(eObject);
		}
	}
	
	public boolean containsResource(String resourceId, ResourceSet resourceSet) {
		URI uri = devtoolUtils.getResourceURI(resourceId);
		return resourceSet.getResource(uri, true) != null;
	}

	public void removeResourceFromResourceSet(String resourceId, ResourceSet resourceSet){
		URI uri = devtoolUtils.getResourceURI(resourceId);
		EList<Resource> resourceList = resourceSet.getResources();
		int removeIndex = -1;
		for(int index = 0 ; index < resourceList.size() ; index++){
			if(resourceList.get(index).getURI().equals(uri)){
				removeIndex = index;
				break;
			}
		}
		if(removeIndex > -1){
			resourceList.remove(removeIndex);
		}		
	}
	
	public void resetResourceSet(ResourceSet resourceSet){
		EList<Resource> resourceList = resourceSet.getResources();
		resourceList.clear();
	}
	
	private boolean containsModelReference(Model model, ModelReference reference) {
		for (ModelReference ref : model.getReferences()) {
			if (ref.getImportedNamespace().equals(reference.getImportedNamespace())) {
				return true;
			}
		}
		return false;
	}
}
