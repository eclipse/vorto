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
package org.eclipse.vorto.codegen.hono.java

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class Log4jTemplate implements IFileTemplate<InformationModel> {
	
	override getContent(InformationModel model,InvocationContext invocationContext) {
		'''
		# Root logger option
		log4j.rootLogger=INFO, stdout
		
		# Direct log messages to stdout
		log4j.appender.stdout=org.apache.log4j.ConsoleAppender
		log4j.appender.stdout.Target=System.out
		log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
		log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
		'''
	}
	
	override getFileName(InformationModel context) {
		'''log4j.properties'''
	}
	
	override getPath(InformationModel context) {
		return Utils.getBasePath(context)+"/src/main/resources"
	}

}
