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
package org.eclipse.vorto.editor;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.ui.parser.IModelParser;
import org.eclipse.vorto.core.ui.parser.ParseModelResult;
import org.eclipse.xtext.linking.impl.XtextLinkingDiagnostic;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * 
 * Uses the Xtext Parser to parse DSL model files
 * 
 */
public class XtextResourceModelParser implements IModelParser {

	private static Predicate<Resource.Diagnostic> notXtextLinkingDiagnostics = new Predicate<Resource.Diagnostic>() {
		public boolean apply(Resource.Diagnostic diagnostic) {
			return !(diagnostic instanceof XtextLinkingDiagnostic);
		}
	};
	
	public <M extends Model> ParseModelResult<M> parseModelWithError(IFile modelFile, Class<M> modelClass) {
		try {
			URI uri = URI.createPlatformResourceURI(modelFile.getFullPath()
					.toString(), true);
			return parseModel(uri, modelClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public <M extends Model> M parseModel(IFile modelFile, Class<M> modelClass) {
		try {
			URI uri = URI.createPlatformResourceURI(modelFile.getFullPath()
					.toString(), true);
			ParseModelResult<M> result = parseModel(uri, modelClass); 
			return result.getModel();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <M> ParseModelResult<M> parseModel(URI uri, Class<M> modelClass) {
		ResourceSet rs = new XtextResourceSet();
		Resource resource = rs.getResource(uri, true);
		if (!resource.getContents().isEmpty()) {
			Collection<Resource.Diagnostic> errorDiagnostics = Lists.newArrayList();
			EObject eModel = resource.getContents().get(0);
			// linking errors
			errorDiagnostics.addAll(getLinkingErrors(eModel));
			// syntax errors
			errorDiagnostics.addAll(Collections2.filter(resource.getErrors(), notXtextLinkingDiagnostics));
			
			return ParseModelResult.newResult(errorDiagnostics, (M) eModel);
		} else {
			return ParseModelResult.newResult(Collections2.filter(resource.getErrors(), notXtextLinkingDiagnostics), 
					null);
		}
	}
	
	private Collection<Resource.Diagnostic> getLinkingErrors(EObject model) {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(model);
		switch (diagnostic.getSeverity()) {
		  case Diagnostic.ERROR:
			  return Collections2.transform(diagnostic.getChildren(), emfDiagnosticToResourceDiagnostic);
		}
		return Collections.emptyList();
	}
	
	private static Function<Diagnostic, Resource.Diagnostic> emfDiagnosticToResourceDiagnostic = new Function<Diagnostic, Resource.Diagnostic>() {
		public Resource.Diagnostic apply(final Diagnostic input) {
			return new Resource.Diagnostic() {
				public String getMessage() {
					return input.getMessage();
				}
				
				public String getLocation() {
					return input.getSource();
				}
				
				public int getLine() {
					return 0;
				}
				
				public int getColumn() {
					return 0;
				}
			};
		}
	};

	@Override
	public <M extends Model> M parseModel(File modelFile, Class<M> modelClass) {
		try {
			URI uri = URI.createFileURI(modelFile.getAbsolutePath());
			ParseModelResult<M> result = parseModel(uri, modelClass); 
			return result.getModel();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
