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
package org.eclipse.vorto.repository.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.eclipse.vorto.repository.client.ModelContent;
import org.eclipse.vorto.repository.client.ModelInfo;
import org.eclipse.vorto.repository.client.ModelQueryException;
import org.eclipse.vorto.repository.client.RepositoryClientException;
import org.eclipse.vorto.repository.client.attachment.Attachment;
import org.eclipse.vorto.repository.client.generation.GeneratedOutput;
import org.eclipse.vorto.repository.client.generation.GeneratorInfo;
import com.google.gson.reflect.TypeToken;

public class DefaultRepositoryClient extends ImplementationBase implements IRepositoryClient {

  private static final String REST_BASE = "api/v1/generators";
  private static final String REST_SEARCH_BASE = "api/v1/search/models";
  private static final String REST_MODEL_BASE = "api/v1/models";
  private static final String REST_ATTACHMENT_BASE = "api/v1/attachments";

  public DefaultRepositoryClient(HttpClient httpClient, RequestContext context) {
    super(httpClient, context);
  }

  @Override
  public Set<String> getAvailableGeneratorKeys() {
    return getAllGenerators(generators -> {
      return generators.stream().map(generator -> generator.getKey()).collect(Collectors.toSet());
    });
  }

  public List<GeneratorInfo> getAvailableGenerators() {
    return getAllGenerators(Function.identity());
  }

  @Override
  public GeneratorInfo getInfo(String generatorKey) {
    Objects.requireNonNull(generatorKey);

    return getAllGenerators(generators -> generators.stream()
        .filter(generator -> generator.getKey().equals(generatorKey)).findFirst().orElse(null));
  }

  private <K> K getAllGenerators(Function<List<GeneratorInfo>, K> converter) {
    Objects.requireNonNull(converter);

    String getAllGeneratorsUrl =
        String.format("%s/%s", getRequestContext().getBaseUrl(), String.format(REST_BASE));

    return requestAndTransform(getAllGeneratorsUrl,
        converter.compose(transformToType(new TypeToken<ArrayList<GeneratorInfo>>() {}.getType())),
        () -> converter.apply(Collections.emptyList()));
  }

  @Override
  public GeneratedOutput generate(ModelId modelId, String generatorKey,
      Map<String, String> invocationParams) {
    Objects.requireNonNull(modelId);
    Objects.requireNonNull(generatorKey);

    String generateUrl =
        getGeneratorUrl(getRequestContext().getBaseUrl(), modelId, generatorKey, invocationParams);

    return requestAndTransform(generateUrl, this::getGeneratedOutput);
  }

  private GeneratedOutput getGeneratedOutput(HttpResponse response) {
    try {
      String contentDisposition = response.getFirstHeader("Content-Disposition").getValue();
      String filename = contentDisposition
          .substring(contentDisposition.indexOf("filename = ") + "filename = ".length());
      long length = response.getEntity().getContentLength();
      byte[] content = IOUtils.toByteArray(response.getEntity().getContent());
      return new GeneratedOutput(content, filename, length);
    } catch (IOException e) {
      throw new RepositoryClientException("Error in converting response to GeneratedOutput", e);
    }
  }

  private String getGeneratorUrl(String baseUrl, ModelId modelId, String generatorKey,
      Map<String, String> invocationParams) {
    try {
      StringBuilder url = new StringBuilder();

      url.append(baseUrl).append("/" + String.format(REST_BASE) + "/").append(generatorKey)
          .append("/models/").append(modelId.getPrettyFormat())
          .append(URLEncoder.encode(generatorKey, "utf-8"));

      if (invocationParams != null && !invocationParams.isEmpty()) {
        StringJoiner joiner = new StringJoiner("&");
        invocationParams.forEach((key, value) -> {
          joiner.add(key + "=" + value);
        });
        url.append("?").append(joiner.toString());
      }

      return url.toString();
    } catch (UnsupportedEncodingException e) {
      throw new RepositoryClientException("Error in generating URL for the generator", e);
    }
  }

  @Override
  public Collection<ModelInfo> search(String expression) {
    try {
      String url = String.format("%s/%s?expression=%s", getRequestContext().getBaseUrl(),
          String.format(REST_SEARCH_BASE), URLEncoder.encode(expression, "utf-8"));
      return requestAndTransform(url, transformToType(new TypeToken<ArrayList<ModelInfo>>() {}.getType()));
    } catch (UnsupportedEncodingException e) {
      throw new ModelQueryException("Error encoding the query", e);
    } 
  }

  @Override
  public ModelInfo getById(ModelId modelId) {
    String url = String.format("%s/%s/%s", getRequestContext().getBaseUrl(),
        String.format(REST_MODEL_BASE), modelId.getPrettyFormat());
    return requestAndTransform(url, transformToClass(ModelInfo.class));
  }

  @Override
  public ModelContent getContent(ModelId modelId) {
    String url = String.format("%s/%s/%s/content", getRequestContext().getBaseUrl(),
        String.format(REST_MODEL_BASE), modelId.getPrettyFormat());
    return requestAndTransform(url, transformToClass(ModelContent.class));
  }

  @Override
  public ModelContent getContent(ModelId modelId, String targetPlatformKey) {
    String url = String.format("%s/%s/%s/content/%s", getRequestContext().getBaseUrl(),
        String.format(REST_MODEL_BASE), modelId.getPrettyFormat(), targetPlatformKey);
    return requestAndTransform(url, transformToClass(ModelContent.class));
  }

  @Override
  public ModelContent getContent(ModelId modelId, ModelId mappingModelId) {
    String url = String.format("%s/%s/%s/content/mappings/%s", getRequestContext().getBaseUrl(),
        String.format(REST_MODEL_BASE), modelId.getPrettyFormat(),
        mappingModelId.getPrettyFormat());
    return requestAndTransform(url, transformToClass(ModelContent.class));
  }

  @Override
  public List<Attachment> getAttachments(ModelId modelId) {
    String url = String.format("%s/%s/%s", getRequestContext().getBaseUrl(),
        String.format(REST_ATTACHMENT_BASE), modelId.getPrettyFormat());
    return requestAndTransform(url,
        transformToType(new TypeToken<ArrayList<Attachment>>() {}.getType()));
  }

  @Override
  public byte[] downloadAttachment(ModelId modelId, String filename) {
    String url = String.format("%s/%s/%s/files/%s", getRequestContext().getBaseUrl(),
        String.format(REST_ATTACHMENT_BASE), modelId.getPrettyFormat(), filename);
    return requestAndTransform(url, (response) -> {
      try {
        return IOUtils.toByteArray(response.getEntity().getContent());
      } catch (UnsupportedOperationException | IOException e) {
        throw new RepositoryClientException(
            "Error while getting attachment + '" + filename + "' with url '" + url + "'", e);
      }
    });
  }
}
