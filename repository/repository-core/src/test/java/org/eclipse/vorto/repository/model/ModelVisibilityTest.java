package org.eclipse.vorto.repository.model;

import static org.junit.Assert.fail;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.model.impl.ModelService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.junit.Test;

public class ModelVisibilityTest extends AbstractIntegrationTest {

  private void importAndMakePublic(String filename, String modelId) {
    importModel("creator", filename);
    
    IUserContext publisher = createUserContext("publisher");
    
    IModelService modelService = new ModelService(repositoryFactory, tenantService);
    
    modelService.makeModelPublic(publisher, ModelId.fromPrettyFormat(modelId));
  }
  
  @Test(expected = ModelNamespaceNotOfficialException.class)
  public void cannotMakePublicModelsWithPrivatePrefix() {
    
    importAndMakePublic("privateprefix_point3d.type", "vorto.private.playground:Point3d:1.0.0");
    
    fail("Making a model public if it has private namespace should have thrown an error!");
  }
  
  @Test(expected = ModelNotReleasedException.class)
  public void cannotMakePublicUnReleasedModels() {
    
    importAndMakePublic("publicprefix_point3d.type", "com.mycompany:Point3d:1.0.0");
    
    fail("Making a model public if it is unreleased should have thrown an error!");
  }
  
  @Test
  public void nonPublisherUsersCannotMakeModelPublic() {
    importModel("creator", "publicprefix_point3d.type");
    
    try {
      releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    } catch (WorkflowException e) {
      fail("Cannot release newly imported model?");
    }
    
    IUserContext publisher = createUserContext("reviewer");
    
    IModelService modelService = new ModelService(repositoryFactory, tenantService);
    
    modelService.makeModelPublic(publisher, ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
    fail("Making a model public if done by non-publisher should have thrown an error!");
  }
}
