/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.core.tests;

import org.eclipse.vorto.core.tests.model.EntityMappingTest;
import org.eclipse.vorto.core.tests.model.EnumMappingTest;
import org.eclipse.vorto.core.tests.model.FunctionBlockMappingTest;
import org.eclipse.vorto.core.tests.model.InfoModelMappingTest;
import org.eclipse.vorto.core.tests.model.MappingResourceFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EntityMappingTest.class, EnumMappingTest.class, FunctionBlockMappingTest.class,
		InfoModelMappingTest.class, MappingResourceFactoryTest.class })
public class AllCoreTests {

}
