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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.vorto.remoterepository.internal.converter.parser.IModelParser;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelReference;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractXPathModelParser implements IModelParser {

	XPath xpath = null;
	XPathExpression nameExpr = null;
	XPathExpression namespaceExpr = null;
	XPathExpression versionExpr = null;
	XPathExpression descriptionExpr = null;
	XPathExpression refExpr = null;
	
	public AbstractXPathModelParser() {
		xpath = XPathFactory.newInstance().newXPath();
	}

	DocumentBuilder getBuilder() {
		DocumentBuilder builder = null;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Error in constructing DOM builder", e);
		}
		return builder;
	}

	public ModelView parse(ModelContent modelContent) {
		
		ModelView modelView = null;
		InputStream inputStream = new ByteArrayInputStream(
				modelContent.getContent());
		try {			
			compileSpecificExpression();
			Document dom = getBuilder().parse(inputStream);
			modelView = generateModelView(dom);
			List<ModelReference> referenceModels = analysModelReferences(dom);
			modelView.setReferenceModels(referenceModels);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("XPath Expression Error parsing model XMI file", e);
		} catch (SAXException e) {
			throw new RuntimeException("SAXException parsing model XMI file", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException parsing model XMI file", e);
		}
		return modelView;
	}

	void compileSpecificExpression()
			throws XPathExpressionException{
		nameExpr = xpath.compile(getNameExpression());
		namespaceExpr = xpath.compile(getNameSpaceExpression());
		versionExpr = xpath.compile(getVersionExpression());	
		refExpr = xpath.compile(getModelReferenceExpression());	
		if(getDescriptionExpression()!=null){
			descriptionExpr = xpath.compile(getDescriptionExpression());
		}
	}
	
	abstract String getNameExpression();
	abstract String getNameSpaceExpression();
	abstract String getVersionExpression();
	abstract String getDescriptionExpression();
	abstract String getModelReferenceExpression();
	
	abstract ModelView generateModelView(Document dom) 
			throws XPathExpressionException;

	List<ModelReference> analysModelReferences(Document dom) throws XPathExpressionException{
		
		NodeList nodes = (NodeList)refExpr.evaluate(dom, XPathConstants.NODESET);
		List<ModelReference> rs = new ArrayList<ModelReference>();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			NamedNodeMap attMap = nodes.item(i).getAttributes();
			ModelReference mr = new ModelReference();

			 mr.setName(attMap.getNamedItem("importedNamespace").getNodeValue());
			 mr.setNamespace(attMap.getNamedItem("importedNamespace").getNodeValue());
			 mr.setVersion(attMap.getNamedItem("version").getNodeValue());
			 rs.add(mr);
			 
//			 System.out.println(attMap.getNamedItem("version").getNodeName());
//			 System.out.println(attMap.getNamedItem("version").getNodeValue());			 
//			 System.out.println(attMap.getNamedItem("importedNamespace").getNodeName());
//			 System.out.println(attMap.getNamedItem("importedNamespace").getNodeValue());
		}
		return rs;
	}
}
