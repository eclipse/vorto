package org.eclipse.vorto.repository.model;

import static org.junit.Assert.assertNotNull;
import java.util.Optional;
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.conversion.ModelContentToEcoreConverter;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelContentDeserializationTest {

  @Test
  public void testDeserializeWithDictionaryType() throws Exception {
    
   ModelContent content = ObjectMapperFactory.getInstance().readValue(new ClassPathResource("json/TestModelContent_dictionary.json").getInputStream(), ModelContent.class);
   assertNotNull(content);
   
   ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
   FunctionblockModel model = (FunctionblockModel)converter.convert(content, Optional.empty());
   assertNotNull(model.getFunctionblock().getStatus().getProperties().get(0).getType() instanceof DictionaryPropertyType);

  }
  
  @Test
  public void testDeserializeWithPrimitiveType() throws Exception {
    
   ModelContent content = ObjectMapperFactory.getInstance().readValue(new ClassPathResource("json/TestModelContent_primitive.json").getInputStream(), ModelContent.class);
   assertNotNull(content);
   
   ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
   FunctionblockModel model = (FunctionblockModel)converter.convert(content, Optional.empty());
   
   assertNotNull(model.getFunctionblock().getStatus().getProperties().get(0).getType() instanceof PrimitivePropertyType);
  }

}
