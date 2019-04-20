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
package org.eclipse.vorto.codegen.spi.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.spi.templates.DefaultGeneratorConfigUI;
import org.eclipse.vorto.codegen.spi.exception.GeneratorCreationException;
import org.eclipse.vorto.codegen.spi.templates.IGeneratorConfigUITemplate;
import org.eclipse.vorto.codegen.spi.utils.GatewayUtils;

public class Generator {
  private GeneratorServiceInfo info;
  private IVortoCodeGenerator instance;
  private IGeneratorConfigUITemplate configUi = null;

  private static final IGeneratorConfigUITemplate EMPTY_TEMPLATE =
      new IGeneratorConfigUITemplate() {

        @Override
        public Set<String> getKeys() {
          return Collections.emptySet();
        }

        @Override
        public String getContent(GeneratorServiceInfo serviceInfo) {
          return "";
        }
      };

   public static Generator create(String configFile,
    	      Class<? extends IVortoCodeGenerator> generatorClass, IGeneratorConfigUITemplate configTemplate) {
	Objects.requireNonNull(configFile);
    Objects.requireNonNull(generatorClass);
    Objects.requireNonNull(configTemplate);

    try {
      IVortoCodeGenerator instance = generatorClass.newInstance();
      Generator generator = new Generator(GatewayUtils.generatorInfoFromFile(configFile, instance),
      instance, configTemplate);
      generator.info.setDescription(instance.getInfo().getDescription());
      generator.info.setCreator(instance.getInfo().getOrganisation());
      generator.info.setName(instance.getInfo().getName());
      generator.info.setTags(
      instance.getInfo().getTags().toArray(new String[instance.getInfo().getTags().size()]));
      return generator;
    } catch (Exception e) {
      throw new GeneratorCreationException("Error in instantiating Generator", e);
    }
  }
      
  public static Generator create(String configFile,
      Class<? extends IVortoCodeGenerator> generatorClass) {
    Objects.requireNonNull(configFile);
    Objects.requireNonNull(generatorClass);

    try {
      IVortoCodeGenerator instance = generatorClass.newInstance();
      Generator generator = new Generator(GatewayUtils.generatorInfoFromFile(configFile, instance),
          instance, createConfigUI(instance));
      generator.info.setDescription(instance.getInfo().getDescription());
      generator.info.setCreator(instance.getInfo().getOrganisation());
      generator.info.setName(instance.getInfo().getName());
      generator.info.setTags(
          instance.getInfo().getTags().toArray(new String[instance.getInfo().getTags().size()]));
      return generator;
    } catch (Exception e) {
      throw new GeneratorCreationException("Error in instantiating Generator", e);
    }
  }

  private static IGeneratorConfigUITemplate createConfigUI(IVortoCodeGenerator generator) {
	  return new DefaultGeneratorConfigUI(generator.getInfo());
  }

  private Generator(GeneratorServiceInfo info, IVortoCodeGenerator instance,
      IGeneratorConfigUITemplate configTemplate) {
    this.info = Objects.requireNonNull(info);
    this.instance = Objects.requireNonNull(instance);
    this.configUi = Objects.requireNonNull(configTemplate);
  }

  public GeneratorServiceInfo getInfo() {
    return info;
  }

  public void setInfo(GeneratorServiceInfo info) {
    this.info = info;
  }

  public IVortoCodeGenerator getInstance() {
    return instance;
  }

  public void setInstance(IVortoCodeGenerator instance) {
    this.instance = instance;
  }

  public GeneratorServiceInfo getFullInfo() {
    this.info.setConfigTemplate(configUi.getContent(info));
    this.info.setConfigKeys(configUi.getKeys());
    return this.info;
  }
}
