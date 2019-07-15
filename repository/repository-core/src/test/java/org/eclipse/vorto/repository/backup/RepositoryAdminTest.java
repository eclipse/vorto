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
import org.eclipse.vorto.repository.core.IUserContext;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class RepositoryAdminTest extends AbstractIntegrationTest {

  @Test
  public void testBackupFilesNoImages() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    
    IUserContext admin = createUserContext("admin");
    
    byte[] backedUpContent = getRepoManager(admin).backup();
    assertNotNull(backedUpContent);
  }

  @Test
  public void testRestoreBackup1() throws Exception {
    IUserContext admin = createUserContext("admin");
    
    assertEquals(0, getModelRepository(admin).search("*").size());
    
    getRepoManager(admin).restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    
    assertEquals(4, getModelRepository(admin).search("*").size());
  }

  @Test
  public void testRestoreBackupExistingData() throws Exception {
    IUserContext admin = createUserContext("admin");
    
    getRepoManager(admin).restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    assertEquals(4, getModelRepository(admin).search("*").size());
    
    System.out.println(getModelRepository(admin).search(("*")));
    getRepoManager(admin).restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    assertEquals(4, getModelRepository(admin).search("*").size());
    System.out.println(getModelRepository(admin).search(("*")));
    assertEquals("com.mycompany",
        getModelRepository(admin).search("HueLightStrips").get(0).getId().getNamespace());
  }

  @Test
  public void testRestoreCorruptBackup() {
    IUserContext admin = createUserContext("admin");
    
    try {
      getRepoManager(admin).restore(
          IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
    } catch (Exception e1) {
      fail("Should not have occurred because backup is valid");
    }
    assertEquals(4, getModelRepository(admin).search("*").size());

    try {
      getRepoManager(admin).restore(IOUtils.toByteArray(
          new ClassPathResource("sample_models/vortobackup_corrupt.xml").getInputStream()));
      fail("Exception that vorto backup could not be restored expected");
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(4, getModelRepository(admin).search("*").size());
  }

}
