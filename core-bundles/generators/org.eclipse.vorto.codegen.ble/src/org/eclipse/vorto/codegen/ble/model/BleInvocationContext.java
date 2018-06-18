package org.eclipse.vorto.codegen.ble.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.codegen.ble.model.blegatt.Device;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.model.Model;

public class BleInvocationContext extends InvocationContext {

	private InvocationContext context;
	private Device device;

	private static final IGeneratorLookup NOOP_RUNTIME = new NoopGeneratorLookup();
	private static final IVortoCodeGenerator NOOP_GEN = new NoopGenerator();
	
	public BleInvocationContext(InvocationContext context, Device device) {
		super(new ArrayList<MappingModel>(), NOOP_RUNTIME, new HashMap<String, String>());
		this.context = context;
		this.device = device;
	}
	
	public BleInvocationContext(List<MappingModel> mappingModels, IGeneratorLookup generatorRuntime,
			Map<String, String> configProperties) {
		super(mappingModels, generatorRuntime, configProperties);
		context = null;
	}

	public boolean equals(Object obj) {
		return context.equals(obj);
	}

	public IMapped<InformationModel> getMappedElement(InformationModel informationModel, String stereoType) {
		return context.getMappedElement(informationModel, stereoType);
	}

	public IMapped<FunctionblockModel> getMappedElement(FunctionblockModel functionblockModel, String stereoType) {
		return context.getMappedElement(functionblockModel, stereoType);
	}

	public IMapped<EnumLiteral> getMappedElement(EnumLiteral enumLiteral, String stereoType) {
		return context.getMappedElement(enumLiteral, stereoType);
	}

	public IMapped<Property> getMappedElement(Property property, String stereoType) {
		return context.getMappedElement(property, stereoType);
	}

	public IMapped<Operation> getMappedElement(Operation operation, String stereoType) {
		return context.getMappedElement(operation, stereoType);
	}

	public IMapped<ModelAttribute> getMappedModelAttribute(Model model, ModelAttribute attribute, String stereoType) {
		return context.getMappedModelAttribute(model, attribute, stereoType);
	}

	public IVortoCodeGenerator lookupGenerator(String key) {
		return context.lookupGenerator(key);
	}

	public Map<String, String> getConfigurationProperties() {
		return context.getConfigurationProperties();
	}

	public int hashCode() {
		return context.hashCode();
	}

	public String toString() {
		return context.toString();
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
    static class NoopGeneratorLookup implements IGeneratorLookup {

		@Override
		public IVortoCodeGenerator lookupByKey(String key) {
			return  NOOP_GEN;
		}
	}
    
    static class NoopGenerator implements IVortoCodeGenerator {
    	
    	@Override
    	public IGenerationResult generate(InformationModel model, InvocationContext context, IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
    		return null;
    	}

    	@Override
    	public String getServiceKey() {
    		return "noop";
    	}

		@Override
		public GeneratorInfo getInfo() {
			return null;
		}
    }

}
