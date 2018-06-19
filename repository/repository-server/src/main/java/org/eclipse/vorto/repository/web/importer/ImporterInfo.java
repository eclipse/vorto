package org.eclipse.vorto.repository.web.importer;

import java.util.HashSet;
import java.util.Set;

public class ImporterInfo {
	
	private String key; 

	private Set<String> extensionTypes = new HashSet<>();
	
	private String shortDescription;
	
	public ImporterInfo(String key, Set<String> extensionTypes, String shortDescription) {
		this.key = key;
		this.extensionTypes = extensionTypes;
		this.shortDescription = shortDescription;
	}
	
	protected ImporterInfo() {
		
	}

	public Set<String> getExtensionTypes() {
		return extensionTypes;
	}

	public void setExtensionTypes(Set<String> extensionTypes) {
		this.extensionTypes = extensionTypes;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
