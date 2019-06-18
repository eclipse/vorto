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
package org.eclipse.vorto.plugin.generator.adapter;

import java.util.Optional;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.conversion.ModelContentToEcoreConverter;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.utils.Utils;

public class ICodeGeneratorModelAdapter {

	private ICodeGenerator adaptee;
	
	private ModelContentToEcoreConverter converter;
	
	public ICodeGeneratorModelAdapter(ICodeGenerator adaptee) {
		this.adaptee = adaptee;
		this.converter = new ModelContentToEcoreConverter();
	}
	
	public IGenerationResult generate (ModelContent model, InvocationContext context) throws GeneratorException {
		
		Model convertedModel = converter.convert(model, Optional.of(adaptee.getMeta().getKey()));		
		InformationModel infomodel = Utils.toInformationModel(convertedModel);
		return adaptee.generate(infomodel, context);
	}
}
