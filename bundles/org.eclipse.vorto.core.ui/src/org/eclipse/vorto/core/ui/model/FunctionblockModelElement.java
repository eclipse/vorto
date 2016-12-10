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

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.parser.IModelParser;
import org.eclipse.vorto.core.ui.parser.ParseModelResult;

public class FunctionblockModelElement extends AbstractModelElement {
	
	private IFile modelFile;
	
	private FunctionblockModel model;
	
	private Collection<Resource.Diagnostic> diagnostics;
	
	private ModelType[] possibleReferenceTypes = new ModelType[] { ModelType.Datatype, ModelType.Functionblock };
	
	private IModelParser modelParser;

	public FunctionblockModelElement(IModelProject modelProject, IFile modelFile, IModelParser modelParser) {
		super(modelProject);
		this.modelFile = modelFile;
		this.modelParser = modelParser;
		//this.model = modelParser.parseModel(modelFile, FunctionblockModel.class);
		ParseModelResult<FunctionblockModel> parseResult = modelParser.parseModelWithError(modelFile, FunctionblockModel.class);
		this.model = parseResult.getModel();
		this.diagnostics = parseResult.getErrors();
	}

	@Override
	public IFile getModelFile() {
		return modelFile;
	}

	@Override
	public Model getModel() {
		return model;
	}
	
	@Override
	public Collection<Resource.Diagnostic> getDiagnostics() { 
		return diagnostics; 
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/fb.png";
	}
	
	@Override
	protected String getErrorImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/fb-error.png";
	}

	@Override
	protected ModelType[] getPossibleReferenceTypes() {
		return possibleReferenceTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((modelFile == null) ? 0 : modelFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionblockModelElement other = (FunctionblockModelElement) obj;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (modelFile == null) {
			if (other.modelFile != null)
				return false;
		} else if (!modelFile.equals(other.modelFile))
			return false;
		return true;
	}

	@Override
	public void reload() {
		ParseModelResult<FunctionblockModel> parseResult = modelParser.parseModelWithError(modelFile, FunctionblockModel.class);
		this.model = parseResult.getModel();
		this.diagnostics = parseResult.getErrors();
	}

	
}
