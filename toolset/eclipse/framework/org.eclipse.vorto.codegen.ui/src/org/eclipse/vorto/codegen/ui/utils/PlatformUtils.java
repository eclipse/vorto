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
package org.eclipse.vorto.codegen.ui.utils;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**
 * This is helper class and provides custom functions using Eclipse core library  
 *
 */
public class PlatformUtils {
	
	public static final String ORG_ECLIPSE_M2E_CORE_PLUGINID = "org.eclipse.m2e.core";
	
	public static final String JAVA_PERSPECTIVE = "org.eclipse.jdt.ui.JavaPerspective";
	
	/**
	 * Determine whether the plugin is installed/available in the eclipse runtime.
	 * e.g "org.eclipse.m2e.core" to check whether maven plugins is installed. 
	 * @param pluginIdentifier
	 * @return true if plugin is available in the runtime
	 */
	public static boolean isPluginAvailable(String pluginIdentifier) {
		 return Platform.getBundle(pluginIdentifier)!=null;
		
	}
	
	
	/***
	 * Open perspective with the passed in perspective id
	 * @param perspectiveId - unique perspective id to switch
	 */
	public static void switchPerspective(String perspectiveId) {
		try {
		   PlatformUI.getWorkbench().showPerspective(perspectiveId,       
		         PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} 
		catch (WorkbenchException e) {
		   e.printStackTrace();
		}
		
	}

}
