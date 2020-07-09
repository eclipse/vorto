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
package org.eclipse.vorto.repository.server.config.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class RepositoryCachingConfiguration {

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(
        Arrays.asList(
            new ConcurrentMapCache("userRepositoryRolesCache"),
            new ConcurrentMapCache("namespaceRolesCache"),
            new ConcurrentMapCache("repositoryRolesCache"),
            new ConcurrentMapCache("privilegesCache"),
            new ConcurrentMapCache("namespaceRoleCache"),
            new ConcurrentMapCache("repositoryRoleCache"),
            new ConcurrentMapCache("privilegeCache")
        )
    );
    return cacheManager;
  }
}
