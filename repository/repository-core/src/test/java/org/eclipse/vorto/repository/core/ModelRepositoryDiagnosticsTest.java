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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.jcr.RepositoryException;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics;
import org.eclipse.vorto.repository.core.impl.diagnostics.ModelValidationDiagnostic;
import org.eclipse.vorto.repository.core.impl.diagnostics.NodeDiagnosticUtils;
import org.eclipse.vorto.repository.core.impl.diagnostics.ReferenceIntegrityDiagnostic;
import org.junit.Test;
import org.modeshape.jcr.api.observation.Event.Sequencing;
import org.springframework.core.io.ClassPathResource;

public class ModelRepositoryDiagnosticsTest extends AbstractIntegrationTest {

  private DefaultModelBackupService repositoryManager = null;

  @Override
  public void beforeEach() throws Exception {
    super.beforeEach();
    repositoryManager = new DefaultModelBackupService();
    repositoryManager.setModelRepository(this.modelRepository);
    repositoryManager.setSession(((JcrModelRepository) this.modelRepository).getSession());
  }

  @Test
  public void baseline() {
    ModelValidationDiagnostic modelValidationTest = new ModelValidationDiagnostic();
    modelValidationTest.setModelParserFactory(modelParserFactory);

    RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
    modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(modelValidationTest));
    modelRepository.setRepositoryDiagnostics(modelDiagnostics);

    try {
      repositoryManager.restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-baseline.xml")
              .getInputStream()));
    } catch (Exception e) {
      fail("Failed to load backup file.");
    }

    Collection<Diagnostic> diagnostics = modelRepository.diagnoseAllModels();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void withValidationProblems() {
    ModelValidationDiagnostic modelValidationTest = new ModelValidationDiagnostic();
    modelValidationTest.setModelParserFactory(modelParserFactory);

    RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
    modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(modelValidationTest));
    modelRepository.setRepositoryDiagnostics(modelDiagnostics);

    try {
      repositoryManager.restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-validation-error.xml")
              .getInputStream()));
    } catch (Exception e) {
      fail("Failed to load backup file.");
    }

    Collection<Diagnostic> diagnostics = modelRepository.diagnoseAllModels();
    diagnostics.forEach(diagnostic -> System.out.println("-erle- : " + diagnostic.toString()));
    assertEquals(1, diagnostics.size());
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
    ReferenceIntegrityDiagnostic diagnosticTest = new ReferenceIntegrityDiagnostic();

    RepositoryDiagnostics modelDiagnostics = new RepositoryDiagnostics();
    modelDiagnostics.setNodeDiagnosticTests(Arrays.asList(diagnosticTest));
    modelRepository.setRepositoryDiagnostics(modelDiagnostics);

    CountDownLatch sequencingFinished = new CountDownLatch(1);
    try {
      modelRepository.getSession().getWorkspace().getObservationManager()
          .addEventListener(new EventListener() {
            public void onEvent(EventIterator events) {
              sequencingFinished.countDown();
            }
          }, Sequencing.ALL, "/", true, null, null, false);
    } catch (RepositoryException e1) {
      fail("Can't add a sequencing listener.");
    }

    try {
      repositoryManager.restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/diagnosis/vorto-test-diagnosis-baseline.xml")
              .getInputStream()));
    } catch (Exception e) {
      fail("Failed to load backup file.");
    }

    try {
      sequencingFinished.await(60, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      fail("Sequencing interrupted.");
    }

    Collection<Diagnostic> diagnostics = modelRepository.diagnoseAllModels();
    diagnostics.forEach(diagnostic -> System.out.println("-erle- : " + diagnostic.toString()));
    assertEquals(0, diagnostics.size());
  }

}
