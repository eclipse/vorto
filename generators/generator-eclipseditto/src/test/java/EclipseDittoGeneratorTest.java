import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.codegen.ditto.EclipseDittoGenerator;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class EclipseDittoGeneratorTest extends AbstractGeneratorTest {
  public static final String folderPath = "dsls/";

  @BeforeClass
  public static void initParser() {
    ModelWorkspaceReader.init();
  }

  public InformationModel modelProvider(String infomodel, String functionBlock) {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addFile(getClass().getClassLoader().getResourceAsStream(folderPath + infomodel),
            ModelType.InformationModel)
        .addFile(getClass().getClassLoader().getResourceAsStream(folderPath + functionBlock),
            ModelType.Functionblock)
        .read();
    InformationModel model = (InformationModel) workspace.get().stream()
        .filter(p -> p instanceof InformationModel).findAny().get();

    return model;
  }

  ICodeGenerator eclipseDittoGenerator = new EclipseDittoGenerator();

  /*
   * Test case for checking whether the multiple keyword generates corresponding array type in json
   *
   */
  @Test
  public void checkMultipleKeywordInFunctionBlock() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("target", "jsonSchema");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
        modelProvider("MultiplTypeIm.infomodel", "MultipleTypeFb.functionblock"),
        InvocationContext.simpleInvocationContext(params));

    Generated generatedfile = zipFileReader(generationResult, "properties-status.schema", ".json");

    File defaultFile = new File(getClass().getClassLoader()
        .getResource("defaultFileFormat/properties-status.schema.json").toURI());

    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)),
        new String(generatedfile.getContent(), "utf-8"));

  }

  /*
   * Test case for checking whether the config properties are reflected in json
   *
   */
  @Test
  public void checkAPIConfigPropertiesInFunctionBlock() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("target", "jsonSchema");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
        modelProvider("MultiplTypeIm.infomodel", "MultipleTypeFb.functionblock"),
        InvocationContext.simpleInvocationContext(params));

    Generated generatedfile = zipFileReader(generationResult, "properties-configuration.schema",
        ".json");

    File defaultFile = new File(getClass().getClassLoader()
        .getResource("defaultFileFormat/properties-configuration.schema.json").toURI());

    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)),
        new String(generatedfile.getContent(), "utf-8"));

  }

  @Test
  public void checkThingStructure() throws Exception {
    Map<String, String> config = new HashMap<>();
    config.put("target", "thingJson");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
        modelProvider("MultiplTypeIm.infomodel", "MultipleTypeFb.functionblock"),
        InvocationContext.simpleInvocationContext(config));

    assertEquals("application/json", generationResult.getMediatype());
  }
}
