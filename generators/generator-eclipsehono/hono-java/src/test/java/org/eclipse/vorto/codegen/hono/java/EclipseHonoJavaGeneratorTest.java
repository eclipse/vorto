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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.repository.core.impl.parser.ParsingException;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class EclipseHonoJavaGeneratorTest extends AbstractGeneratorTest {

	String validInfoModel = "SuperInfomodel";
	String emptyNamespaceInfoModel = "EmptyNamespace";
	String infoModelWithType = "InfoModelWithType";
	String validFunctionBlock = "SuperSuperFb";
	String functionBlockWithDataType = "FunctionBlockWithDataType";
	String dataTypeFile = "SimpleType";
	ICodeGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	/** Test case for checking whether the returned file is a Zip file */

	@Test
	public void checkResultZipFileHonoJava() throws Exception {
		checkResultZipFile(eclipseHonoJavaGenerator,
				modelProvider(validInfoModel + ModelType.InformationModel.getExtension(),
						validFunctionBlock + ModelType.Functionblock.getExtension(), ""));
	}

	/*
	 * Test case for checking infomodel with empty namespace
	 */
	@Ignore // Issue created https://github.com/eclipse/vorto/issues/1885
	@Test(expected = ParsingException.class)
	public void checkEmptyNamespaceInfomodelHono() throws Exception {
		generateResult(eclipseHonoJavaGenerator,
				modelProvider(emptyNamespaceInfoModel + ModelType.InformationModel.getExtension(),
						validFunctionBlock + ModelType.Functionblock.getExtension(), ""));
	}

	/*
	 * Test case for checking whether functionblock has corresponding java file in
	 * generated source code
	 */
	@Test
	public void checkFunctionBlockExists() throws Exception {
		IGenerationResult generationResult = generateResult(eclipseHonoJavaGenerator,
				modelProvider(validInfoModel + ModelType.InformationModel.getExtension(),
						validFunctionBlock + ModelType.Functionblock.getExtension(), ""));
		assertEquals(true, checkFileExists(generationResult, validFunctionBlock));
	}

	/*
	 * Test case for checking whether datatype has corresponding java file in
	 * generated source code
	 */
	@Test
	public void checkDataTypeExists() throws Exception {
		IGenerationResult generationResult = generateResult(eclipseHonoJavaGenerator,
				modelProvider(infoModelWithType + ModelType.InformationModel.getExtension(),
						functionBlockWithDataType + ModelType.Functionblock.getExtension(),
						dataTypeFile + ModelType.Datatype.getExtension()));
		assertEquals(true, checkFileExists(generationResult, dataTypeFile));
	}
	
	/*
	 * Test case for checking whether Hono Data Service file exists in
	 * generated source code
	 */
	@Test
	public void checkHonoDataServiceFileExists() throws Exception {
		IGenerationResult generationResult = generateResult(eclipseHonoJavaGenerator,
				modelProvider(infoModelWithType + ModelType.InformationModel.getExtension(),
						functionBlockWithDataType + ModelType.Functionblock.getExtension(),
						dataTypeFile + ModelType.Datatype.getExtension()));
		assertEquals(true, checkFileExists(generationResult, "HonoDataService"));
	}
	
	/*
	 * Test case for checking whether HonoMqttClient file exists in
	 * generated source code
	 */
	@Test
	public void checkHonoMQttClientFileExists() throws Exception {
		IGenerationResult generationResult = generateResult(eclipseHonoJavaGenerator,
				modelProvider(infoModelWithType + ModelType.InformationModel.getExtension(),
						functionBlockWithDataType + ModelType.Functionblock.getExtension(),
						dataTypeFile + ModelType.Datatype.getExtension()));
		assertEquals(true, checkFileExists(generationResult, "HonoMqttClient"));
	}

	public boolean checkFileExists(IGenerationResult generationResult, String fileName) throws IOException {
		List<ZipEntry> zipEntryList = extractZipEntries(generationResult);
		for (ZipEntry zipEntry : zipEntryList) {
			if (zipEntry.getName().contains(fileName + ".java")) {
				return true;
			}
		}
		return false;
	}
}
