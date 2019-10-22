package org.eclipse.vorto.repository.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Optional;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.junit.Test;

public class ModelIdToModelContentConverterTest extends AbstractIntegrationTest {

  @Test
  public void testConvertWithTargetPlatform() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");
   
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);
    
    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"), Optional.of("ios"));
    assertNotNull(content);
    assertEquals(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"),content.getModels().get(content.getRoot()).getId());
    assertEquals("colortype",((EntityModel)content.getModels().get(content.getRoot())).getStereotypes().get(0).getName());
  }
  
  @Test
  public void testConvertWithoutTargetPlatform() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");
   
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);
    
    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"), Optional.empty());
    assertEquals(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"),content.getModels().get(content.getRoot()).getId());
    assertEquals(0,((EntityModel)content.getModels().get(content.getRoot())).getStereotypes().size());
  }
}
