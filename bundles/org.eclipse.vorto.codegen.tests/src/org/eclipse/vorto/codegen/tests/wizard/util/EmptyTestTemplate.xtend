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

class EmptyTestTemplate {
		public def String printTribleQuotationMarks() {
		return "'''";
	}

	public def String printEntryArrows() {
		return "«";
	}

	public def String printExitArrows() {
		return "»";
	}

	public def String print() {
		return '''
		package testPackageName
		
		import org.eclipse.vorto.codegen.api.ICodeGenerator
		import org.eclipse.core.runtime.IProgressMonitor
		import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
		
		class testGeneratorName implements ICodeGenerator<InformationModel> {
		
			override generate(InformationModel ctx, IProgressMonitor monitor) {
				throw new UnsupportedOperationException("TODO: auto-generated method")
			}
			override getName() {
				return "testGeneratorName";
			}
		}
		'''
	}}