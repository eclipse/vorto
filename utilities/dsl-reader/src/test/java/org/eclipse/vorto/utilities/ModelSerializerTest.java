package org.eclipse.vorto.utilities;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.model.VortoLangVersion;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Test;
import com.google.inject.Injector;

public class ModelSerializerTest {

  @Test
  public void serializeModelWithVortolang() throws Exception {
    final Injector injector = new FunctionblockStandaloneSetup().createInjectorAndDoEMFRegistration();
    final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

    final FunctionblockModel model = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
    model.setVersion("1.0.0");
    model.setDisplayname("Test");
    model.setDescription("Test");
    model.setName("Test");
    model.setNamespace("com.simple.test");
    model.setLang(VortoLangVersion.VERSION1);
    
    final  FunctionBlock fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
    model.setFunctionblock(fb);

    final Resource resource = resourceSet.createResource(URI.createURI(model.getName() + ".fbmodel"));
    resource.getContents().add(model);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    final Map<Object, Object> optionsMap = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
    optionsMap.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
    resource.save(baos,optionsMap);
    
    assertEquals(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("dsls/expected_saved_model.fbmodel")),new String(baos.toByteArray(),StandardCharsets.UTF_8));
    
  }
}
