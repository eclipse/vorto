/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class WebXmlTemplate implements ITemplate<FunctionblockProperty> {
		
	override getContent(FunctionblockProperty fbProperty) {
		return '''
		<web-app id="«fbProperty.name»" version="2.4"
			xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee 
			http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
			<display-name>Sample Rest Service Generated from Sample REST Generator</display-name>
		
			<servlet>
				<servlet-name>jersey-serlvet</servlet-name>
				<servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>		
		        <init-param>
		            <param-name>com.sun.jersey.api.json.POJOMappingFeature</param-name>
		            <param-value>true</param-value>
		        </init-param>			
				<load-on-startup>1</load-on-startup>
			</servlet>
			
			<servlet-mapping>
				<servlet-name>jersey-serlvet</servlet-name>
				<url-pattern>/service/*</url-pattern>
			</servlet-mapping>
		
		</web-app>'''
	}
}