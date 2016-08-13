/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.internal.service.emf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.repository.internal.service.IModelParser;
import org.eclipse.vorto.repository.model.ModelEMFResource;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.validation.ValidationException;
import org.eclipse.xtext.linking.impl.XtextLinkingDiagnostic;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public abstract class AbstractModelParser implements IModelParser {

	private String fileName;

	public AbstractModelParser(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public ModelResource parse(InputStream is) {
		XtextResourceSet resourceSet = getInjector().getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.createResource(URI.createURI("dummy:/" + fileName));
		try {
			resource.load(is, resourceSet.getLoadOptions());
		} catch (IOException e) {
			throw new ValidationException(e.getMessage(), null);
		}

		List<org.eclipse.emf.ecore.resource.Resource.Diagnostic> grammarErrors = getGrammarErrors(resource.getErrors());
		if (!grammarErrors.isEmpty()) {
			ModelResource invalidModelResource = new ModelResource(parseModelIdFromFileName(),
					ModelType.fromFileName(fileName));
			throw new ValidationException(grammarErrors.get(0).getMessage(), invalidModelResource);
		}

		return new ModelEMFResource((Model) resource.getContents().get(0));
	}

	private ModelId parseModelIdFromFileName() {
		String pureFileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."));
		ModelId modelId = new ModelId();
		try {
			modelId.setNamespace(pureFileName.substring(0, pureFileName.lastIndexOf(".")));
			modelId.setName(pureFileName.substring(pureFileName.lastIndexOf(".") + 1, pureFileName.indexOf("_")));

			String version = pureFileName.substring(pureFileName.indexOf("_") + 1);
			version = version.replaceAll("_", ".");
			modelId.setVersion(version.substring(0, 5));
		} catch (Throwable t) {
			return new ModelId(pureFileName, "", "0.0.0");
		}
		return modelId;
	}

	private List<org.eclipse.emf.ecore.resource.Resource.Diagnostic> getGrammarErrors(
			EList<org.eclipse.emf.ecore.resource.Resource.Diagnostic> errors) {
		List<org.eclipse.emf.ecore.resource.Resource.Diagnostic> grammarErrors = new ArrayList<>();
		for (org.eclipse.emf.ecore.resource.Resource.Diagnostic diagnostic : errors) {
			if (!(diagnostic instanceof XtextLinkingDiagnostic)) { // ignoring
																	// references
																	// to other
																	// models,
																	// as we
																	// still
																	// allow
																	// them to
																	// be
																	// uploaded
				grammarErrors.add(diagnostic);
			}
		}
		return grammarErrors;
	}

	protected abstract Injector getInjector();

}
