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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.AbstractDummyDeviceGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BlueprintConfigGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.DummyDeviceGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.EventReadTaskGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.IDummyDeviceGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.PomTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class DummyBaseDriverGenerator implements ICodeGeneratorTask<FunctionblockModel> {

	@Override
	public void generate(FunctionblockModel fbm, InvocationContext mappingContext, IGeneratedWriter outputter) {
		ChainedCodeGeneratorTask<FunctionblockModel> generator = new ChainedCodeGeneratorTask<FunctionblockModel>();
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new PomTemplate()));
		generator.addTask(new BaseDriverGeneratorTask());
		generator.addTask(new IDummyDeviceGeneratorTask());
		generator.addTask(new AbstractDummyDeviceGeneratorTask());
		generator.addTask(new DummyDeviceGeneratorTask());
		generator.addTask(new EventReadTaskGeneratorTask());
		generator.addTask(new BlueprintConfigGeneratorTask());
		generator.generate(fbm, mappingContext, outputter);
	}
}
