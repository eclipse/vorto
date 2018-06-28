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
package org.eclipse.vorto.repository.importer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
import org.eclipse.vorto.repository.core.impl.StorageItem;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.web.core.exceptions.BulkUploadException;
import org.modeshape.common.collection.Collections;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Extend this class for Implementation of a special importer for Vorto
 *
 */
public abstract class AbstractModelImporter implements IModelImporter {

	public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;
	
	@Autowired
	private ITemporaryStorage uploadStorage;
	
	@Autowired
	private IModelRepository modelRepository;
	
	@Autowired
	private IUserRepository userRepository;

	private Set<String> supportedFileExtensions = new HashSet<>();
	
	protected static final String EXTENSION_ZIP = ".zip";
	
	public AbstractModelImporter(String modelTypeFileExtension, String...additionalExtensions) {
		if (handleZipUploads()) {
			supportedFileExtensions.add(EXTENSION_ZIP);
		}
		supportedFileExtensions.add(modelTypeFileExtension);
		supportedFileExtensions.addAll(Arrays.asList(additionalExtensions));
	}

	@Override
	public UploadModelResult upload(FileUpload fileUpload, IUserContext user) {
		if (!this.supportedFileExtensions.contains(fileUpload.getFileExtension())) {
			return new UploadModelResult(null,Arrays.asList(ValidationReport.invalid(null,"File type is invalid. Must be "+this.supportedFileExtensions)));
		}
		List<ValidationReport> reports = new ArrayList<ValidationReport>();
		if (handleZipUploads() && fileUpload.getFileExtension().equalsIgnoreCase(EXTENSION_ZIP)) {

			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileUpload.getContent()));
			ZipEntry entry = null;
			
			try {
				while ((entry = zis.getNextEntry()) != null) {
					if (!entry.isDirectory() && !entry.getName().substring(entry.getName().lastIndexOf("/")+1).startsWith(".")) {
						final FileUpload extractedFile = FileUpload.create(entry.getName(), copyStream(zis, entry));
						reports.addAll(this.validate(extractedFile, user));
					}
				}
			} catch (IOException e) {
				throw new BulkUploadException("Problem while reading zip file during validation", e);
			}
		} else {
			reports.addAll(this.validate(fileUpload, user));
		}

		if (reports.stream().filter(report -> !report.isValid()).count() == 0) {
			return new UploadModelResult(createUploadHandle(fileUpload), reports);
		} else {
			return new UploadModelResult(null,reports);
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
	
	private String createUploadHandle(FileUpload fileUpload) {
		final String handleId = UUID.randomUUID().toString();
		return this.uploadStorage.store(handleId, fileUpload, TTL_TEMP_STORAGE_INSECONDS).getKey();
	}

	@Override
	public List<ModelInfo> doImport(String uploadHandleId, IUserContext user) {
		StorageItem uploadedItem = this.uploadStorage.get(uploadHandleId);

		if (uploadedItem == null) {
			throw new ModelImporterException("No uploaded file found for handleId '" + uploadHandleId + "'");
		}
		
		List<ModelInfo> importedModels = new ArrayList<>();
		
		try {
			
			if (handleZipUploads() && uploadedItem.getValue().getFileExtension().equalsIgnoreCase(EXTENSION_ZIP)) {
				ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(uploadedItem.getValue().getContent()));
				ZipEntry entry = null;
				
				try {
					while ((entry = zis.getNextEntry()) != null) {
						if (!entry.isDirectory() && !entry.getName().substring(entry.getName().lastIndexOf("/")+1).startsWith(".")) {
							final FileUpload extractedFile = FileUpload.create(entry.getName(), copyStream(zis, entry));
							List<ModelEMFResource> resources = this.convert(extractedFile, user);
							
							importedModels.addAll(sortAndSaveToRepository(resources,extractedFile,user));
							
						}
					}
				} catch (IOException e) {
					throw new BulkUploadException("Problem while reading zip file during validation", e);
				}
			} else {
				List<ModelEMFResource> resources = this.convert(uploadedItem.getValue(), user);							
				importedModels.addAll(sortAndSaveToRepository(resources,uploadedItem.getValue(),user));
			}
		} finally {
			this.uploadStorage.remove(uploadHandleId);
		}
	
		return importedModels;
	}
	
	private List<ModelInfo> sortAndSaveToRepository(List<ModelEMFResource> resources, FileUpload extractedFile, IUserContext user) {
		List<ModelInfo> savedModels = new ArrayList<ModelInfo>();
		DependencyManager dm = new DependencyManager();
		for (ModelInfo resource : resources) {
			dm.addResource(resource);
		}
		
		dm.getSorted().stream().forEach(resource -> {
			try {
				ModelInfo importedModel = this.modelRepository.save(resource.getId(), ((ModelEMFResource)resource).toDSL(), createFileName(resource), user);
				savedModels.add(importedModel);
				postProcessImportedModel(importedModel, new FileContent(extractedFile.getFileName(),extractedFile.getContent()));
			} catch (Exception e) {
				throw new ModelImporterException("Problem importing model",e);
			}
		});
		
		return savedModels;
	}

	protected boolean handleZipUploads() {
		return true;
	}
	
	@Override
	public Set<String> getSupportedFileExtensions() {
		return Collections.unmodifiableSet(this.supportedFileExtensions);
	}
		
	protected ModelEMFResource parseDSL(String fileName, byte[] content) {
		return (ModelEMFResource)ModelParserFactory.getParser(fileName).parse(new ByteArrayInputStream(content));
	}
	
	private String createFileName(ModelInfo resource) {
		return resource.getId().getName() + resource.getType().getExtension();
	}
	
	protected abstract void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent);

	/**
	 * validates the given fileUpload content
	 * @param content
	 * @param fileName
	 * @param user
	 * @return
	 */
	protected abstract List<ValidationReport> validate(FileUpload fileUpload, IUserContext user);
	
	/**
	 * converts the given file upload content to Vorto DSL content
	 * @param fileUpload
	 * @param user
	 * @return Vorto DSL content
	 */
	protected abstract List<ModelEMFResource> convert(FileUpload fileUpload, IUserContext user);

	public void setUploadStorage(ITemporaryStorage uploadStorage) {
		this.uploadStorage = uploadStorage;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public void setUserRepository(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	public ITemporaryStorage getUploadStorage() {
		return uploadStorage;
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public IUserRepository getUserRepository() {
		return userRepository;
	}

	
}
