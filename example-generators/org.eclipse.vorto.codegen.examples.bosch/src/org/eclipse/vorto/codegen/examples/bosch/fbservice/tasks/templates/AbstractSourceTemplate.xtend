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

package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.IFileTemplate
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

abstract class AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {

	private String SRC_LOC = "/src/main/java/";

	override getPath(FunctionblockModel ctx) {
		return SRC_LOC + "/" + new FbModelWrapper(ctx).javaPackageName.toString().replaceAll("\\.", "/") + "/"+subPath;
	}
	
	def abstract String getSubPath();
}
