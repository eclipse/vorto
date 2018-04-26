package org.eclipse.vorto.repository;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
	protected IUserRepository userRepository = Mockito.mock(IUserRepository.class);
	
	public void beforeEach() throws Exception {
		super.beforeEach();
		startRepositoryWithConfiguration(new ClassPathResource("vorto-repository.json").getInputStream());

		Mockito.when(userRepository.findByUsername("alex")).thenReturn(User.create("alex"));
		Mockito.when(userRepository.findByUsername("admin")).thenReturn(User.create("admin", Role.ADMIN));
		
		modelRepository = new JcrModelRepository();
		modelRepository.setUploadStorage(new InMemoryTemporaryStorage());
		modelRepository.setSession(jcrSession());
		modelRepository.setUserRepository(userRepository);
		modelRepository.createValidators();
		modelRepository.setModelSearchUtil(modelSearchUtil);

	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	protected void checkinModel(String modelName) {
		checkinModel(modelName, UserContext.user(getCallerId()));
	}
	
	protected String getCallerId() {
		return "alex";
	}
	
	protected void checkinModel(String modelName, IUserContext userContext) {
		try {
			UploadModelResult uploadResult = modelRepository.upload(
					IOUtils.toByteArray(new ClassPathResource("sample_models/" + modelName).getInputStream()),
					modelName, userContext);
			Assert.isTrue(uploadResult.isValid(), uploadResult.getErrorMessage());
			
			modelRepository.checkin(uploadResult.getHandleId(), userContext);
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
