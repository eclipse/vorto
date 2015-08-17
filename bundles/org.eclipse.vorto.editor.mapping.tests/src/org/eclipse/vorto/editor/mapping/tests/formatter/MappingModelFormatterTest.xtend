
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
import org.eclipse.vorto.editor.mapping.MappingInjectorProvider
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.resource.XtextResource
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

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
	def void testFormattingForBasicBlock() {
		val expectedText = getformatted
		val rawText = getUnformatted
		rawText.assertFormattedAs(expectedText)
	}

	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).
			formattedText
		assertEquals(expectedText, formattedText)
	}
	
	def private getUnformatted() {
		return '''namespace com.mycompany
version 1.0.0
using com.mycompany.MyDevice ; 1.0.0
using com.mycompany.MyFunctionBlock ; 1.0.0
using com.mycompany.type.EntityA ; 1.0.0
using com.mycompany.type.EnumA ; 1.0.0
mapping MyPlatformMapping {

	from MyDevice
				to targetdevice with { Attribute1 : "i1", Attribute2 : "i2" }

		from MyDevice.name
	to DummyTarget

	from MyDevice.namespace
		to DummyTarget

	from MyDevice.version to DummyTarget

	from MyDevice.description
	to DummyTarget

	from MyDevice.category
	to DummyTarget

	from MyDevice.displayname
		to DummyTarget

	from MyDevice.myfunctionblock
	to targetFunctionBlock

	from MyDevice.myfunctionblock.name
	to DummyTarget

	from MyDevice.myfunctionblock.configuration.entityAParam.name
		to param

	from MyDevice.myfunctionblock.configuration.entityAParam.paramRefEntityB
	to param

			functionblock {

		from MyFunctionBlock
			to DummyTarget with { Attribute1 : "f1", Attribute2 : "f2" }

		from MyFunctionBlock.category
			to DummyTarget

		from MyFunctionBlock.operation.off
			to TargetOperation

			from MyFunctionBlock.configuration.entityAParam
		to DummyTarget

		from MyFunctionBlock.status.entityAParam
			to DummyTarget

		from MyFunctionBlock.status.entityAParam.paramRefEntityB
			to DummyTarget

		from MyFunctionBlock.status.enumAParam
			to TargetEnum

		from MyFunctionBlock.status.enumAParam.name to TargetEnumName
	}

		datatype {
		
			from EntityA
		to DummyTarget with { Attribute1 : "d1", Attribute2 : "d2" }

		from EntityA.name
		to aDummyTarget

		from EntityA.version
			to DummyTarget

		from EntityA.paramRefEntityB.paramRefEntityC.paramRefEntityD
		to DummyTarget

			from EnumA
		to DummyTarget with { Value1 : "dummy value 1", Value2 : "dummy value 2" }

			from EnumA.name
		to Dummy
	}
}'''
	}
	
	def private getformatted() {
		return '''namespace com.mycompany
version 1.0.0
using com.mycompany.MyDevice ; 1.0.0
using com.mycompany.MyFunctionBlock ; 1.0.0
using com.mycompany.type.EntityA ; 1.0.0
using com.mycompany.type.EnumA ; 1.0.0
mapping MyPlatformMapping {

	from MyDevice
	to targetdevice with { Attribute1 : "i1", Attribute2 : "i2" }

	from MyDevice.name
	to DummyTarget

	from MyDevice.namespace
	to DummyTarget

	from MyDevice.version
	to DummyTarget

	from MyDevice.description
	to DummyTarget

	from MyDevice.category
	to DummyTarget

	from MyDevice.displayname
	to DummyTarget

	from MyDevice.myfunctionblock
	to targetFunctionBlock

	from MyDevice.myfunctionblock.name
	to DummyTarget

	from MyDevice.myfunctionblock.configuration.entityAParam.name
	to param

	from MyDevice.myfunctionblock.configuration.entityAParam.paramRefEntityB
	to param

	functionblock {

		from MyFunctionBlock
		to DummyTarget with { Attribute1 : "f1", Attribute2 : "f2" }

		from MyFunctionBlock.category
		to DummyTarget

		from MyFunctionBlock.operation.off
		to TargetOperation

		from MyFunctionBlock.configuration.entityAParam
		to DummyTarget

		from MyFunctionBlock.status.entityAParam
		to DummyTarget

		from MyFunctionBlock.status.entityAParam.paramRefEntityB
		to DummyTarget

		from MyFunctionBlock.status.enumAParam
		to TargetEnum

		from MyFunctionBlock.status.enumAParam.name
		to TargetEnumName
	}

	datatype {

		from EntityA
		to DummyTarget with { Attribute1 : "d1", Attribute2 : "d2" }

		from EntityA.name
		to aDummyTarget

		from EntityA.version
		to DummyTarget

		from EntityA.paramRefEntityB.paramRefEntityC.paramRefEntityD
		to DummyTarget

		from EnumA
		to DummyTarget with { Value1 : "dummy value 1", Value2 : "dummy value 2" }

		from EnumA.name
		to Dummy
	}
}'''
	}	
}
