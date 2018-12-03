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
