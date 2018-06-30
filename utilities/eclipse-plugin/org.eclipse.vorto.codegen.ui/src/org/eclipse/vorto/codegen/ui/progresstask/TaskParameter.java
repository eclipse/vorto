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
package org.eclipse.vorto.codegen.ui.progresstask;

import java.util.HashMap;


public class TaskParameter {

	private HashMap<String, Object> map = new HashMap<>();

	public void add(String string, Object param) {
		map.put(string, param);
	}

	public Object get(String string) {
		return map.get(string);
	}

	public boolean contains(String string) {
		return map.containsKey(string);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

}
