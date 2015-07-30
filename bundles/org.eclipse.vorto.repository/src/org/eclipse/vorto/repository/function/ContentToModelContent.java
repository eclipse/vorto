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
package org.eclipse.vorto.repository.function;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.fluent.Content;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.repository.ModelContent;
import org.eclipse.vorto.core.model.ModelType;

import com.google.common.base.Function;

public class ContentToModelContent<M extends Model> implements
		Function<Content, ModelContent> {

	private Class<M> modelClass;
	private ModelType modelType;

	public ContentToModelContent(Class<M> modelClass, ModelType type) {
		this.modelClass = modelClass;
		this.modelType = type;
		init();
	}

	public ModelContent apply(Content input) {
		return new ModelContent(input.asBytes(), modelType, xmiToModel(
				input.asStream(), modelClass), null);
	}

	private <M extends Model> M xmiToModel(InputStream xmiStream,
			Class<M> modelClass) {
		return parseXmi(xmiStream, new ArrayList<InputStream>(), modelClass);
	}

	@SuppressWarnings("unchecked")
	private <M extends Model> M parseXmi(InputStream xmiStream,
			List<InputStream> references, Class<M> modelClass) {

		String myType = modelClass.getSimpleName();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				myType, new XMIResourceFactoryImpl());

		ResourceSet resourceSet = new ResourceSetImpl();

		List<Object> refModels = new ArrayList<Object>();
		for (int i = 0; i < references.size(); i++) {
			InputStream inputStream = references.get(i);
			Resource resource = resourceSet.createResource(URI
					.createURI("dummyResource_" + i + "." + myType));
			try {
				resource.load(inputStream, null);
				refModels.add(resource.getContents().get(0));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		final Resource resource = resourceSet.createResource(URI
				.createURI("dummyResource." + myType));

		try {
			resource.load(xmiStream, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return (M) resource.getContents().get(0);
	}

	private void init() {
		DatatypePackage.eINSTANCE.eClass();
		FunctionblockPackage.eINSTANCE.eClass();
		InformationModelPackage.eINSTANCE.eClass();
	}
}
