/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.importer.ipso;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.importer.AbstractModelImporter;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.ModelImporterException;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.springframework.stereotype.Component;

/**
 * Imports (a bulk of) LwM2M XML definitions to the Vorto Repository by converting them to Vorto Function Blocks and Mapping files
 *
 */
@Component
public class ModelImporterIPSO extends AbstractModelImporter {

	public ModelImporterIPSO() {
		super(".xml");
	}

	private static final String NAMESPACE = "com.ipso.smartobjects";
	private static final String VERSION = "1.1.0"; // set globally because object definitions do not contain the version information

	private static final FunctionblockTemplate FB_TEMPLATE = new FunctionblockTemplate();
	private static final MappingTemplate MAPPING_TEMPLATE = new MappingTemplate();

	@Override
	public String getKey() {
		return "IPSO";
	}

	@Override
	public String getShortDescription() {
		return "Imports LwM2M / IPSO descriptions";
	}

	@Override
	protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent) {
		if (importedModel.getType() == ModelType.Functionblock) {
			getModelRepository().addFileContent(importedModel.getId(), originalFileContent);
		}
	}

	@Override
	protected List<ValidationReport> validate(FileUpload fileUpload, IUserContext user) {
		try {
			LWM2M lwm2mModel = parse(fileUpload);
			if (lwm2mModel.getObject().isEmpty()) {
				return Arrays.asList(ValidationReport.invalid("File "+fileUpload.getFileName() + " does not contain any object definitions."));
			}
			LWM2M.Object obj = lwm2mModel.getObject().get(0);
			ModelInfo modelInfo = createModelInfo(obj);			
			return Arrays.asList(ValidationReport.valid(modelInfo));
		} catch (Exception ex) {
			return Arrays.asList(ValidationReport.invalid(ex.getMessage()));
		}
	}
	
	private ModelInfo createModelInfo(LWM2M.Object obj) {
		ModelInfo modelInfo = new ModelInfo();
		modelInfo.setDescription(obj.getDescription1());
		modelInfo.setDisplayName(obj.getName());
		modelInfo.setId(new ModelId(parseId(obj.getName()), NAMESPACE, VERSION));
		modelInfo.setType(ModelType.Functionblock);
		return modelInfo;
	}

	private String parseId(String name) {
		return name.replace("/", "_").replace(" ", "_").replace("-", "_").replace("\"", "'");
	}

	@Override
	protected List<ModelResource> convert(FileUpload fileUpload, IUserContext user) {	
		List<ModelResource> vortoModels = new ArrayList<ModelResource>(2);
		
		try {
			LWM2M lwm2mModel = parse(fileUpload);
			LWM2M.Object obj = lwm2mModel.getObject().get(0);
			final ModelInfo modelInfo = createModelInfo(obj);
			vortoModels.add(this.parseDSL(ModelType.Functionblock,FB_TEMPLATE.create(obj, modelInfo).getBytes()));
			vortoModels.add(this.parseDSL(ModelType.Mapping,MAPPING_TEMPLATE.create(obj, modelInfo).getBytes()));
			return Collections.unmodifiableList(vortoModels);
		} catch (Exception ex) {
			throw new ModelImporterException("Problem importing ipso files", ex);
		}
	}

	private LWM2M parse(FileUpload fileUpload) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(LWM2M.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (LWM2M) unmarshaller.unmarshal(new ByteArrayInputStream(fileUpload.getContent()));
	}

}
