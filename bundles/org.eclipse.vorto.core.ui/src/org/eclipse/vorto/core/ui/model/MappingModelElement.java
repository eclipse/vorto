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
package org.eclipse.vorto.core.ui.model;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.parser.IModelParser;
import org.eclipse.vorto.core.ui.parser.ParseModelResult;

public class MappingModelElement extends AbstractModelElement {
	
	private IFile modelFile;
	
	private MappingModel model;
	
	private Collection<Resource.Diagnostic> diagnostics; 
	
	private ModelType[] possibleReferenceTypes = new ModelType[] { ModelType.Datatype };

	public MappingModelElement(IModelProject modelProject, IFile modelFile, IModelParser modelParser) {
		super(modelProject);
		this.modelFile = modelFile;
		//this.model = modelParser.parseModel(modelFile, MappingModel.class);
		ParseModelResult<MappingModel> parseResult = modelParser.parseModelWithError(modelFile, MappingModel.class);
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
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/mapping.png";
	}
	
	@Override
	protected String getErrorImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/mapping.png";
	}

	@Override
	protected ModelType[] getPossibleReferenceTypes() {
		return possibleReferenceTypes;
	}

}
