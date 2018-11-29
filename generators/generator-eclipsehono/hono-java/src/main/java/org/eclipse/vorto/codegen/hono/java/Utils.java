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
