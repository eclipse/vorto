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
package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator;

import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.ConfigurationClassGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.FaultClassGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.FunctionBlockClassGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.IndexHtmlFileGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.ModuleUtilTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.ServiceClassGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.StatusClassGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.WebXmlGeneratorTaskTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks.WordSeperatorTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.ConfigurationClassTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.FaultClassTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.FunctionBlockClassTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.IndexHtmlFileTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.PomFileTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.ServiceClassTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.StatusClassTemplateTest;
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates.WebXmlTemplateTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	WordSeperatorTest.class,
	ModuleUtilTest.class,
	ConfigurationClassTemplateTest.class,
	FaultClassTemplateTest.class,
	FunctionBlockClassTemplateTest.class,
	IndexHtmlFileTemplateTest.class,
	PomFileTemplateTest.class,
	ServiceClassTemplateTest.class,
	StatusClassTemplateTest.class,
	WebXmlTemplateTest.class,
	ConfigurationClassGeneratorTaskTest.class,
	FaultClassGeneratorTaskTest.class,
	FunctionBlockClassGeneratorTaskTest.class,
	IndexHtmlFileGeneratorTaskTest.class,
	ServiceClassGeneratorTaskTest.class,
	StatusClassGeneratorTaskTest.class,
	WebXmlGeneratorTaskTest.class	
})
public class AllWebDeviceGeneratorTests {

}
