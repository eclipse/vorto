/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.LocalModelWorkspace;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.domain.Tenant;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.core.io.ClassPathResource;
import static org.mockito.Mockito.when;

public class ModelParserTest extends AbstractIntegrationTest {

  @Test
  public void testModelParsing() {
    try {
      ModelInfo modelInfo = modelParserFactory.getParser("Color_encoding.type")
          .parse(new ClassPathResource("sample_models/Color_encoding.type").getInputStream());
      assertTrue(modelInfo != null);
      assertEquals("org.eclipse.vorto.examples.type", modelInfo.getId().getNamespace());
      assertEquals("Farbe", modelInfo.getId().getName());
      assertEquals("1.0.0", modelInfo.getId().getVersion());
    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

  @Test
  public void testModelParsingWithUnloadedDependency() {
    try {
      modelParserFactory.getParser("ColorLightIM.infomodel")
          .enableValidation()
          .parse(new ClassPathResource("sample_models/ColorLightIM.infomodel").getInputStream());

      fail("Able to get ModelInfo even if dependency is not loaded.");
    } catch (CouldNotResolveReferenceException e) {
      assertEquals(1, e.getMissingReferences().size());
    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

  @Test
  public void testModelParsingWithDependencyInRepo() {
    try {
      importModel("Color.type");
      importModel("Colorlight.fbmodel");
      try {
        ModelInfo modelInfo = modelParserFactory.getParser("ColorLightIM.infomodel")
            .enableValidation()
            .parse(new ClassPathResource("sample_models/ColorLightIM.infomodel").getInputStream());
        assertTrue(modelInfo != null);
        assertEquals("com.mycompany", modelInfo.getId().getNamespace());
        assertEquals("ColorLightIM", modelInfo.getId().getName());
        assertEquals("1.0.0", modelInfo.getId().getVersion());

      } catch (ValidationException e) {
        fail("There should be no exception");
      }

    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

  @Test
  public void testModelParsingWithDependencyPassedIn() {
    try {
      FileContent file1 = new FileContent("Color.type",
          IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()));
      FileContent file2 = new FileContent("Colorlight.fbmodel", IOUtils
          .toByteArray(new ClassPathResource("sample_models/Colorlight.fbmodel").getInputStream()));
      IModelParser parser = modelParserFactory.getParser("ColorLightIM.infomodel");
      parser.setWorkspace(new LocalModelWorkspace(repositoryFactory, Arrays.asList(file1, file2)));
      parser.enableValidation();
      ModelInfo modelInfo = parser
          .parse(new ClassPathResource("sample_models/ColorLightIM.infomodel").getInputStream());
      assertTrue(modelInfo != null);
      assertEquals("com.mycompany", modelInfo.getId().getNamespace());
      assertEquals("ColorLightIM", modelInfo.getId().getName());
      assertEquals("1.0.0", modelInfo.getId().getVersion());

    } catch (ValidationException e) {
      fail("There should be no exception");
    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

  @Test
  public void testModelWithValidatorProblem() {
    try {
      modelParserFactory.getParser("Accelerometer-invalid.fbmodel")
          .enableValidation()
          .parse(new ClassPathResource("sample_models/Accelerometer-invalid.fbmodel")
              .getInputStream());

      fail("Able to get ModelInfo even if model has validation issues.");
    } catch (ValidationException e) {
      assertTrue(e.getMessage().contains("Constraint cannot apply on this property's datatype"));
    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

  @Test(expected = ValidationException.class)
  public void testModelWithInvalidReference() throws IOException {
    Optional<Tenant> tenant = Optional.empty();
    when(tenantService.getTenantFromNamespace(Matchers.anyString())).thenReturn(tenant);
    IModelParser parser = modelParserFactory.getParser("InfoModelWithoutNamespace.infomodel");
    parser.enableValidation();
    parser.parse(new ClassPathResource("sample_models/InfoModelWithoutNamespace.infomodel")
        .getInputStream());
  }

}
