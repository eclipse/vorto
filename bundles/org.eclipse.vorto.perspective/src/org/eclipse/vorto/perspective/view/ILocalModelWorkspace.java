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
package org.eclipse.vorto.perspective.view;

import java.util.Collection;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.vorto.core.ui.model.IModelProject;

public interface ILocalModelWorkspace {

	IModelProjectBrowser getProjectBrowser();
	
	Shell getShell();
	
	/**
	 * refreshes the model browser by reloading all models for the selected project
	 */
	void refresh();
	
	/**
	 * Only refreshes the current selected project
	 */
	void refreshCurrent();
			
	public interface IModelProjectBrowser {
				
		/**
		 * Gets the currently selected model project
		 * @return
		 */
		IModelProject getSelectedProject();
		
		/**
		 * Returns all model project that are available 
		 * @return
		 */
		Collection<IModelProject> getModelProjects();
		
		/**
		 * Removes the given model project from the the project browser
		 * @param project
		 */
		void removeProject(IModelProject project);
		
		
	}
}
