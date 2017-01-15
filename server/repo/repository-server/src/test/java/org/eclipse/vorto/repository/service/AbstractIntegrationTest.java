package org.eclipse.vorto.repository.service;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.internal.service.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.internal.service.JcrModelRepository;
import org.eclipse.vorto.repository.internal.service.notification.INotificationService;
import org.eclipse.vorto.repository.internal.service.utils.ModelSearchUtil;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modeshape.test.ModeShapeSingleUseTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

public abstract class AbstractIntegrationTest extends ModeShapeSingleUseTest {

	@InjectMocks
	protected JcrModelRepository modelRepository;
	@InjectMocks
	protected ModelSearchUtil modelSearchUtil = new ModelSearchUtil();
	@Mock
	protected INotificationService notificationService;
	@Mock
	protected IUserRepository userRepository;
	
	public void beforeEach() throws Exception {
		super.beforeEach();
		startRepositoryWithConfiguration(new ClassPathResource("vorto-repository.json").getInputStream());

		modelRepository = new JcrModelRepository();
		modelRepository.setUploadStorage(new InMemoryTemporaryStorage());
		modelRepository.setSession(jcrSession());
		modelRepository.createValidators();
		modelRepository.setModelSearchUtil(modelSearchUtil);

	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	protected void checkinModel(String modelName) {
		try {
			UploadModelResult uploadResult = modelRepository.upload(
					IOUtils.toByteArray(new ClassPathResource("sample_models/" + modelName).getInputStream()),
					modelName);
			Assert.isTrue(uploadResult.isValid(), uploadResult.getErrorMessage());
			when(userRepository.findAll()).thenReturn(Collections.emptyList());
			modelRepository.checkin(uploadResult.getHandleId(), "alex");
			modelRepository.search("*");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
