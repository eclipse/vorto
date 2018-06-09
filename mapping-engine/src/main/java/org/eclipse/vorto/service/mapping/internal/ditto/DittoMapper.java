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
package org.eclipse.vorto.service.mapping.internal.ditto;

import org.eclipse.vorto.service.mapping.AbstractDataMapper;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.normalized.FunctionblockData;
import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

/**
 * 
 * Maps data input to Eclipse Ditto / Vorto compliant data format
 *
 */
public class DittoMapper extends AbstractDataMapper<DittoData> {

	public DittoMapper(IMappingSpecification mappingSpecification) {
		super(mappingSpecification);
	}

	@Override
	protected DittoData doMap(InfomodelData input, MappingContext mappingContext) {
		DittoData output = new DittoData();
		
		for (FunctionblockData fbData : input.getFunctionblockData()) {
			FeatureBuilder featureBuilder = Feature.newBuilder(fbData.getId());
			featureBuilder.withStatus(fbData.getStatus());
			featureBuilder.withConfiguration(fbData.getConfiguration());
			output.withFeature(featureBuilder.build());
		}

		return output;
	}

}
