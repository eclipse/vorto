/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.codegen.jsonschema;

import org.eclipse.vorto.codegen.jsonschema.templates.PropertiesTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;
import org.eclipse.vorto.plugin.generator.utils.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.plugin.generator.utils.IGeneratedWriter;
import org.eclipse.vorto.plugin.generator.utils.SingleGenerationResult;

public class JSONSchemaGenerator implements ICodeGenerator {

    private static final String KEY = "jsonschema";

    private final String version;

    public JSONSchemaGenerator() {
        version = loadVersionFromResources();
    }

    @Override
    public IGenerationResult generate(InformationModel infomodel, InvocationContext context) throws GeneratorException {
        IGenerationResult output = initializeResultObject(infomodel);
        for (FunctionblockProperty fbProperty : infomodel.getProperties()) {
            new GeneratorTaskFromFileTemplate(new PropertiesTemplate()).generate(fbProperty.getType(), context,
                    (IGeneratedWriter) output);
        }
        return output;
    }

    private IGenerationResult initializeResultObject(final InformationModel infomodel) {
        if (infomodel.getProperties().size() == 1) {
            return new SingleGenerationResult("application/schema+json");
        } else {
            return new GenerationResultZip(infomodel, KEY);
        }
    }

    @Override
    public GeneratorPluginInfo getMeta() {
        return GeneratorPluginInfo.Builder(KEY)
                .withDescription("Generates JSON Schema for Vorto Models")
                .withName("JSON Schema")
                .withDocumentationUrl("https://json-schema.org")
                .withVendor("Eclipse Vorto Team")
                .withPluginVersion(version)
                .build();
    }
}
