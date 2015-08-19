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
package org.eclipse.vorto.editor.mapping.tests;

import org.eclipse.vorto.editor.mapping.tests.formatter.MappingModelFormatterTest;
import org.eclipse.vorto.editor.mapping.tests.parser.LWM2MMappingParseTest;
import org.eclipse.vorto.editor.mapping.tests.parser.MappingModelSyntaxTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MappingModelSyntaxTest.class, MappingModelFormatterTest.class, LWM2MMappingParseTest.class})
public class AllMappingEditorTests {

}
