/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.bosch.things.javaclient.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class LogbackTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel model) {
		return "logback.xml"
	}
	
	override getPath(InformationModel model) {
		return "/simulator/src/main/resources"
	}
	
	override getContent(InformationModel model,InvocationContext invocationContext) {
		'''
<configuration debug="false" scan="false" scanPeriod="30 seconds">
   <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
      <encoder>
         <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
      </encoder>
   </appender>

   <logger name="com.example.things.«model.name»ThingsClient" level="info"
           additivity="false">
      <appender-ref ref="STDOUT" />
   </logger>

   <root level="${LOG_LEVEL:-WARN}">
      <appender-ref ref="STDOUT"/>
   </root>
</configuration>
		'''
	}

}
