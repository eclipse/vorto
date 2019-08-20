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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.eclipse.xtext.xbase.scoping.batch.SimpleIdentifiableElementDescription;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.*;
import org.eclipse.vorto.plugin.generator.utils.DatatypeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate;
import org.eclipse.vorto.plugin.generator.utils.IGeneratedWriter;
import org.eclipse.vorto.repository.core.impl.parser.ParsingException;

public class EclipseHonoJavaGeneratorTest extends AbstractGeneratorTest{
	@Before
	public void beforeEach() throws Exception {
	}

	@Before
	public void setUp() {

	}

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	/** Test case for checking whether the returned file is a Zip file */

	@Test
	public void checkResultZipFileHonoJava() throws Exception {
		InformationModel model = modelProvider("SuperInfomodel.infomodel","SuperSuperFb.fbmodel");
		ICodeGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();
		checkResultZipFile(eclipseHonoJavaGenerator,model);
	}

	/*
	 * Test case for checking infomodel with empty namespace
	 */
	@Ignore
	@Test(expected = ParsingException.class)
	public void checkEmptyNamespaceInfomodelHono() throws Exception {
		InformationModel model = modelProvider("EmptyNamespace.infomodel","SuperSuperFb.fbmodel");
		EclipseHonoJavaGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();
		checkEmptyNamespaceInfomodel(eclipseHonoJavaGenerator,model);
	}

}
