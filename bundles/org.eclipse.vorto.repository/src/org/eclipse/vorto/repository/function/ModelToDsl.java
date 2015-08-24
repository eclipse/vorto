package org.eclipse.vorto.repository.function;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule;
import org.eclipse.vorto.editor.functionblock.FunctionblockRuntimeModule;
import org.eclipse.vorto.editor.infomodel.InformationModelRuntimeModule;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.SaveOptions;

import com.google.common.base.Function;
import com.google.inject.Guice;

public class ModelToDsl implements Function<Model, byte[]> {

	@Override
	public byte[] apply(Model model) {
		IResourceFactory resourceFactory = getIResourceFactory(model);

		Resource resource = resourceFactory.createResource(URI
				.createURI("NON_EXIST_DSL_FILE.fbmodel"));
		resource.getContents().add(model);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			Map<Object, Object> saveOptions = SaveOptions.newBuilder().format()
					.getOptions().toOptionsMap();
			resource.save(baos, saveOptions);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(
					"Something went wrong during serialization", e);
		}
	}

	private IResourceFactory getIResourceFactory(Model model) {
		if (model instanceof Type) {
			return Guice.createInjector(new DatatypeRuntimeModule())
					.getInstance(IResourceFactory.class);
		} else if (model instanceof FunctionblockModel) {
			return Guice.createInjector(new FunctionblockRuntimeModule())
					.getInstance(IResourceFactory.class);
		} else if (model instanceof InformationModel) {
			return Guice.createInjector(new InformationModelRuntimeModule())
					.getInstance(IResourceFactory.class);
		}
		throw new UnsupportedOperationException(
				"Resource factory not found for model: "
						+ model.getClass().getName());
	}
}
