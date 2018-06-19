package org.eclipse.vorto.repository.importer;

public class FileUpload {

	private String fileName;
	
	private byte[] content;
	
	private FileUpload() {		
	}
	
	public static FileUpload create(String fileName, byte[] content) {
		FileUpload fileUpload = new FileUpload();
		fileUpload.fileName = fileName;
		fileUpload.content = content;
		return fileUpload;
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
