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
package org.eclipse.vorto.codegen.hono.java;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class Utils {

  public static String getJavaPackageBasePath(InformationModel context) {
    return getBasePath(context) + "/src/main/java/device/" + context.getName().toLowerCase();
  }

  public static String getJavaPackage(InformationModel context) {
    return "device." + context.getName().toLowerCase();
  }

  public static String getBasePath(InformationModel context) {
    return "/" + context.getName().toLowerCase() + "-app";
  }
}
