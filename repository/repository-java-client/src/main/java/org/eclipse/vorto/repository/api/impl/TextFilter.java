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
