package org.eclipse.vorto.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.BuilderUtils.MappingBuilder;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.IGeneratedWriter;
import org.junit.Before;
import org.junit.Test;

public class HelloWorldGeneratorTest implements ICodeGenerator {

	private InvocationContext invocationContext = null;
	private Enum enumeration = null;
	private FunctionblockModel functionBlock;
	private InformationModel infomodel;
	private Entity _entity;

	@Before
	public void createTestData() {

		invocationContext = InvocationContext.simpleInvocationContext();
		enumeration = BuilderUtils.newEnum(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype,
				"UnitEnum", "org.eclipse.vorto", "1.0.0")).withLiterals("KG", "G").build();

		_entity = BuilderUtils
				.newEntity(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype, "UnitEntity",
						"org.eclipse.vorto", "1.0.0"))
				.withProperty("value", PrimitiveType.FLOAT).withProperty("unitEnum", enumeration).build();

		Event eventBlock = BuilderUtils.newEvent("testEvent").build();

		functionBlock = BuilderUtils
				.newFunctionblock(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
						"StatusPropertiesFunctionBlock", "org.eclipse.vorto", "1.0.0"))
				.withStatusProperty("statusValue", _entity).withStatusProperty("statusBoolean", PrimitiveType.BOOLEAN)
				.build();

		infomodel = BuilderUtils
				.newInformationModel(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.InformationModel,
						"MySensor", "org.eclipse.vorto", "1.0.0"))
				.withFunctionBlock(functionBlock, "statusPropertiesFunctionBlock", "", false).build();

	}

	/*
	 * Test case for checking the attribute mapping and corresponding property value for a function block
	 * 
	 * 
	 */
	@Test
	public void getMappedElementFunctionbLock() throws Exception {

		MappingBuilder mapping = BuilderUtils.newMapping(new ModelId(ModelType.Mapping, "", "", ""), "helloworld");
		FunctionBlockMappingRule rule = MappingFactory.eINSTANCE.createFunctionBlockMappingRule();
		FunctionBlockPropertySource statusValueSource = MappingFactory.eINSTANCE.createFunctionBlockPropertySource();
		statusValueSource.setModel(functionBlock);

		rule.getSources().add(statusValueSource);

		StereoTypeTarget stereotypeTarget = MappingFactory.eINSTANCE.createStereoTypeTarget();
		stereotypeTarget.setName("OBJECT_ID");
		Attribute objectIDattribute = MappingFactory.eINSTANCE.createAttribute();
		objectIDattribute.setName("ID");
		objectIDattribute.setValue("functionBlockAttributID");
		stereotypeTarget.getAttributes().add(objectIDattribute);
		rule.setTarget(stereotypeTarget);

		mapping.addRule(rule);

		InvocationContext ctx = new InvocationContext(createMappingList(mapping.build()), Collections.emptyMap());

		Property prop = functionBlock.getFunctionblock().getStatus().getProperties().get(0);
		assertNotNull(ctx.getMappedElement(prop, "OBJECT_ID"));
		assertEquals("functionBlockAttributID", ctx.getMappedElement(prop, "OBJECT_ID").getAttributeValue("ID", "functionBlockAttributID"));
	}
	
	/*
	 * Test case for checking the attribute mapping and corresponding property value for an entity
	 * 
	 * 
	 */
	@Test
	public void getMappedElementEntity() throws Exception {

		MappingBuilder mapping = BuilderUtils.newMapping(new ModelId(ModelType.Mapping, "", "", ""), "helloworld");
		EntityMappingRule entityMappingRule = MappingFactory.eINSTANCE.createEntityMappingRule();
		EntityAttributeSource entityAttributeSource = MappingFactory.eINSTANCE.createEntityAttributeSource();
		
		entityAttributeSource.setModel(_entity);
		
		entityMappingRule.getSources().add(entityAttributeSource);

		StereoTypeTarget stereotypeTarget = MappingFactory.eINSTANCE.createStereoTypeTarget();
		stereotypeTarget.setName("OBJECT_ID");
		Attribute objectIDattribute = MappingFactory.eINSTANCE.createAttribute();
		objectIDattribute.setName("ID");
		objectIDattribute.setValue("entityAttributID");
		stereotypeTarget.getAttributes().add(objectIDattribute);
		entityMappingRule.setTarget(stereotypeTarget);
		
		mapping.addRule(entityMappingRule);

		InvocationContext ctx = new InvocationContext(createMappingList(mapping.build()), Collections.emptyMap());

		Property prop = functionBlock.getFunctionblock().getStatus().getProperties().get(0);
		assertNotNull(ctx.getMappedElement(prop, "OBJECT_ID"));
		assertEquals("entityAttributID", ctx.getMappedElement(prop, "OBJECT_ID").getAttributeValue("ID", "entityAttributID"));
	}

	private List<MappingModel> createMappingList(MappingModel... mappings) {
		return Arrays.asList(mappings);
	}

	private void generateForEntity(InformationModel infomodel, Entity entity, IGeneratedWriter outputter) {

	}

	private void generateForEnum(InformationModel infomodel, Enum en, IGeneratedWriter outputter) {

	}

	@Override
	public GeneratorPluginInfo getMeta() {
		return GeneratorPluginInfo.Builder("sampleString").withVendor("sampleString").withName("sampleString")
				.withDescription("sampleString").withDocumentationUrl("sampleString").build();
	}

	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context) throws GeneratorException {
		// TODO Auto-generated method stub
		return null;
	}

}
