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
package org.eclipse.vorto.codegen.api;

import java.util.Optional;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.conversion.ModelContentToEcoreConverter;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class IVortoCodeGeneratorAdapter {

	private IVortoCodeGenerator adaptee;
	
	private ModelContentToEcoreConverter converter;
	
	public IVortoCodeGeneratorAdapter(IVortoCodeGenerator adaptee) {
		this.adaptee = adaptee;
		this.converter = new ModelContentToEcoreConverter();
	}
	
	public IGenerationResult generate (ModelContent model, InvocationContext context,
		      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		
		Model convertedModel = converter.convert(model, Optional.of(adaptee.getServiceKey()));		
		InformationModel infomodel = Utils.toInformationModel(convertedModel);
		return adaptee.generate(infomodel, context, monitor);
	}
}
