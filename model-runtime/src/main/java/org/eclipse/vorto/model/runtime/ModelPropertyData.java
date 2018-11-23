/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.ModelProperty;

public class ModelPropertyData {
	private ModelProperty propertyMeta;
	private Object value;
	
	public ModelPropertyData(ModelProperty meta, Object value) {
		this.propertyMeta = meta;
		this.value = value;
	}
	
	public ModelProperty getPropertyMeta() {
		return propertyMeta;
	}
	public void setPropertyMeta(ModelProperty propertyMeta) {
		this.propertyMeta = propertyMeta;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ModelPropertyData [propertyMeta=" + propertyMeta + ", value=" + value + "]";
	}
}
