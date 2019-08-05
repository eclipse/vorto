package org.eclipse.vorto.codegen.spi.config;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory.ModelDeserializer;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory.ModelMapDeserializer;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory.ModelReferenceDeserializer;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory.PropertyAttributeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class SerializationConfiguration {

	@Bean
	  public ObjectMapper objectMapper() {
	    ObjectMapper mapper = new ObjectMapper();
	    SimpleModule module = new SimpleModule();
	    module.addDeserializer(IPropertyAttribute.class, new PropertyAttributeDeserializer());
	    module.addDeserializer(IReferenceType.class, new ModelReferenceDeserializer());
	    module.addDeserializer(IModel.class, new ModelDeserializer());
	    module.addDeserializer(Map.class, new ModelMapDeserializer());
	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    mapper.registerModule(module);
	    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
	    return mapper;
	  } 
}
