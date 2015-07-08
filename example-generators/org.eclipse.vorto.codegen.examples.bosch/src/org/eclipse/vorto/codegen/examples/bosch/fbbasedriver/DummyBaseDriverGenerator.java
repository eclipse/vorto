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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.examples.bosch.common.BoschM2MNature;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.AbstractDummyDeviceGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverUtil;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BlueprintConfigGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.DummyDeviceGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.EventReadTaskGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.IDummyDeviceGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.PomTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class DummyBaseDriverGenerator implements
		ICodeGenerator<FunctionblockModel> {

	@Override
	public void generate(FunctionblockModel fbm, final IProgressMonitor monitor) {

		new EclipseProjectGenerator<FunctionblockModel>(
				BaseDriverUtil.getArtifactId(fbm))
				.mavenNature(new PomTemplate())
				.addNature(BoschM2MNature.M2M_NATURE_ID)
				.addTask(new BaseDriverGeneratorTask())
				.addTask(new IDummyDeviceGeneratorTask())
				.addTask(new AbstractDummyDeviceGeneratorTask())
				.addTask(new DummyDeviceGeneratorTask())
				.addTask(new EventReadTaskGeneratorTask())
				.addTask(new BlueprintConfigGeneratorTask())
				.generate(fbm, monitor);
		;
	}

	@Override
	public String getName() {
		return "Dummy Base Driver Generator";
	}
}
