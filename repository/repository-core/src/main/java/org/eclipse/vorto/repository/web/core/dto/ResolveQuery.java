package org.eclipse.vorto.repository.web.core.dto;

public class ResolveQuery {
	
	private String targetPlatformKey;
	private String attributeId;
	private String attributeValue;
	private String stereoType;
	
	public ResolveQuery(String targetPlatformKey, String attributeId, String attributeValue, String stereoType) {
		super();
		this.targetPlatformKey = targetPlatformKey;
		this.attributeId = attributeId;
		this.attributeValue = attributeValue;
		this.stereoType = stereoType;
	}

	public String getTargetPlatformKey() {
		return targetPlatformKey;
	}

	public String getAttributeId() {
		return attributeId;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public String getStereoType() {
		return stereoType;
	}

	
	
}
