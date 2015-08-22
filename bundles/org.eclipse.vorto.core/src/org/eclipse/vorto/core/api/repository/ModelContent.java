/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.core.api.repository;

import java.util.List;

import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.ModelType;

/**
 *	This class represents a resource that we can get from
 *  the repository. 
 */
public class ModelContent {
	
	private byte[] content;
	private Model model;
	private ModelType modelType;
	private List<ModelContent> references;
	
	public ModelContent(byte[] content, ModelType type, Model model,
			List<ModelContent> references) {
		this.content = content;
		this.modelType = type;
		this.model = model;
		this.references = references;
	}
	
	/**
	 * The actual content in byte array of this resource
	 * @return
	 */
	public byte[] getModelContent() {
		return content;
	}

	/**
	 * Returns the EMF metamodel of this resource
	 * 
	 * @return
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Returns the type of this resource
	 * @return
	 */
	public ModelType getType() {
		return modelType;
	}

	/**
	 * Returns the dependencies of this resource
	 * @return
	 */
	public List<ModelContent> getReferences() {
		return references;
	}
}

