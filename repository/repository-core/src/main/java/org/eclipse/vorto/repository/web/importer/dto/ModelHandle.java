/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web.importer.dto;

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
