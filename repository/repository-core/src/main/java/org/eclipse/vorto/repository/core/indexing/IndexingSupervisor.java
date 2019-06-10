package org.eclipse.vorto.repository.core.indexing;

import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class IndexingSupervisor implements ApplicationListener<AppEvent>  {
  
  @Autowired
  private IIndexingService indexingService;
  
  @Override
  public void onApplicationEvent(AppEvent event) {
    if (event.getEventType() == EventType.MODEL_CREATED) {
      ModelInfo modelInfo = (ModelInfo) event.getSubject();
      indexingService.indexModel(modelInfo, event.getUserContext());
    }
  }

}
