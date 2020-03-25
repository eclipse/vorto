package org.eclipse.vorto.repository.server.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

/**
 * Tests for CORS requests with regards to any allowed Origin headers
 * (see application yml configuration).
 */
public class CORSRequestIntegrationTest extends AbstractIntegrationTest {

  @Override
  protected void setUpTest() throws Exception {
    testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer,userCreator);
  }

  @Test
  public void testRequestWithPassingOriginsForEclipseOrg() throws Exception {
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
        .header("Origin", "https://vorto.eclipse.org")
        .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://vorto.eclipse.org")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "https://vorto.eclipse.org:8080")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://vorto.eclipse.org:8888")
            .with(userCreator))
        .andExpect(status().isOk());
  }

  @Test
  public void testRequestWithPassingOriginsForLocalhost() throws Exception {
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "https://localhost")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://localhost")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "https://localhost:8080")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://localhost:8888")
            .with(userCreator))
        .andExpect(status().isOk());
  }

  @Test
  public void testRequestWithPassingOriginsForBosch() throws Exception {
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "https://bosch-iot-suite.com")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://test.bosch-iot-suite.com")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "https://something.bosch-iot-cloud.com:8080")
            .with(userCreator))
        .andExpect(status().isOk());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://test.bosch-iot-suite.com:8888")
            .with(userCreator))
        .andExpect(status().isOk());
  }

  @Test
  public void testRequestWithFailingOrigins() throws Exception {
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://pwned.com")
            .with(userCreator))
        .andExpect(status().isForbidden());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "http://fakebosch-iot-suite.com:6666")
            .with(userCreator))
        .andExpect(status().isForbidden());
    repositoryServer.perform(
        get("/api/v1/models/" + testModel.prettyName)
            .header("Origin", "mySpoofedOrigin")
            .with(userCreator))
        .andExpect(status().isForbidden());
  }

}
