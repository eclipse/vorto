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

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.parser.IModelParser;

public class DatatypeModelElement extends AbstractModelElement {
	
	private IFile modelFile;
	
	private Type model;

	public DatatypeModelElement(IModelProject modelProject, IFile modelFile, IModelParser modelParser) {
		super(modelProject);
		this.modelFile = modelFile;
		this.model = modelParser.parseModel(modelFile, Type.class);
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
	protected String getImageURLAsString() {
		Model model = getModel();
		if(model instanceof Entity) 
			return "platform:/plugin/org.eclipse.vorto.core.ui/icons/dt_entity.png";
		else if(model instanceof Enum)
			return "platform:/plugin/org.eclipse.vorto.core.ui/icons/dt_enum.png";
		else 
			return "platform:/plugin/org.eclipse.vorto.core.ui/icons/dt.png";
	}

	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.Datatype;
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
		DatatypeModelElement other = (DatatypeModelElement) obj;
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
