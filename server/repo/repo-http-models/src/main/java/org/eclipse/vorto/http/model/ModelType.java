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
<<<<<<< 8fce91bd96d10ca25d52d3920c7f9527391e78e2:server/repo/repo-core/src/main/java/org/eclipse/vorto/repository/model/ModelType.java
 */
package org.eclipse.vorto.http.model;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public enum ModelType {
	Functionblock(".fbmodel"),
	InformationModel(".infomodel"),
	Datatype(".type"),
	Mapping(".mapping");
	
	private String extension;
	
	ModelType(String extension) {
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public static ModelType fromFileName(String fileName) {
		String type = fileName.substring(fileName.lastIndexOf("."));
		if (type.equals(ModelType.Functionblock.getExtension())) {
			return ModelType.Functionblock;
		} else if (type.equals(ModelType.InformationModel.getExtension())) {
			return ModelType.InformationModel;
		} else if (type.equals(ModelType.Datatype.getExtension())) {
			return ModelType.Datatype;
		} else if (type.equals(ModelType.Mapping.getExtension())) {
			return ModelType.Mapping;
		} else {
			throw new IllegalArgumentException(fileName);
		}
	}
}
