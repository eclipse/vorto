/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
