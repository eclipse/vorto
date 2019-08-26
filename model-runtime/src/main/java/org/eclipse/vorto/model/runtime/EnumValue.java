package org.eclipse.vorto.model.runtime;

import org.eclipse.vorto.model.EnumModel;

public class EnumValue {

  private EnumModel meta;
  private String value;
  
  public EnumValue(EnumModel meta) {
    super();
    this.meta = meta;
  }
  
  public EnumModel getMeta() {
    return meta;
  }
  public void setMeta(EnumModel meta) {
    this.meta = meta;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    if (!meta.getLiterals().stream().filter(literal -> literal.getName().equals(value)).findAny().isPresent()) {
      throw new IllegalArgumentException(value + " is not defined as literal in Enumeration");
    }
    this.value = value;
  }
  
  public Object serialize() {
    return value;
  }
  
}
