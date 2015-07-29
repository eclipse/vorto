package org.eclipse.vorto.core.internal.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.service.IModelTransformerService;

public class DefaultModelTransformerService implements IModelTransformerService {

	public DefaultModelTransformerService() {
		init();
	}

	@Override
	public String xmiToDsl(InputStream xmiStream) {
		// TODO Need to implement this
		return null;
	}

	@Override
	public String toXmi(Model model) {
		// TODO Need to implement this
		return null;
	}

	@Override
	public String toDsl(Model model) {
		// TODO Need to implement this
		return null;
	}

	@Override
	public <M extends Model> M xmiToModel(InputStream xmiStream,
			Class<M> modelClass) {
		return this.parseXmi(xmiStream, new ArrayList<InputStream>(),
				modelClass);
	}

	@SuppressWarnings("unchecked")
	private <M extends Model> M parseXmi(InputStream xmiStream,
			List<InputStream> references, Class<M> modelClass) {

		String myType = modelClass.getSimpleName();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				myType, new XMIResourceFactoryImpl());

		ResourceSet resourceSet = new ResourceSetImpl();

		List<Object> refModels = new ArrayList<Object>();
		for (int i = 0; i < references.size(); i++) {
			InputStream inputStream = references.get(i);
			Resource resource = resourceSet.createResource(URI
					.createURI("dummyResource_" + i + "." + myType));
			try {
				resource.load(inputStream, null);
				refModels.add(resource.getContents().get(0));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		final Resource resource = resourceSet.createResource(URI
				.createURI("dummyResource." + myType));

		try {
			resource.load(xmiStream, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return (M) resource.getContents().get(0);
	}

	private void init() {
		DatatypePackage.eINSTANCE.eClass();
		FunctionblockPackage.eINSTANCE.eClass();
		InformationModelPackage.eINSTANCE.eClass();
	}
}
