package org.eclipse.vorto.repository.api.attachment;

import org.eclipse.vorto.repository.api.ModelId;

public class Attachment {

	private ModelId modelId;
	private String filename;
	private String downloadLink;

	public static Attachment newInstance(ModelId modelId, String filename) {
		return new Attachment(modelId, filename, getLink(modelId, filename));
	}
	
	public static String getLink(ModelId modelId, String filename) {
		StringBuffer link = new StringBuffer("/rest/model");

		link.append("/");
		link.append(modelId.getNamespace());
		link.append("/");
		link.append(modelId.getName());
		link.append("/");
		link.append(modelId.getVersion());
		link.append("/attachment/");
		link.append(filename);

		return link.toString();
	}
	
	private Attachment(ModelId modelId, String filename, String downloadLink) {
		this.modelId = modelId;
		this.filename = filename;
		this.downloadLink = downloadLink;
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

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

}
