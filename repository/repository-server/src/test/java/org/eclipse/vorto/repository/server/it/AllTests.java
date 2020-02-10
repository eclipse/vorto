package org.eclipse.vorto.repository.server.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AttachmentsControllerIntegrationTest.class, ModelControllerIntegrationTest.class,
    ModelSearchControllerIntegrationTest.class, RepositoryJavaClientTest.class,
    NamespaceControllerIntegrationTest.class})
public class AllTests {

}
