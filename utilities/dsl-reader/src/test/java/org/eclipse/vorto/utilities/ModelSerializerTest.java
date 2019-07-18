package org.eclipse.vorto.utilities;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Test;
import com.google.inject.Injector;

public class ModelSerializerTest {

  @Test
  public void serializeFbModelWithVortolang() throws Exception {
    final Injector injector = new FunctionblockStandaloneSetup().createInjectorAndDoEMFRegistration();
    final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

    final FunctionblockModel model = BuilderUtils.newFunctionblock(ModelIdFactory.newInstance(ModelType.Functionblock, "com.simple.test", "1.0.0", "Test"))
         .withDescription("Test")
         .withDisplayName("Test")
         .withVortolang("1.0")
         .build();
    
    final Resource resource = resourceSet.createResource(URI.createURI(model.getName() + ".fbmodel"));
    resource.getContents().add(model);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    final Map<Object, Object> optionsMap = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
    optionsMap.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
    resource.save(baos,optionsMap);
    
    assertTrue(equalsIgnoreNewlineStyle(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("dsls/expected_saved_model.fbmodel")),new String(baos.toByteArray(),StandardCharsets.UTF_8)));
  }
  
  @Test
  public void serializeEnumModelWithVortolang() throws Exception {
    final Injector injector = new DatatypeStandaloneSetup().createInjectorAndDoEMFRegistration();
    final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

    final org.eclipse.vorto.core.api.model.datatype.Enum model = BuilderUtils.newEnum(ModelIdFactory.newInstance(ModelType.Datatype, "com.simple.test", "1.0.0", "Test"))
         .withLiterals("kg","g")
         .withDescription("Test")
         .withDisplayName("Test")
         .withVortolang("1.0")
         .build();
    
    final Resource resource = resourceSet.createResource(URI.createURI(model.getName() + ".type"));
    resource.getContents().add(model);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    final Map<Object, Object> optionsMap = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
    optionsMap.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
    resource.save(baos,optionsMap);
    
    assertTrue(equalsIgnoreNewlineStyle(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("dsls/expected_saved_enum.type")),new String(baos.toByteArray(),StandardCharsets.UTF_8)));
    
  }
  
  @Test
  public void serializeEntityModelWithVortolang() throws Exception {
    final Injector injector = new DatatypeStandaloneSetup().createInjectorAndDoEMFRegistration();
    final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

    final org.eclipse.vorto.core.api.model.datatype.Entity model = BuilderUtils.newEntity(ModelIdFactory.newInstance(ModelType.Datatype, "com.simple.test", "1.0.0", "Test"))
         .withProperty("value", PrimitiveType.STRING)
         .withDescription("Test")
         .withDisplayName("Test")
         .withVortolang("1.0")
         .build();
    
    final Resource resource = resourceSet.createResource(URI.createURI(model.getName() + ".type"));
    resource.getContents().add(model);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    final Map<Object, Object> optionsMap = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
    optionsMap.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
    resource.save(baos,optionsMap);
    
    assertTrue(equalsIgnoreNewlineStyle(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("dsls/expected_saved_entity.type")),new String(baos.toByteArray(),StandardCharsets.UTF_8)));    
  }
  
  @Test
  public void serializeInfoModelWithVortolang() throws Exception {
    final Injector injector = new InformationModelStandaloneSetup().createInjectorAndDoEMFRegistration();
    final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

    final InformationModel model = BuilderUtils.newInformationModel(ModelIdFactory.newInstance(ModelType.InformationModel, "com.simple.test", "1.0.0", "Test"))
         .withDescription("Test")
         .withDisplayName("Test")
         .withVortolang("1.0")
         .build();
    
    final Resource resource = resourceSet.createResource(URI.createURI(model.getName() + ".infomodel"));
    resource.getContents().add(model);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    final Map<Object, Object> optionsMap = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
    optionsMap.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
    resource.save(baos,optionsMap);
    
    assertTrue(equalsIgnoreNewlineStyle(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("dsls/expected_saved_infomodel.infomodel")),new String(baos.toByteArray(),StandardCharsets.UTF_8)));    
  }
  
  @Test
  public void testSerializeMappingWithVortolang() throws Exception {
    final Injector injector = new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
    final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
    
    ModelWorkspaceReader.init();
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/sample.mapping"),
            org.eclipse.vorto.model.ModelType.Mapping)
        .read();
    
    Model model = workspace.get().get(0);
    
    final Resource resource = resourceSet.createResource(URI.createURI(model.getName() + ".mapping"));
    resource.getContents().add(model);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    final Map<Object, Object> optionsMap = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
    optionsMap.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
    resource.save(baos,optionsMap);
    
    assertTrue(equalsIgnoreNewlineStyle(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("dsls/sample.mapping")),new String(baos.toByteArray(),StandardCharsets.UTF_8)));    
  }
  
  public static boolean equalsIgnoreNewlineStyle(String s1, String s2) {
    return s1 != null && s2 != null && normalizeLineEnds(s1).equals(normalizeLineEnds(s2));
  }
  
  private static String normalizeLineEnds(String s) {
      return s.replace("\r\n", "\n").replace('\r', '\n');
  }
}
