package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class MappingTest extends AbstractIntegrationTest {
	
	@Test
	public void tesUploadMapping() throws IOException {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()), "Color.type", UserContext.user("admin"));
		assertEquals(true, uploadResult.isValid());
		assertNull(uploadResult.getErrorMessage());
		assertNotNull(uploadResult.getHandleId());
		ModelInfo resource = uploadResult.getModelResource();
		assertEquals("org.eclipse.vorto.examples.type", resource.getId().getNamespace());
		assertEquals("Color", resource.getId().getName());
		assertEquals("1.0.0", resource.getId().getVersion());
		assertEquals(ModelType.Datatype, resource.getType());
		assertEquals(0, resource.getReferences().size());
		assertEquals("Color", resource.getDisplayName());
		assertNull(resource.getDescription());
		assertEquals(0, modelRepository.search("*").size());
	}

	
	@Test
	public void testCheckinValidMapping() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()), "Color.type", UserContext.user("admin"));
		assertEquals(true, uploadResult.isValid());
		assertEquals(0, modelRepository.search("*").size());

		User user = new User();
		user.setUsername("alex");

		Collection<User> users = new ArrayList<User>();
		users.add(user);

		when(userRepository.findAll()).thenReturn(users);

		modelRepository.checkin(uploadResult.getHandleId(), UserContext.user("alex"));
		Thread.sleep(2000); // hack coz it might take awhile until index is
							// updated to do a search
		assertEquals(1, modelRepository.search("*").size());

		uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/sample.mapping").getInputStream()),
				"sample.mapping", UserContext.user("admin"));
		assertEquals(true, uploadResult.isValid());
		modelRepository.checkin(uploadResult.getHandleId(), UserContext.user("alex"));
		assertEquals(1, modelRepository.search("-Mapping").size());
	}

	@Test
	public void testGetMappingsOfEntityForTargetPlatform() throws Exception {
		checkinModel("Color.type");
		checkinModel("sample.mapping");
		Thread.sleep(2000);
		assertEquals(1, modelRepository.getMappingModelsForTargetPlatform(
				ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), "ios").size());
	}

	@Test
	public void testUsedByMappingOfEntity() throws Exception {
		checkinModel("Color.type");
		checkinModel("sample.mapping");
		Thread.sleep(2000);
		assertEquals(1, modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
				.getReferencedBy().size());
		assertEquals("org.eclipse.vorto.examples.type.Color_ios:1.0.0",
				modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
						.getReferencedBy().get(0).getPrettyFormat());
		
		assertEquals(1,modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0")).getPlatformMappings().size());
	}
	
	@Test
	public void testGetPlatformMappingsOfEntity() throws Exception {
		checkinModel("Color.type");
		checkinModel("sample.mapping");
		Thread.sleep(2000);
		ModelInfo colorInfo = modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertEquals(1,colorInfo.getPlatformMappings().size());
		assertEquals("ios",colorInfo.getPlatformMappings().keySet().iterator().next());
	}
	
	
}
