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
package org.eclipse.vorto.plugin.generator.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ConfigTemplateBuilder {

  private List<ConfigurationItem> configurationItems =
      new ArrayList<ConfigTemplateBuilder.ConfigurationItem>();

  public static ConfigTemplateBuilder builder() {
    ConfigTemplateBuilder info = new ConfigTemplateBuilder();
    return info;
  }

  private ConfigTemplateBuilder() {}
  
  public ConfigTemplateBuilder withBinaryConfigurationItem(String key, String label) {
    this.configurationItems.add(new BinaryConfigurationItem(key, label));
    return this;
  }

  public ConfigTemplateBuilder withChoiceConfigurationItem(String key, String label,
      ChoiceItem... choices) {
    this.configurationItems.add(new ChoiceConfigurationItem(key, label, choices));
    return this;
  }

  public ConfigTemplateBuilder withTextConfigurationItem(String key, String label,
      Optional<String> defaultValue) {
    this.configurationItems.add(new TextConfigurationItem(key, label, defaultValue));
    return this;
  }
  
  
  public String build() {
    return new DefaultGeneratorConfigUI(this.configurationItems).getContent().toString();
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
