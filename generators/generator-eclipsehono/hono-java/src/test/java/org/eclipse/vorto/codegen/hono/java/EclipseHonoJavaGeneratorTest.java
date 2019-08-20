/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.hono.java;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.repository.core.impl.parser.ParsingException;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class EclipseHonoJavaGeneratorTest extends AbstractGeneratorTest {

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	/** Test case for checking whether the returned file is a Zip file */

	@Test
	public void checkResultZipFileHonoJava() throws Exception {
		InformationModel model = modelProvider("SuperInfomodel.infomodel", "SuperSuperFb.fbmodel");
		ICodeGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();
		checkResultZipFile(eclipseHonoJavaGenerator, model);
	}

	/*
	 * Test case for checking infomodel with empty namespace
	 */
	@Ignore // Issue created https://github.com/eclipse/vorto/issues/1885
	@Test(expected = ParsingException.class)
	public void checkEmptyNamespaceInfomodelHono() throws Exception {
		InformationModel model = modelProvider("EmptyNamespace.infomodel", "SuperSuperFb.fbmodel");
		EclipseHonoJavaGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();
		generateResult(eclipseHonoJavaGenerator, model);
	}

}
