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
import org.eclipse.vorto.core.api.model.datatype.Property

class LatexSimplePropertyConstraintTemplate implements ITemplate<Property> {
	
	override getContent(Property property,InvocationContext invocationContext) {
		'''
			\begin{tabular}{|p{4.5cm}|p{4cm}|p{2.05cm}|p{2.05cm}|p{2.05cm}|}
			\hline
				\multicolumn{5}{|p{15.5cm}|}{\textbf{Property}}\\
			\hline
				Name &  Data Type & Min Value & Max Value & Scaling\\
			\hline\hline
				«property.name.replace("_","\\_")» & «Utils.getPropertyType(property)» & «Utils.getMinConstraint(property)»&«Utils.getMaxConstraint(property)»& \\
				\hline
				\multicolumn{5}{|p{15.5cm}|}{
					«property.description»
					«IF Utils.isEventable(property)|| Utils.isReadable(property)||Utils.isWritable(property)»
						The property is:
						\begin{itemize}
							«IF Utils.isReadable(property)»
								\item readable
							«ENDIF»
							«IF Utils.isWritable(property)»
								\item writable
							«ENDIF»
							«IF Utils.isEventable(property)»
								\item eventable
							«ENDIF»
						\end{itemize}
					«ENDIF»
					«IF (Utils.getMeasurementUnit(property)!=null)»
					Values are measured in «Utils.getMeasurementUnit(property)».
					«ENDIF»
				}\\
				\hline
			\end{tabular}\\\\\\
		'''
	}
}