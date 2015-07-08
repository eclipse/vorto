/*******************************************************************************
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

package org.eclipse.vorto.remoterepository.service.converter;

import java.util.Objects;

import org.eclipse.vorto.remoterepository.internal.converter.parser.IModelParser;
import org.eclipse.vorto.remoterepository.internal.converter.parser.ModelParserFactory;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.springframework.stereotype.Service;

@Service
public class DefaultConverterService implements IModelConverterService {

	@Override
	public ModelView convert(ModelContent modelContent) {
		
		Objects.requireNonNull(modelContent.getContent(), "model content must not be null");
		
		IModelParser modelConverter = new ModelParserFactory()
				.getModelParser(modelContent.getModelType());
		
		return modelConverter.parse(modelContent);
	}

}
