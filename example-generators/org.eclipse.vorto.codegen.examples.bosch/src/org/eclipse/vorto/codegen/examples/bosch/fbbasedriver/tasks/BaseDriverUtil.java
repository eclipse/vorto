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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class BaseDriverUtil {
	public final static String JAVA_SRC_LOC = "/src/main/java/";

	public static String getDriverPath() {
		String driverPackage = getDriverPackage();
		String packagePath = driverPackage.replaceAll("\\.", "/");
		return JAVA_SRC_LOC + packagePath;
	}

	public static String getDevicePath() {
		String driverPackage = getDevicePackage();
		String packagePath = driverPackage.replaceAll("\\.", "/");
		return JAVA_SRC_LOC + packagePath;
	}

	public static String getDeviceApiPath() {
		String devicePackage = getDeviceApiPackage();
		String packagePath = devicePackage.replaceAll("\\.", "/");
		return JAVA_SRC_LOC + packagePath;
	}

	public static String getDriverPackage() {
		return "com.bosch.functionblock.dummy.internal.basedriver";
	}

	public static String getDeviceApiPackage() {
		return "com.bosch.functionblock.dummy.api.device";
	}

	public static String getDevicePackage() {
		return "com.bosch.functionblock.dummy.internal.device";
	}

	/*
	 * public static String getDevicePackage(FunctionblockModel model) { return
	 * "com.bosch.functionblock.dummy.device." + model.getName().toLowerCase();
	 * }
	 */

	public static String getArtifactId(FunctionblockModel model) {
		return model.getNamespace() + ".dummy-basedriver";
	}
}
