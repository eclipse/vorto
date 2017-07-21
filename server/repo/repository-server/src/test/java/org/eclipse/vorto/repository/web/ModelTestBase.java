package org.eclipse.vorto.repository.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.server.commons.ModelZipFileExtractor;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.Maps;

public class ModelTestBase {
	protected Model getModel(String modelName) throws IOException {
		Map<String, byte[]> models = getModels(Arrays.asList("sample_models/Color.type", 
				"sample_models/Colorlight.fbmodel", "sample_models/Colorlight2.fbmodel",
				"sample_models/Switcher.fbmodel"));
		
		ModelZipFileExtractor extractor = new ModelZipFileExtractor(createZip(models));
		return extractor.extract(modelName);
	}
	
	protected Model getModel(String modelName, Collection<String> modelFiles) throws IOException {
		ModelZipFileExtractor extractor = new ModelZipFileExtractor(createZip(getModels(modelFiles)));
		return extractor.extract(modelName);
	}
	
	protected Map<String, byte[]> getModels(Collection<String> modelFiles) throws IOException {
		Map<String, byte[]> modelMap = Maps.newHashMap();
		
		for(String modelFile : modelFiles) {
			String[] filenameComponents = modelFile.split("/");
			modelMap.put(filenameComponents[filenameComponents.length-1], 
					IOUtils.toByteArray(new ClassPathResource(modelFile).getInputStream()));
		}
		
		return modelMap;
	}
	
	private byte[] createZip(Map<String, byte[]> contents) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		
		for(Map.Entry<String, byte[]> entry : contents.entrySet()) {
			zos.putNextEntry(new ZipEntry(entry.getKey()));
			zos.write(entry.getValue());
			zos.closeEntry();
		}
		zos.close();
		baos.close();
		
		return baos.toByteArray();
	}
}
