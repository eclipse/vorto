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
package org.eclipse.vorto.repository.search;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class IndexingEventListener implements ApplicationListener<AppEvent>  {
  
  private IIndexingService indexingService;
  
  public IndexingEventListener(@Autowired IIndexingService indexingService) {
    this.indexingService = indexingService;
  }

  @Override
  public void onApplicationEvent(AppEvent event) {
    if (event.getEventType() == EventType.MODEL_CREATED) {
      ModelInfo modelInfo = (ModelInfo) event.getSubject();
      indexingService.indexModel(modelInfo, event.getUserContext().getWorkspaceId());
    } else if (event.getEventType() == EventType.MODEL_UPDATED) {
      ModelInfo modelInfo = (ModelInfo) event.getSubject();
      indexingService.updateIndex(modelInfo);
    } else if (event.getEventType() == EventType.MODEL_DELETED) {
      ModelId modelId = (ModelId) event.getSubject();
      indexingService.deleteIndex(modelId);
    } else if (event.getEventType() == EventType.NAMESPACE_DELETED) {
      indexingService.deleteIndexForWorkspace(event.getUserContext().getWorkspaceId());
    }
  }

}
