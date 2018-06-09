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

package org.eclipse.vorto.editor.functionblock.validation

import java.util.ArrayList
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.vorto.core.api.model.datatype.Type

class TypeFileAccessingHelper implements TypeHelper{
	
	override List<Type> getAllTypeFromReferencedTypeFile(EObject eObject) {
		var all = new ArrayList<Type>();

		var rSet = eObject.eResource.resourceSet;

		for (r : rSet.resources) {
			var obj = r.contents.get(0);
			if (obj instanceof Type) {
				all.add(obj);
			}
		}

		return all;
	}
}