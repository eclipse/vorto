/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.client.generation;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.vorto.model.ModelId;

public class GeneratorUrlBuilder {

  private static final String REST_BASE = "api/v1/generators";

  private GeneratorUrlBuilder() {
    // no instantiation required - only static methods
  }

  public static String getGeneratorUrl(String baseUrl, ModelId modelId, String generatorKey,
      Map<String, String> invocationParams) {
    StringBuilder url = new StringBuilder().append(baseUrl)
        .append("/")
        .append(REST_BASE)
        .append("/")
        .append(generatorKey)
        .append("/models/")
        .append(modelId.getPrettyFormat());

    if (Objects.nonNull(invocationParams) && !invocationParams.isEmpty()) {
      StringJoiner joiner = new StringJoiner("&");
      invocationParams.forEach((key, value) -> joiner.add(key + "=" + value));
      url.append("?").append(joiner.toString());
    }

    return url.toString();
  }

  public static String getAllGeneratorsUrl(String baseUrl) {
    return String.format("%s/%s", baseUrl, REST_BASE);
  }

}
