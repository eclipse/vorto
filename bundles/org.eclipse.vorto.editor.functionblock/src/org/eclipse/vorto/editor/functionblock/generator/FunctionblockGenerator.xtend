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

package org.eclipse.vorto.editor.functionblock.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.vorto.core.model.FunctionblockModelProject
import org.eclipse.vorto.core.service.ModelProjectServiceFactory
import org.eclipse.vorto.editor.EclipseFileSystemAccessOutputter
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

/**
 * Generates code from your model files on save.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#TutorialCodeGeneration
 */
class FunctionblockGenerator implements IGenerator {

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		var vfs = fsa as EclipseFileSystemAccessOutputter

		if (FunctionblockModelProject.isFunctionBlockModelProject(vfs.getIProject)) {
			var iotProject = ModelProjectServiceFactory.getDefault.getProjectFromEclipseProject(vfs.getIProject);
			ModelProjectServiceFactory.getDefault.save(iotProject)
		}
	}
}
