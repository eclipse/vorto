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
import org.eclipse.vorto.repository.core.search.AuthorSearchSimpleTest;
import org.eclipse.vorto.repository.core.search.ModelRepositorySearchTest;
import org.eclipse.vorto.repository.core.search.NameSearchSimpleTest;
import org.eclipse.vorto.repository.core.search.NamespaceSearchSimpleTest;
import org.eclipse.vorto.repository.core.search.StateSearchSimpleTest;
import org.eclipse.vorto.repository.core.search.TypeSearchSimpleTest;
import org.eclipse.vorto.repository.core.search.UserReferenceSearchSimpleTest;
import org.eclipse.vorto.repository.core.search.VersionSearchSimpleTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AttachmentValidatorTest.class, ErrorMessageProviderTest.class, MappingTest.class,
    ModelDtoFactoryTest.class, ModelIdTest.class, ModelParserTest.class,
    ModelRepositoryAttachmentTest.class, ModelRepositoryDiagnosticsTest.class,
    ModelRepositorySearchTest.class,
    ModelRepositoryTest.class, ModelRepositoryTest2.class,
    BlueToothDeviceInfoProfileResolverTest.class,
    Lwm2mObjectIdResolverTest.class, NameSearchSimpleTest.class, AuthorSearchSimpleTest.class,
    UserReferenceSearchSimpleTest.class, TypeSearchSimpleTest.class, StateSearchSimpleTest.class,
    NamespaceSearchSimpleTest.class, VersionSearchSimpleTest.class})
public class AllTests {

}
