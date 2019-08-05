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
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.backup.IBackupRestoreService;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiParam;

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

  private Predicate<Tenant> tenantWithId(String tenantId) {
    return tenant -> tenant.getTenantId().equals(ControllerUtils.sanitize(tenantId));
  }
  
  private Predicate<Tenant> tenantWhoOwnsNamespace(String namespace) {
    return tenant -> tenant.owns(namespace);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/rest/tenants/{tenantId}/backup")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public void backupRepository(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final HttpServletResponse response) throws Exception {
    
    backupRepository(response, tenantWithId(tenantId), 
        Optional.of(String.format("-%s", ControllerUtils.sanitize(tenantId))));
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/rest/namespaces/{namespace}/backup")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public void backupRepositoryOfNamespace(
      @ApiParam(value = "The namespace to be restored",
          required = true) final @PathVariable String namespace,
      final HttpServletResponse response) throws Exception {
    
    backupRepository(response, tenantWhoOwnsNamespace(namespace), 
        Optional.of(String.format("-%s", ControllerUtils.sanitize(namespace))));
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/rest/tenants/backup")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public void backupRepository(final HttpServletResponse response) {
    
    backupRepository(response, id -> true, Optional.empty());
  }
  
  private void backupRepository(final HttpServletResponse response, Predicate<Tenant> tenantFilter, Optional<String> ext) {
    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + 
        String.format("backup-%s%s.zip", SIMPLEDATEFORMAT.format(new Date()), ext.orElse("")));
    response.setContentType(APPLICATION_OCTET_STREAM);

    try {
      IOUtils.copy(new ByteArrayInputStream(backupRestoreService.createBackup(tenantFilter)), response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new GenericApplicationException("Error copying file.", e);
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "/rest/tenants/restore")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public Collection<String> restoreRepository(@RequestParam("file") MultipartFile file) 
      throws Exception {
    
    return restoreRepository(file, x -> true);
  }
  
  @RequestMapping(method = RequestMethod.POST, value = "/rest/tenants/{tenantId}/restore")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public Collection<String> restoreRepository(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestParam("file") MultipartFile file) throws Exception {
    
    return restoreRepository(file, tenantWithId(tenantId));
  }
  
  @RequestMapping(method = RequestMethod.POST, value = "/rest/namespaces/{namespace}/restore")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public Collection<String> restoreRepositoryForNamespace(
      @ApiParam(value = "The namespace to be restored",
          required = true) final @PathVariable String namespace,
      @RequestParam("file") MultipartFile file) throws Exception {
    
    return restoreRepository(file, tenantWhoOwnsNamespace(namespace));
  }
  
  public Collection<String> restoreRepository(MultipartFile file, Predicate<Tenant> tenantPredicate) 
      throws Exception {
    if (file.getSize() > maxBackupSize) {
      throw new UploadTooLargeException("backup", maxBackupSize);
    }
    
    return backupRestoreService.restoreRepository(file.getBytes(), tenantPredicate)
        .stream().map(tenant -> tenant.getNamespaces().iterator().next().getName())
        .collect(Collectors.toList());
  }
}
