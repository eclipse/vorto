/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class GeneratorInfo {

  private String name;

  private String description;

  private String organisation;

  private Set<String> tags = new HashSet<String>();

  private List<ConfigurationItem> configurationItems =
      new ArrayList<GeneratorInfo.ConfigurationItem>();

  public static final String TAG_DEMO = "demo";
  public static final String TAG_PROD = "production";
  public static final String TAG_INFRA = "infra";

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

  public GeneratorInfo tags(String... tags) {
    this.tags = new HashSet<String>(Arrays.asList(tags));
    return this;
  }

  public GeneratorInfo infra() {
    this.tags.clear();
    this.tags.add(TAG_INFRA);
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
    this.configurationItems.add(new BinaryConfigurationItem(key, label));
    return this;
  }

  public GeneratorInfo withChoiceConfigurationItem(String key, String label,
      ChoiceItem... choices) {
    this.configurationItems.add(new ChoiceConfigurationItem(key, label, choices));
    return this;
  }

  public GeneratorInfo withTextConfigurationItem(String key, String label,
      Optional<String> defaultValue) {
    this.configurationItems.add(new TextConfigurationItem(key, label, defaultValue));
    return this;
  }

  public List<ConfigurationItem> getConfigurationItems() {
    return this.configurationItems;
  }

  public boolean isConfigurable() {
    return !this.configurationItems.isEmpty();
  }

  public static class ChoiceItem {
    private String label;
    private String value;

    private ChoiceItem(String label, String value) {
      super();
      this.label = label;
      this.value = value;
    }

    public static ChoiceItem of(String label, String value) {
      return new ChoiceItem(label, value);
    }

    public String getLabel() {
      return label;
    }

    public String getValue() {
      return value;
    }
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

    private Set<ChoiceItem> choices = new HashSet<ChoiceItem>();


    public ChoiceConfigurationItem(String key, String label, ChoiceItem... choices) {
      super(key, label);
      this.choices.addAll(Arrays.asList(choices));
    }


    public Set<ChoiceItem> getChoices() {
      return choices;
    }
  }

  public static class BinaryConfigurationItem extends ConfigurationItem {

    public BinaryConfigurationItem(String key, String label) {
      super(key, label);
    }
  }

  public static class TextConfigurationItem extends ConfigurationItem {

    private Optional<String> defaultValue;

    public TextConfigurationItem(String key, String label, Optional<String> defaultValue) {
      super(key, label);
      this.defaultValue = defaultValue;
    }

    public Optional<String> getDefaultValue() {
      return defaultValue;
    }
  }
}
