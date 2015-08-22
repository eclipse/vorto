package org.eclipse.vorto.repository.function;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnType;
import org.eclipse.vorto.core.api.model.model.Model;

import com.google.common.base.Function;

public class ModelToXmi implements Function<Model, byte[]> {

	public byte[] apply(Model model) {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("*", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet
				.createResource(URI.createFileURI("temp.xmi"));

		updateModelProxyUri(model);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		resource.getContents().add(model);
		try {
			resource.save(outputStream, Collections.EMPTY_MAP);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return outputStream.toByteArray();
	}

	private void updateModelProxyUri(Model model) {
		if (model instanceof FunctionblockModel) {
			updateFunctionModelProxyUri((FunctionblockModel) model);
		} else if (model instanceof Entity) {
			Entity entity = (Entity) model;
			updateProperties(entity.getProperties());
		}
	}

	private void updateFunctionModelProxyUri(
			FunctionblockModel functionblockModel) {
		FunctionBlock fb = functionblockModel.getFunctionblock();

		if (fb == null) {
			throw new IllegalArgumentException("fb is null");
		}

		if (fb.getConfiguration() != null)
			updateProperties(fb.getConfiguration().getProperties());

		if (fb.getStatus() != null)
			updateProperties(fb.getStatus().getProperties());

		if (fb.getFault() != null)
			updateProperties(fb.getFault().getProperties());

		if (fb.getEvents() != null) {
			for (Event event : fb.getEvents()) {
				updateProperties(event.getProperties());
			}
		}

		if (fb.getOperations() != null) {
			for (Operation operation : fb.getOperations()) {
				updateReturnType(operation.getReturnType());
				updateParams(operation.getParams());
			}
		}
	}

	private void updateReturnType(ReturnType returnType) {
		if (returnType instanceof ReturnObjectType) {
			ReturnObjectType returnObjectType = (ReturnObjectType) returnType;
			updateProxyURIToFullImports(returnObjectType,
					returnObjectType.getReturnType());
		}
	}

	private void updateParams(EList<Param> params) {
		for (Param param : params) {
			if (param instanceof RefParam) {
				RefParam refParam = (RefParam) param;
				updateProxyURIToFullImports(refParam, refParam.getType());
			}
		}
	}

	private void updateProperties(EList<Property> properties) {
		for (Property property : properties) {
			PropertyType propertyType = property.getType();
			if (propertyType instanceof ObjectPropertyType) {
				ObjectPropertyType objectPropertyType = (ObjectPropertyType) propertyType;
				updateProxyURIToFullImports(objectPropertyType,
						objectPropertyType.getType());
			}
		}
	}

	/**
	 * if uri not converted, xmi reference will be like: <type
	 * href="__synthetic0.fbmodel#|0"/> After update proxy URI, xmi referenc
	 * will be like <type href="org.eclipse.vorto.Color;1.2.1"/>
	 * 
	 * @param objectPropertyType
	 */

	private void updateProxyURIToFullImports(EObject typeContainer,
			Type propertyType) {
		String qualifiedName = "using|" + propertyType.getNamespace() + "."
				+ propertyType.getName() + ";" + propertyType.getVersion();
		InternalEObject internalType = (InternalEObject) propertyType;
		internalType.eSetProxyURI(URI.createURI(qualifiedName));
	}
}
