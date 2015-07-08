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

package org.eclipse.vorto.codegen.api.tasks.eclipse.natures;

import java.util.Collections;
import java.util.List;

import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;

public class CustomNature {

	private String nature;

	public CustomNature(String nature) {
		this.nature = nature;
	}

	public <Context> List<ICodeGeneratorTask<Context>> getGeneratorTasks() {
		return Collections.emptyList();
	}

	public String asString() {
		return nature;
	}

	public String toString() {
		return asString();
	}
}
