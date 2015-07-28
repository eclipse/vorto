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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelReference;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XpathModelViewBuilder {

	private static final String VERSION = "version";
	private static final String IMPORTED_NAMESPACE = "importedNamespace";
	protected XPathExpression nameExpr = null;
	protected XPathExpression namespaceExpr = null;
	protected XPathExpression versionExpr = null;
	protected XPathExpression descriptionExpr = null;
	protected XPathExpression refExpr = null;
	protected ModelType modelType = null;

	public XpathModelViewBuilder(ModelType modelType, String nameExpr,
			String namespaceExpr, String versionExpr, String descriptionExpr,
			String refExpr) {
		this.modelType = modelType;
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		this.nameExpr = getExpression(xpath, nameExpr);
		this.namespaceExpr = getExpression(xpath, namespaceExpr);
		this.versionExpr = getExpression(xpath, versionExpr);
		this.descriptionExpr = getExpression(xpath, descriptionExpr);
		this.refExpr = getExpression(xpath, refExpr);
	}

	public ModelView build(Document dom) {

		ModelId modelId = ModelFactory.newModelId(modelType,
				evaluate(namespaceExpr, dom), evaluate(versionExpr, dom),
				evaluate(nameExpr, dom));

		ModelView modelView = ModelFactory.newModelView(modelId,
				evaluate(descriptionExpr, dom));

		modelView.setReferenceModels(getReferenceModels(dom));
		
		return modelView;

	}

	private String evaluate(XPathExpression expr, Document dom) {
		try {
			return expr != null ? expr.evaluate(dom) : null;
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Error while parsing model DOM with expr "
							+ expr.toString(), e);
		}
	}
	
	private XPathExpression getExpression(XPath xpath, String expr) {
		if (expr != null) {
			try {
				return xpath.compile(expr);
			} catch (XPathExpressionException e) {
				throw new RuntimeException(
						"Error in compiling XPath expression for " + expr, e);
			}
		}

		return null;
	}

	private List<ModelReference> getReferenceModels(Document dom) {
		try {
			if (refExpr != null) {
				NodeList nodes = (NodeList) refExpr.evaluate(dom,
						XPathConstants.NODESET);
				List<ModelReference> rs = new ArrayList<ModelReference>();

				for (int i = 0; i < nodes.getLength(); i++) {
					NamedNodeMap attMap = nodes.item(i).getAttributes();
					if (attMap != null) {
						ModelReference mr = new ModelReference();

						mr.setName(attMap.getNamedItem(IMPORTED_NAMESPACE)
								.getNodeValue());
						mr.setNamespace(attMap.getNamedItem(IMPORTED_NAMESPACE)
								.getNodeValue());
						mr.setVersion(attMap.getNamedItem(VERSION).getNodeValue());
						rs.add(mr);
					}
				}
				return rs;
			}
			
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RuntimeException("Error getting referenced models", e);
		}
	}
}
