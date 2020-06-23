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
package org.eclipse.vorto.repository.web.backup;

import io.swagger.annotations.ApiParam;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.backup.IBackupRestoreService;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BackupController extends AbstractRepositoryController {

  @Autowired
  private IBackupRestoreService backupRestoreService;

  @Value("${repo.config.maxBackupSize}")
  private long maxBackupSize;

  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";

  private static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

  private static Predicate<Namespace> namespaceFilter(String namespace) {
    return n -> n.getName().equals(namespace);
  }

  /**
   * Backs up the whole repository.
   *
   * @param response
   */
  @RequestMapping(method = RequestMethod.GET, value = "/rest/namespaces/backup")
  @PreAuthorize("hasAuthority('sysadmin')")
  public void backupRepository(final HttpServletResponse response) {

    backupRepository(response, n -> true, Optional.empty());
  }

  /**
   * Backs up models for a given namespace
   *
   * @param namespace
   * @param response
   */
  @RequestMapping(method = RequestMethod.GET, value = "/rest/namespaces/{namespace}/backup")
  @PreAuthorize("hasAuthority('sysadmin')")
  public void backupRepositoryOfNamespace(
      @ApiParam(value = "The namespace to be restored",
          required = true) final @PathVariable String namespace,
      final HttpServletResponse response) {

    backupRepository(response, namespaceFilter(namespace),
        Optional.of(String.format("-%s", ControllerUtils.sanitize(namespace))));
  }

  private void backupRepository(final HttpServletResponse response,
      Predicate<Namespace> namespaceFilter, Optional<String> ext) {
    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME +
        String.format("backup-%s%s.zip", SIMPLEDATEFORMAT.format(new Date()), ext.orElse("")));
    response.setContentType(APPLICATION_OCTET_STREAM);

    try {
      IOUtils.copy(new ByteArrayInputStream(backupRestoreService.createBackup(namespaceFilter)),
          response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new GenericApplicationException("Error copying file.", e);
    }
  }

  /**
   * Restores a whole repository
   *
   * @param file
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/rest/namespaces/restore")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ResponseEntity<Collection<String>> restoreRepository(
      @RequestParam("file") MultipartFile file) {
    try {
      return new ResponseEntity<>(restoreRepository(file, x -> true), HttpStatus.OK);
    } catch (IOException | UploadTooLargeException e) {
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Restores models for a given namespace.
   *
   * @param namespace
   * @param file
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/rest/namespaces/{namespace}/restore")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ResponseEntity<Collection<String>> restoreRepositoryForNamespace(
      @ApiParam(value = "The namespace to be restored",
          required = true) final @PathVariable String namespace,
      @RequestParam("file") MultipartFile file) {
    try {
      return new ResponseEntity<>(restoreRepository(file, namespaceFilter(namespace)),
          HttpStatus.OK);
    } catch (IOException | UploadTooLargeException e) {
      return new ResponseEntity(Collections.emptyList(), HttpStatus.BAD_REQUEST);
    }
  }

  public Collection<String> restoreRepository(MultipartFile file,
      Predicate<Namespace> namespaceFilter)
      throws UploadTooLargeException, IOException {
    if (file.getSize() > maxBackupSize) {
      throw new UploadTooLargeException("backup", maxBackupSize);
    }

    return backupRestoreService.restoreRepository(file.getBytes(), namespaceFilter)
        .stream().map(Namespace::getName)
        .collect(Collectors.toList());
  }

}
