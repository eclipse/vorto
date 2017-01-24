package org.eclipse.vorto.repository.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelQueryBuilder;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.api.generation.GeneratorInfo;
import org.junit.Test;

import oeg.eclipse.vorto.repository.client.RepositoryClientBuilder;

public class TestClient {

	@Test
	public void testOnMockedServer() {
		RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder()
				.setBaseUrl("http://localhost:8080/infomodelrepository");
		/*
		IModelRepository modelRepo = builder.buildModelRepositoryClient();
		*/
		try {
			IModelRepository modelRepo = builder.buildModelRepositoryClient();
			
			// test search
			Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().freeText("*").build()).get();
			assertTrue(models.size() > 0);
			
			models.stream().forEach(modelInfo -> System.out.println(modelInfo));
			
			// test getById
			for(ModelInfo model : models) {
				ModelInfo modelInfo = modelRepo.getById(model.getId()).get();
				System.out.print(model.getId());
				assertEquals(modelInfo, model);
				System.out.println(" - SAME");
			};
			
			// test getContent
			ModelInfo testFb = models.stream().filter(modelInfo -> modelInfo.getType().equals(ModelType.Functionblock)).findFirst().get();
			FunctionblockModel fbModel = modelRepo.getContent(testFb.getId(), FunctionblockModel.class).get();
			System.out.println(fbModel);
			
			IModelGeneration modelGen = builder.buildModelGenerationClient();
			
			// test get generator keys
			Set<String> keys = modelGen.getAvailableGeneratorKeys().get();
			keys.forEach(key -> System.out.println(key));
			assertTrue(keys.size() > 0);
			
			// test generator getInfo
			GeneratorInfo info = modelGen.getInfo(keys.stream().findFirst().get()).get();
			assertNotNull(info);
			System.out.println(info.toString());
			
			// test generate
			ModelInfo testInfoModel = models.stream().filter(modelInfo -> modelInfo.getType().equals(ModelType.InformationModel)).findFirst().get();
			GeneratedOutput generatedOutput = modelGen.generate(testInfoModel.getId(), info.getKey(), null).get();
			assertNotNull(generatedOutput);
			
			System.out.println(generatedOutput);
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("Call failed.");
		}
	}
}
