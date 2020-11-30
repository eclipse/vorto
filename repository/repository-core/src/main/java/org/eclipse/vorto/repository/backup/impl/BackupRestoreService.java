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
package org.eclipse.vorto.repository.backup.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.backup.IBackupRestoreService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IRepositoryManager;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.utils.ZipUtils;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.modeshape.common.collection.ImmutableMapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackupRestoreService implements IBackupRestoreService {

  private static final Logger LOGGER = Logger.getLogger(BackupRestoreService.class);

  private IModelRepositoryFactory modelRepositoryFactory;

  private IIndexingService indexingService;

  private NamespaceRepository namespaceRepository;

  public BackupRestoreService(@Autowired IModelRepositoryFactory modelRepositoryFactory,
      @Autowired IIndexingService indexingService,
      @Autowired NamespaceRepository namespaceRepository) {
    this.modelRepositoryFactory = modelRepositoryFactory;
    this.indexingService = indexingService;
    this.namespaceRepository = namespaceRepository;
  }

  @Override
  public byte[] createBackup(Predicate<Namespace> namespaceFilter) {
    return createZippedInputStream(createBackups(namespaceFilter));
  }

  private Map<String, byte[]> createBackups(Predicate<Namespace> namespaceFilter) {
    return createBackups(namespaceRepository.findAll()
        .stream()
        .filter(namespaceFilter)
        .collect(Collectors.toList()));
  }

  private Map<String, byte[]> createBackups(Collection<Namespace> namespaces) {
    return namespaces.stream()
        .map(namespace ->
            new ImmutableMapEntry<>(
                namespace.getName(),
                modelRepositoryFactory
                    .getRepositoryManager(namespace.getWorkspaceId()).backup())
        ).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
  }

  private byte[] createZippedInputStream(Map<String, byte[]> backups) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);

    try {
      for (Map.Entry<String, byte[]> entry : backups.entrySet()) {
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
  public Collection<Namespace> restoreRepository(byte[] backupFile,
      Predicate<Namespace> namespaceFilter) {
    Preconditions.checkNotNull(backupFile, "Backup file must not be null");
    try {
      Collection<Namespace> namespacesRestored = Lists.newArrayList();
      Map<String, byte[]> backups = getBackups(backupFile);

      backups.forEach((namespaceName, backup) -> {
        LOGGER.info(String.format("Restoring backup for [%s]", namespaceName));
        Namespace namespace = namespaceRepository.findByName(namespaceName);
        if (null != namespace && namespaceFilter.test(namespace)) {
          try {
            String workspaceId = namespace.getWorkspaceId();
            IRepositoryManager repoMgr = modelRepositoryFactory
                .getRepositoryManager(workspaceId);

            if (!repoMgr.exists(workspaceId)) {
              repoMgr.createWorkspace(workspaceId);
            } else {
              repoMgr.removeWorkspace(workspaceId);
              repoMgr.createWorkspace(workspaceId);
            }

            repoMgr.restore(backup);
            this.modelRepositoryFactory.getPolicyManager(workspaceId)
                .restorePolicyEntries();
            namespacesRestored.add(namespace);
          } catch (Exception e) {
            LOGGER.error(String.format("Error while restoring [%s]", namespaceName), e);
          }
        } else {
          LOGGER.info(String.format(
              "Skipping restoration of [%s] either because the namespace could not be found, or was filtered out.",
              namespaceName));
        }
      });

      if (!namespacesRestored.isEmpty()) {
        indexingService.reindexAllModels();
      }

      return namespacesRestored;

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
        String namespace = entry.getName().substring(entry.getName().lastIndexOf("/") + 1)
            .replace(".xml", "");
        backups.put(namespace, ZipUtils.copyStream(zis, entry));
      }
    }

    return backups;
  }

}
