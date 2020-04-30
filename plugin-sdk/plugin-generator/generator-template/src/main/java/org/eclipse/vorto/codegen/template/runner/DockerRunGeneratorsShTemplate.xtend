/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class DockerRunGeneratorsShTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'run_generators.sh'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/docker'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
SPRING_APPLICATION_JSON=$(jq .generators /gen/config/config.json | sed $'s/\r//' | tr -d '\n');
export SPRING_APPLICATION_JSON
java -jar /gen/generators.jar;
		'''
	}
	
}
