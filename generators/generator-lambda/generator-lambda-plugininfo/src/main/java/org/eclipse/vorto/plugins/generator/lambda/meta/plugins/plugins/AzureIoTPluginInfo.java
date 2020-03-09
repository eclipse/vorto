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

import org.eclipse.vorto.codegen.azure.AzureGenerator;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;

public class AzureIoTPluginInfo implements IPluginMeta {

  private GeneratorPluginInfo info;

  public AzureIoTPluginInfo() {
    info = new AzureGenerator().getMeta();
  }

  @Override
  public GeneratorPluginInfo getInfo() {
    return info;
  }
}
