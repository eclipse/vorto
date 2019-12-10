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
package org.eclipse.vorto.repository.oauth;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.vorto.repository.oauth.internal.Resource;

public class ResourceIdentificationHelper {
  
  private static final Pattern modelIdPattern = Pattern.compile("[a-zA-Z0-9_\\.]+:[a-zA-Z0-9_]+:[a-zA-Z0-9\\.\\-]+");
  
  private static final Pattern namespacePattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)+");
  
  public static Optional<Resource> identifyResource(String resourceUrl) {
    // find a modelId
    Matcher matcher = modelIdPattern.matcher(resourceUrl);
    if (matcher.find()) {
      return Optional.ofNullable(Resource.modelId(matcher.group()));
    }
    
    // find a namespace
    matcher = namespacePattern.matcher(resourceUrl);
    if (matcher.find()) {
      return Optional.ofNullable(Resource.namespace(matcher.group()));
    }
    
    return Optional.empty();
  }
  
}
