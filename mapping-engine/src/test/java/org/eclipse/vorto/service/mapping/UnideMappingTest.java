package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.eclipse.vorto.service.mapping.spec.MappingSpecificationBuilder;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class UnideMappingTest extends AbstractMappingTest  {

	
	@Test
	public void testMapUnideMessageToDitto() throws Exception {
		IMappingSpecification mappingSpecification = MappingSpecificationBuilder.create()
											.infomodelId("org.eclipse.unide.devices.MobilePhone:1.0.0")
											.targetPlatformKey("org_eclipse_unide_devices_MobilePhone_1_0_0")
											.remoteClient(this.getModelRepository())
											.build();
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder().withSpecification(mappingSpecification).buildDittoMapper();

		final String unideMessage = IOUtils.toString(UnideMappingTest.class.getClassLoader().getResourceAsStream("unide.json"));
		
		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromJson(unideMessage), MappingContext.empty());
		Feature directionFeature = mappedDittoOutput.getFeatures().get("direction");
		assertEquals(45.4231, directionFeature.getStatusProperties().get("compass_direction"));
		System.out.println(mappedDittoOutput.toJson());

	}
}
