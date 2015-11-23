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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.nature.IoTProjectNature;
import org.eclipse.vorto.core.parser.IModelParser;

public class FunctionblockModelProject extends AbstractModelProject {

	public FunctionblockModelProject(IProject project, IModelParser modelParser) {
		super(project, modelParser);
	}

	public static final String FBMODEL = "fbmodel";

	public IFile getModelFile() {
		return this.modelLookupHelper.getModelFileByExtension(FBMODEL);
	}

	public static boolean isFunctionBlockModelProject(IProject project) {
		try {
			return project.getNature(IoTProjectNature.NATURE_ID) != null;
		} catch (CoreException e) {
			return false;
		}
	}

	public Model parseModel(IFile typeFile) {
		return this.modelParser.parseModel(typeFile, FunctionblockModel.class);
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/fb.png";
	}

	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.Datatype;
	}

}
