package org.eclipse.vorto.repository.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.model.impl.ModelService;
import org.eclipse.vorto.repository.model.impl.ModelVisibilityService;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.junit.Test;

public class ModelVisibilityTest extends AbstractIntegrationTest {

  private IModelService getModelService() {
    return new ModelService(new ModelVisibilityService(repositoryFactory, tenantService));
  }
  
  private void importAndMakePublic(String filename, String modelId) {
    importModel("creator", filename);
    
    IUserContext publisher = createUserContext("publisher");
    
    IModelService modelService = getModelService();
    
    modelService.makeModelPublic(publisher, ModelId.fromPrettyFormat(modelId));
  }
  
  @Test(expected = ModelNamespaceNotOfficialException.class)
  public void cannotMakePublicModelsWithPrivatePrefix() {
    
    importAndMakePublic("privateprefix_point3d.type", "vorto.private.playground:Point3d:1.0.0");
    
    fail("Making a model public if it has private namespace should have thrown an error!");
  }
  
  @Test(expected = ModelNotReleasedException.class)
  public void cannotMakePublicUnReleasedModels() {
    
    importAndMakePublic("officialprefix_point3d.type", "com.mycompany:Point3d:1.0.0");
    
    fail("Making a model public if it is unreleased should have thrown an error!");
  }
  
  @Test(expected = NotAuthorizedException.class)
  public void nonPublisherUsersCannotMakeModelPublic() {
    importModel("creator", "officialprefix_point3d.type");
    
    try {
      releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"), createUserContext("creator"));
    } catch (WorkflowException e) {
      fail("Cannot release newly imported model?");
    }
    
    IUserContext reviewer = createUserContext("reviewer");
    
    IModelService modelService = getModelService();
    
    modelService.makeModelPublic(reviewer, ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
    fail("Making a model public if done by non-publisher should have thrown an error!");
  }
  
  @Test
  public void makeModelPublic() {
    importModel("creator", "officialprefix_point3d.type");
    
    try {
      releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"), createUserContext("creator"));
    } catch (WorkflowException e) {
      fail("Cannot release newly imported model?");
    }

    checkCorrectness("com.mycompany:Point3d:1.0.0", "private", "Released");
    
    IUserContext publisher = createUserContext("publisher");
    
    IModelService modelService = getModelService();
    
    modelService.makeModelPublic(publisher, ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
    ModelInfo modelInfo = getModelRepository(createUserContext("creator"))
        .getById(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
    assertEquals("public", modelInfo.getVisibility());
    
    assertTrue(hasAnonymousPolicy("com.mycompany:Point3d:1.0.0"));
  }
  
  private void checkCorrectness(String modelId, String visibility, String state) {
    ModelInfo modelInfo = getModelRepository(createUserContext("creator"))
        .getById(ModelId.fromPrettyFormat(modelId));
    
    assertEquals(visibility, modelInfo.getVisibility());
    assertEquals(state, modelInfo.getState());
  }
  
  @Test
  public void modelsWithDependencyWithPrivatePrefixCannotBeMadePublic() {
    importModel("creator", "privateprefix_point2d.type");
    importModel("creator", "officialprefix_point3d_with_dependency.type");
    
    try {
      releaseModel(ModelId.fromPrettyFormat("vorto.private.playground:Point2d:1.0.0"), createUserContext("creator"));
      releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point3D:1.0.0"), createUserContext("creator"));
    } catch (WorkflowException e) {
      fail("Cannot release newly imported model?");
    }
    
    checkCorrectness("vorto.private.playground:Point2d:1.0.0", "private", "Released");
    checkCorrectness("com.mycompany:Point3D:1.0.0", "private", "Released");
    
    IUserContext publisher = createUserContext("publisher");
    
    IModelService modelService = getModelService();
    
    try {
      modelService.makeModelPublic(publisher, ModelId.fromPrettyFormat("com.mycompany:Point3D:1.0.0"));
      fail("Making a model public with a dependency that has a private prefix should throw an error!");
    } catch(ModelNamespaceNotOfficialException e) {
      assertTrue(e.getMessage().contains("vorto.private.playground:Point2d:1.0.0"));
    }
    
    checkCorrectness("vorto.private.playground:Point2d:1.0.0", "private", "Released");
    checkCorrectness("com.mycompany:Point3D:1.0.0", "private", "Released");
    
    assertFalse(hasAnonymousPolicy("vorto.private.playground:Point2d:1.0.0"));
    assertFalse(hasAnonymousPolicy("com.mycompany:Point3D:1.0.0"));
  }
  
  @Test
  public void makingModelPublicWillMakeDependenciesPublic() {
    importModel("creator", "officialprefix_point3d.type");
    importModel("creator", "officialprefix_point4d_with_dependency.type");
    
    try {
      releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"), createUserContext("creator"));
      releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point4D:1.0.0"), createUserContext("creator"));
    } catch (WorkflowException e) {
      fail("Cannot release newly imported model?");
    }
    
    checkCorrectness("com.mycompany:Point3d:1.0.0", "private", "Released");
    checkCorrectness("com.mycompany:Point4D:1.0.0", "private", "Released");
    
    IUserContext publisher = createUserContext("publisher");
    
    IModelService modelService = getModelService();
    
    modelService.makeModelPublic(publisher, ModelId.fromPrettyFormat("com.mycompany:Point4D:1.0.0"));
    
    checkCorrectness("com.mycompany:Point3d:1.0.0", "public", "Released");
    checkCorrectness("com.mycompany:Point4D:1.0.0", "public", "Released");
    
    assertTrue(hasAnonymousPolicy("com.mycompany:Point3d:1.0.0"));
    assertTrue(hasAnonymousPolicy("com.mycompany:Point4D:1.0.0"));
  }
  
  public boolean hasAnonymousPolicy(String modelId) {
    IModelPolicyManager policyMgr = repositoryFactory.getPolicyManager(createUserContext("creator"));
    return policyMgr.getPolicyEntries(ModelId.fromPrettyFormat(modelId))
        .stream().anyMatch(policyEntry -> IModelPolicyManager.ANONYMOUS_ACCESS_POLICY.equals(policyEntry.getPrincipalId()));
  }
}
