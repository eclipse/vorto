package org.eclipse.vorto.editor.functionblock.validation

import com.google.common.base.Function
import com.google.common.collect.Lists
import java.util.Collection
import java.util.HashMap
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.editor.datatype.validation.ValidatorUtils

class FbValidatorUtils {
	
	public static val modelToChildrenSupplierFunction = ValidatorUtils.newModelTypeChildrenSupplier(newModelToFunctionMap());
	
	private static def newModelToFunctionMap() {
		val entityToFunctionMap = new HashMap<Class<?>, Function<Model, Collection<Model>>>();
		entityToFunctionMap.put(typeof(Entity), new ValidatorUtils.EntityChildrenSupplier());
		entityToFunctionMap.put(typeof(FunctionblockModel), new FunctionblockChildrenSupplier());
		return entityToFunctionMap;
	}
	
	static private class FunctionblockChildrenSupplier extends ValidatorUtils.AbstractChildrenSupplier {
		override apply(Model input) {
			val children = Lists.newArrayList();
			val parent = input as FunctionblockModel;
			
			if (parent.superType != null) {
				children.add(parent.superType);	
			}
			
			return children;
		}		
	}
}