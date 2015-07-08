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

package org.eclipse.vorto.remoterepository.service;

import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.remoterepository.internal.converter.parser.IModelParser;
import org.eclipse.vorto.remoterepository.internal.converter.parser.ModelParserFactory;
import org.eclipse.vorto.remoterepository.internal.converter.parser.xml.DataTypeXmiModelParser;
import org.eclipse.vorto.remoterepository.internal.converter.parser.xml.FunctionBlockXmiModelParser;
import org.eclipse.vorto.remoterepository.internal.converter.parser.xml.InformationModelXmiModelParser;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.junit.Test;

public class ModelParseStrategyFactoryTest {

	@Test
	public void getInforModelParseStrategy() {
		ModelParserFactory factory = new ModelParserFactory();
		IModelParser modelParseStrategy = factory.getModelParser(ModelType.INFORMATIONMODEL);
		assertTrue(modelParseStrategy instanceof InformationModelXmiModelParser);
	}
	
	@Test
	public void getFunctionBlockModelParseStrategy() {
		ModelParserFactory factory = new ModelParserFactory();
		IModelParser modelParseStrategy = factory.getModelParser(ModelType.FUNCTIONBLOCK);
		assertTrue(modelParseStrategy instanceof FunctionBlockXmiModelParser);
	}
	
	@Test
	public void getDataTypeModelParseStrategy() {
		ModelParserFactory factory = new ModelParserFactory();
		IModelParser modelParseStrategy = factory.getModelParser(ModelType.DATATYPE);
		assertTrue(modelParseStrategy instanceof DataTypeXmiModelParser);
	}
}
