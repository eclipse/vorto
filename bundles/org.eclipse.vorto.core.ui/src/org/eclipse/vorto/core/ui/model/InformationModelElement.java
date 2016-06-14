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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.parser.IModelParser;
import org.eclipse.vorto.core.ui.parser.ParseModelResult;

public class InformationModelElement extends AbstractModelElement {
	
	private IFile modelFile;
	
	private InformationModel model;
	
	private Collection<Resource.Diagnostic> diagnostics;
	
	private ModelType[] possibleReferenceTypes = new ModelType[] { ModelType.Functionblock };

	public InformationModelElement(IModelProject modelProject, IFile modelFile, IModelParser modelParser) {
		super(modelProject);
		this.modelFile = modelFile;
		// this.model = modelParser.parseModel(modelFile, InformationModel.class);
		ParseModelResult<InformationModel> parseResult = modelParser.parseModelWithError(modelFile, InformationModel.class);
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
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/im-20x20.png";
	}
	
	@Override
	protected String getErrorImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/im-error.png";
	}

	@Override
	protected ModelType[] getPossibleReferenceTypes() {
		return possibleReferenceTypes;
	}
	
	@Override
	public void addModelReference(IModelElement modelElementReference) {
		
		ModelReference modelReference = modelElementReference.getId()
				.asModelReference();
		if (!containsModelReference(modelReference)) {
			getModel().getReferences().add(modelReference);
			getModel().eResource().getContents()
					.add(modelElementReference.getModel());
		}
		InformationModel infomodel = (InformationModel) getModel();
		infomodel.getProperties().add(
				createFunctionblockProperty(
						(FunctionblockModel) modelElementReference.getModel(),
						getVariableNames(infomodel.getProperties())));
	}
	
	private boolean containsModelReference(ModelReference reference) {
		for (ModelReference ref : getModel().getReferences()) {
			if (ref.getImportedNamespace().equals(
					reference.getImportedNamespace())) {
				return true;
			}
		}

		return false;
	}
	
	private Set<String> getVariableNames(EList<FunctionblockProperty> properties) {
		Set<String> variableNames = new HashSet<String>();
		for (FunctionblockProperty property : properties) {
			variableNames.add(property.getName());
		}
		return variableNames;
	}

	private FunctionblockProperty createFunctionblockProperty(
			FunctionblockModel fbm, Set<String> existingVariableNames) {
		FunctionblockProperty fbp = InformationModelFactory.eINSTANCE
				.createFunctionblockProperty();
		fbp.setType(fbm);
		fbp.setName(generateFunctionBlockVariableName(fbm,
				existingVariableNames));
		return fbp;
	}
	
	private String generateFunctionBlockVariableName(FunctionblockModel fbm,
			Set<String> variableNames) {
		String variableName = fbm.getName().toLowerCase();
		int i = 0;
		while (variableNames.contains(variableName)) {
			variableName += ++i;
		}
		return variableName;
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
		InformationModelElement other = (InformationModelElement) obj;
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
}
