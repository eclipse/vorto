/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneratorInfo {

	private String name;
	
	private String description;
	
	private String organisation;
	
	private Set<String> tags = new HashSet<String>();
	
	private List<ConfigurationItem> configurationItems = new ArrayList<GeneratorInfo.ConfigurationItem>();
	
	public static final String TAG_DEMO = "demo";
	public static final String TAG_PROD = "production";
	
	public static GeneratorInfo basicInfo(String name, String description, String organisation) {
		GeneratorInfo info = new GeneratorInfo();
		info.name = name;
		info.description = description;
		info.organisation = organisation;
		info.tags.add(TAG_DEMO);
		return info;
	}
	
	public GeneratorInfo() {
		
	}
	
	public GeneratorInfo tags(String...tags) {
		this.tags = new HashSet<String>(Arrays.asList(tags));
		return this;
	}

	public GeneratorInfo demo() {
		this.tags.add(TAG_DEMO);
		return this;
	}
	
	public GeneratorInfo production() {
		this.tags.remove(TAG_DEMO);
		this.tags.add(TAG_PROD);
		return this;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags);
	}
	 
	public GeneratorInfo withBinaryConfigurationItem(String key, String label) {
		this.configurationItems.add(new BinaryConfigurationItem(key,label));
		return this;
	}
	
	public GeneratorInfo withChoiceConfigurationItem(String key, String label, String...choices) {
		this.configurationItems.add(new ChoiceConfigurationItem(key,label, choices));
		return this;
	}
	
	public List<ConfigurationItem> getConfigurationItems() {
		return this.configurationItems;
	}
	
	public boolean isConfigurable() {
		return !this.configurationItems.isEmpty();
	}
	
	public abstract static class ConfigurationItem {
		private String key;
		private String label;
		
		public ConfigurationItem(String key, String label) {
			this.key = key;
			this.label = label;
		}

		public String getKey() {
			return key;
		}

		public String getLabel() {
			return this.label;
		}

		

	}
	
	public static class ChoiceConfigurationItem extends ConfigurationItem {
		
		private Set<String> choices = new HashSet<String>();
		
		
		public ChoiceConfigurationItem(String key, String label, String...choices) {
			super(key,label);
			this.choices.addAll(Arrays.asList(choices));
		}

		
		public Set<String> getChoices() {
			return choices;
		}
	}
	
	public static class BinaryConfigurationItem extends ConfigurationItem {

		public BinaryConfigurationItem(String key, String label) {
			super(key,label);
		}
		
	}
}
