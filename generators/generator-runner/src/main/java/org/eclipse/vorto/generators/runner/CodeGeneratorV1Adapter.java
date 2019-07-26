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
package org.eclipse.vorto.generators.runner;

import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;

public class CodeGeneratorV1Adapter implements IVortoCodeGenerator {

	private ICodeGenerator generator;

	public CodeGeneratorV1Adapter(ICodeGenerator generator) {
		this.generator = generator;
	}

	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		try {
			org.eclipse.vorto.plugin.generator.IGenerationResult result = generator.generate(model, org.eclipse.vorto.plugin.generator.InvocationContext
					.simpleInvocationContext(context.getConfigurationProperties()));
			return new IGenerationResult() {

				@Override
				public String getFileName() {
					return result.getFileName();
				}

				@Override
				public byte[] getContent() {
					return result.getContent();
				}

				@Override
				public String getMediatype() {
					return result.getMediatype();
				}
				
			};
		} catch (GeneratorException e) {
			throw new VortoCodeGeneratorException(e);
		}
	}

	@Override
	public String getServiceKey() {
		return this.generator.getMeta().getKey();
	}

	@Override
	public GeneratorInfo getInfo() {
		return GeneratorInfo.basicInfo(generator.getMeta().getKey(), generator.getMeta().getDescription(),
				generator.getMeta().getVendor()).production();
	}

}
