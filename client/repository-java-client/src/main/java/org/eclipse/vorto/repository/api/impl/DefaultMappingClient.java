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
package org.eclipse.vorto.repository.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.vorto.repository.api.content.EntityModel;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.IMappedElement;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.mapping.IMapping;
import org.eclipse.vorto.repository.api.mapping.IMappingQuery;

public class DefaultMappingClient implements IMapping{

	@Override
	public IMappingQuery<ModelProperty> newPropertyQuery(IMappedElement element) {
		final List<ModelProperty> properties = new ArrayList<ModelProperty>();
		
		if (element instanceof FunctionblockModel) {
			FunctionblockModel fbm = (FunctionblockModel)element;
			properties.addAll(fbm.getConfigurationProperties());
			properties.addAll(fbm.getStatusProperties());
			properties.addAll(fbm.getFaultProperties());
		} else if (element instanceof Infomodel) {
			Infomodel im = (Infomodel)element;
			properties.addAll(im.getFunctionblocks());
		} else if (element instanceof EntityModel) {
			EntityModel entity = (EntityModel)element;
			properties.addAll(entity.getProperties());
		} else {
			throw new UnsupportedOperationException();
		}
		
		return new MappingQueryJxPath<ModelProperty>() {

			@Override
			protected Collection<ModelProperty> getAll() {
				return properties;
			}	
		};
	}

}
