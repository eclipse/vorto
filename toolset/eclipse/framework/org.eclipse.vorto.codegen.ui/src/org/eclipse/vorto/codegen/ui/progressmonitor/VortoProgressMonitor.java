/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.progressmonitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.vorto.codegen.api.IVortoCodeGenInfrastructureProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;

public class VortoProgressMonitor implements IVortoCodeGenInfrastructureProgressMonitor, IVortoCodeGenProgressMonitor{
	
	
	private static VortoProgressMonitor fDefault = null;
	private static List<Status> generatorStatusList = new ArrayList<>();
	private static List<Status> infrastructureStatusList = new ArrayList<>();
	private static boolean warningPresent = false;
	
	private static final String PLUGIN_ID = "org.eclipse.vorto.vortonature";
	private static final String INFO_PREFIX = "[INFO] ";
	private static final String WARNING_PREFIX = "[WARNING] ";
	
	public static VortoProgressMonitor getDefault() {
		if (fDefault == null) {
			fDefault = new VortoProgressMonitor();
		}
		return fDefault;
	}

	@Override
	public void monitorWarning(final String message) {
		generatorStatusList.add(new Status(IStatus.WARNING, PLUGIN_ID, WARNING_PREFIX + message));
		warningPresent = true;
	}

	@Override
	public void monitorInfo(final String message) {
		generatorStatusList.add(new Status(IStatus.INFO, PLUGIN_ID, INFO_PREFIX + message));
	}

	@Override
	public void monitorInfrastructureWarning(final String message) {
		infrastructureStatusList.add(new Status(IStatus.WARNING, PLUGIN_ID, WARNING_PREFIX + message));
		warningPresent = true;
	}

	@Override
	public void monitorInfrastructureInfo(final String message) {
		infrastructureStatusList.add(new Status(IStatus.INFO, PLUGIN_ID, INFO_PREFIX + message));
	}
	
	
	public void display() {
		List<Status> buildResults = new ArrayList<>();
		String message;

		if (!generatorStatusList.isEmpty()) {
			buildResults.add(new MultiStatus(PLUGIN_ID, IStatus.INFO, generatorStatusList.toArray(new Status[] {}),
					"Code Generator Status Messages", null));
		}

		if (!infrastructureStatusList.isEmpty()) {
			buildResults.add(new MultiStatus(PLUGIN_ID, IStatus.INFO, infrastructureStatusList.toArray(new Status[] {}),
					"Code Generator Infrastructure Status Messages", null));
		}

		if (warningPresent) {
			message = "Code generation finished with warnings!";
		} else {
			message = "Code generation successul!";
		}

		MessageDisplayFactory.getMessageDisplay().displayStatus(
				new MultiStatus(PLUGIN_ID, IStatus.INFO, buildResults.toArray(new Status[] {}), message, null));

		/* Reset status lists */
		generatorStatusList = new ArrayList<>();
		infrastructureStatusList = new ArrayList<>();
		warningPresent = false;
	}

}
