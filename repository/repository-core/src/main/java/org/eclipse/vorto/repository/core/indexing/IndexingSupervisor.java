package org.eclipse.vorto.repository.core.indexing;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class IndexingSupervisor implements ApplicationListener<AppEvent>  {
  
  private IIndexingService indexingService;
  
  public IndexingSupervisor(@Autowired IIndexingService indexingService) {
    this.indexingService = indexingService;
  }

  @Override
  public void onApplicationEvent(AppEvent event) {
    if (event.getEventType() == EventType.MODEL_CREATED) {
      ModelInfo modelInfo = (ModelInfo) event.getSubject();
      indexingService.indexModel(modelInfo, event.getUserContext().getTenant());
    } else if (event.getEventType() == EventType.MODEL_UPDATED) {
      ModelInfo modelInfo = (ModelInfo) event.getSubject();
      indexingService.updateIndex(modelInfo);
    } else if (event.getEventType() == EventType.MODEL_DELETED) {
      ModelId modelId = (ModelId) event.getSubject();
      indexingService.deleteIndex(modelId);
    } else if (event.getEventType() == EventType.TENANT_DELETED) {
      indexingService.deleteIndexForTenant(event.getUserContext().getTenant());
    }
  }

}
