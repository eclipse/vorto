package org.eclipse.vorto.repository.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.mapping.engine.decoder.JSONDeserializer;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.mapping.impl.DefaultPayloadMappingService;
import org.junit.Before;
import org.junit.Test;

public class PayloadMappingSpecificationTest extends AbstractIntegrationTest {

  private DefaultPayloadMappingService mappingService;
  
  @Before
  public void beforeEach() throws Exception {
    super.beforeEach();
    
    this.mappingService = new DefaultPayloadMappingService(this.repositoryFactory,this.workflow);
  }
  
  @Test
  public void testCreateSpecification() {
    importModel("payloadmapping/org.eclipse.vorto:Voltage:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:Battery:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:MyDevice:1.0.0.infomodel");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0"));
    
    assertNotNull(specification);
    assertNotNull(specification.getInfoModel().getTargetPlatformKey());
  }
  
  @Test
  public void testRunMappingTest() {
    importModel("payloadmapping/org.eclipse.vorto:Voltage:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:Battery:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:MyDevice:1.0.0.infomodel");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0"));
    
    specification.getFunctionBlock("battery").getStatusProperty("remainingCapacity").get().getStereotype(Stereotype.SOURCE).get().setAttributes(createXpathRule("/cap"));
    
    specification.getFunctionBlock("battery").getStatusProperty("value").get().getStereotype(Stereotype.SOURCE).get().setAttributes(createXpathRule("/voltage"));
    
    JSONDeserializer deserializer = new JSONDeserializer();
    InfomodelValue testResult = mappingService.runTest(specification,deserializer.deserialize("{\"cap\": 23 ,\"voltage\": 2242.2 }") );
    assertEquals(23.0,testResult.get("battery").getStatusProperty("remainingCapacity").get().getValue());
    assertEquals(2242.2,testResult.get("battery").getStatusProperty("value").get().getValue());
  }
  
  @Test
  public void testSaveMappingSpecificationWithoutChanges() {
    importModel("payloadmapping/org.eclipse.vorto:Voltage:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:Battery:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:MyDevice:1.0.0.infomodel");
    
    final ModelId modelId = ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(modelId);
    
    mappingService.saveSpecification(specification, createUserContext("alex", "playground"));
  }
  
  @Test
  public void testSaveMappingSpecificationWithChanges() {
    importModel("payloadmapping/org.eclipse.vorto:Voltage:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:Battery:1.0.0.fbmodel");
    importModel("payloadmapping/org.eclipse.vorto:MyDevice:1.0.0.infomodel");
    
    final ModelId modelId = ModelId.fromPrettyFormat("org.eclipse.vorto:MyDevice:1.0.0");
    
    IMappingSpecification specification = mappingService.getOrCreateSpecification(modelId);
    
    specification.getFunctionBlock("battery").getStatusProperty("value").get().getStereotype(Stereotype.SOURCE).get().setAttributes(createXpathRule("/voltage"));

    
    mappingService.saveSpecification(specification, createUserContext("alex", "playground"));
  }
  
  private Map<String, String> createXpathRule(String rule) {
    Map<String,String> attributes = new HashMap<String, String>();
    attributes.put(Stereotype.XPATH_ATT,rule);
    return attributes;
  }

}
