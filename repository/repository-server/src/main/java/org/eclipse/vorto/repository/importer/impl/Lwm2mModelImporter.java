//package org.eclipse.vorto.repository.importer.impl;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.eclipse.vorto.repository.api.ModelInfo;
//import org.eclipse.vorto.repository.api.upload.ValidationReport;
//import org.eclipse.vorto.repository.core.FileContent;
//import org.eclipse.vorto.repository.core.IUserContext;
//import org.eclipse.vorto.repository.core.impl.ModelEMFResource;
//import org.eclipse.vorto.repository.importer.AbstractModelImporter;
//import org.eclipse.vorto.repository.importer.FileUpload;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Lwm2mModelImporter extends AbstractModelImporter {
//
//	@Override
//	public String getKey() {
//		return "IPSO";
//	}
//
//	@Override
//	public String getShortDescription() {
//		return "";
//	}
//
//	@Override
//	public Set<String> getSupportedFileExtensions() {
//		return new HashSet<>(Arrays.asList(".xml"));
//	}
//
//	@Override
//	protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent) {
//	}
//
//	@Override
//	protected ValidationReport validate(FileUpload fileUpload, IUserContext user) {
//		return ValidationReport.valid(null);
//	}
//
//	@Override
//	protected List<ModelEMFResource> convert(FileUpload fileUpload, IUserContext user) {
//		return Collections.emptyList();
//	}
//
//}
