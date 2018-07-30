package org.eclipse.vorto.repository.core;

public class FileContent {

	private String fileName;
	private byte[] content;
	private long size;
	
	public FileContent(String fileName, byte[] content) {
		this.fileName = fileName;
		this.content = content;
	}

	public FileContent(String fileName, byte[] content, long size) {
		this.fileName = fileName;
		this.content = content;
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public long getSize() {
		return size;
	}

}
