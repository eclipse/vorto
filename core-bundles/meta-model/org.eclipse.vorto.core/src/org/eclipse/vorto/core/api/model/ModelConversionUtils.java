package org.eclipse.vorto.core.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class ModelConversionUtils {
	
	public static FunctionblockModel convertToFlatHierarchy(FunctionblockModel fbm) {
		FunctionBlock fb = fbm.getFunctionblock();
		
		List<Property> properties = getFlatProperties(fbm);
					
		Status status = FunctionblockFactory.eINSTANCE.createStatus();
		status.getProperties().addAll(properties.stream().filter(p -> p.eContainer() instanceof Status).collect(Collectors.toList()));
		
		fb.setStatus(status);
					
		Configuration configuration = FunctionblockFactory.eINSTANCE.createConfiguration();
		configuration.getProperties().addAll(properties.stream().filter(p -> p.eContainer() instanceof Configuration).collect(Collectors.toList()));			
		fb.setConfiguration(configuration);
		
		List<Operation> operations = getFlatOperations(fbm);
		fb.getOperations().clear();
		fb.getOperations().addAll(operations);
		return fbm;
	}

	private static List<Operation> getFlatOperations(FunctionblockModel fbm) {
		List<Operation> operations = new ArrayList<Operation>();
		TreeIterator<EObject> iter = fbm.eAllContents();
		while (iter.hasNext()) {
			EObject obj = iter.next();
			if (obj instanceof Operation) {
				operations.add((Operation)obj);
			}
		}
		if (fbm.getSuperType() != null) {
			operations.addAll(getFlatOperations(fbm.getSuperType()));
		}		
		return operations;
	}

	public static InformationModel convertToFlatHierarchy(InformationModel infomodel) {
		for (FunctionblockProperty fbProperty : infomodel.getProperties()) {
			FunctionblockModel fbm = fbProperty.getType();
			fbProperty.setType(convertToFlatHierarchy(fbm));
			
			// merge any extended properties from information model to the FB properties
			if (fbProperty.getExtendedFunctionBlock() != null && fbProperty.getExtendedFunctionBlock().getStatus() != null) {
				for (Property extendedProperty : fbProperty.getExtendedFunctionBlock().getStatus().getProperties()) {
					Optional<Property> baseProperty = fbm.getFunctionblock().getStatus().getProperties().stream().filter(p -> p.getName().equals(extendedProperty.getName())).findFirst();
					if (baseProperty.isPresent()) {
						baseProperty.get().setConstraintRule(extendedProperty.getConstraintRule());
					}
				}
			}
		}
		
		return infomodel;
	}
	
	private static List<Property> getFlatProperties(FunctionblockModel fbm) {
		List<Property> properties = new ArrayList<Property>();
		TreeIterator<EObject> iter = fbm.eAllContents();
		while (iter.hasNext()) {
			EObject obj = iter.next();
			if (obj instanceof Property) {
				properties.add((Property)obj);
			}
		}
		if (fbm.getSuperType() != null) {
			properties.addAll(getFlatProperties(fbm.getSuperType()));
		}		
		return properties;
	}
}
