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
package org.eclipse.vorto.codegen.tests;

import org.eclipse.vorto.codegen.tests.wizard.ActivatorFileTaskTest;
import org.eclipse.vorto.codegen.tests.wizard.JsonFileTaskTest;
import org.eclipse.vorto.codegen.tests.wizard.LocationWrapperTest;
import org.eclipse.vorto.codegen.tests.wizard.ManifestFileModuleTest;
import org.eclipse.vorto.codegen.tests.wizard.PluginBuildFileModuleTest;
import org.eclipse.vorto.codegen.tests.wizard.PluginNatureConfigurationTest;
import org.eclipse.vorto.codegen.tests.wizard.TemplateFileTaskTest;
import org.eclipse.vorto.codegen.tests.wizard.XMLFileTaskTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		ActivatorFileTaskTest.class, JsonFileTaskTest.class,
		LocationWrapperTest.class, ManifestFileModuleTest.class,
		PluginBuildFileModuleTest.class, PluginNatureConfigurationTest.class,
		TemplateFileTaskTest.class, XMLFileTaskTest.class })
public class AllCodeGenTests {

}
