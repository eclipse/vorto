package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.repository.core.resolver.BlueToothDeviceInfoProfileResolverTest;
import org.eclipse.vorto.repository.core.resolver.Lwm2mObjectIdResolverTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AttachmentValidatorTest.class, ErrorMessageProviderTest.class, MappingTest.class,
    ModelDtoFactoryTest.class, ModelIdTest.class, ModelParserTest.class,
    ModelRepositoryAttachmentTest.class, ModelRepositoryDiagnosticsTest.class,
    ModelRepositoryTest.class, ModelRepositoryTest2.class,BlueToothDeviceInfoProfileResolverTest.class,Lwm2mObjectIdResolverTest.class})
public class AllTests {

}
