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
package org.eclipse.vorto.wizard;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.vorto.codegen.ui.display.IMessageDisplay;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.codegen.ui.utils.WizardUtil;

public abstract class AbstractVortoWizard extends Wizard implements INewWizard,
		IExecutableExtension {

	private IConfigurationElement configurationElement;
	private IWorkbench workbench;
	private IStructuredSelection selection;
	private IMessageDisplay console = MessageDisplayFactory.getMessageDisplay();
		
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

		this.configurationElement = config;

	}

	public IConfigurationElement getConfigurationElement() {
		if (this.configurationElement != null) {
			return this.configurationElement;
		} else {
			return WizardUtil.getWizardConfigurationElement(this.getClass()
					.getName());
		}
	}

	public IWorkbench getWorkbench() {
		return workbench;
	}

	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	public IMessageDisplay getConsole() {
		return console;
	}

	public void setConsole(IMessageDisplay console) {
		this.console = console;
	}

	public void setConfigurationElement(
			IConfigurationElement configurationElement) {
		this.configurationElement = configurationElement;
	}
}
