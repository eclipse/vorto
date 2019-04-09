package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.junit.Test;

public class ModelRepositoryTest2 extends AbstractIntegrationTest {

  @Test
  public void testReferencedByApi() {
    IUserContext creator = createUserContext("creator", "playground");
    importModel("Color.type", creator);
    importModel("Color4.type", creator);
    importModel("Colorlight.fbmodel", creator);
    importModel("Colorlight2.fbmodel", creator);
    importModel("Colorlight3.fbmodel", creator);
    
    List<ModelInfo> referencedBy = repositoryFactory.getRepository(creator).getModelsReferencing(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"));
    assertEquals(2, referencedBy.size());
    assertTrue(referencedBy.stream().anyMatch(model -> model.getId().getName().equals("ColorLight")));
    assertTrue(referencedBy.stream().anyMatch(model -> model.getId().getName().equals("ColorLight2")));
  }
  
}
