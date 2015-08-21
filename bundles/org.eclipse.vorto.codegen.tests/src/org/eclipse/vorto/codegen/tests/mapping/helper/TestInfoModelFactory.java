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
package org.eclipse.vorto.codegen.tests.mapping.helper;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;

public class TestInfoModelFactory {
	public static InformationModel createInformationModel() {
		InformationModel informationModel = InformationModelFactory.eINSTANCE
				.createInformationModel();

		informationModel.setName("LightingDevice");
		informationModel.setDescription("Lighting Device");
		informationModel.setCategory("demo");
		informationModel.setDescription("comment");
		informationModel.setNamespace("www.bosch.com");
		informationModel.setVersion("1.2.3");

		informationModel
				.getProperties()
				.add(createFunctionblockProperty(TestFunctionBlockFactory.createFunctionBlockModel()));
		return informationModel;
	}

	private static FunctionblockProperty createFunctionblockProperty(
			FunctionblockModel fbm) {
		FunctionblockProperty fbp = InformationModelFactory.eINSTANCE
				.createFunctionblockProperty();
		fbp.setName("fbm1");
		fbp.setType(fbm);
		return fbp;
	}
}
