package org.eclipse.vorto.repository.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.model.impl.DefaultBulkOperationsService;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.junit.Test;
import org.springframework.security.core.Authentication;

public class BulkOperationServiceTest extends AbstractIntegrationTest {

  private IBulkOperationsService getModelService(final String username) {
    return new DefaultBulkOperationsService(repositoryFactory, tenantService) {
      @Override
      protected Authentication getAuthenticationToken() {
        return createAuthenticationToken(username);
      }
    };
  }
  
  private void importAndMakePublic(String filename, String modelId) {
    importModel("creator", filename);
        
    IBulkOperationsService modelService = getModelService("publisher");
    
    modelService.makeModelPublic(ModelId.fromPrettyFormat(modelId));
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
       
    IBulkOperationsService modelService = getModelService("reviewer");
    
    modelService.makeModelPublic(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
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
        
    IBulkOperationsService modelService = getModelService("publisher");
    
    List<ModelId> result = modelService.makeModelPublic(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
    assertEquals(1,result.size());
    
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
        
    IBulkOperationsService modelService = getModelService("publisher");
    
    try {
      modelService.makeModelPublic(ModelId.fromPrettyFormat("com.mycompany:Point3D:1.0.0"));
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
        
    IBulkOperationsService modelService = getModelService("publisher");
    
    List<ModelId> result = modelService.makeModelPublic(ModelId.fromPrettyFormat("com.mycompany:Point4D:1.0.0"));
    
    assertEquals(2,result.size());
    
    checkCorrectness("com.mycompany:Point3d:1.0.0", "public", "Released");
    checkCorrectness("com.mycompany:Point4D:1.0.0", "public", "Released");
    
    assertTrue(hasAnonymousPolicy("com.mycompany:Point3d:1.0.0"));
    assertTrue(hasAnonymousPolicy("com.mycompany:Point4D:1.0.0"));
  }
  
  @Test
  public void makeSearchPublicModelsByNameWildcard() throws Exception {
    importModel("creator", "officialprefix_point3d.type");
    
    releaseModel(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"), createUserContext("creator"));
            
    IBulkOperationsService modelService = getModelService("publisher");
    
    modelService.makeModelPublic(ModelId.fromPrettyFormat("com.mycompany:Point3d:1.0.0"));
    
    assertEquals(1,searchService.search("visibility:public").size());
    assertEquals(1,searchService.search("visibility:public name:Point*").size());
   
  }
  
  public boolean hasAnonymousPolicy(String modelId) {
    IModelPolicyManager policyMgr = repositoryFactory.getPolicyManager(createUserContext("creator"));
    return policyMgr.getPolicyEntries(ModelId.fromPrettyFormat(modelId))
        .stream().anyMatch(policyEntry -> IModelPolicyManager.ANONYMOUS_ACCESS_POLICY.equals(policyEntry.getPrincipalId()));
  }
}
