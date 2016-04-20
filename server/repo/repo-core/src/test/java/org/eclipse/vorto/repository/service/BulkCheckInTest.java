package org.eclipse.vorto.repository.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.internal.service.JcrModelRepository;
import org.eclipse.vorto.repository.internal.service.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.model.ModelHandle;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.notification.IMessage;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modeshape.jcr.SingleUseAbstractTest;
import org.springframework.core.io.ClassPathResource;

public class BulkCheckInTest extends SingleUseAbstractTest  {
	
	@InjectMocks
	private JcrModelRepository modelRepository;
	@Mock
	private INotificationService notificationService;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private ModelSearchUtil modelSearchUtil = new ModelSearchUtil();
	
	@Before
	public void beforeEach() throws Exception {
		super.beforeEach();
		startRepositoryWithConfiguration(new ClassPathResource("vorto-repository.json").getInputStream());

		modelRepository = new JcrModelRepository();
		modelRepository.setModelSearchUtil(modelSearchUtil);
		modelRepository.setSession(session());
	}
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void tesCheckinValidModels() throws IOException {
		String fileName = "sample_models/valid-models1.zip";
		List<UploadModelResult> uploadResults = modelRepository.uploadMultipleModels(fromClasspath(fileName));
		assertEquals(2, uploadResults.size());
		ModelHandle[] modelHandles = prepareModelHandles(uploadResults);

		when(userRepository.findAll()).thenReturn(testUsers());
		modelRepository.checkinMultiple(modelHandles , "Vijay");
		verify(notificationService, Mockito.atLeastOnce()).sendNotification(Mockito.any(IMessage.class));
		assertEquals(2, modelRepository.search("*").size());
	}
	
	@Test
	public void tesCheckinInvalidModels() throws IOException {
		String fileName = "sample_models/invalid-models.zip";
		List<UploadModelResult> uploadResults = new ArrayList<UploadModelResult>();
		try {
			uploadResults = modelRepository.uploadMultipleModels(fromClasspath(fileName));
		} catch (ValidationException e) {
		}
		assertEquals(0, uploadResults.size());
		ModelHandle[] modelHandles = prepareModelHandles(uploadResults);
		
		when(userRepository.findAll()).thenReturn(testUsers());
		modelRepository.checkinMultiple(modelHandles , "Vijay");
		verify(notificationService, Mockito.times(0)).sendNotification(Mockito.any(IMessage.class));
		assertEquals(0, modelRepository.search("*").size());
	}

	private Iterable<User> testUsers() {
		User vijay = new User();
		vijay.setUsername("Vijay");
		vijay.setHasWatchOnRepository(true);

		User bill = new User();
		bill.setUsername("Bill");
		bill.setHasWatchOnRepository(false);

		Collection<User> recipients = new ArrayList<User>();
		recipients.add(vijay);
		recipients.add(bill);
		return recipients;
	}

	private ModelHandle[] prepareModelHandles(List<UploadModelResult> uploadResults) {
		List<ModelHandle> modelHandles = uploadResults.stream().parallel().map(new Function<UploadModelResult, ModelHandle>() {
			@Override
			public ModelHandle apply(UploadModelResult result) {
				ModelHandle modelHandle = new ModelHandle();
				modelHandle.setHandleId(result.getHandleId());
				modelHandle.setId(result.getModelResource().getId());
				return modelHandle;
			}
		}).collect(Collectors.toList());
		return modelHandles.toArray(new ModelHandle[0]);
	}

	private String fromClasspath(String fileName) throws IOException {
		return new ClassPathResource(fileName).getFile().getAbsolutePath();
	}

}
