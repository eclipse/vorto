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
package org.eclipse.vorto.repository.web.api.v1.dto;

import org.eclipse.vorto.model.ModelId;

/**
 * Model class to hold file handle id and associated model details.
 *
 */
public class ModelHandle {

  private String handleId;
  private ModelId id;

  public String getHandleId() {
    return handleId;
  }

  public void setHandleId(String handleId) {
    this.handleId = handleId;
  }

  public ModelId getId() {
    return id;
  }

  public void setId(ModelId id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((handleId == null) ? 0 : handleId.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    ModelHandle other = (ModelHandle) obj;
    if (handleId == null) {
      if (other.handleId != null)
        return false;
    } else if (!handleId.equals(other.handleId))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "ModelHandle [handleId=" + handleId + ", id=" + id + "]";
  }


}
