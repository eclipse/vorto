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

package org.eclipse.vorto.repository.web.core;

import org.junit.Test;

public class ModelRepositoryController2Test extends ModelRepositoryControllerTest {

  @Override
  protected void setUpTest() throws Exception {
    super.setTenant("");
    super.setUpTest();
  }

  @Test
  public void getModelImage() throws Exception {
    super.getModelImage();
  }

  @Test
  public void uploadModelImage() throws Exception {
    super.uploadModelImage();
  }

  @Test
  public void saveModel() throws Exception {
    super.saveModel();
  }

  @Test
  public void createModelWithAPI() throws Exception {
    super.createModelWithAPI();
  }

  @Test
  public void createVersionOfModel() throws Exception {
    super.createVersionOfModel();
  }

  @Test
  public void deleteModelResource() throws Exception {
    super.deleteModelResource();
  }

  @Test
  public void getUserModels() throws Exception {
    super.getUserModels();
  }

  @Test
  public void downloadMappingsForPlatform() throws Exception {
    super.downloadMappingsForPlatform();
  }

  @Test
  public void runDiagnostics() throws Exception {
    super.runDiagnostics();
  }

  @Test
  public void getPolicies() throws Exception {
    super.getPolicies();
  }

  @Test
  public void getUserPolicy() throws Exception {
    super.getUserPolicy();
  }

  @Test
  public void addValidPolicyEntry() throws Exception {
    super.addValidPolicyEntry();
  }

  @Test
  public void editOwnPolicyEntry() throws Exception {
    super.editOwnPolicyEntry();
  }

  @Test
  public void addInvalidPolicyEntry() throws Exception {
    super.addInvalidPolicyEntry();
  }

  @Test
  public void removePolicyEntry() throws Exception {
    super.removePolicyEntry();
  }  
}
