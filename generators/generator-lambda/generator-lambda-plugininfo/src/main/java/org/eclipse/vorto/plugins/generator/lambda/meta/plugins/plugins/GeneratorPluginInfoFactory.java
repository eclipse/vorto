/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.plugins.generator.lambda.meta.plugins.plugins;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;

public class GeneratorPluginInfoFactory {
  
  
  private static GeneratorPluginInfoFactory instance = null;
  
  private static final EclipseDittoPluginInfo eclipsedittoPlugin = new EclipseDittoPluginInfo();
  private static final EclipseHonoPluginInfo eclipsehonoPlugin = new EclipseHonoPluginInfo();
  private static final BoschIoTSuitePluginInfo boschiotsuitePlugin = new BoschIoTSuitePluginInfo();
  private static final OpenAPIPluginInfo openApiPlugin = new OpenAPIPluginInfo();
  private static final JsonSchemaPluginInfo jsonSchemaPlugin = new  JsonSchemaPluginInfo();
  
  
  private final static Map<String, GeneratorPluginInfo> infos = new HashMap<String, GeneratorPluginInfo>();
  
  static {
    infos.put(eclipsedittoPlugin.getInfo().getKey(),eclipsedittoPlugin.getInfo());
    infos.put(eclipsehonoPlugin.getInfo().getKey(),eclipsehonoPlugin.getInfo());
    infos.put(boschiotsuitePlugin.getInfo().getKey(),boschiotsuitePlugin.getInfo());
    infos.put(openApiPlugin.getInfo().getKey(), openApiPlugin.getInfo());
    infos.put(jsonSchemaPlugin.getInfo().getKey(), jsonSchemaPlugin.getInfo());
  }
  
  private GeneratorPluginInfoFactory() {
  }


  public static GeneratorPluginInfoFactory getInstance() {
    if (instance == null) {
      instance = new GeneratorPluginInfoFactory();
    }
    return instance;
  }
  public GeneratorPluginInfo getForPlugin(String pluginkey) {
    return infos.get(pluginkey);
  }
}
