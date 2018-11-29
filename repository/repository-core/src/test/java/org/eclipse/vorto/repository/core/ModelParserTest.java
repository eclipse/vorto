package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

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
      parser.setReferences(Arrays.asList(file1, file2));
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
      modelParserFactory.getParser("Accelerometer-invalid.fbmodel").parse(
          new ClassPathResource("sample_models/Accelerometer-invalid.fbmodel").getInputStream());
      fail("Able to get ModelInfo even if model has validation issues.");
    } catch (ValidationException e) {
      assertTrue(e.getMessage().contains("Constraint cannot apply on this property's datatype"));
    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

}
