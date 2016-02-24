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
package org.eclipse.vorto.codegen.api;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ChainedCodeGeneratorTask<Context> implements ICodeGeneratorTask<Context> {

	private List<ICodeGeneratorTask<Context>> tasks = new ArrayList<ICodeGeneratorTask<Context>>();
		
	public void addTask(ICodeGeneratorTask<Context> task) {
		this.tasks.add(task);
	}

	public void generate(Context ctx, IMappingContext mappingContext, IGeneratedWriter outputter) {
		for (ICodeGeneratorTask<Context> task : tasks) {
			task.generate(ctx, mappingContext, outputter);
		}
	}
}
