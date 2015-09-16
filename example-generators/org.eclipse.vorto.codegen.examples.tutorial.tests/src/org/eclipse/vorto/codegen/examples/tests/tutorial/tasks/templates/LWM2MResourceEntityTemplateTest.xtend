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
import org.eclipse.vorto.codegen.examples.tutorial.tasks.templates.LWM2MResourceEntityTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * 
 */
class LWM2MResourceEntityTemplateTest extends AbstractTutorialTest {
	@Test
	def testResourceTypeFromEntityTemplate() {
		var mapping = createMapping("LWM2MResourceEntity.mapping");
		var template = new LWM2MResourceEntityTemplate(mapping);
		var output = template.getContent(createInformationModel());
		assertEquals(fetchExpected, output);
	}

	def fetchExpected() {
		return  '''
<Resource name="MovingObjectDefinition">
	<Entity>
		<Name>Altitude</Name>
		<Units>m</Units>
	</Entity>		
	<Entity>
		<Name>Latitude</Name>
		<Units>deg</Units>
	</Entity>		
	<Entity>
		<Name>Longitude</Name>
		<Units>deg</Units>
	</Entity>		
</Resource>	
'''
	}
}