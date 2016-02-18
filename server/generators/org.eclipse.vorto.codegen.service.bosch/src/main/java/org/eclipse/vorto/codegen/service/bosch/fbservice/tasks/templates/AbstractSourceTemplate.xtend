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

package org.eclipse.vorto.codegen.service.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.service.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

abstract class AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {

	override getPath(FunctionblockModel ctx) {
		var SRC_LOC = '''com.bosch.« ctx.name.toLowerCase»-service/src/main/java''';
		return SRC_LOC + "/" + new FbModelWrapper(ctx).javaPackageName.toString().replaceAll("\\.", "/") + "/"+subPath;
	}
	
	def abstract String getSubPath();
}
