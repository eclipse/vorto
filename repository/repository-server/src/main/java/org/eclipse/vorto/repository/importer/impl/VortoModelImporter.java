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
package org.eclipse.vorto.repository.importer.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.ValidationReport;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.AbstractModelImporter;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.springframework.stereotype.Component;

@Component
public class VortoModelImporter extends AbstractModelImporter {

	@Override
	public String getKey() {
		return "Vorto";
	}

	@Override
	public String getShortDescription() {
		return "";
	}

	@Override
	public Set<String> getSupportedFileExtensions() {
		return new HashSet<>(Arrays.asList(".infomodel",".fbmodel",".type",".mapping"));
	}

	@Override
	protected ValidationReport validate(FileUpload fileUpload, IUserContext user) {
		ModelValidationHelper validationHelper = new ModelValidationHelper(getModelRepository(), getUserRepository());
		try {
		final ModelInfo modelInfo = ModelParserFactory.getParser(fileUpload.getFileName()).parse(new ByteArrayInputStream(fileUpload.getContent()));
		return validationHelper.validate(modelInfo, user);
		} catch(ValidationException ex) {
			return ValidationReport.invalid(null, ex.getMessage());
		}
		
	}

	@Override
	protected List<ModelEMFResource> convert(FileUpload fileUpload, IUserContext user) {
		List<ModelEMFResource> result = new ArrayList<ModelEMFResource>(1);
		final ModelEMFResource modelInfo = (ModelEMFResource)ModelParserFactory.getParser(fileUpload.getFileExtension()).parse(new ByteArrayInputStream(fileUpload.getContent()));
		result.add(modelInfo);
		return Collections.unmodifiableList(result);
	}

	@Override
	protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent) {
	}
}
