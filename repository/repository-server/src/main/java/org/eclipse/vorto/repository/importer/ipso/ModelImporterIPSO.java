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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.importer.AbstractModelImporter;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.ModelImporterException;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.web.core.exceptions.BulkUploadException;
import org.springframework.stereotype.Component;

@Component
public class ModelImporterIPSO extends AbstractModelImporter {

	private static final String NAMESPACE = "com.ipso.smartobjects";
	private static final String VERSION = "1.1.0";

	private static final FunctionblockTemplate FB_TEMPLATE = new FunctionblockTemplate();
	private static final MappingTemplate MAPPING_TEMPLATE = new MappingTemplate();

	@Override
	public String getKey() {
		return "IPSO";
	}

	@Override
	public String getShortDescription() {
		return "Imports IPSO descriptions and converts them to Vorto Function Blocks";
	}

	@Override
	public Set<String> getSupportedFileExtensions() {
		return new HashSet<>(Arrays.asList(".xml", ".zip"));
	}

	@Override
	protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent) {
		if (importedModel.getType() == ModelType.Functionblock) {
			getModelRepository().addFileContent(importedModel.getId(), originalFileContent);
		}
	}

	@Override
	protected List<ValidationReport> validate(FileUpload fileUpload, IUserContext user) {
		List<ValidationReport> result = new ArrayList<ValidationReport>();

		try {
			if (fileUpload.getFileExtension().equalsIgnoreCase(".zip")) {

				ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileUpload.getContent()));
				ZipEntry entry = null;

				try {
					while ((entry = zis.getNextEntry()) != null) {
						if (!entry.isDirectory() && !entry.getName().substring(entry.getName().lastIndexOf("/")+1).startsWith(".")) {
							LWM2M lwm2mModel = parse(FileUpload.create(entry.getName(), copyStream(zis, entry)));
							LWM2M.Object obj = lwm2mModel.getObject().get(0);
							result.add(ValidationReport.valid(createModelInfo(obj)));
						}
					}
				} catch (IOException e) {
					throw new BulkUploadException("Problem while reading ipso zip file", e);
				}
			} else {
				LWM2M lwm2mModel = parse(fileUpload);
				LWM2M.Object obj = lwm2mModel.getObject().get(0);
				result.add(ValidationReport.valid(createModelInfo(obj)));
			}

			return result;
		} catch (Exception ex) {
			return Arrays.asList(ValidationReport.invalid(null, ex.getMessage()));
		}
	}

	private static byte[] copyStream(ZipInputStream in, ZipEntry entry) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int size;
			byte[] buffer = new byte[2048];

			BufferedOutputStream bos = new BufferedOutputStream(out);

			while ((size = in.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, size);
			}

			bos.flush();
			bos.close();
		} catch (IOException e) {
			throw new BulkUploadException("IOException while copying stream to ZipEntry", e);
		}

		return out.toByteArray();
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
	protected List<ModelEMFResource> convert(FileUpload fileUpload, IUserContext user) {
		List<ModelEMFResource> result = new ArrayList<ModelEMFResource>();
		
		try {
			if (fileUpload.getFileExtension().equalsIgnoreCase(".zip")) {

				ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileUpload.getContent()));
				ZipEntry entry = null;

				try {
					while ((entry = zis.getNextEntry()) != null) {
						if (!entry.isDirectory() && !entry.getName().substring(entry.getName().lastIndexOf("/")+1).startsWith(".")) {
							LWM2M lwm2mModel = parse(FileUpload.create(entry.getName(), copyStream(zis, entry)));
							LWM2M.Object obj = lwm2mModel.getObject().get(0);
							result.add((ModelEMFResource) ModelParserFactory.getParser("model.fbmodel")
									.parse(IOUtils.toInputStream(FB_TEMPLATE.create(obj, createModelInfo(obj)))));
							result.add((ModelEMFResource) ModelParserFactory.getParser("model.mapping")
									.parse(IOUtils.toInputStream(MAPPING_TEMPLATE.create(obj, createModelInfo(obj)))));
						}
					}
				} catch (IOException e) {
					throw new BulkUploadException("Problem while reading ipso zip file", e);
				}
			} else {
				
				final LWM2M lwm2mModel = parse(fileUpload);
				final LWM2M.Object obj = lwm2mModel.getObject().get(0);
				result.add((ModelEMFResource) ModelParserFactory.getParser("model.fbmodel")
						.parse(IOUtils.toInputStream(FB_TEMPLATE.create(obj, createModelInfo(obj)))));
				result.add((ModelEMFResource) ModelParserFactory.getParser("model.mapping")
						.parse(IOUtils.toInputStream(MAPPING_TEMPLATE.create(obj, createModelInfo(obj)))));
			}
			return result;
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
