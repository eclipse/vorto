package org.eclipse.vorto.editor.functionblock.tests.formatter

import com.google.inject.Inject
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl
import org.eclipse.vorto.editor.functionblock.FunctionblockInjectorProvider
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.resource.XtextResource
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(FunctionblockInjectorProvider))
class FunctionblockModelFormatterTest extends AbstractXtextTests {
	@Inject extension ParseHelper<FunctionblockModel> parserHelper;
	@Inject extension INodeModelFormatter formatter;
	
	@BeforeClass
	def static void initializeModel() {
		FunctionblockPackageImpl.init();
	}
	
	@Test
	def void testFormattingForBasicBlock() {
		val expectedText = getFormatted
		val rawText = getUnFormatted
		rawText.assertFormattedAs(expectedText)
	}
	
	def String getFormatted(){
		'''namespace org.eclipse.vorto.example
version 1.0.0
displayname "BinarySwitch"
description "Function block model for BinarySwitch"
category example
using eclipse.vorto.type.OnOff ; 1.0.0
using eclipse.vorto.type.OnOffUnit ; 1.0.0

functionblock BinarySwitch {
	status {
		mandatory currentStatus as OnOff
	}

	operations {
		on()
		off()
		toggle()
		setStatus(targetStatus as OnOffUnit) returns OnOff
	}

}'''
	}
	
		def String getUnFormatted(){
			return '''namespace org.eclipse.vorto.example version 1.0.0 
			displayname "BinarySwitch" description "Function block model for BinarySwitch" category example
			using eclipse.vorto.type.OnOff ; 1.0.0 using eclipse.vorto.type.OnOffUnit ; 1.0.0 functionblock BinarySwitch {	status { mandatory currentStatus as OnOff} operations {	on()	off() toggle() setStatus(targetStatus as OnOffUnit) returns OnOff}}'''
		}
	
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}
}