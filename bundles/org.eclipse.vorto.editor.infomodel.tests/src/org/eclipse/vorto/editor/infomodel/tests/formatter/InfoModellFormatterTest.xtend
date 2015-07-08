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
package org.eclipse.vorto.editor.infomodel.tests.formatter

import com.google.inject.Inject
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl
import org.eclipse.vorto.editor.infomodel.InformationModelInjectorProvider
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.xtext.junit.util.ParseHelper
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.resource.XtextResource
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(InformationModelInjectorProvider))
class InfoModellFormatterTest extends AbstractXtextTests {

	@Inject extension ParseHelper<InformationModel> parserHelper;
	@Inject extension INodeModelFormatter formatter;

	@BeforeClass
	def static void initializeModel() {
		InformationModelPackageImpl.init();
	}
		
	@Test
	def void testFormattingForBasicBlock() {
		val expectedText = '''
		namespace www.bosch.com
		version 1.2.3
		infomodel Computer {
			displayname "Computer"
			description "A Super gadget"
			category home
			functionblocks {
				Hardisk , Memory
			}
		}'''
		val rawText = '''namespace www.bosch.com version 1.2.3 infomodel Computer {	displayname "Computer" 	description "A Super gadget" 	category home
			functionblocks { Hardisk , Memory } }'''
		rawText.assertFormattedAs(expectedText)
	}
	
	
	@Test
	def void testSingleTabIndendation() {
		val expectedText = '''
		namespace mynamespace.com.sg
		version 1.1.1
		infomodel someIM {
			displayname "someIM"
			description "IoT Bulb"
			category light
			functionblocks {
				Lamp , Remote
			}
		}'''
		
		val rawText = '''
		namespace mynamespace.com.sg
		version 1.1.1
		infomodel someIM
		{
		displayname "someIM"
		description "IoT Bulb"
		category light
		functionblocks
		{
		Lamp,Remote
		}
		}'''
		rawText.assertFormattedAs(expectedText)
	}
	
	@Test
	def void testNoDoubleIndendation() {
		val expectedText = '''
		namespace mynamespace.com.sg
		version 1.1.1
		infomodel someIM {
			displayname "someIM"
			description "IoT Bulb"
			category light
			functionblocks {
				Lamp , Remote
			}
		}'''
		
			val rawText = '''
			namespace mynamespace.com.sg
			version 1.1.1
			infomodel someIM {
			displayname "someIM"
			description "IoT Bulb"
			category light
			functionblocks {
				Lamp , Remote
			}
	}'''
		expectedText.assertNotFormattedAs(rawText)
	}


	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}

	def void assertNotFormattedAs(CharSequence input, CharSequence unexpected) {
		val unExpectedText = unexpected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertNotEquals(unExpectedText, formattedText)
	}
}
