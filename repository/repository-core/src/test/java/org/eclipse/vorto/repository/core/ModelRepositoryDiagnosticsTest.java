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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.Diagnostician;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics;
import org.eclipse.vorto.repository.core.impl.diagnostics.ModelValidationDiagnostic;
import org.eclipse.vorto.repository.core.impl.diagnostics.NodeDiagnosticUtils;
import org.eclipse.vorto.repository.core.impl.diagnostics.ReferenceIntegrityDiagnostic;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelRepositoryDiagnosticsTest extends AbstractIntegrationTest {

  @Test
  public void testDiagnosisAllValidModels() {
    IUserContext admin = createUserContext("admin");

    ModelValidationDiagnostic modelValidationTest = new ModelValidationDiagnostic();
    modelValidationTest.setModelParserFactory(modelParserFactory);

    RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
    modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(modelValidationTest));

    Diagnostician diagnostician = (Diagnostician) repositoryFactory
        .getDiagnosticsService("playground", admin.getAuthentication());
    diagnostician.setRepoDiagnostics(modelDiagnostics);

    try {
      getModelRepository(admin).restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-baseline.xml")
              .getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to load backup file.");
    }

    Collection<Diagnostic> diagnostics = diagnostician.diagnoseAllModels();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testDiagnosisMissingReference() {
    IUserContext admin = createUserContext("admin");

    ModelValidationDiagnostic modelValidationTest = new ModelValidationDiagnostic();
    modelValidationTest.setModelParserFactory(modelParserFactory);

    RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
    modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(modelValidationTest));

    Diagnostician diagnostician = (Diagnostician) repositoryFactory
        .getDiagnosticsService("playground", admin.getAuthentication());
    diagnostician.setRepoDiagnostics(modelDiagnostics);

    try {
      getModelRepository(admin).restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-missingreference.xml")
              .getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to load backup file.");
    }

    Collection<Diagnostic> diagnostics = diagnostician.diagnoseAllModels();
    diagnostics.forEach(diagnostic -> System.out.println("-erle- : " + diagnostic.toString()));
    assertEquals(2, diagnostics.size());
  }

  @Test
  public void testGetModelIdOnFileNode() {
    ModelId modelId = NodeDiagnosticUtils
        .getModelId("/com/ipso/smartobjects/load_control/1.1.0/Load_Control.fbmodel").get();
    assertEquals("load_control", modelId.getName());
    assertEquals("com.ipso.smartobjects", modelId.getNamespace());
    assertEquals("1.1.0", modelId.getVersion());
  }

  @Test
  public void testGetModelIdOnParentNode() {
    ModelId modelId =
        NodeDiagnosticUtils.getModelId("/com/ipso/smartobjects/load_control/1.1.0").get();
    assertEquals("load_control", modelId.getName());
    assertEquals("com.ipso.smartobjects", modelId.getNamespace());
    assertEquals("1.1.0", modelId.getVersion());
  }

  // TODO : This test doesn't actually work as the sequencer will rebuild the references everytime
  // you restore the backup file, and so the references will
  // correct themselves but I'm not deleting it as it shows how to code around the possible race
  // problem when writing test that uploads a model then access
  // the metadata that still needs to be built by the sequencer. This shows how to execute code that
  // actually waits for the sequencer
  // to finish.

  // @Test
  public void withReferenceIntegrityProblems() {
    IUserContext admin = createUserContext("admin");

    ReferenceIntegrityDiagnostic diagnosticTest = new ReferenceIntegrityDiagnostic();

    RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
    modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(diagnosticTest));

    Diagnostician diagnostician = (Diagnostician) repositoryFactory
        .getDiagnosticsService("playground", admin.getAuthentication());
    diagnostician.setRepoDiagnostics(modelDiagnostics);

    Collection<Diagnostic> diagnostics = diagnostician.diagnoseAllModels();
    diagnostics.forEach(diagnostic -> System.out.println("-erle- : " + diagnostic.toString()));
    assertEquals(0, diagnostics.size());
  }
}
