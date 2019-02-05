/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.mapping.engine.model.blegatt;

import java.util.List;

public class GattDevice {

  private String modelNumber;
  private List<GattService> services;
  private List<GattCharacteristic> characteristics;

  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public List<GattService> getServices() {
    return services;
  }

  public void setServices(List<GattService> services) {
    this.services = services;
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
    result = prime * result + ((modelNumber == null) ? 0 : modelNumber.hashCode());
    result = prime * result + ((services == null) ? 0 : services.hashCode());
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
    GattDevice other = (GattDevice) obj;
    if (characteristics == null) {
      if (other.characteristics != null)
        return false;
    } else if (!characteristics.equals(other.characteristics))
      return false;
    if (modelNumber == null) {
      if (other.modelNumber != null)
        return false;
    } else if (!modelNumber.equals(other.modelNumber))
      return false;
    if (services == null) {
      if (other.services != null)
        return false;
    } else if (!services.equals(other.services))
      return false;
    return true;
  }


}
