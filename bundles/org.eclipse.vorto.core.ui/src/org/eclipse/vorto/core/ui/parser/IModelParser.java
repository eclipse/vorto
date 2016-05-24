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

package org.eclipse.vorto.core.ui.parser;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;

/**
 * Parser for different models
 */
public interface IModelParser {

	public static final String EXTENSIONPOINT_ID = "org.eclipse.vorto.core.org_eclipse_vorto_core_ModelParser";

	/**
	 * Parses a given model file
	 * 
	 * @param modelFile
	 *            file to parse
	 * @param modelClass
	 *            expected model type @see {@link InformationModel},
	 *            {@link FunctionblockModel} {@link Type}
	 * @return model object
	 */
	<M extends Model> M parseModel(IFile modelFile, Class<M> modelClass);

	/**
	 * Parses a given model file
	 * 
	 * @param modelFile
	 *            java.io.File to parse
	 * @param modelClass
	 *            expected model type @see {@link InformationModel},
	 *            {@link FunctionblockModel} {@link Type}
	 * @return model object
	 */
	<M extends Model> M parseModel(File modelFile, Class<M> modelClass);
}
