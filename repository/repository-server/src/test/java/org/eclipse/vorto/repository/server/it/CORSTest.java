package org.eclipse.vorto.repository.server.it;

public class CORSTest extends AbstractIntegrationTest {

  @Override
  protected void setUpTest() throws Exception {
    testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer,userCreator);
  }


}
