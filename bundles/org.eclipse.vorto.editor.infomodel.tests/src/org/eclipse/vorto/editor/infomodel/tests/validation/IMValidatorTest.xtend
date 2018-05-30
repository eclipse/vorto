package org.eclipse.vorto.editor.infomodel.tests.validation

import org.eclipse.xtext.junit4.AbstractXtextTests
import org.eclipse.xtext.junit4.validation.ValidatorTester
import org.eclipse.vorto.editor.infomodel.validation.InformationModelValidator
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup
import org.junit.After
import org.junit.Test
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory
import org.eclipse.vorto.editor.infomodel.validation.SystemMessage

class IMValidatorTest extends AbstractXtextTests {
	
	private ValidatorTester<InformationModelValidator> tester;

	def override void setUp() throws Exception {
		super.setUp();
		with(InformationModelStandaloneSetup);
		var validator = get(InformationModelValidator);
		tester = new ValidatorTester<InformationModelValidator>(validator, getInjector());		
	}
	
	/**
	 * Need to overwrite tearDown method to avoid emf classes get de-registered causing inconsistency between test methods
	 * as static variable in *PackageImpl already set to true and it's not re-inited again
	 */
    @After
	def override void tearDown() throws Exception {
		
	}
	
	@Test
	def test_IMName() {
		var imModel = InformationModelFactory.eINSTANCE.createInformationModel()		
		imModel.setName("fame")
		
		tester.validator().checkInformationModelName(imModel)
		tester.diagnose().assertErrorContains(SystemMessage.ERROR_IMNAME_INVALID);
	}
	
}