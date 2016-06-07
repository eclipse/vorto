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
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.resource.XtextResource
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.eclipse.vorto.editor.infomodel.tests.InformationModelInjectorProvider

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
		val expectedText = getFormattedColorLamp
		val rawText = getUnFormattedColorLamp
		rawText.assertFormattedAs(expectedText)
	}

	def String getFormattedColorLamp(){
		return '''namespace org.eclipse.vorto.example
version 1.0.0
displayname "ColorLamp"
description "Information model for a standard color lamp."
category example
using eclipse.vorto.basic.BinarySwitch ; 1.0.0
using com.mycompany.fb.RGBColorPicker ; 1.0.0
using com.mycompany.fb.Dimmer ; 1.0.0

infomodel ColorLamp {

	functionblocks {
		binaryswitch as BinarySwitch
		rgbcolorpicker as RGBColorPicker
		dimmer as Dimmer
	}
}'''
	}
	
	def String getUnFormattedColorLamp(){
		return '''namespace org.eclipse.vorto.example version 1.0.0 
displayname "ColorLamp" description "Information model for a standard color lamp." category example 		
		using eclipse.vorto.basic.BinarySwitch ; 1.0.0 using com.mycompany.fb.RGBColorPicker ; 1.0.0 using com.mycompany.fb.Dimmer ; 1.0.0
infomodel ColorLamp {functionblocks {binaryswitch as BinarySwitch rgbcolorpicker as RGBColorPicker 
		dimmer as Dimmer
	}
}'''
	}
	
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}
}
