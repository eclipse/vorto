package org.eclipse.vorto.repository.internal.service.emf;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

public class MappingModelParser extends AbstractModelParser {

	private String fileName;

	public MappingModelParser(String fileName) {
		init();
		this.fileName = fileName;
	}

	protected Model doParse(InputStream is) {
		Injector injector = new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.createResource(URI.createURI("dummy:/" + fileName));
		try {
			resource.load(is, resourceSet.getLoadOptions());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (Model) resource.getContents().get(0);
	}

	private void init() {
		MappingPackage.eINSTANCE.eClass();
	}
}
