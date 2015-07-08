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
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks;

import java.text.MessageFormat;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class ModuleUtil {
	public final static String JAVA_SRC_LOC = "/src/main/java/";
	private final static String MODEL_PACKAGE = "com.bosch.iot.{0}.model";
	private final static String SERVICE_PACKAGE = "com.bosch.iot.{0}.service";

	public static String getModelPath(FunctionblockModel model) {
		String path = MODEL_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, model.getName().toLowerCase());
	}

	public static String getModelPackage(FunctionblockModel model) {
		return MessageFormat.format(MODEL_PACKAGE, model.getName()
				.toLowerCase());
	}

	public static String getServicePath(FunctionblockModel model) {
		String path = SERVICE_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, model.getName().toLowerCase());
	}

	public static String getServicePackage(FunctionblockModel model) {
		return MessageFormat.format(SERVICE_PACKAGE, model.getName()
				.toLowerCase());
	}

	public static String getArtifactId(InformationModel informationModel) {
		return informationModel.getName().toLowerCase() + "-webapp";
	}
}
