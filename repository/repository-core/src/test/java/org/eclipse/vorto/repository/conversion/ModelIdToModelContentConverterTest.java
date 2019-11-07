package org.eclipse.vorto.repository.conversion;

import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

  @Test
  public void testConvertWithoutTargetPlatformLatestTag() throws Exception {
    setupTestDataForLatestTag();

    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);
    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:latest"), Optional.empty());

    assertEquals(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.1"),content.getModels().get(content.getRoot()).getId());
    assertEquals(0,((EntityModel)content.getModels().get(content.getRoot())).getStereotypes().size());
  }

  private void setupTestDataForLatestTag() throws WorkflowException {
    IUserContext user = createUserContext("alex", "playground");
    ModelInfo color = importModel("Color.type");
    ModelInfo color6 = importModel("Color6.type");
    importModel("Color7.type");
    importModel("sample.mapping");
    color.setState(ModelState.RELEASED.getName());
    color6.setState(ModelState.RELEASED.getName());
    this.workflow.start(color.getId(), user);
    this.workflow.start(color6.getId(), user);
    setReleaseState(color);
    setReleaseState(color6);
  }
}
