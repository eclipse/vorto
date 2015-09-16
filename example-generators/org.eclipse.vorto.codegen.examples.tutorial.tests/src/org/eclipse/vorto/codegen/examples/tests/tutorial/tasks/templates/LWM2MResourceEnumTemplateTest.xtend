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
import org.eclipse.vorto.codegen.examples.tutorial.tasks.templates.LWM2MResourceEnumTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * 
 */
class LWM2MResourceEnumTemplateTest extends AbstractTutorialTest {
	@Test
	def testResourceTypeFromEnumTemplate() {
		var mapping = createMapping("LWM2MResourceEnum.mapping");
		var template = new LWM2MResourceEnumTemplate(mapping);
		var output = template.getContent(createInformationModel());
		assertEquals(fetchExpected, output);
	}

	def fetchExpected() {
		return  '''
<Resource name="Directions">
	<Type>
		<Name>N</Name>
		<Units>Moving North</Units>
	</Type>		
	<Type>
		<Name>S</Name>
		<Units>Moving South</Units>
	</Type>		
	<Type>
		<Name>E</Name>
		<Units>Moving  East</Units>
	</Type>		
	<Type>
		<Name>W</Name>
		<Units>Moving West</Units>
	</Type>		
</Resource>
'''
	}
}