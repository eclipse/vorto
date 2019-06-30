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
package org.eclipse.vorto.repository.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

  private static final char NEWLINE = '\n';
  
  final static Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
      traceRequest(request, body);
      ClientHttpResponse response = execution.execute(request, body);
      traceResponse(response);
      return response;
  }

  private void traceRequest(HttpRequest request, byte[] body) throws IOException {
      log.debug("===========================request begin================================================");
      log.debug("URI         : {}", request.getURI());
      log.debug("Method      : {}", request.getMethod());
      log.debug("Headers     : {}", request.getHeaders() );
      log.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
      log.debug("==========================request end================================================");
  }

  private void traceResponse(ClientHttpResponse response) throws IOException {
      StringBuilder inputStringBuilder = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
      String line = bufferedReader.readLine();
      while (line != null) {
          inputStringBuilder.append(line);
          inputStringBuilder.append(NEWLINE);
          line = bufferedReader.readLine();
      }
      log.debug("============================response begin==========================================");
      log.debug("Status code  : {}", response.getStatusCode());
      log.debug("Status text  : {}", response.getStatusText());
      log.debug("Headers      : {}", response.getHeaders());
      log.debug("Response body: {}", inputStringBuilder.toString());
      log.debug("=======================response end=================================================");
  }
}
