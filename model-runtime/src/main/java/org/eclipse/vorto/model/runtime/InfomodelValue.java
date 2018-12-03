/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.model.runtime;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelProperty;

public class InfomodelValue implements IValidatable {

  private Infomodel meta = null;

  private Map<String, FunctionblockValue> functionblocks = new HashMap<>();

  public InfomodelValue(Infomodel meta) {
    this.meta = meta;
  }

  public void withFunctionblock(String propertyName, FunctionblockValue data) {
    this.functionblocks.put(propertyName, data);
  }

  public Map<String, FunctionblockValue> getProperties() {
    return functionblocks;
  }

  public FunctionblockValue get(String fbProperty) {
    return functionblocks.get(fbProperty);
  }

  @Override
  public String toString() {
    return "InfomodelData [functionblocks=" + functionblocks + "]";
  }

  @Override
  public ValidationReport validate() {
    ValidationReport report = new ValidationReport();
    for (ModelProperty fbProperty : meta.getFunctionblocks()) {
      if (fbProperty.isMandatory() && !functionblocks.containsKey(fbProperty.getName())) {
        report.addItem(fbProperty, "Mandatory property is missing!");
      } else {
        FunctionblockValue fbData = functionblocks.get(fbProperty.getName());
        if (fbData != null) {
          ValidationReport fbReport = fbData.validate();
          report.addReport(fbReport);
        }

      }
    }
    return report;
  }

  public Map<String, Object> serialize() {
    Map<String, Object> result = new HashMap<String, Object>();
    for (String fbProperty : functionblocks.keySet()) {
      result.put(fbProperty, functionblocks.get(fbProperty).serialize());
    }
    return result;
  }

}
