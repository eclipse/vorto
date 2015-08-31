/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.core.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.service.IModelElementResolver;

public abstract class AbstractModelElement implements IModelElement,
		Comparable<IModelElement> {

	@Override
	public Image getImage() {
		URL url = null;
		try {
			url = new URL(getImageURLAsString());
		} catch (MalformedURLException e) {
			throw new RuntimeException("URL to datatype model image is not correct!", e);
		}
		return ImageDescriptor.createFromURL(url).createImage();
	}
	
	protected abstract String getImageURLAsString();
	
	@Override
	public ModelId getId() {
		return ModelIdFactory.newInstance(getModel());
	}

	@Override
	public String getDescription() {
		return ModelIdFactory.newInstance(getModel()).toString();
	}
	
	public Set<IModelElement> getReferences() {
		Set<IModelElement> references = new TreeSet<>();

		for (ModelReference modelReference : getModel().getReferences()) {
			try {
				// check if the reference is a project
				ModelId modelId = ModelIdFactory.newInstance(getPossibleReferenceType(), modelReference);
				for(IModelElementResolver resolver : getResolvers()) {
					IModelElement modelElement = resolver.resolve(modelId);
					if (modelElement != null) {
						references.add(modelElement);
						break;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return references;
	}
	
	protected abstract ModelType getPossibleReferenceType();
	
	protected abstract IModelElementResolver[] getResolvers();

	@Override
	public int compareTo(IModelElement o) {
		if (o == null || o.getId() == null)
			return -1;
		else
			return o.getId().compareTo(this.getId());
	}
}
