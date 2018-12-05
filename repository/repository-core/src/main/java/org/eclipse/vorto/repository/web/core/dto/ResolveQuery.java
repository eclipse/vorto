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
package org.eclipse.vorto.repository.web.core.dto;

public class ResolveQuery {

  private String targetPlatformKey;
  private String attributeId;
  private String attributeValue;
  private String stereoType;

  public ResolveQuery(String targetPlatformKey, String attributeId, String attributeValue,
      String stereoType) {
    super();
    this.targetPlatformKey = targetPlatformKey;
    this.attributeId = attributeId;
    this.attributeValue = attributeValue;
    this.stereoType = stereoType;
  }

  public String getTargetPlatformKey() {
    return targetPlatformKey;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public String getAttributeValue() {
    return attributeValue;
  }

  public String getStereoType() {
    return stereoType;
  }



}
