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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.internal.model.ModelFileLookupHelper;
import org.eclipse.vorto.core.parser.IModelParser;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public abstract class AbstractModelProject extends AbstractModelElement
		implements IModelProject {

	protected IProject project;
	protected IModelParser modelParser;
	protected ModelFileLookupHelper modelLookupHelper;

	private Model model;

	public AbstractModelProject(IProject project, IModelParser modelParser) {
		this.project = project;
		this.modelParser = modelParser;
		this.modelLookupHelper = new ModelFileLookupHelper(project);
		this.model = parseModel(getModelFile());

	}

	protected abstract Model parseModel(IFile modelFile);

	@Override
	public Image getImage() {
		URL url = null;
		try {
			url = new URL(getImageURLAsString());
		} catch (MalformedURLException e) {
			throw new RuntimeException(
					"URL to datatype model image is not correct!", e);
		}
		return ImageDescriptor.createFromURL(url).createImage();
	}

	protected abstract String getImageURLAsString();

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public void addReference(IModelElement reference) {
		if (reference instanceof IModelProject) {
			addProjectReference((IModelProject) reference);
		}
		addModelReference(reference);
	}

	private void addProjectReference(IModelProject modelProject) {
		try {
			Set<IProject> immutableReferencedProjects = new HashSet<IProject>(
					Arrays.asList(project.getReferencedProjects()));
			if (immutableReferencedProjects == null
					|| immutableReferencedProjects.isEmpty()) {
				immutableReferencedProjects = new HashSet<IProject>();
			}
			Set<IProject> referencedProjects = new HashSet<IProject>();
			referencedProjects.addAll(immutableReferencedProjects);
			referencedProjects.add(modelProject.getProject());

			IProjectDescription prjDescriptor = project.getDescription();

			prjDescriptor.setReferencedProjects(referencedProjects
					.toArray(new IProject[referencedProjects.size()]));
			project.setDescription(prjDescriptor, new NullProgressMonitor());

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	protected void addModelReference(IModelElement modelElementReference) {
		ModelReference referenceToAdd = modelElementReference.getId()
				.asModelReference();
		for (ModelReference modelReference : getModel().getReferences()) {
			if (EcoreUtil.equals(modelReference, referenceToAdd)) {
				return; // model reference already exists
			}
		}
		getModel().getReferences().add(referenceToAdd);
		getModel().eResource().getContents()
				.add(modelElementReference.getModel());

	}

	@Override
	public IProject getProject() {
		return project;
	}

	public void save() {
		try {
			Map<String, Object> options = new HashMap<String, Object>();
			// It's better to use org.eclipse.xtext.resource.SaveOptions to
			// construct save options using xtext api
			// However that will create dependency on xtext bundle
			options.put("org.eclipse.xtext.resource.XtextResource.FORMAT", true);
			model.eResource().save(options);

		} catch (IOException e) {
			throw new RuntimeException(
					"Something went wrong during infomodel serialization", e);
		}
	}

	@Override
	public void refresh(IProgressMonitor monitor) {
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			// ignore exception
		}
	}

	public MappingModel getMapping(String modelName) {
		IFile mappingFile = modelLookupHelper.getModelFile(modelName
				+ ".mapping");
		return modelParser.parseModel(mappingFile, MappingModel.class);
	}
	
	@Override
	public Set<IModelElement> getReferences() {
		Set<IModelElement> references = new TreeSet<>();

		for (ModelReference modelReference : getModel().getReferences()) {
			try {
				IModelElement reference = ModelProjectServiceFactory.getDefault()
				.getProjectByModelId(
						ModelIdFactory.newInstance(getPossibleReferenceType(),
								modelReference));
				if (reference != null) {
					references.add(reference);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return references;
	}
	
	protected abstract ModelType getPossibleReferenceType();

}
