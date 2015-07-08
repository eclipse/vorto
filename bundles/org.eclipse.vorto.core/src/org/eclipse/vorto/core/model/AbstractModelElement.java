/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.core.model;

public abstract class AbstractModelElement implements IModelElement,
		Comparable<IModelElement> {

	@Override
	public ModelId getId() {
		return ModelIdFactory.newInstance(getModel());
	}

	@Override
	public String getDescription() {
		return ModelIdFactory.newInstance(getModel()).toString();
	}

	@Override
	public int compareTo(IModelElement o) {
		if (o == null || o.getId() == null)
			return -1;
		else
			return o.getId().compareTo(this.getId());
	}

}
