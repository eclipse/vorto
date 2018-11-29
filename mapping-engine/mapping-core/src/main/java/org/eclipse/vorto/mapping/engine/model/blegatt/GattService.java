package org.eclipse.vorto.mapping.engine.model.blegatt;

import java.util.List;

public class GattService {

  private String uuid;
  private List<GattCharacteristic> characteristics;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public List<GattCharacteristic> getCharacteristics() {
    return characteristics;
  }

  public void setCharacteristics(List<GattCharacteristic> characteristics) {
    this.characteristics = characteristics;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((characteristics == null) ? 0 : characteristics.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GattService other = (GattService) obj;
    if (characteristics == null) {
      if (other.characteristics != null)
        return false;
    } else if (!characteristics.equals(other.characteristics))
      return false;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }



}
