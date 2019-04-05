package org.eclipse.vorto.repository.core.events;

import org.springframework.context.ApplicationEvent;

public class AppEvent extends ApplicationEvent {

  /**
   * 
   */
  private static final long serialVersionUID = 6573745642902228283L;

  private Object subject;

  private EventType eventType;

  public AppEvent(Object source, Object subject, EventType eventType) {
    super(source);
    this.subject = subject;
    this.eventType = eventType;
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
}
