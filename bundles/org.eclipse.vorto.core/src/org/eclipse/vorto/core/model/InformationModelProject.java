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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.model.nature.InformationModelProjectNature;
import org.eclipse.vorto.core.parser.IModelParser;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public class InformationModelProject extends AbstractModelProject {

	public static final String INFOMODEL = "infomodel";

	public InformationModelProject(IProject project, IModelParser modelParser) {
		super(project, modelParser);
	}

	public IFile getModelFile() {
		return this.modelLookupHelper.getModelFileByExtension(INFOMODEL);
	}

	public static boolean isInformationModelProject(IProject project) {
		try {
			return project.getNature(InformationModelProjectNature.NATURE_ID) != null;
		} catch (CoreException e) {
			return false;
		}
	}

	@Override
	protected Model parseModel(IFile modelFile) {
		return modelParser.parseModel(modelFile, InformationModel.class);
	}

	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core/icons/im.png";
	}

	@Override
	protected void addModelReference(IModelElement modelElementReference) {
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
	public Set<IModelElement> getReferences() {
		Set<IModelElement> references = new TreeSet<>();

		for (ModelReference modelReference : getModel().getReferences()) {
			try {
				references.add(ModelProjectServiceFactory.getDefault()
						.getProjectByModelId(
								ModelIdFactory
										.newInstance(ModelType.FUNCTIONBLOCK,
												modelReference)));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return references;
	}

}
