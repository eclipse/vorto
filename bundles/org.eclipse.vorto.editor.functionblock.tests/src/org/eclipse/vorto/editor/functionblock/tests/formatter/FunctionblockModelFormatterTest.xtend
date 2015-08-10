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
		val expectedText = this.readFileIntoString("resources/BinarySwitchFormatted.fbmodel")
		val rawText = this.readFileIntoString("resources/BinarySwitchUnformatted.fbmodel")
		rawText.assertFormattedAs(expectedText)
	}
	
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}
}