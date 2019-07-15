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
package org.eclipse.vorto.repository.core.events;

import org.eclipse.vorto.repository.core.IUserContext;
import org.springframework.context.ApplicationEvent;

public class AppEvent extends ApplicationEvent {

  /**
   * 
   */
  private static final long serialVersionUID = 6573745642902228283L;

  private Object subject;

  private IUserContext userContext;
  
  private EventType eventType;

  public AppEvent(Object source, Object subject, EventType eventType) {
    super(source);
    this.subject = subject;
    this.eventType = eventType;
  }
  
  public AppEvent(Object source, Object subject, IUserContext userContext, EventType eventType) {
    this(source, subject, eventType);
    this.userContext = userContext;
  }

  public Object getSubject() {
    return subject;
  }

  public void setSubject(Object subject) {
    this.subject = subject;
  }

  public EventType getEventType() {
    return eventType;
  }

  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }

  public IUserContext getUserContext() {
    return userContext;
  }

  public void setUserContext(IUserContext userContext) {
    this.userContext = userContext;
  }
}
