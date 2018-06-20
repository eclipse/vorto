package org.eclipse.vorto.repository.api.attachment;

import org.eclipse.vorto.repository.api.ModelId;

public class AttachResult {
	private boolean success;
	private ModelId modelId;
	private String filename;
	private String urlLink;
	private String errorMessage;

	public static AttachResult success(ModelId modelId, String filename) {
		return new AttachResult(true, modelId, filename, Attachment.getLink(modelId, filename), null);
	}

	public static AttachResult fail(ModelId modelId, String filename, String errorMessage) {
		return new AttachResult(false, modelId, filename, null, errorMessage);

	}

	public AttachResult(boolean success, ModelId modelId, String filename, String urlLink, String errorMessage) {
		this.success = success;
		this.modelId = modelId;
		this.filename = filename;
		this.urlLink = urlLink;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ModelId getModelId() {
		return modelId;
	}

	public void setModelId(ModelId modelId) {
		this.modelId = modelId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
