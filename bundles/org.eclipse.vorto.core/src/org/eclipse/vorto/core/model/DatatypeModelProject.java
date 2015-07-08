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

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.model.nature.FbDatatypeProjectNature;
import org.eclipse.vorto.core.parser.IModelParser;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public class DatatypeModelProject extends AbstractModelProject {

	public static final String DATATYPE = "type";

	public DatatypeModelProject(IProject project, IModelParser modelParser) {
		super(project, modelParser);
	}

	public IFile getModelFile() {
		return this.modelLookupHelper.getModelFileByExtension(DATATYPE);
	}

	public static boolean isDatatypeModelProject(IProject project) {
		try {
			return project.isOpen()
					&& project.getNature(FbDatatypeProjectNature.NATURE_ID) != null;
		} catch (CoreException e) {
			return false;
		}
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core/icons/dt.png";
	}

	@Override
	protected Model parseModel(IFile modelFile) {
		return this.modelParser.parseModel(modelFile, Type.class);
	}

	@Override
	public Set<IModelElement> getReferences() {
		Set<IModelElement> references = new TreeSet<>();

		for (ModelReference modelReference : getModel().getReferences()) {
			try {
				references.add(ModelProjectServiceFactory.getDefault()
						.getProjectByModelId(
								ModelIdFactory.newInstance(ModelType.DATATYPE,
										modelReference)));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return references;
	}
}
