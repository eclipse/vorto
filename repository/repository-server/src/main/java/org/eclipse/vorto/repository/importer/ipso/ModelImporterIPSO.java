package org.eclipse.vorto.repository.importer.ipso;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.stereotype.Component;

@Component
public class ModelImporterIPSO extends AbstractModelImporter {

	private static final String NAMESPACE = "com.ipso.smartobjects";
	private static final String VERSION = "1.0.0";
	
	private static final FunctionblockTemplate FB_TEMPLATE = new FunctionblockTemplate();
	
	@Override
	public String getKey() {
		return "IPSO";
	}

	@Override
	public String getShortDescription() {
		return "";
	}

	@Override
	public Set<String> getSupportedFileExtensions() {
		return new HashSet<>(Arrays.asList(".xml"));
	}

	@Override
	protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent) {
		getModelRepository().addFileContent(importedModel.getId(), originalFileContent);
	}

	
	@Override
	protected ValidationReport validate(FileUpload fileUpload, IUserContext user) {
		try {
			LWM2M lwm2mModel = parse(fileUpload);
			LWM2M.Object obj = lwm2mModel.getObject().get(0);
			return ValidationReport.valid(createModelInfo(obj));
		} catch(Exception ex) {
			return ValidationReport.invalid(null, ex.getMessage());
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
	protected List<ModelEMFResource> convert(FileUpload fileUpload, IUserContext user) {
		try {
			final LWM2M lwm2mModel = parse(fileUpload);
			final LWM2M.Object obj = lwm2mModel.getObject().get(0);
			ModelEMFResource fbModel = (ModelEMFResource)ModelParserFactory.getParser("model.fbmodel").parse(IOUtils.toInputStream(FB_TEMPLATE.create(obj, createModelInfo(obj))));
			return Arrays.asList(fbModel);
		} catch(Exception ex) {
			throw new ModelImporterException("Problem importing lwm2m",ex);
		}
	}
	
	private LWM2M parse(FileUpload fileUpload) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(LWM2M.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (LWM2M) unmarshaller.unmarshal(new ByteArrayInputStream(fileUpload.getContent()));
	}

}
