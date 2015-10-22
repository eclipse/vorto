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
package org.eclipse.vorto.remoterepository.internal.converter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.converter.IModelConverterService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class XpathConverterService implements IModelConverterService {

	@Override
	public ModelView convert(ModelContent modelContent) {
		return convertToModelView(modelContent.getContent());
	}

	private static final String ROOT_ENUM = "datatype:Enum";
	private static final String ROOT_DATATYPE = "datatype:Entity";
	private static final String ROOT_FUNCTIONBLOCK = "functionblock:FunctionblockModel";
	private static final String ROOT_INFORMATIONMODEL = "informationmodel:InformationModel";

	private DocumentBuilder documentBuilder;
	private XPathExpression modelTypeExpr;
	private Map<String, XpathModelViewBuilder> modelViewBuilders = new HashMap<String, XpathModelViewBuilder>();
	
	public XpathConverterService() {
		// Initialize Dom builder
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		try {
			documentBuilder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Error in constructing DOM builder", e);
		}

		// Initialize model view builders
		modelViewBuilders.put(ROOT_FUNCTIONBLOCK,
				getFunctionBlockBuilder());
		modelViewBuilders
				.put(ROOT_INFORMATIONMODEL, getInfoModelBuilder());
		modelViewBuilders.put(ROOT_DATATYPE, getDataTypeBuilder());
		modelViewBuilders.put(ROOT_ENUM, getEnumBuilder());
		// initialize XPath expression for getting the model type
		try {
			modelTypeExpr = XPathFactory.newInstance().newXPath().compile("name(/*)");
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Error in compiling model type expression", e);
		}
	}

	@Override
	public ModelContent convertToModelContent(byte[] modelFile) {
		ModelView modelView = convertToModelView(modelFile);
		return new ModelContent(modelView.getModelId(),
				modelView.getModelId().getModelType(), modelFile);
	}
	
	@Override
	public ModelView convertToModelView(byte[] modelFile) {
		// Convert to something we can parse
		Document xmiDocument = convertToXmiDocument(modelFile);
		
		// Use model view builder to create model view
		return modelViewBuilders.get(getModelType(xmiDocument)).build(xmiDocument);
	}
	
	private Document convertToXmiDocument(byte[] modelFile) {
		InputStream inputStream = new ByteArrayInputStream(modelFile);
		try {
			return documentBuilder.parse(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing model file inputstream", e);
		} finally {
			documentBuilder.reset();
		}
	}
	
 	private String getModelType(Document dom) {
 		try {
 			return modelTypeExpr.evaluate(dom);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Error while evaluation DOM to get model type", e);
		}
 	}
 	
 	private XpathModelViewBuilder getFunctionBlockBuilder() {
		return new XpathModelViewBuilder(ModelType.FUNCTIONBLOCK,
				"/FunctionblockModel/@name", "/FunctionblockModel/@namespace",
				"/FunctionblockModel/@version",
				"/FunctionblockModel/@description",
				"/FunctionblockModel/references");
	}

	private XpathModelViewBuilder getInfoModelBuilder() {
		return new XpathModelViewBuilder(ModelType.INFORMATIONMODEL,
				"/InformationModel/@name", "/InformationModel/@namespace",
				"/InformationModel/@version", "/InformationModel/@description",
				"/InformationModel/references");
	}

	private XpathModelViewBuilder getDataTypeBuilder() {
		return new XpathModelViewBuilder(ModelType.DATATYPE,
				"/Entity/@name", "/Entity/@namespace", "/Entity/@version",
				null, "/Entity/references");
	}
	
	private XpathModelViewBuilder getEnumBuilder() {
		return new XpathModelViewBuilder(ModelType.DATATYPE,
				"/Enum/@name", "/Enum/@namespace", "/Enum/@version",
				null, null); 
	}
}
