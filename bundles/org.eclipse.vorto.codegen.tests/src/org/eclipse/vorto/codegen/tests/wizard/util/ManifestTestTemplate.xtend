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

package org.eclipse.vorto.codegen.tests.wizard.util

class ManifestTestTemplate {
	public def String printFilled() {
		return '''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: testProjectName
			Bundle-SymbolicName: testProjectName;singleton:=true
			Bundle-Version: 1.0.0.qualifier
			Bundle-Activator: testPackageName.Activator
			Require-Bundle: a,
			 b,
			 c,
			 d,
			 e
			Bundle-RequiredExecutionEnvironment: JavaSE-1.7
			Bundle-ActivationPolicy: lazy
			Import-Package: f,
			 g,
			 h,
			 i,
			 j
		'''
	}

	public def String printEmpty() {
		return '''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: testProjectName
			Bundle-SymbolicName: testProjectName;singleton:=true
			Bundle-Version: 1.0.0.qualifier
			Bundle-Activator: testPackageName.Activator
			Bundle-RequiredExecutionEnvironment: JavaSE-1.7
			Bundle-ActivationPolicy: lazy
		'''
	}
}
