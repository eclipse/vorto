package org.eclipse.vorto.codegen.api;

public class FileContent {

	private String fileName;
	private byte[] value;
	
	public FileContent(String fileName, byte[] value) {
		super();
		this.fileName = fileName;
		this.value = value;
	}
	public String getFileName() {
		return fileName;
	}
	public byte[] getValue() {
		return value;
	}
	
	
}
