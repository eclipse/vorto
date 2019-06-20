package org.eclipse.vorto.codegen.hono;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.junit.Test;

public class HonoGeneratorTest {

  @Test
  public void testGenerateHonoForPython() throws Exception {
    
    EclipseHonoGenerator generator = new EclipseHonoGenerator();
    
    ModelId batteryFb = ModelIdFactory.newInstance(ModelType.InformationModel, "org.eclipse.vorto", "1.0.0", "Battery");
    FunctionblockModel fbm = BuilderUtils.newFunctionblock(ModelIdFactory.newInstance(ModelType.InformationModel, "org.eclipse.vorto", "1.0.0", "Battery"))
      .withDescription("Battery description").build();
          
    
    InformationModel infomodel = BuilderUtils.newInformationModel(ModelIdFactory.newInstance(ModelType.InformationModel, "org.eclipse.vorto", "1.0.0", "MyDevice"))
      .withFunctionBlock(fbm, "battery", "battery prop", false)
      .withReference(batteryFb)
      .build();
    
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("language", "python");
    
    IGenerationResult result = generator.generate(infomodel, InvocationContext.simpleInvocationContext(properties));
    assertNotNull(result);
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(result.getContent()));
    
    ZipEntry entry = null;
    
    Set<String> filePaths = new HashSet<>();
    while ((entry = zis.getNextEntry()) != null) {
      filePaths.add(entry.getName());
    }

    assertTrue(filePaths.contains("MyDeviceApp.py"));
  }
}
