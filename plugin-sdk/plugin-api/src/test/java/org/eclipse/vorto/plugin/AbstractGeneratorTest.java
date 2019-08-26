package org.eclipse.vorto.plugin;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;

public class AbstractGeneratorTest {

	public static final String folderPath = "dsls/";

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	public InformationModel modelProvider(String infomodel, String functionBlock, String dataType) {
		ModelWorkspaceReader modelWorkspaceReader = IModelWorkspace.newReader()
				.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + infomodel),
						ModelType.InformationModel)
				.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + functionBlock),
						ModelType.Functionblock);
		if (!dataType.isEmpty()) {
			modelWorkspaceReader.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + dataType),
					ModelType.Datatype);
		}
		IModelWorkspace workspace = modelWorkspaceReader.read();
		InformationModel model = (InformationModel) workspace.get().stream().filter(p -> p instanceof InformationModel)
				.findAny().get();
		return model;
	}
	
	public InformationModel modelProvider() {
		Enum _enum = BuilderUtils.newEnum(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype,
				"UnitEnum", "org.eclipse.vorto", "1.0.0")).withLiterals("KG", "G").build();

		Entity _entity = BuilderUtils
				.newEntity(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype, "UnitEntity",
						"org.eclipse.vorto", "1.0.0"))
				.withProperty("value", PrimitiveType.FLOAT).withProperty("unitEnum", _enum).build();
		
		Event eventBlock = BuilderUtils
				.newEvent("testEvent")
				.build();
				
		FunctionblockModel statusPropertiesFunctionBlock = BuilderUtils
				.newFunctionblock(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
						"StatusPropertiesFunctionBlock", "org.eclipse.vorto", "1.0.0"))
				.withStatusProperty("statusValue", _entity)
				.withStatusProperty("statusBoolean", PrimitiveType.BOOLEAN)
				.build();
		
		FunctionblockModel configPropertiesFunctionBlock = BuilderUtils
				.newFunctionblock(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
						"ConfigPropertiesFunctionBlock", "org.eclipse.vorto", "1.0.0"))
				.withConfiguration("configValue", _entity)
				.withConfiguration("configBoolean", PrimitiveType.BOOLEAN)
				.build();
		
		FunctionblockModel eventsAndOperationsFunctionBlock = BuilderUtils
				.newFunctionblock(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
						"eventsAndOperationsFunctionBlock", "org.eclipse.vorto", "1.0.0"))
				.withEvent(eventBlock)
				.withOperation("testOperation", null, null, true, null)
				.build();

		InformationModel infomodel = BuilderUtils
				.newInformationModel(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.InformationModel,
						"MySensor", "org.eclipse.vorto", "1.0.0"))
				.withFunctionBlock(statusPropertiesFunctionBlock, "statusPropertiesFunctionBlock", "", false)
				.withFunctionBlock(configPropertiesFunctionBlock, "configPropertiesFunctionBlock", "", false)
				.withFunctionBlock(eventsAndOperationsFunctionBlock, "eventsAndOperationsFunctionBlock", "", false)
				.build();
		return infomodel;
	}

	public List<ZipEntry> extractZipEntries(IGenerationResult generationResult) throws IOException {
		List<ZipEntry> entries = new ArrayList<>();
		ZipInputStream zi = null;
		try {
			zi = new ZipInputStream(new ByteArrayInputStream(generationResult.getContent()));

			ZipEntry zipEntry = null;
			while ((zipEntry = zi.getNextEntry()) != null) {
				entries.add(zipEntry);
			}
		} finally {
			if (zi != null) {
				zi.close();
			}
		}
		return entries;
	}

	public File zipFileReader(IGenerationResult generationResult,String inputTypeName) throws IOException {

		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(generationResult.getContent()));
		ZipEntry entry = null;
		File file =null;
		while ((entry = zipStream.getNextEntry()) != null) {
			String entryName = entry.getName();
			if (entryName.contains(inputTypeName)) {
				file = File.createTempFile(inputTypeName, ".java");
				FileOutputStream out = new FileOutputStream(file);

				byte[] byteBuff = new byte[4096];
				int bytesRead = 0;
				while ((bytesRead = zipStream.read(byteBuff)) != -1) {
					out.write(byteBuff, 0, bytesRead);
				}

				out.close();
				zipStream.closeEntry();
			}

		}
		zipStream.close();
		return file;

	}

	public void checkResultZipFile(ICodeGenerator iCodeGenerator, InformationModel model)
			throws GeneratorException, IOException {
		assertTrue(generateResult(iCodeGenerator, model).getFileName().endsWith("zip"));
	}

	public IGenerationResult generateResult(ICodeGenerator iCodeGenerator, InformationModel model)
			throws GeneratorException, IOException {
		IGenerationResult generationResult = iCodeGenerator.generate(model,
				InvocationContext.simpleInvocationContext());
		return generationResult;
	}

	public boolean checkFileExists(IGenerationResult generationResult, String fileName) throws IOException {
		List<ZipEntry> zipEntryList = extractZipEntries(generationResult);
		for (ZipEntry zipEntry : zipEntryList) {
			if (zipEntry.getName().contains(fileName + ".java")) {
				return true;
			}
		}
		return false;
	}

}
