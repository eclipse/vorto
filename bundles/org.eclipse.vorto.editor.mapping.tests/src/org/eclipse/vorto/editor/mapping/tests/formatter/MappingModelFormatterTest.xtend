package org.eclipse.vorto.editor.mapping.tests.formatter

import com.google.inject.Inject
import org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl
import org.eclipse.vorto.editor.mapping.MappingInjectorProvider
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.vorto.core.api.model.mapping.MappingModel
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
		val expectedText = this.readFileIntoString("resources/MyDeviceFormatted.mapping")
		val rawText = this.readFileIntoString("resources/MyDeviceUnformatted.mapping")
		rawText.assertFormattedAs(expectedText)
	}
	
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		val expectedText = expected.toString
		val formattedText = (input.parse.eResource as XtextResource).parseResult.rootNode.format(0, input.length).formattedText
		assertEquals(expectedText, formattedText)
	}
}