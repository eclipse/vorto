package org.eclipse.vorto.editor.mapping.tests.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;
import org.junit.Test;

public class LWM2MMappingParseTest {
	private static final String EXAMPLES_DIRECTORY = "resources/org/eclipse/vorto/editor/mapping/tests/parser/examples/lwm2m/";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
	}

	@Test
	public void parseEntityLocationMapping() throws IOException {
		MappingModel lwMapping = this.createMappingModel("LocationMapping.mapping");

		EList<EntityMappingRule> rules = ((EntityMapping) lwMapping.getMappingType()).getEntityMappingRules();
		assertEquals(3, rules.size());

	}

	@Test
	public void parseFunctionBlockDroneMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("DroneMapping.mapping");

		EList<FunctionBlockMappingRule> rules = ((FunctionBlockMapping) mappingModel.getMappingType())
				.getFunctionBlockMappingRules();
		assertEquals(2, rules.size());
	}

	@Test
	public void parseLWM2MMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("LWM2M.mapping");

		EList<InfoModelMappingRule> rules = ((InfoModelMapping) mappingModel.getMappingType())
				.getInfoModelMappingRules();
		assertEquals(2, rules.size());
	}

	private MappingModel createMappingModel(String mappingFileName) throws IOException {
		ResourceSet rset = new ResourceSetImpl();
		rset.getPackageRegistry().put(MappingPackage.eNS_URI, MappingPackage.eINSTANCE);
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		Resource resource = rset.getResource(URI.createFileURI(EXAMPLES_DIRECTORY + mappingFileName), true);
		resource.load(null);
		MappingModel mappingModel = (MappingModel) resource.getContents().get(0);
		return mappingModel;
	}
}
