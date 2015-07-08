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
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.tests;

import org.eclipse.xtext.junit4.AbstractXtextTests

abstract class FbAbstractGeneratorTest extends AbstractXtextTests  {
	
	def void compare_With_Empty_Operation_XML(String actual,String comment){
    	val expected ='''
					<?xml version='1.0' encoding='UTF-8'?>
					<tns:service eventConsumer="false" revision="$Rev:$"
						javaPackageName="com.bosch.functionblock.demo.fridge"
						name="Fridge"
						namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
						xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
						xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
						xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
						<tns:documentation>«comment»</tns:documentation>
					</tns:service>	
					'''.toString
		assertEquals(expected,actual)
    }
}