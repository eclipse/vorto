/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.examples.webui.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModuleUtil {
	public final static String JAVA_SRC_LOC = "src/main/java/";

	public static String getArtifactId(InformationModel informationModel) {
		return informationModel.getName().toLowerCase() + "-webapp";
	}
	
	public static String getCamelCase(String string) {
		return StringUtils.capitalize(string);
	}
	
	public static List<FunctionblockModel> getFunctionBlocksUsingStatus(InformationModel model) {
		List<FunctionblockModel> result = new ArrayList<>();
		for (FunctionblockProperty fbProp : model.getProperties()) {
			if (fbProp.getType().getFunctionblock().getStatus() != null) {
				result.add(fbProp.getType());
			}
		}
		return result;
	}
}
