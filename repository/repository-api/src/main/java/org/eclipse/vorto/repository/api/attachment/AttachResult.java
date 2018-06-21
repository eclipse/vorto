package org.eclipse.vorto.repository.api.attachment;

import org.eclipse.vorto.repository.api.ModelId;

public class AttachResult {
	private boolean success;
	private Attachment attachment;
	private String errorMessage;

	public static AttachResult success(ModelId modelId, String filename) {
		return new AttachResult(true, Attachment.newInstance(modelId, filename), null);
	}

	public static AttachResult fail(ModelId modelId, String filename, String errorMessage) {
		return new AttachResult(false, null, errorMessage);

	}

	public AttachResult(boolean success, Attachment attachment, String errorMessage) {
		this.success = success;
		this.attachment = attachment;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
