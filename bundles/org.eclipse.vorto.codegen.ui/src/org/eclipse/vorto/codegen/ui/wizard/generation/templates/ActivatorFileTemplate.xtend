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
 
 package org.eclipse.vorto.codegen.ui.wizard.generation.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class ActivatorFileTemplate implements ITemplate<IGeneratorProjectContext> {
	
	public override String getContent(IGeneratorProjectContext metaData) {
		return '''
		package «metaData.packageName»;
		
		import org.eclipse.ui.plugin.AbstractUIPlugin;
		import org.osgi.framework.BundleContext;
		
		/**
		 * The activator class controls the plug-in life cycle
		 */
		public class Activator extends AbstractUIPlugin {
		
			// The plug-in ID
			public static final String PLUGIN_ID = "«metaData.generatorName»"; //$NON-NLS-1$
		
			// The shared instance
			private static Activator plugin;
			
			/**
			 * The constructor
			 */
			public Activator() {
			}
		
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
			 */
			public void start(BundleContext context) throws Exception {
				super.start(context);
				plugin = this;
			}
		
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
			 */
			public void stop(BundleContext context) throws Exception {
				plugin = null;
				super.stop(context);
			}
		
			/**
			 * Returns the shared instance
			 *
			 * @return the shared instance
			 */
			public static Activator getDefault() {
				return plugin;
			}
		
		}
		'''
	}
	
}
