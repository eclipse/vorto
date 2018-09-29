package org.eclipse.vorto.service.mapping;

import java.util.Collections;

import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.serializer.MappingSpecificationSerializer;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.eclipse.vorto.service.mapping.spec.MappingSpecificationBuilder;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MappingSerializerTest extends AbstractMappingTest {

	@Test
	public void testCreateAndSerializeMappingSpec() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch:BoschGLM100C:1.0.0")
											.remoteClient(this.getModelRepository())
											.build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithXpath("/@incl"));
		
		MappingSpecificationSerializer.create(mappingSpecification).iterator().forEachRemaining(p -> System.out.println(p.serialize()));

	}
	

	@Test
	public void testCreateAndSerializeMappingSpec2() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch:BoschGLM100C:1.0.0")
											.remoteClient(this.getModelRepository())
											.build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("sensor_units").get().addStereotype(Stereotype.createWithXpath(""));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithXpath("/@incl"));
		
		MappingSpecificationSerializer.create(mappingSpecification).iterator().forEachRemaining(p -> System.out.println(p.serialize()));

	}
	
	@Test
	public void testCreateAndSerializeMappingSpecContainingCustomFunctions() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch:BoschGLM100C:1.0.0")
											.remoteClient(this.getModelRepository())
											.build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("sensor_units").get().addStereotype(Stereotype.createWithXpath(""));
		mappingSpecification.getFunctionBlock("distancesensor").addStereotype(Stereotype.createWithFunction("convert", "function convert() {}"));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithXpath("/@incl"));
		
		MappingSpecificationSerializer.create(mappingSpecification).iterator().forEachRemaining(p -> System.out.println(p.serialize()));

	}
	
	@Test
	public void testCreateAndSerializeMappingSpecContainingEmptyFunctions() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch:BoschGLM100C:1.0.0")
											.remoteClient(this.getModelRepository())
											.build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("sensor_units").get().addStereotype(Stereotype.createWithXpath(""));
		mappingSpecification.getFunctionBlock("distancesensor").addStereotype(Stereotype.create("functions", Collections.emptyMap()));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithXpath("/@incl"));
		
		MappingSpecificationSerializer.create(mappingSpecification).iterator().forEachRemaining(p -> System.out.println(p.serialize()));

	}
	
	@Test
	public void testCreateAndSerializeMappingSpecContainingDoubleQuotesFunction() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch:BoschGLM100C:1.0.0")
											.remoteClient(this.getModelRepository())
											.build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("sensor_units").get().addStereotype(Stereotype.createWithXpath(""));
		mappingSpecification.getFunctionBlock("distancesensor").addStereotype(Stereotype.createWithFunction("convert", "function convert(value) { return \"\"}"));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithXpath("/@incl"));
		
		MappingSpecificationSerializer.create(mappingSpecification).iterator().forEachRemaining(p -> System.out.println(p.serialize()));

	}
	
	@Test
	public void testCreateAndSerializeMappingSpecContainingDoubleQuotesCondition() throws Exception {

		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("com.bosch:BoschGLM100C:1.0.0")
											.remoteClient(this.getModelRepository())
											.build();
		
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("distance").get().addStereotype(Stereotype.createWithXpath("/@dist"));
		mappingSpecification.getFunctionBlock("distancesensor").getStatusProperty("sensor_units").get().addStereotype(Stereotype.createWithXpath(""));
		mappingSpecification.getFunctionBlock("inclinesensor").getStatusProperty("degree").get().addStereotype(Stereotype.createWithConditionalXpath("xpath:eval(\"value/x\",this) == 2", "/@incl"));
		
		MappingSpecificationSerializer.create(mappingSpecification).iterator().forEachRemaining(p -> System.out.println(p.serialize()));

	}
}
