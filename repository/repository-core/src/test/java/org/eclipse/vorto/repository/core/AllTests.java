/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 * <p>
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.repository.core.resolver.BlueToothDeviceInfoProfileResolverTest;
import org.eclipse.vorto.repository.core.resolver.Lwm2mObjectIdResolverTest;
import org.eclipse.vorto.repository.core.search.AllSearchTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AttachmentValidatorTest.class, ErrorMessageProviderTest.class, MappingTest.class,
    ModelDtoFactoryTest.class, ModelIdTest.class, ModelParserTest.class,
    ModelRepositoryAttachmentTest.class, ModelRepositoryDiagnosticsTest.class,
    ModelRepositoryTest.class, ModelRepositoryTest2.class,
    BlueToothDeviceInfoProfileResolverTest.class,
    Lwm2mObjectIdResolverTest.class, AllSearchTests.class})
public class AllTests {

}
