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
package org.eclipse.vorto.editor.functionblock.generator;

import static com.google.common.collect.Sets.newHashSet;
import java.util.Set;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.OutputConfiguration;


public class FbOutputConfigurationProvider implements IOutputConfigurationProvider {

  public static final String FB_OUTPUT = ".";

  @Override
  public Set<OutputConfiguration> getOutputConfigurations() {
    OutputConfiguration defaultOutput = new OutputConfiguration(IFileSystemAccess.DEFAULT_OUTPUT);
    defaultOutput.setDescription("Root Folder");
    defaultOutput.setOutputDirectory(FB_OUTPUT);
    defaultOutput.setOverrideExistingResources(true);
    defaultOutput.setCreateOutputDirectory(true);
    defaultOutput.setCanClearOutputDirectory(false);
    defaultOutput.setCleanUpDerivedResources(false);
    defaultOutput.setSetDerivedProperty(false);


    return newHashSet(defaultOutput);
  }

}
