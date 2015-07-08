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
package org.eclipse.vorto.editor;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.parser.IModelParser;
import org.eclipse.xtext.resource.XtextResourceSet;

/**
 * 
 * Uses the Xtext Parser to parse DSL model files
 * 
 */
public class XtextResourceModelParser implements IModelParser {

	@Override
	public <M> M parseModel(IFile modelFile, Class<M> modelClass) {
		try {

			URI uri = URI.createPlatformResourceURI(modelFile.getFullPath()
					.toString(), true);
			return parseModel(uri, modelClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <M> M parseModel(URI uri, Class<M> modelClass) {
		ResourceSet rs = new XtextResourceSet();
		if (!rs.getResource(uri, true).getContents().isEmpty()) {
			return (M) rs.getResource(uri, true).getContents().get(0);
		} else {
			return null;
		}
	}

	@Override
	public <M extends Model> M parseModel(File modelFile, Class<M> modelClass) {
		try {
			URI uri = URI.createFileURI(modelFile.getAbsolutePath());
			return parseModel(uri, modelClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
