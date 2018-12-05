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
package org.eclipse.vorto.editor.infomodel.generator;

import static com.google.common.collect.Sets.newHashSet;
import java.util.Set;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.OutputConfiguration;

public class InformationModelOutputConfigurationProvider implements IOutputConfigurationProvider {

  public static final String OUTPUT_DIR = ".";

  @Override
  public Set<OutputConfiguration> getOutputConfigurations() {
    OutputConfiguration defaultOutput = new OutputConfiguration(IFileSystemAccess.DEFAULT_OUTPUT);
    defaultOutput.setDescription("Root Folder");
    defaultOutput.setOutputDirectory(OUTPUT_DIR);
    defaultOutput.setOverrideExistingResources(true);
    defaultOutput.setCreateOutputDirectory(true);
    defaultOutput.setCanClearOutputDirectory(false);
    defaultOutput.setCleanUpDerivedResources(false);
    defaultOutput.setSetDerivedProperty(false);

    return newHashSet(defaultOutput);
  }



}
