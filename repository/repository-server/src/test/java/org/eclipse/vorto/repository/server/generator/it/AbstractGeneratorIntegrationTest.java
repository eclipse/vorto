package org.eclipse.vorto.repository.server.generator.it;

import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.generators.runner.GeneratorRunner;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.eclipse.vorto.repository.web.core.dto.ModelContent;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractGeneratorIntegrationTest {

  protected static MockMvc vortoMockMvc;

  protected static MockMvc generatorMockMvc;

  protected Gson gson = new Gson();

  @BeforeClass
  public static void runGeneratorAndVorto() {
    HashMap<String, Object> vortoRepoProps = new HashMap<>();
    vortoRepoProps.put("github_clientid", "foo");
    vortoRepoProps.put("github_clientSecret", "foo");
    vortoRepoProps.put("eidp_clientid", "foo");
    vortoRepoProps.put("eidp_clientSecret", "foo");

    ConfigurableApplicationContext vortoContext =
        new SpringApplicationBuilder(VortoRepository.class).properties(vortoRepoProps)
            .run(new String[] {"--spring.jmx.enabled=false"});

    vortoMockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) vortoContext)
        .apply(springSecurity()).build();

    HashMap<String, Object> generatorProps = new HashMap<>();
    generatorProps.put("vorto.serverUrl", "http://localhost:8080/infomodelrepository");
    generatorProps.put("vorto.tenantId", "default");
    generatorProps.put("server.serviceUrl", "http://localhost:8081/generatorgateway");

    ConfigurableApplicationContext generatorContext =
        new SpringApplicationBuilder(GeneratorRunner.class).properties(generatorProps)
            .run(new String[] {"--server.port=8081", "--spring.jmx.enabled=false",
                "--spring.security.enabled=false", "--management.security.enabled=false",
                "--security.basic.enabled=false", "--server.contextPath=/generatorgateway"});

    generatorMockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) generatorContext)
        .apply(springSecurity()).build();
  }
  
  protected String loadModel(String type, String filename) {
    ModelContent modelContent = new ModelContent();
    modelContent.setType(type);
    try {
      modelContent.setContentDsl(
          IOUtils.toString(new ClassPathResource(filename).getInputStream(), "utf-8"));
    } catch (IOException e) {
      fail("Cannot load test model");
    }
    return gson.toJson(modelContent);
  }
  
  protected byte[] loadResource(String filename) throws IOException {
    return IOUtils.toByteArray(new ClassPathResource(filename).getInputStream());
  }
}
