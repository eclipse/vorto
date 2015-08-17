package org.eclipse.vorto.editor.mapping.tests.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.TargetElement;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;
import org.junit.Test;

public class LWM2MMappingParseTest {
	private static final String EXAMPLES_DIRECTORY = "resources/org/eclipse/vorto/editor/mapping/tests/parser/examples/lwm2m/";
	private static final String EXAMPLE_MAPPING_FILE = EXAMPLES_DIRECTORY
			+ "LWM2M.mapping";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
	}

	@Test
	public void parseLWM2MMapping() throws IOException {
		MappingModel lwMapping = this.loadMappingModel();

		InfoModelMappingRule objectTypeRule = lwMapping
				.getInfoModelMappingRules().get(0);
		StereoType objectType = objectTypeRule.getTargetElement()
				.getStereoTypes().get(0);
		assertObjectTypeAttributes(objectType);

		assertFunctionBlockRule(lwMapping);
	}

	private void assertFunctionBlockRule(MappingModel lwMapping) {
		EList<FunctionBlockMappingRule> functionBlockMappingRules = lwMapping
				.getFunctionBlockMappingRules();

		for (FunctionBlockMappingRule rule : functionBlockMappingRules) {
			FunctionBlockElement functionBlockElement = rule
					.getFunctionBlockSourceElements().get(0)
					.getFunctionBlockElement();
			
			if (functionBlockElement instanceof StatusElement) {
				TargetElement targetElement = rule.getTargetElement();
				StereoType itemStereoType = targetElement.getStereoTypes().get(
						0);

				Attribute itemId = this.getAttributeByName("ItemID",
						itemStereoType);
				Attribute unit = this.getAttributeByName("Units",
						itemStereoType);
				assertNotNull(itemId.getValue());
				assertNotNull(unit.getValue());
			}
		}
	}

	private void assertObjectTypeAttributes(StereoType objectType) {
		assertEquals("ObjectType", objectType.getName());

		Attribute type = this.getAttributeByName("Type", objectType);
		Attribute name = this.getAttributeByName("Name", objectType);
		Attribute objectID = this.getAttributeByName("ObjectID", objectType);
		Attribute objectURN = this.getAttributeByName("ObjectURN", objectType);
		Attribute multipleInstances = this.getAttributeByName(
				"MultipleInstances", objectType);
		Attribute mandatory = this.getAttributeByName("Mandatory", objectType);

		assertEquals("MODefinition", type.getValue());
		assertEquals("Location", name.getValue());
		assertEquals("6", objectID.getValue());
		assertEquals("TBD", objectURN.getValue());
		assertEquals("Single", multipleInstances.getValue());
		assertEquals("Optional", mandatory.getValue());
	}

	private Attribute getAttributeByName(String attributeName,
			StereoType stereoType) {
		for (Attribute attribute : stereoType.getAttributes()) {
			if (attributeName.equals(attribute.getName())) {
				return attribute;
			}
		}
		return null;
	}

	private MappingModel loadMappingModel() throws IOException {
		ResourceSet rset = new ResourceSetImpl();
		rset.getPackageRegistry().put(MappingPackage.eNS_URI,
				MappingPackage.eINSTANCE);
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());

		Resource resource = rset.getResource(
				URI.createFileURI(EXAMPLE_MAPPING_FILE), true);
		resource.load(null);
		MappingModel mappingModel = (MappingModel) resource.getContents()
				.get(0);
		return mappingModel;
	}
}
