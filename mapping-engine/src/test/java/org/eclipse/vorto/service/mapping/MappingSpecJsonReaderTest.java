package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.*;

import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecificationReader;
import org.eclipse.vorto.service.mapping.spec.JsonMappingSpecificationReader;
import org.junit.Test;

public class MappingSpecJsonReaderTest {

	@Test
	public void testReadFromJson() {
		IMappingSpecificationReader reader = new JsonMappingSpecificationReader();
		IMappingSpecification spec = reader.read(MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json"));
		assertNotNull(spec);
		assertNotNull(spec.getInfoModel());
		
	}
}
