package org.eclipse.vorto.repository.core;

public class FileContent {

	private String fileName;
	private byte[] content;
	
	public FileContent(String fileName, byte[] content) {
		this.fileName = fileName;
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getContent() {
		return content;
	}
}
