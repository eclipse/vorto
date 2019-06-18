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
package org.eclipse.vorto.codegen.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class ApiGatewayResponse {
  private final int statusCode;
  private final String body;
  private final Map<String, String> headers;
  private final boolean isBase64Encoded;

  public ApiGatewayResponse(int statusCode, String body, Map<String, String> headers, boolean isBase64Encoded) {
      this.statusCode = statusCode;
      this.body = body;
      this.headers = headers;
      this.isBase64Encoded = isBase64Encoded;
  }

  public int getStatusCode() {
      return statusCode;
  }

  public String getBody() {
      return body;
  }

  public Map<String, String> getHeaders() {
      return headers;
  }

  public boolean isIsBase64Encoded() {
      return isBase64Encoded;
  }

  public static Builder builder() {
      return new Builder();
  }

  public static class Builder {

      private static final Logger LOG = Logger.getLogger(ApiGatewayResponse.Builder.class);

      private static final ObjectMapper objectMapper = new ObjectMapper();

      private int statusCode = 200;
      private Map<String, String> headers = Collections.emptyMap();
      private String rawBody;
      private Object objectBody;
      private byte[] binaryBody;
      private boolean base64Encoded;

      public Builder setStatusCode(int statusCode) {
          this.statusCode = statusCode;
          return this;
      }

      public Builder setHeaders(Map<String, String> headers) {
          this.headers = headers;
          return this;
      }

      public Builder setRawBody(String rawBody) {
          this.rawBody = rawBody;
          return this;
      }

      public Builder setObjectBody(Object objectBody) {
          this.objectBody = objectBody;
          return this;
      }

      public Builder setBinaryBody(byte[] binaryBody) {
          this.binaryBody = binaryBody;
          setBase64Encoded(true);
          return this;
      }

      public Builder setBase64Encoded(boolean base64Encoded) {
          this.base64Encoded = base64Encoded;
          return this;
      }

      public ApiGatewayResponse build() {
          String body = null;
          if (rawBody != null) {
              body = rawBody;
          } else if (objectBody != null) {
              try {
                  body = objectMapper.writeValueAsString(objectBody);
              } catch (JsonProcessingException e) {
                  LOG.error("could not serialize object to json", e);
                  throw new RuntimeException(e);
              }
          } else if (binaryBody != null) {
              body = new String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8);
          }
          return new ApiGatewayResponse(statusCode, body, headers, base64Encoded);
      }
  }
}
