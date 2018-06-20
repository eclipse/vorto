package org.eclipse.vorto.repository.importer;

import java.io.File;

public class FileUpload {

	private String fileName;
	
	private byte[] content;
	
	private FileUpload() {		
	}
	
	public static FileUpload create(String filePath, byte[] content) {
		FileUpload fileUpload = new FileUpload();
		fileUpload.fileName = extractFileName(filePath);
		fileUpload.content = content;
		return fileUpload;
	}

	private static String extractFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf('/')+1);
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public String getFileExtension() {
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
