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
package org.eclipse.vorto.repository.core.impl;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Repository;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Component
public class JcrRepositoryFactory implements FactoryBean<Repository> {

  private static final Logger LOG = LoggerFactory.getLogger(JcrRepositoryFactory.class);
  private static final ModeShapeEngine ENGINE = new ModeShapeEngine();

  private Repository repository;

  @Autowired
  private RepositoryConfiguration repositoryConfiguration;

  @PostConstruct
  public void start() throws Exception {
    LOG.debug("Starting Vorto Modeshape Repository");
    ENGINE.start();
    repository = ENGINE.deploy(repositoryConfiguration);
    ENGINE.startRepository(repositoryConfiguration.getName());
  }

  @PreDestroy
  public void stop() throws Exception {
    try {
      ENGINE.shutdown().get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOG.error("Error while waiting for the ModeShape engine to shutdown", e);
    }
  }

  @Override
  public Repository getObject() throws Exception {
    return repository;
  }

  @Override
  public Class<?> getObjectType() {
    return javax.jcr.Repository.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
