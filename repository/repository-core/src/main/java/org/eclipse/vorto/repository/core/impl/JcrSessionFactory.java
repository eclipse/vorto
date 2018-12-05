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

import javax.annotation.PreDestroy;
import javax.jcr.Repository;
import javax.jcr.Session;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Component
public class JcrSessionFactory implements FactoryBean<Session> {

  @Autowired
  private Repository repository;
  private Session session;

  @Override
  public Session getObject() throws Exception {
    if (session == null) {
      session = repository.login();
    }
    return session;
  }

  @Override
  public Class<?> getObjectType() {
    return Session.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @PreDestroy
  public void logout() throws Exception {
    session.logout();
    session = null;
  }
}
