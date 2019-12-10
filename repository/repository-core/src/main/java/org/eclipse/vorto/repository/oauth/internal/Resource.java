package org.eclipse.vorto.repository.oauth.internal;

public class Resource {

  public enum Type {
    ModelId, Namespace
  }

  private String name;
  private Type type;

  public static Resource modelId(String modelId) {
    return new Resource(modelId, Type.ModelId);
  }
  
  public static Resource namespace(String namespace) {
    return new Resource(namespace, Type.Namespace);
  }
  
  private Resource(String name, Type type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
