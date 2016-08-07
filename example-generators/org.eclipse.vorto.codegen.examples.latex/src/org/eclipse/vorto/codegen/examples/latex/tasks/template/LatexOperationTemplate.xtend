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
package org.eclipse.vorto.codegen.examples.latex.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.utils.Utils
import org.eclipse.vorto.core.api.model.functionblock.Operation

class LatexOperationTemplate implements ITemplate<Operation> {
	
	override getContent(Operation operation,InvocationContext invocationContext) {
		'''
			\begin{tabular}{|p{4.5cm}|p{4cm}|p{7cm}|}
			\hline
				\multicolumn{3}{|p{15.5cm}|}{\textbf{Operation}}\\
			\hline
				Name &  ReturnType & Description\\
			\hline\hline
				«operation.name.replace("_","\\_")» & «Utils.getReturnType(operation.returnType)» & «operation.description»\\
				\hline
			\end{tabular}\\\\\\
		'''
	}
}