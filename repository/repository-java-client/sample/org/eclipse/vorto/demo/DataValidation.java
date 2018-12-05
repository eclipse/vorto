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
package org.eclipse.vorto.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Example code that validates JSON against JSON Schema for Eclipse Ditto 
 * The schema is dynamically generated and downloaded from the Vorto Repository upon initialization.
 * 
 * Uses third-party dependency: <a href="https://github.com/everit-org/json-schema/tree/1.3.0">JSON Schema Validator</a> 
 * 
 * @author Alexander Edelmann
 */
public class DataValidation {

	private static final String GENERATOR_KEY = "eclipseditto";

	private static final String PUBLIC_ECLIPSE_REPO_URL = "http://vorto.eclipse.org";

	private static final IModelGeneration repoClient = getGenerationClient();
	
	private static Map<ModelId, String> schemata = new HashMap<ModelId, String>();
	
	static {
		try {
			schemata = loadSchemataFromRepository(ModelId.fromPrettyFormat("com.ipso.smartobjects.Presence:0.0.1"),
												  ModelId.fromPrettyFormat("com.ipso.smartobjects.Buzzer:0.0.1"));
		} catch (Exception e) {
			System.err.println("Problem loading JSON schemata from Vorto Repository");
			e.printStackTrace();
		}
	}

	private static IModelGeneration getGenerationClient() {
		return RepositoryClientBuilder.newBuilder().setBaseUrl(PUBLIC_ECLIPSE_REPO_URL).buildModelGenerationClient();
	}

	private static Map<ModelId, String> loadSchemataFromRepository(ModelId... modelIds)
			throws Exception {
		Map<ModelId, String> schemata = new HashMap<ModelId, String>();
		for (ModelId modelId : modelIds) {
			schemata.put(modelId, generateJsonSchema(modelId));
		}
		return schemata;
	}

	private static String generateJsonSchema(ModelId modelId) throws Exception {
		CompletableFuture<GeneratedOutput> result = repoClient.generate(modelId, GENERATOR_KEY, Collections.emptyMap());
		byte[] jsonSchemaZip = result.get().getContent();
		return extractJsonFileFromZip(jsonSchemaZip);
	}

	private static String extractJsonFileFromZip(byte[] jsonSchemaZip) {
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(jsonSchemaZip));
		ZipEntry entry = null;
		try {
			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory() && entry.getName().contains("properties-status.schema")) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(zis, baos);
					return baos.toString();
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Problem reading json schema zip file", ex);
		}
		return null;
	}

	public static void main(String[] args)  {

		ModelId presenceCapability = ModelId.fromPrettyFormat("com.ipso.smartobjects.Presence:0.0.1");

		JSONObject jsonSchema = getSchemaForCapability(presenceCapability);
		JSONObject jsonInput = getJsonInput();

		Schema schema = SchemaLoader.load(jsonSchema);
		try {
			schema.validate(jsonInput);
			System.out.println("Congrats. The JSON is valid");
		} catch (ValidationException validationException) {
			System.err.println("JSON is not valid according to schema");
			validationException.printStackTrace();
		}
	}
	
	private static JSONObject getSchemaForCapability(ModelId modelId) {
		return new JSONObject(new JSONTokener(schemata.get(modelId)));
	}
	
	private static JSONObject getJsonInput() {
		return new JSONObject(
				new JSONTokener(DataValidation.class.getResourceAsStream("/input.json")));
	}
}
