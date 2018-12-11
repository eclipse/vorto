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
package org.eclipse.vorto.repository.web.backup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.backup.IModelBackupService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/rest/{tenant}/backups")
public class BackupController extends AbstractRepositoryController {

  @Autowired
  private IModelBackupService backupService;

  @Value("${repo.config.maxBackupSize}")
  private long maxBackupSize;

  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";

  private static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm");

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void backupRepository(final HttpServletResponse response) throws Exception {
    byte[] backup = this.backupService.backup();
    response.setHeader(CONTENT_DISPOSITION,
        ATTACHMENT_FILENAME + "vortobackup_" + SIMPLEDATEFORMAT.format(new Date()) + ".xml");
    response.setContentLengthLong(backup.length);
    response.setContentType(APPLICATION_OCTET_STREAM);
    try {
      IOUtils.copy(new ByteArrayInputStream(backup), response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void restoreRepository(@RequestParam("file") MultipartFile file) throws Exception {
    if (file.getSize() > maxBackupSize) {
      throw new UploadTooLargeException("backup", maxBackupSize);
    }

    this.backupService.restore(file.getBytes());
  }
}
