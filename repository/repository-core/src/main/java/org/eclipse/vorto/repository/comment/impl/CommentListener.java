package org.eclipse.vorto.repository.comment.impl;

import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CommentListener implements ApplicationListener<AppEvent> {

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
