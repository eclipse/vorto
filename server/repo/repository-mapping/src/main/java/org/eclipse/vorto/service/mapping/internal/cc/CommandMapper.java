package org.eclipse.vorto.service.mapping.internal.cc;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;

import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Operation;
import org.eclipse.vorto.repository.api.content.Param;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.DataInput;
import org.eclipse.vorto.service.mapping.IDataMapper;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.internal.DynamicBean;
import org.eclipse.vorto.service.mapping.json.JsonData;
import org.eclipse.vorto.service.mapping.normalized.Command;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandMapper implements IDataMapper<JsonData> {

	private IMappingSpecification mappingSpecification;
	
	private static final String STEREOTYPE = "source";
	private static final String ATTRIBUTE_XPATH = "xpath";
	private static final String ATTRIBUTE_VALUE = "value";
	
	public CommandMapper(IMappingSpecification specification) {
		this.mappingSpecification = specification;
	}
	
	@Override
	public JsonData map(DataInput input, MappingContext context) {
		Command cmd = (Command)input.getValue();
		DynamicBean mappedPayload = new DynamicBean();
		
		final Infomodel deviceInfoModel = mappingSpecification.getInfoModel();

		for (ModelProperty fbProperty : deviceInfoModel.getFunctionblocks()) {
			FunctionblockModel fbModel = mappingSpecification.getFunctionBlock(fbProperty.getName());
			for (Operation operation : fbModel.getOperations()) {
				Optional<Stereotype> operationStereotype = operation.getStereotype(STEREOTYPE);
				if (operationStereotype.isPresent() && hasXpath(operationStereotype.get().getAttributes())) {
					final String expression = operationStereotype.get().getAttributes().get(ATTRIBUTE_XPATH);
					final String value = operationStereotype.get().getAttributes().get(ATTRIBUTE_VALUE);
					mappedPayload.setProperty(expression, value);
				}
				operation.getParams().stream().forEach(param -> {
					Optional<Stereotype> paramStereotype = param.getStereotype(STEREOTYPE);
					if (paramStereotype.isPresent() && hasXpath(paramStereotype.get().getAttributes())) {
						final String expression = paramStereotype.get().getAttributes().get(ATTRIBUTE_XPATH);
						final String value = replacePlaceHolders(paramStereotype.get().getAttributes().get(ATTRIBUTE_VALUE),param,cmd.getParams());
						mappedPayload.setProperty(expression, value);
					}
				});
				
			}
		}
		
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mapper.writeValue(baos, mappedPayload.asMap());
			return new JsonData() {
				
				@Override
				public String toJson() {
					return new String(baos.toByteArray());
				}
			};
		} catch (Exception e) {
			throw new IllegalArgumentException("Provided json not valid");
		}
	}
	
	private String replacePlaceHolders(String expression, Param param, Map<String,Object> values) {
		if (expression.contains("${value}")) {
			return expression.replace("${value}", values.get(param.getName()).toString());
		} 
		return expression;
	}
	
	private boolean hasXpath(Map<String, String> stereotypeAttributes) {
		return stereotypeAttributes.containsKey(ATTRIBUTE_XPATH)
				&& !stereotypeAttributes.get(ATTRIBUTE_XPATH).equals("");
	}

}
