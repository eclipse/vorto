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
package org.eclipse.vorto.repository.backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

@Ignore
public class RepositoryAdminTest extends AbstractIntegrationTest {

  private DefaultModelBackupService repositoryManager = null;

  @Override
  public void beforeEach() throws Exception {
    super.beforeEach();
    repositoryManager = new DefaultModelBackupService();
    repositoryManager.setModelRepository(this.modelRepository);
    repositoryManager.setSession(((JcrModelRepository) this.modelRepository).getSession());
  }

  @Test
  public void testBackupFilesNoImages() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    byte[] backedUpContent = repositoryManager.backup();
    assertNotNull(backedUpContent);
  }

  @Test
  public void testRestoreBackup1() throws Exception {
    assertEquals(0, this.modelRepository.search("*").size());
    this.repositoryManager.restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    assertEquals(4, this.modelRepository.search("*").size());
  }

  @Test
  public void testRestoreBackupExistingData() throws Exception {
    this.repositoryManager.restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    assertEquals(4, this.modelRepository.search("*").size());
    System.out.println(this.modelRepository.search(("*")));
    this.repositoryManager.restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    assertEquals(4, this.modelRepository.search("*").size());
    System.out.println(this.modelRepository.search(("*")));
    assertEquals("com.mycompany",
        this.modelRepository.search("HueLightStrips").get(0).getId().getNamespace());
  }

  @Test
  public void testRestoreCorruptBackup() {
    try {
      this.repositoryManager.restore(
          IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    } catch (Exception e1) {
      fail("Should not have occurred because backup is valid");
    }
    assertEquals(4, this.modelRepository.search("*").size());

    try {
      this.repositoryManager.restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/vortobackup_corrupt.xml").getInputStream()));
      fail("Exception that vorto backup could not be restored expected");
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(4, this.modelRepository.search("*").size());
  }

}
