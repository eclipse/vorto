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
package org.eclipse.vorto.repository.comment.impl;

import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CommentAnonymizationListener implements ApplicationListener<AppEvent> {

  private static final String ANONYMOUS = "anonymous";
  
  @Autowired
  private CommentRepository commentRepository;
  
  @Override
  public void onApplicationEvent(AppEvent event) {
    if (event.getEventType() == EventType.USER_DELETED) {
      String userId = (String) event.getSubject();
      
      commentRepository.findByAuthor(userId).forEach(comment -> {
        comment.setAuthor(ANONYMOUS);
        commentRepository.save(comment);
      });
    }
  }

}
