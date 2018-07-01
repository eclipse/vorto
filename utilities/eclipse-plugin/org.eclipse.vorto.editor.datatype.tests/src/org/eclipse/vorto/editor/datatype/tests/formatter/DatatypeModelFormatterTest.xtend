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
package org.eclipse.vorto.editor.datatype.tests.formatter

import com.google.inject.Inject
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl
import org.eclipse.vorto.editor.datatype.tests.DatatypeInjectorProvider
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
@InjectWith(typeof(DatatypeInjectorProvider))
class DatatypeModelFormatterTest extends AbstractXtextTests {
	@Inject extension ParseHelper<Type> parserHelper;
	@Inject extension INodeModelFormatter formatter;
	
	@BeforeClass
	def static void initializeModel() {
		DatatypePackageImpl.init();
	}
	
	@Test
	def void testFormattingEntity() {
		val expectedText = getEntityColorFormatted()
		val rawText = getEntityColorUnFormatted()
		rawText.assertFormattedAs(expectedText)
	}
	
	def String getEntityColorFormatted(){
		return '''namespace org.eclipse.vorto.example
version 1.0.0
displayname "Color"
description "Type for Color"
category demo

entity Color {
	mandatory r as int
	mandatory g as int
	mandatory b as int
}'''
	}
	
	def String getEntityColorUnFormatted(){
		return '''namespace org.eclipse.vorto.example version 1.0.0 
displayname "Color" description "Type for Color" category demo
entity Color {mandatory r as int mandatory g as int mandatory b as int }
'''
	}
	
	@Test
	def void testFormattingEnum() {
		val expectedText = getEnumColorFormatted
		val rawText = getEnumColorUnFormatted
		rawText.assertFormattedAs(expectedText)
	}

	def String getEnumColorFormatted(){
		return '''namespace org.eclipse.vorto.example
version 1.0.0
displayname "Color"
description "Type for Color"
category demo

enum Color {
	RED,
	GREEN,
	BLUE
}'''
	}
	
	def String getEnumColorUnFormatted(){
		return '''namespace org.eclipse.vorto.example version 1.0.0 
displayname "Color" description "Type for Color" category demo
enum Color {RED, GREEN, BLUE}'''
	}
		
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}
}