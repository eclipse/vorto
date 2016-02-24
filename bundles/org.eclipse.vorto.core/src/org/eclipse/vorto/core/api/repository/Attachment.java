package org.eclipse.vorto.core.api.repository;

public class Attachment {
	
	String filename;
	long length;
	String type;
	byte[] content;
	
	public Attachment(String filename, long length, String type, byte[] content) {
		this.filename = filename;
		this.length = length;
		this.type = type;
		this.content = content;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
