/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
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