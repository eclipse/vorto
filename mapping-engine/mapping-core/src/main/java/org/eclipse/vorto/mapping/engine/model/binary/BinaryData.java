package org.eclipse.vorto.mapping.engine.model.binary;

public class BinaryData {

  private byte[] data;

  public BinaryData(byte[] data) {
    this.data = data;
  }

  public BinaryData() {

  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
