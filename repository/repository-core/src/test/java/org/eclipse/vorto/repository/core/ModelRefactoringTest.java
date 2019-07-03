package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IModelRefactoring.RefactoringResult;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.junit.Test;

public class ModelRefactoringTest extends AbstractIntegrationTest {

  
  @Test
  public void testRenameModelName() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type.Colour:1.0.0");
    
    IModelRefactoring refactoring = repositoryFactory.getRepository(createUserContext("admin")).newRefactoring(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"));
    RefactoringResult result = refactoring.newId(newId).execute(createUserContext("admin"));
    assertEquals(newId,result.getModel().getId());
    assertEquals(1,result.getAffectedModels().size());
    assertEquals(newId,result.getAffectedModels().get(0).getReferences().get(0));
  }
  
  
  @Test
  public void testRenameModelNamespace() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.Color:1.0.0");
    
    IModelRefactoring refactoring = repositoryFactory.getRepository(createUserContext("admin")).newRefactoring(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"));
    RefactoringResult result = refactoring.newId(newId).execute(createUserContext("admin"));
    assertEquals(newId,result.getModel().getId());
    assertEquals(1,result.getAffectedModels().size());
    assertEquals(newId,result.getAffectedModels().get(0).getReferences().get(0));
  }
  
  @Test
  public void testRenameModelVersion() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.types.Color:2.0.0");
    
    IModelRefactoring refactoring = repositoryFactory.getRepository(createUserContext("admin")).newRefactoring(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"));
    RefactoringResult result = refactoring.newId(newId).execute(createUserContext("admin"));
    assertEquals(newId,result.getModel().getId());
    assertEquals(1,result.getAffectedModels().size());
    assertEquals(newId,result.getAffectedModels().get(0).getReferences().get(0));
  }
  
  @Test (expected = NewNamespacesNotSupersetException.class)
  public void testRenameNamespaceUnownedNamespace() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId newId = ModelId.fromPrettyFormat("org.apache.Color:1.0.0");
    
    IModelRefactoring refactoring = repositoryFactory.getRepository(createUserContext("admin")).newRefactoring(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"));
    refactoring.newId(newId).execute(createUserContext("admin"));
  }
  
  @Test (expected = ModelAlreadyExistsException.class)
  public void testRenameToIdThatAlreadyExist() {
    importModel("Color.type");
    importModel("Color5.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.Color:1.0.0");
    
    IModelRefactoring refactoring = repositoryFactory.getRepository(createUserContext("admin")).newRefactoring(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"));
    refactoring.newId(newId).execute(createUserContext("admin"));
  }
}
