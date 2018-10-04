package org.eclipse.vorto.repository.web.core.dto.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.internal.converter.JavascriptFunctions;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MappingSpecification implements IMappingSpecification {

	private Infomodel infomodel;
	
	private Map<String,FunctionblockModel> properties = new HashMap<String, FunctionblockModel>();
		
	public MappingSpecification(Infomodel infomodel, Map<String,FunctionblockModel> properties) {
		this();
		this.infomodel = infomodel;
		this.properties = properties;
	}
	
	public MappingSpecification() {	
	}
	
	public void setInfomodel(Infomodel infomodel) {
		this.infomodel = infomodel;
	}
	
	public void setProperties(Map<String,FunctionblockModel> properties) {
		this.properties = properties;
	}
	
	
	
	public Map<String, FunctionblockModel> getProperties() {
		return properties;
	}

	@Override
	public Infomodel getInfoModel() {
		return infomodel;
	}

	@Override
	public FunctionblockModel getFunctionBlock(String propertyName) {
		return properties.get(propertyName);
	}

	@Override
	@JsonIgnore
	public Optional<Functions> getCustomFunctions() {
		FunctionLibrary library = new FunctionLibrary();
		for (String propertyKey : this.properties.keySet()) {
			FunctionblockModel fbm = this.properties.get(propertyKey);
			if (fbm.getStereotype("functions").isPresent()) {
				Stereotype functionsStereoType = fbm.getStereotype("functions").get();
				JavascriptFunctions propertyMappingFunctions = new JavascriptFunctions(propertyKey.toLowerCase());
				functionsStereoType.getAttributes().keySet().stream()
					.filter(functionName -> !functionName.equalsIgnoreCase("_namespace"))
					.forEach(functionName -> propertyMappingFunctions.addFunction(functionName,functionsStereoType.getAttributes().get(functionName)));
				library.addFunctions(propertyMappingFunctions);
			}
		}
		return Optional.of(library);
	}
}
