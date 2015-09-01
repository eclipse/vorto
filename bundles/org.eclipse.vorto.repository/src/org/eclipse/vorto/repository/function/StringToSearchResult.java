/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.function;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.repository.model.ModelView;

import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class StringToSearchResult implements Function<String, List<ModelView>> {

	private Gson gson = new GsonBuilder().create();
	
	public List<ModelView> apply(String input) {
		Type resourcesList = new TypeToken<ArrayList<ModelView>>() {}.getType();
		return gson.fromJson(input, resourcesList);
	}

}

