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

import java.util.Arrays
import org.eclipse.vorto.codegen.examples.tests.tutorial.AbstractTutorialTest
import org.eclipse.vorto.codegen.examples.tutorial.tasks.templates.LWM2MObjectTypeCompleteTemplate
import org.eclipse.vorto.core.model.IMapping
import org.eclipse.vorto.core.model.MappingResourceFactory
import org.junit.Test

import static org.junit.Assert.assertEquals

class LWM2MObjectTypeCompleteTemplateTest extends AbstractTutorialTest {

	@Test
	def testObjectTypeCompleteTemplate() {
		var template = new LWM2MObjectTypeCompleteTemplate(mapping);
		var output = template.getContent(createInformationModel());
		assertEquals(fetchExpected, output);
	}

	def IMapping getMapping() {
		var infomodelMappingModel = createMappingModel("LWM2MObjectTypeComplete.mapping");
		var functionblockMapping = createMapping("LWM2MResourceComplete.mapping");
		return MappingResourceFactory.getInstance().createMapping(infomodelMappingModel,
			Arrays.asList(functionblockMapping));
	}

	def fetchExpected() {
		return '''
<LWM2M xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://openmobilealliance.org/tech/profiles/LWM2M.xsd" >
  <Object ObjectType="FlyingThing">
		<Name>MyQuadcopter</Name>
		<MultipleInstances>Single</MultipleInstances>
		<Mandatory>Optional</Mandatory>
		<Description>Information model for MyQuadcopter</Description>
		<Resources>
			    <Item>
			    	<Name>Altitude</Name>
			    	<Operations>Ascend, Descend</Operations>
			    	<MultipleInstances>Single</MultipleInstances>
			    	<Mandatory>Mandatory</Mandatory>
			    	<Type></Type>
			    	<RangeEnumeration></RangeEnumeration>
			    	<Units>m</Units>
			    	<Description><![CDATA[The decimal notation of altitude in meters above sea level.]]></Description>
			    </Item>
			    <Item>
			    	<Name>Latitude</Name>
			    	<Operations>Forward, Back</Operations>
			    	<MultipleInstances>Single</MultipleInstances>
			    	<Mandatory>Mandatory</Mandatory>
			    	<Type></Type>
			    	<RangeEnumeration></RangeEnumeration>
			    	<Units>deg</Units>
			    	<Description><![CDATA[The decimal notation of latitude, e.g. -43.5723 [World Geodetic System 1984].]]></Description>
			    </Item>
			    <Item>
			    	<Name>Longitude</Name>
			    	<Operations>Left, Right</Operations>
			    	<MultipleInstances>Single</MultipleInstances>
			    	<Mandatory>Mandatory</Mandatory>
			    	<Type></Type>
			    	<RangeEnumeration></RangeEnumeration>
			    	<Units>deg</Units>
			    	<Description><![CDATA[The decimal notation of longitude, e.g. 153.21760 [World Geodetic System 1984].]]></Description>
			    </Item>
		</Resources>	
	</Object>
</LWM2M>
'''
	}

}