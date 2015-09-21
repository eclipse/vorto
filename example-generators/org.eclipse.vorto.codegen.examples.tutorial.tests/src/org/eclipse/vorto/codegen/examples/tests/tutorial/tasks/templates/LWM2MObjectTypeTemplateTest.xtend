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
package org.eclipse.vorto.codegen.examples.tests.tutorial.tasks.templates

import org.eclipse.vorto.codegen.examples.tests.tutorial.AbstractTutorialTest
import org.eclipse.vorto.codegen.examples.tutorial.tasks.templates.LWM2MObjectTypeTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class LWM2MObjectTypeTemplateTest extends AbstractTutorialTest{
	
	@Test
	def testObjectTypeTemplate(){
		var mapping = createMapping("LWM2MObjectType.mapping");
		var template = new LWM2MObjectTypeTemplate(mapping);
		var output = template.getContent(createInformationModel());
		assertEquals(fetchExpected, output);
	}
	
	def fetchExpected() {
		return '''
<LWM2M xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://openmobilealliance.org/tech/profiles/LWM2M.xsd" >
  <Object ObjectType="FlyingThing">
		<Name>MyQuadcopter</Name>
		<MultipleInstances>Single</MultipleInstances>
		<Mandatory>Optional</Mandatory>
		<Description>Information model for DjiPhantomVision</Description>
	</Object>
</LWM2M>'''
	}
	

}