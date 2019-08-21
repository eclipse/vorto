package org.eclipse.vorto.plugin;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
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

	public InformationModel modelProvider(String infomodel, String functionBlock) {
		IModelWorkspace workspace = IModelWorkspace.newReader()
				.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + infomodel),
						ModelType.InformationModel)
				.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + functionBlock),
						ModelType.Functionblock)
				.read();
		InformationModel model = (InformationModel) workspace.get().stream().filter(p -> p instanceof InformationModel)
				.findAny().get();
		return model;
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
}
