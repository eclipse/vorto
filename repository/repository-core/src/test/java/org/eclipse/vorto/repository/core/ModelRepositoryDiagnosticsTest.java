package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics;
import org.eclipse.vorto.repository.core.impl.diagnostics.ModelValidationTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelRepositoryDiagnosticsTest extends AbstractIntegrationTest {
	
	private DefaultModelBackupService repositoryManager = null;
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		repositoryManager = new DefaultModelBackupService();
		repositoryManager.setModelRepository(this.modelRepository);
		repositoryManager.setSession(((JcrModelRepository)this.modelRepository).getSession());
	}
	
	@Test
	public void baseline() {
		ModelValidationTest modelValidationTest = new ModelValidationTest();
		modelValidationTest.setModelParserFactory(modelParserFactory);
		
		RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
		modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(modelValidationTest));
		modelRepository.setRepositoryDiagnostics(modelDiagnostics);
		
		try {
			repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-baseline.xml").getInputStream()));
		} catch (Exception e) {
			fail("Failed to load backup file.");
		}
		
		Collection<Diagnostic> diagnostics = modelRepository.diagnose();
		assertEquals(0, diagnostics.size());
	}
	
	@Test
	public void withValidationProblems() {
		ModelValidationTest modelValidationTest = new ModelValidationTest();
		modelValidationTest.setModelParserFactory(modelParserFactory);
		
		RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
		modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(modelValidationTest));
		modelRepository.setRepositoryDiagnostics(modelDiagnostics);
		
		try {
			repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-validation-error.xml").getInputStream()));
		} catch (Exception e) {
			fail("Failed to load backup file.");
		}
		
		Collection<Diagnostic> diagnostics = modelRepository.diagnose();
		diagnostics.forEach(diagnostic -> System.out.println("-erle- : " + diagnostic.toString()));
		assertEquals(1, diagnostics.size());
	}
	
}
 