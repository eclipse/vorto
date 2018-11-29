package org.eclipse.vorto.repository.web.core.dto;

public class LWM2MQuery extends ResolveQuery {

  public LWM2MQuery(String attributeValue) {
    super("lwm2m", "ObjectID", attributeValue, "Object");
  }

}
