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
package org.eclipse.vorto.repository.backup.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.backup.IBackupRestoreService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IRepositoryManager;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.utils.ZipUtils;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.modeshape.common.collection.ImmutableMapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@Component
public class BackupRestoreService implements IBackupRestoreService {

  private static Logger logger = Logger.getLogger(BackupRestoreService.class);
  
  private static Function<Tenant, String> tenantSignature = (tenant) -> 
    tenant.getNamespaces().iterator().next().getName();
  
  private IModelRepositoryFactory modelRepositoryFactory;
  
  private ITenantService tenantService;
  
  private IIndexingService indexingService;
  
  private Supplier<Authentication> authSupplier = 
      () -> SecurityContextHolder.getContext().getAuthentication();
  
  public BackupRestoreService(@Autowired IModelRepositoryFactory modelRepositoryFactory,
      @Autowired ITenantService tenantService, @Autowired IIndexingService indexingService) {
    this.modelRepositoryFactory = modelRepositoryFactory;
    this.tenantService = tenantService;
    this.indexingService = indexingService;
  }
  
  @Override
  public byte[] createBackup(Predicate<Tenant> tenantFilter) {
    return createZippedInputStream(createBackups(tenantFilter));
  }
  
  private Map<String, byte[]> createBackups(Predicate<Tenant> tenantFilter) {
    return createBackups(tenantService.getTenants()
        .stream()
        .filter(tenantFilter)
        .collect(Collectors.toList()));
  }
  
  private Map<String, byte[]> createBackups(Collection<Tenant> tenants) {
  return tenants.stream()
      .map(tenant -> 
        new ImmutableMapEntry<String, byte[]>(
            tenantSignature.apply(tenant), 
            modelRepositoryFactory.getRepositoryManager(tenant.getTenantId(), authSupplier.get()).backup())
      ).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
  }
  
  private byte[] createZippedInputStream(Map<String, byte[]> backups) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);

    try {
      for(Map.Entry<String, byte[]> entry : backups.entrySet()) {
        ZipEntry zipEntry = new ZipEntry(entry.getKey() + ".xml");
        zos.putNextEntry(zipEntry);
        zos.write(entry.getValue());
        zos.closeEntry();
      }

      zos.close();
      baos.close();

      return baos.toByteArray();

    } catch (Exception ex) {
      throw new GenericApplicationException("Error while generating zip file.", ex);
    }
  }
  
  @Override
  public Collection<Tenant> restoreRepository(byte[] backupFile, Predicate<Tenant> tenantFilter) {
    Preconditions.checkNotNull(backupFile, "backupFile must not be null");
    try {
      Collection<Tenant> tenantsRestored = Lists.newArrayList();
      Map<String, byte[]> backups = getBackups(backupFile);
      
      backups.forEach((namespace, backup) -> {
        logger.info("Restoring backup for '" + namespace + "'");
        Optional<Tenant> tenant = tenantService.getTenantFromNamespace(namespace);
        if (tenant.isPresent() && tenantFilter.test(tenant.get())) {
          try {
            String tenantId = tenant.get().getTenantId();
            IRepositoryManager repoMgr = modelRepositoryFactory.getRepositoryManager(tenantId, authSupplier.get()); 
            
            if (!repoMgr.isWorkspaceExist(tenantId)) {
              repoMgr.createTenantWorkspace(tenantId);
            } else {
              repoMgr.removeTenantWorkspace(tenantId);
              repoMgr.createTenantWorkspace(tenantId);
            }
            
            repoMgr.restore(backup);
            
            tenantsRestored.add(tenant.get());
          } catch (Exception e) {
            logger.error("Error in restoration of '" + namespace + "'", e);
          }
        } else {
          logger.info("Skipping restoration of '" + namespace + "' either because the tenant could not be found, or is filtered.");
        }
      });
            
      if (!tenantsRestored.isEmpty()) {
        indexingService.reindexAllModels();
      }
      
      return tenantsRestored;
      
    } catch (IOException e) {
      throw new GenericApplicationException("Problem while reading zip file during restore", e);
    }
  }
  
  private Map<String, byte[]> getBackups(byte[] file) throws IOException {
    Map<String, byte[]> backups = new HashMap<>();
    
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file));
    ZipEntry entry = null;
    
    while ((entry = zis.getNextEntry()) != null) {
      if (!entry.isDirectory()
          && !entry.getName().substring(entry.getName().lastIndexOf("/") + 1).startsWith(".")) {
        String namespace = entry.getName().substring(entry.getName().lastIndexOf("/") + 1).replace(".xml", "");
        backups.put(namespace, ZipUtils.copyStream(zis, entry));
      }
    }
    
    return backups;
  }
  
  public Supplier<Authentication> getAuthSupplier() {
    return authSupplier;
  }

  public void setAuthSupplier(Supplier<Authentication> authSupplier) {
    this.authSupplier = authSupplier;
  }
}
