/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.core.ui.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.model.model.ModelType;

public abstract class AbstractModelElement implements IModelElement,
		Comparable<IModelElement> {

	protected IModelProject modelProject;
	
	public AbstractModelElement(IModelProject modelProject) {
		this.modelProject = modelProject;
	}
	
	// override the BufferedInputStream#close() method to NOT close the ZipInputStream since
	// multiple entries will be read if available.
	// Resource#load(InputStream) close the stream after end of file
	@Override
	public Image getImage() {
		return getImage(getImageURLAsString());
	}
	
	public Image getErrorImage() {
		return getImage(getErrorImageURLAsString());
	}
	
	private Image getImage(String urlStr) {
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			throw new RuntimeException("URL to datatype model image is not correct!", e);
		}
		return ImageDescriptor.createFromURL(url).createImage();
	}
	
	protected abstract String getImageURLAsString();
	
	protected abstract String getErrorImageURLAsString();
	
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
			for(ModelType possibleType : getPossibleReferenceTypes()) {
				ModelId modelId = ModelIdFactory.newInstance(possibleType, modelReference);
				IModelElement modelElementReference = this.modelProject.getModelElementById(modelId);
				if (modelElementReference != null) {
					references.add(modelElementReference);
					break;
				}
			}
		}
		return references;
	}
	
	public IModelElement addModelReference(ModelId modelId) {
		IModelElement reference = this.modelProject.getModelElementById(modelId);
		if (reference == null) {
			throw new ModelNotFoundException("Model with ID "+modelId+" does not exist in project", this.modelProject.getProject());
		}
		addModelReference(reference);
		return reference;
	}
	
	public void addModelReference(IModelElement modelElementReference) {
		ModelReference referenceToAdd = modelElementReference.getId().asModelReference();
		for (ModelReference modelReference : getModel().getReferences()) {
			if (EcoreUtil.equals(modelReference, referenceToAdd)) {
				return; // model reference already exists
			}
		}
		getModel().getReferences().add(referenceToAdd);
		getModel().eResource().getContents().add(modelElementReference.getModel());
		EcoreUtil.resolveAll(getModel());
	}
	
	public void save() {
		try {
			Map<String, Object> options = new HashMap<String, Object>();
			// It's better to use org.eclipse.xtext.resource.SaveOptions to
			// construct save options using xtext api
			// However that will create dependency on xtext bundle
			options.put("org.eclipse.xtext.resource.XtextResource.FORMAT", true);
			getModel().eResource().save(options);

		} catch (IOException e) {
			throw new RuntimeException("Something went wrong during model serialization", e);
		}
	}

	
	protected abstract ModelType[] getPossibleReferenceTypes();
		
	public IModelProject getProject() {
		return this.modelProject;
	}

	@Override
	public int compareTo(IModelElement o) {
		if (o == null || o.getId() == null)
			return -1;
		else
			return o.getId().compareTo(this.getId());
	}
	
	
}
