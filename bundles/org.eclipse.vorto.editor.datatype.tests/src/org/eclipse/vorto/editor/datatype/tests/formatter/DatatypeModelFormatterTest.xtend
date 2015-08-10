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
import org.eclipse.vorto.editor.datatype.DatatypeInjectorProvider
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
		val expectedText = this.readFileIntoString("resources/EntityColorFormatted.type")
		val rawText = this.readFileIntoString("resources/EntityColorUnformatted.type")
		rawText.assertFormattedAs(expectedText)
	}
	
	@Test
	def void testFormattingEnum() {
		val expectedText = this.readFileIntoString("resources/EnumColorFormatted.type")
		val rawText = this.readFileIntoString("resources/EnumColorUnformatted.type")
		rawText.assertFormattedAs(expectedText)
	}
	
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}
}