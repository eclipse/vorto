package org.eclipse.vorto.repository.oauth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({PublicKeyHelperTest.class, VerificationHelperTest.class, 
  HydraTokenVerifierTest.class, LegacyTokenVerificationTest.class, 
  ResourceIdentifierTest.class})
public class AllTests {

}
