/*******************************************************************************
 *  Copyright (c) 2015,2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.editor.functionblock.tests;

import org.eclipse.vorto.editor.functionblock.tests.formatter.FunctionblockModelFormatterTest;
import org.eclipse.vorto.editor.functionblock.tests.validator.FBEntityEnumCrossReferenceTest;
import org.eclipse.vorto.editor.functionblock.tests.validator.FBOperationCrossReferenceTest;
import org.eclipse.vorto.editor.functionblock.tests.validator.FbConstraintParametersValidationTest;
import org.eclipse.vorto.editor.functionblock.tests.validator.FbConstraintValidationTest;
import org.eclipse.vorto.editor.functionblock.tests.validator.FbValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FbValidatorTest.class, FBOperationCrossReferenceTest.class, FBEntityEnumCrossReferenceTest.class,
		FbConstraintValidationTest.class, FbConstraintParametersValidationTest.class,
		FunctionblockModelFormatterTest.class })
public class AllEditorTests {

}
