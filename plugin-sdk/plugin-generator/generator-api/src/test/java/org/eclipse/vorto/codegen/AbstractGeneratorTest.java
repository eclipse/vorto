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
package org.eclipse.vorto.codegen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.testutils.GeneratorTest.ModelEntry;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;

public class AbstractGeneratorTest {

  @BeforeClass
  public static void initParser() {
    ModelWorkspaceReader.init();
  }
  
  protected InvocationContext withInvocationContext(Map<String, String> parameters) {
    return new InvocationContext(Collections.emptyList(), null, parameters);
  }
  
  protected ZipInputStream fromZipLocation(String location) throws IOException {
    return new ZipInputStream(new ByteArrayInputStream(
        IOUtils.toByteArray(this.getClass().getClassLoader().getResource(location).openStream())));
  }

  protected ModelEntry functionBlock(String location) throws IOException {
    return loadModel(ModelType.Functionblock, location);
  }

  protected ModelEntry informationModel(String location) throws IOException {
    return loadModel(ModelType.InformationModel, location);
  }

  protected ModelEntry datatype(String location) throws IOException {
    return loadModel(ModelType.Datatype, location);
  }

  protected ModelEntry mapping(String location) throws IOException {
    return loadModel(ModelType.Mapping, location);
  }

  private ModelEntry loadModel(ModelType type, String location) throws IOException {
    String dsl = IOUtils
        .toString(this.getClass().getClassLoader().getResource(location).openStream(), "utf-8");
    return new ModelEntry(type, dsl);
  }
}
 