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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests;

import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.AbstractDummyDeviceGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.BaseDriverGeneratorTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.BlueprintConfigGeneratorTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.DummyDeviceGeneratorTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.EventReadTaskGeneratorTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.IDummyDeviceGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.BluePrintConfigTemplateTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.PomTemplateTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.basedriver.BaseDriverTemplateTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.basedriver.DummyEventReadTaskTemplateTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.device.AbstractDummyDeviceTemplateTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.device.DeviceTemplateTest;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tests.tasks.template.device.IDeviceTemplateTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BaseDriverGeneratorTest.class, 
	    DummyDeviceGeneratorTest.class,
	    AbstractDummyDeviceGeneratorTaskTest.class,
		BlueprintConfigGeneratorTest.class, 
		EventReadTaskGeneratorTest.class,
		IDummyDeviceGeneratorTaskTest.class,
		BaseDriverTemplateTest.class,
		IDeviceTemplateTest.class, 
		DeviceTemplateTest.class, 
		BluePrintConfigTemplateTest.class,
		PomTemplateTest.class,
		DummyEventReadTaskTemplateTest.class,
		AbstractDummyDeviceTemplateTest.class

})
public class AllTests {

}
