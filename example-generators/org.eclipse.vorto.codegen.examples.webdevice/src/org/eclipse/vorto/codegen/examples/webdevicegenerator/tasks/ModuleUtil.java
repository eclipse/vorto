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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class ModuleUtil {
	public final static String JAVA_SRC_LOC = "/src/main/java/";
	private final static String MODEL_PACKAGE = "org.eclipse.vorto.iot.{0}.model";
	private final static String INFO_MODEL_MODEL_PACKAGE = "org.eclipse.vorto.iot.infomodel.model";
	private final static String INFO_MODEL_SERVICE_PACKAGE = "org.eclipse.vorto.iot.infomodel.service";
	private final static String SERVICE_PACKAGE = "org.eclipse.vorto.iot.{0}.service";
	private final static String ENTITY_PACKAGE ="org.eclipse.vorto.iot.datatype";
	private final static String ENUM_PACKAGE ="org.eclipse.vorto.iot.datatype";
	
	public static String getEnumPath(Enum e) {
		String path = ENUM_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, e.getName().toLowerCase());
	}
	
	public static String getEnumPackage(Enum e) {
		return MessageFormat.format(ENUM_PACKAGE, e.getName().toLowerCase());
	}
	
	public static String getEntityPath(Entity entity) {
		String path = ENTITY_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, entity.getName().toLowerCase());
	}

	public static String getInfoModelPath(InformationModel infomodel) {
		String path = INFO_MODEL_MODEL_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, infomodel.getName().toLowerCase());
	}
	
	public static String getEntityPackage(Entity entity) {
		return MessageFormat.format(ENTITY_PACKAGE, entity.getName().toLowerCase());
	}
	
	public static String getModelPath(FunctionblockModel model) {
		String path = MODEL_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, model.getName().toLowerCase());
	}

	public static String getModelPackage(FunctionblockModel model) {
		return MessageFormat.format(MODEL_PACKAGE, model.getName()
				.toLowerCase());
	}

	public static String getInfoModelModelPackage(InformationModel model) {
		return MessageFormat.format(INFO_MODEL_MODEL_PACKAGE, model.getName()
				.toLowerCase());
	}
	
	public static String getInfoModelServicePackage(InformationModel model) {
		return MessageFormat.format(INFO_MODEL_SERVICE_PACKAGE, model.getName()
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

	public static String getInfoModelServicePath(InformationModel model) {
		String path = INFO_MODEL_SERVICE_PACKAGE.replaceAll("\\.", "/");
		return JAVA_SRC_LOC
				+ MessageFormat.format(path, model.getName().toLowerCase());
	}

	public static String getArtifactId(InformationModel informationModel) {
		return informationModel.getName().toLowerCase() + "-webapp";
	}
	
	public static String getCamelCase(String string) {
		return StringUtils.capitalize(string);
	}
}
