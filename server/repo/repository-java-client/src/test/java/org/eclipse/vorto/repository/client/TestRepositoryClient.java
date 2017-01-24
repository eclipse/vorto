package org.eclipse.vorto.repository.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelQueryBuilder;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.api.generation.GeneratorInfo;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import oeg.eclipse.vorto.repository.client.RepositoryClientBuilder;

public class TestRepositoryClient {

	@Rule
	public WireMockRule rule = new WireMockRule(options().port(2345));
	
	@Test
	public void testOnMockedServer() {
		try {
			setupSearchServer();
		
			RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder()
				.setBaseUrl("http://localhost:2345");
		
			IModelRepository modelRepo = builder.buildModelRepositoryClient();
			
			// test search
			Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().freeText("*").build()).get();
			assertTrue(models.size() > 0);
			models.stream().forEach(modelInfo -> System.out.println(modelInfo));
			
			ModelId demoFb = new ModelId("DemoFunctionBlock", "com.mycompany.fb", "1.0.0");
			
			// test getById
			ModelInfo modelInfo = modelRepo.getById(demoFb).get();
			assertNotNull(modelInfo);
			assertEquals(modelInfo.getId().getNamespace(), "com.mycompany.fb");
			assertEquals(modelInfo.getId().getName(), "DemoFunctionBlock");
			assertEquals(modelInfo.getId().getVersion(), "1.0.0");
			System.out.println(modelInfo);
			
			// test getContent
			FunctionblockModel fbModel = modelRepo.getContent(demoFb, FunctionblockModel.class).get();
			assertNotNull(modelInfo);
			assertEquals(fbModel.getId().getNamespace(), "com.mycompany.fb");
			assertEquals(fbModel.getId().getName(), "DemoFunctionBlock");
			assertEquals(fbModel.getId().getVersion(), "1.0.0");
			System.out.println(fbModel);
			
			IModelGeneration modelGen = builder.buildModelGenerationClient();
			
			// test get generator keys
			Set<String> keys = modelGen.getAvailableGeneratorKeys().get();
			keys.forEach(key -> System.out.println(key));
			assertTrue(keys.size() > 0);
			
			// test generator getInfo
			GeneratorInfo info = modelGen.getInfo("boschiotthings").get();
			assertNotNull(info);
			System.out.println(info.toString());
			
			ModelId demoIm = new ModelId("DemoInformationModel", "com.mycompany.informationmodels", "1.0.0");
			
			// test generate
			GeneratedOutput generatedOutput = modelGen.generate(demoIm, "boschiotthings", null).get();
			assertNotNull(generatedOutput);
			assertEquals("generated.zip", generatedOutput.getFileName());
			assertEquals(45926, generatedOutput.getContent().length);
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("Call failed.");
		}
		
	}
	
	private void setupSearchServer() {
		try {
			stubFor(get(urlEqualTo("/rest/model/query=*"))
			        .willReturn(aResponse()
			            .withHeader("Content-Type", "application/json")
			            .withBody(loadFile("searchmodel.json"))
			            ));
			
			stubFor(get(urlEqualTo("/rest/model/com.mycompany.fb/DemoFunctionBlock/1.0.0"))
			        .willReturn(aResponse()
			            .withHeader("Content-Type", "application/json")
			            .withBody(loadFile("demofunctionblock.json"))));
			
			stubFor(get(urlEqualTo("/rest/model/content/com.mycompany.fb/DemoFunctionBlock/1.0.0"))
			        .willReturn(aResponse()
			            .withHeader("Content-Type", "application/json")
			            .withBody(loadFile("demofunctionblockcontent.json"))));
			
			stubFor(get(urlEqualTo("/rest/generation-router/platform"))
			        .willReturn(aResponse()
			            .withHeader("Content-Type", "application/json")
			            .withBody(loadFile("searchgenerators.json"))));
			
			stubFor(get(urlEqualTo("/rest/generation-router/com.mycompany.informationmodels/DemoInformationModel/1.0.0/boschiotthings"))
			        .willReturn(aResponse()
			            .withHeader("Content-Type", "application/octet-stream")
			            .withHeader("Content-Length", "45926")
			            .withHeader("content-disposition", "attachment; filename = generated.zip")
			            .withBody(loadBytes("generated.zip"))));
			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private String loadFile(String filename) throws IOException {
		return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filename));
	}
	
	private byte[] loadBytes(String filename) throws IOException {
		return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream(filename));
	}
}
