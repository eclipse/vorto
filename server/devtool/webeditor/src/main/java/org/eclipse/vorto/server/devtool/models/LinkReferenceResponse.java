package org.eclipse.vorto.server.devtool.models;

public class LinkReferenceResponse {

	private String content;
	private String targetResourceId;
	private String referenceResourceId;

	public LinkReferenceResponse(String content, String targetResourceId, String referenceResourceId) {
		this.content = content;
		this.targetResourceId = targetResourceId;
		this.referenceResourceId = referenceResourceId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTargetResourceId() {
		return targetResourceId;
	}

	public void setTargetResourceId(String targetResourceId) {
		this.targetResourceId = targetResourceId;
	}

	public String getReferenceResourceId() {
		return referenceResourceId;
	}

	public void setReferenceResourceId(String referenceResourceId) {
		this.referenceResourceId = referenceResourceId;
	}

}
