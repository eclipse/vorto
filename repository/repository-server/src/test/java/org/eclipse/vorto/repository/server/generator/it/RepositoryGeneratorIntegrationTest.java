package org.eclipse.vorto.repository.server.generator.it;

import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collection;
import java.util.List;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;


public class RepositoryGeneratorIntegrationTest extends AbstractGeneratorIntegrationTest {

  @Test
  public void testGetRegisteredGeneratorServices() {
    try {
      vortoMockMvc.perform(get("/api/v1/generators")).andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(getGenerators().size())));
    } catch (Exception e) {
      fail("failed because of Exception");
    }
  }

  @Test
  public void testGetGeneratorInfo() {
    try {
      for (GeneratorServiceInfo genInfo : getGenerators()) {
        System.out.println("Checking for [" + genInfo.getKey() + "]");
        vortoMockMvc.perform(get("/api/v1/generators/" + genInfo.getKey()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", Matchers.is(genInfo.getName())))
            .andExpect(jsonPath("$.description", Matchers.is(genInfo.getDescription())));
      }
    } catch (Exception e) {
      fail("failed because of Exception");
    }
  }

  @Test
  public void testGenerate() {
    try {
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
          user("admin").password("pass").authorities(
              SpringUserUtils.toAuthorityList(Sets.newHashSet(ADMIN, MODEL_CREATOR, USER)));

      vortoMockMvc
          .perform(put("/rest/default/models/com.test:Location:1.0.0").with(user)
              .contentType(MediaType.APPLICATION_JSON)
              .content(loadModel("Functionblock", "Location.fbmodel")))
          .andExpect(status().isOk());

      vortoMockMvc
          .perform(put("/rest/default/models/com.test:TrackingDevice:1.0.0").with(user)
              .contentType(MediaType.APPLICATION_JSON)
              .content(loadModel("InformationModel", "TrackingDevice.infomodel")))
          .andExpect(status().isOk());

      vortoMockMvc.perform(get("/api/v1/search/models?expression="))
          .andExpect(status().isOk());

      vortoMockMvc
          .perform(get("/api/v1/generators/boschiotsuite/models/com.test:TrackingDevice:1.0.0?language=java"))
          .andExpect(status().isOk())
          .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite.zip")));
        

    } catch (Exception e) {
      fail("failed because of Exception");
    }
  }

  private Collection<GeneratorServiceInfo> getGenerators() throws Exception {
    MvcResult result = generatorMockMvc.perform(get("/rest/generators")).andReturn();
    return gson.fromJson(new String(result.getResponse().getContentAsByteArray()),
        new TypeToken<List<GeneratorServiceInfo>>() {}.getType());
  }
}
