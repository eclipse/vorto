
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
package org.eclipse.vorto.editor.mapping.tests.formatter

import com.google.inject.Inject
import org.eclipse.vorto.core.api.model.mapping.MappingModel
import org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl
import org.eclipse.vorto.editor.mapping.tests.MappingInjectorProvider
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.resource.XtextResource
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Ignore

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(MappingInjectorProvider))
class MappingModelFormatterTest extends AbstractXtextTests {
	@Inject extension ParseHelper<MappingModel> parserHelper;
	@Inject extension INodeModelFormatter formatter;

	@BeforeClass
	def static void initializeModel() {
		MappingPackageImpl.init();
	}

	@Test
	@Ignore
	def void testFormatFunctionBlockMapping() {
		val expectedText = getFunctionBlockMappingFormatted
		val rawText = getFunctionBlockMappingUnformatted
		rawText.assertFormattedAs(expectedText)
	}

	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).
			formattedText
		assertEquals(expectedText, formattedText)
	}
	
	def private getFunctionBlockMappingUnformatted() {
		return '''namespace com.mycompany.map
version 1.0.0
displayname "Color Mapping "
description "Mapping for Color"
category demo
using com.mycompany.fb.NewFunctionBlock ; 1.0.0

functionblockmapping FunctionBlock_Property { 	targetplatform mym2m

	from NewFunctionBlock.status.isOn
				to TargetStatus

			from NewFunctionBlock.configuration.configEntityA
	to TargetConfiguration
from NewFunctionBlock.fault.faultCode
	to MyFaultCode

	from NewFunctionBlock.event.Ready to TargetReadyEvent

	from NewFunctionBlock.event.Ready.entityA
	to 
	TargetReadyEvent with { default : "NotReady" }

	from NewFunctionBlock.operation.on,
NewFunctionBlock.operation.off
	to TargetOperations
}'''
	}
	
	def private getFunctionBlockMappingFormatted() {
		return '''namespace com.mycompany.map
version 1.0.0
displayname "Color Mapping "
description "Mapping for Color"
category demo
using com.mycompany.fb.NewFunctionBlock ; 1.0.0

functionblockmapping FunctionBlock_Property {
	targetplatform mym2m

	from NewFunctionBlock.status.isOn
	to TargetStatus

	from NewFunctionBlock.configuration.configEntityA
	to TargetConfiguration

	from NewFunctionBlock.fault.faultCode
	to MyFaultCode

	from NewFunctionBlock.event.Ready
	to TargetReadyEvent

	from NewFunctionBlock.event.Ready.entityA
	to TargetReadyEvent with { default : "NotReady" }

	from NewFunctionBlock.operation.on, NewFunctionBlock.operation.off
	to TargetOperations
}'''
	}	
}
