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
package org.eclipse.vorto.codegen.ui.wizard.command;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.vorto.codegen.ui.handler.WizardCreateProjectHandler;
import org.eclipse.vorto.codegen.ui.wizard.GeneratorWizard;

public class NewGeneratorProjectCommand extends WizardCreateProjectHandler {

	@Override
	public Wizard getWizard() {
		return new GeneratorWizard();
	}
}
