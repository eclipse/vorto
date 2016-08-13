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
package org.eclipse.vorto.codegen.examples.bosch.common;

import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Type;

public class FbModelUtils {

	public static Set<Type> getTypesOfType(Type type, Set<Type> container) {
		TreeIterator<EObject> iterator = type.eAllContents();
		while (iterator.hasNext()) {
			EObject current = iterator.next();
			if (current instanceof ObjectPropertyType) {
				if (!container.contains(current)) {
					container.add(((ObjectPropertyType) current).getType());
					Set<Type> moreTypes = getTypesOfType(
							((ObjectPropertyType) current).getType(), container);
					container.addAll(moreTypes);
				}

			}
		}

		return container;
	}
}
