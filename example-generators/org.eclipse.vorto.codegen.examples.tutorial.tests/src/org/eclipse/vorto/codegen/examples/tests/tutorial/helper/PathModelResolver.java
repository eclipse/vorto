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
package org.eclipse.vorto.codegen.examples.tests.tutorial.helper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.xtext.resource.XtextResourceSet;

/**
 * Resolve model from Path, with dependencies
 * @param <Context>
 */
public class PathModelResolver<Context> {

	@SuppressWarnings("unchecked")
	public Context resolveModel(File modelFile, Path dependenciesPath) {
		InformationModelStandaloneSetup.doSetup();
		XtextResourceSet resourceSet = new XtextResourceSet();
		try {
			List<Path> allDependencies = new PathSearcher().findPathsWithExtension(dependenciesPath, "*");
			this.addRelatedResourceToResourceSet(resourceSet, allDependencies);
			Resource resource = getResourceFromModelFile(resourceSet, modelFile);
			return (Context) resource.getContents().get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void addRelatedResourceToResourceSet(XtextResourceSet resourceSet, List<Path> allModelPathes) {
		for (Path modelPath : allModelPathes) {
			resourceSet.createResource(URI.createFileURI(modelPath.toFile().getAbsolutePath()));
		}
	}

	private Resource getResourceFromModelFile(XtextResourceSet resourceSet, File modelFile) {
		URI modelFileUri = URI.createFileURI(modelFile.getAbsolutePath());
		return resourceSet.getResource(modelFileUri, true);
	}

}
