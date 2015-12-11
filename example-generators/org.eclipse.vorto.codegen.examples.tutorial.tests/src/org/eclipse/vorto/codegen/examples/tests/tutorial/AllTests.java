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
package org.eclipse.vorto.codegen.examples.tests.tutorial;

import org.eclipse.vorto.codegen.examples.tests.tutorial.tasks.templates.LWM2MObjectTypeCompleteTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.tutorial.tasks.templates.LWM2MObjectTypeTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.tutorial.tasks.templates.LWM2MResourceEntityTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.tutorial.tasks.templates.LWM2MResourceEnumTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.tutorial.tasks.templates.LWM2MResourceTemplateTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	LWM2MObjectTypeCompleteTemplateTest.class,
	LWM2MObjectTypeTemplateTest.class,
	LWM2MResourceEntityTemplateTest.class,
	LWM2MResourceEnumTemplateTest.class,
	LWM2MResourceTemplateTest.class
})
public class AllTests {

}
