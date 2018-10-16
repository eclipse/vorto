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
package org.eclipse.vorto.repository.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFilter {

	private Object text;
	private String key;
	private String whereCondition;

	private int valueCount = 1;

	public Object getText() {
		return text;
	}

	public void setText(Object text) {
		this.text = text;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}

	public void setValueCount(int valueCount) {
		this.valueCount = valueCount;
	}

	public int getValueCount() {
		return this.valueCount;
	}

	public List<Object> getParameters() {
		if (text == null) {
			return Collections.emptyList();
		}

		List<Object> parameters = new ArrayList<Object>(valueCount);
		for (int i = 0; i < valueCount; i++) {
			parameters.add(text);
		}

		return parameters;
	}

}
