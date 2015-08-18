
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
		return '''namespace com.mycompany.map
version 1.0.0
using com.mycompany.DummyMapping ; 1.0.0
using com.mycompany.NewInfomodel ; 1.0.0
using com.mycompany.fb.NewFunctionBlock ; 1.0.0
using com.mycompany.type.EntityA ; 1.0.0
using com.mycompany.type.EntityB ; 1.0.0
mapping AllInOne {

	datatype EntityAMapping {

		from EntityA.entityB 
			ref EntityBMapping
	}

	datatype EntityBMapping {

		from EntityB.entityC.intParam
			to MyIntParam with { DefaultValue : "0" }
	}

	functionblock MyFunctionBlockMapping {
		from NewFunctionBlock.configuration.configEntityA 
				ref EntityAMapping
	}

	from NewInfomodel.myfunctionBlock 
				ref MyFunctionBlockMapping
}'''
	}
	
	def private getformatted() {
		return '''namespace com.mycompany.map
version 1.0.0
using com.mycompany.DummyMapping ; 1.0.0
using com.mycompany.NewInfomodel ; 1.0.0
using com.mycompany.fb.NewFunctionBlock ; 1.0.0
using com.mycompany.type.EntityA ; 1.0.0
using com.mycompany.type.EntityB ; 1.0.0
mapping AllInOne {

	datatype EntityAMapping {

		from EntityA.entityB
		ref EntityBMapping
	}

	datatype EntityBMapping {

		from EntityB.entityC.intParam
		to MyIntParam with { DefaultValue : "0" }
	}

	functionblock MyFunctionBlockMapping {

		from NewFunctionBlock.configuration.configEntityA
		ref EntityAMapping
	}

	from NewInfomodel.myfunctionBlock
	ref MyFunctionBlockMapping
}'''
	}	
}
