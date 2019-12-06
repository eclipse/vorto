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
package org.eclipse.vorto.repository.search;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({GeneralSearchTest.class, MixedSearchTest.class,
    NameSearchSimpleTest.class,
    AuthorSearchSimpleTest.class,
    UserReferenceSearchSimpleTest.class, TypeSearchSimpleTest.class, StateSearchSimpleTest.class,
    NamespaceSearchSimpleTest.class, VersionSearchSimpleTest.class})
public class AllSearchTests {

}
