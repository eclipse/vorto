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

package org.eclipse.vorto.remoterepository.internal.converter.parser.xml;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.w3c.dom.Document;

public class FunctionBlockXmiModelParser extends AbstractXPathModelParser {
	
	@Override
	ModelView generateModelView(Document dom) throws XPathExpressionException {
		ModelId modelId = ModelFactory.newModelId(
				ModelType.FUNCTIONBLOCK, namespaceExpr.evaluate(dom), versionExpr.evaluate(dom), nameExpr.evaluate(dom));
		ModelView modelView =  ModelFactory.newModelView(modelId,
				descriptionExpr.evaluate(dom));
		
		return modelView;
	}

	@Override
	String getNameExpression() {
		return "/FunctionblockModel/@name";
	}

	@Override
	String getNameSpaceExpression() {
		return "/FunctionblockModel/@namespace";
	}

	@Override
	String getVersionExpression() {		
		return "/FunctionblockModel/@version";
	}

	@Override
	String getDescriptionExpression() {
		return "/FunctionblockModel/functionblock/@description";
	}

	@Override
	String getModelReferenceExpression() {
		return "/FunctionblockModel/references";
	}


}
