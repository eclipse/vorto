package org.eclipse.vorto.repository;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.impl.VortoModelImporter;
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
	
	protected VortoModelImporter importer = null;
	
	public void beforeEach() throws Exception {
		super.beforeEach();
		startRepositoryWithConfiguration(new ClassPathResource("vorto-repository.json").getInputStream());

		Mockito.when(userRepository.findByUsername("alex")).thenReturn(User.create("alex"));
		Mockito.when(userRepository.findByUsername("admin")).thenReturn(User.create("admin", Role.ADMIN));
		
		modelRepository = new JcrModelRepository();
		modelRepository.setSession(jcrSession());
		modelRepository.setUserRepository(userRepository);
		modelRepository.setModelSearchUtil(modelSearchUtil);
		
		this.importer = new VortoModelImporter();
		this.importer.setModelRepository(modelRepository);
		this.importer.setUploadStorage(new InMemoryTemporaryStorage());
		this.importer.setUserRepository(userRepository);

	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	protected ModelInfo importModel(String modelName) {
		return importModel(modelName, UserContext.user(getCallerId()));
	}
	
	protected String getCallerId() {
		return "alex";
	}
	
	protected ModelInfo importModel(String modelName, IUserContext userContext) {
		try {
			UploadModelResult uploadResult = this.importer.upload(FileUpload.create(modelName,
					IOUtils.toByteArray(new ClassPathResource("sample_models/" + modelName).getInputStream())), userContext);
			Assert.isTrue(uploadResult.getReport().isValid(), uploadResult.getReport().getErrorMessage());
			
			return this.importer.doImport(uploadResult.getHandleId(), userContext).get(0);
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
