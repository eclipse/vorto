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
package org.eclipse.vorto.mapping.engine;

import org.eclipse.vorto.mapping.engine.json.JsonInput;

public class DataInputFactory {

	private static DataInputFactory singleton = null;
	
	private DataInputFactory() {
		
	}
	
	public static DataInputFactory getInstance() {
		if (singleton == null) {
			singleton = new DataInputFactory();
		}
		
		return singleton;
	}
		
	public DataInput fromObject(Object obj) {
		return new DataInput() {
			
			@Override
			public Object getValue() {
				return obj;
			}
		};
	}
	
	public DataInput fromJson(String json) {
		return new JsonInput(json);
	}
}
