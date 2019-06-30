package org.eclipse.vorto.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({org.eclipse.vorto.repository.web.AllTests.class, org.eclipse.vorto.repository.server.it.AllTests.class})
public class AllTests {

}
