/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel

import java.util.LinkedList
import org.eclipse.vorto.core.api.model.datatype.Property

class ConstraintExceptionAdapter {
	
	private static val String[] exemptedConstraints = #['MIMETYPE']
	
	public static def returnValidConstraintLength(Property property){
		var constraints = property.constraints
		var length = constraints.length
		for(constraint : constraints){
			
		if(exemptedConstraints.indexOf(constraint.type.literal) >= 0)
			length --;
		}
		
		return length;
	}
	
	public static def getValidConstraints(Property property) {
		 var constraints = new LinkedList
		 for( constraint : property.constraints ){
		 	if(exemptedConstraints.indexOf(constraint.type.literal) == -1)
		 		constraints.add(constraint)
		 }
		 return constraints
	}
}
