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
package org.eclipse.vorto.repository.web.api.v1;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.plugin.generator.GeneratedOutput;
import org.eclipse.vorto.repository.plugin.generator.IGeneratorPluginService;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class AbstractGeneratorController extends AbstractRepositoryController {

  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";

  @Autowired
  protected IGeneratorPluginService generatorService;

  @Autowired
  protected NamespaceService namespaceService;

  protected void generateAndWriteToOutputStream(String modelId, String pluginKey, HttpServletRequest request, HttpServletResponse response) {
    generateAndWriteToOutputStream(modelId, pluginKey, getRequestParams(request), response);
  }

  protected void generateAndWriteToOutputStream(String modelId, String pluginKey, Map<String, String> params, HttpServletResponse response) {
    ModelId modelIdToGen = ModelId.fromPrettyFormat(modelId);

    try {
      GeneratedOutput generatedOutput = generatorService.generate(getUserContext(modelIdToGen),
          modelIdToGen, URLDecoder.decode(pluginKey, "utf-8"), params);
      writeToResponse(response, generatedOutput);
    } catch (IOException e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }

  protected IUserContext getUserContext(ModelId modelId) {
    String workspaceId;
    try {
      Namespace namespace = namespaceService.getByName(modelId.getNamespace());
      workspaceId = namespace.getWorkspaceId();
    } catch (DoesNotExistException e) {
      throw new ModelNotFoundException("The namespace for '" + modelId + "' could not be found.");
    }
    return UserContext.user(SecurityContextHolder.getContext().getAuthentication(),
            workspaceId);
  }

  protected void writeToResponse(final HttpServletResponse response, GeneratedOutput generatedOutput)
      throws IOException {
    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + generatedOutput.getFileName());
    response.setContentLengthLong(generatedOutput.getSize());
    response.setContentType(APPLICATION_OCTET_STREAM);

    IOUtils.copy(new ByteArrayInputStream(generatedOutput.getContent()),
        response.getOutputStream());
    response.flushBuffer();
  }

  protected Map<String, String> getRequestParams(final HttpServletRequest request) {
    Map<String, String> requestParams = new HashMap<>();
    request.getParameterMap().forEach((key, value) -> requestParams.put(key, value[0]));
    return requestParams;
  }

}
