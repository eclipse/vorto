/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.plugin.generator.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.repository.conversion.ModelIdToModelContentConverter;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.Tag;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.plugin.generator.GeneratedOutput;
import org.eclipse.vorto.repository.plugin.generator.GenerationException;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.eclipse.vorto.repository.plugin.generator.IGeneratorPluginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Invokes remote Code Generator either of API Version 1 or 2
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultGeneratorPluginService implements IGeneratorPluginService {

  private Map<String, GeneratorPluginConfiguration> generatorsPlugins = new HashMap<>();

  @Autowired
  private IGeneratorMetrics generatorMetrics;

  @Autowired
  private IModelRepositoryFactory modelRepositoryFactory;

  @Autowired
  private RestTemplate restTemplate = null;

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGeneratorPluginService.class);

  private static final Function<FileContent, GeneratedOutput> MAP_TO_OUTPUT =
      f -> new GeneratedOutput(f.getContent(), f.getFileName(), f.getSize());

  public DefaultGeneratorPluginService() {
  }

  public void registerPlugin(GeneratorPluginConfiguration plugin) {
    this.generatorsPlugins.put(plugin.getKey(), plugin);
    if (generatorMetrics.findByGeneratorKey(plugin.getKey()) == null) {
      generatorMetrics.save(new GeneratorMetric(plugin.getKey()));
    }
  }

  @Override
  public Set<String> getKeys() {
    return generatorsPlugins.keySet();
  }

  @Override
  public GeneratorPluginConfiguration getPluginInfo(String serviceKey, boolean includeConfigUI) {
    GeneratorPluginConfiguration plugin = this.generatorsPlugins.get(serviceKey);
    if (plugin.getName() == null) {
      GeneratorPluginConfiguration pluginLoadedFromServer = loadFromRemote(plugin);
      this.generatorsPlugins.put(serviceKey, pluginLoadedFromServer);
      plugin = pluginLoadedFromServer;
    }

    plugin.setAmountOfDownloads(
        this.generatorMetrics.findByGeneratorKey(serviceKey).getInvocationCount());
    
    return plugin;
  }

  @Override
  public GeneratedOutput generate(IUserContext userContext, ModelId modelId, String serviceKey,
      Map<String, String> requestParams) {
    increaseMetric(serviceKey);
    IModelRepository repository = modelRepositoryFactory.getRepositoryByModel(modelId);
    ModelInfo modelInfo = repository.getById(modelId);
    if (modelInfo.isReleased()) {
      List<Attachment> attachments = repository
          .getAttachmentsByTags(modelId,
              Sets.newHashSet(tagsForRequest(serviceKey, requestParams)));
      if (Objects.nonNull(attachments) && attachments.size() == 1) {
        return attachments.stream()
            .findFirst()
            .map(Attachment::getFilename)
            .flatMap(fileName -> repository.getAttachmentContent(modelId, fileName))
            .map(MAP_TO_OUTPUT)
            .orElseGet(() -> doGenerate(userContext, modelInfo, serviceKey, requestParams));
      }
    }
    return doGenerate(userContext, modelInfo, serviceKey, requestParams);
  }

  private GeneratorPluginConfiguration loadFromRemote(GeneratorPluginConfiguration plugin) {
    if (plugin.isApiVersion("1")) {
      ResponseEntity<GeneratorPluginInfoV1> response = restTemplate.getForEntity(
          plugin.getEndpointUrl()
              + "/rest/generators/{pluginkey}/generate/info?includeConfigUI=true",
          GeneratorPluginInfoV1.class, plugin.getKey());
      return GeneratorPluginConfiguration.of(response.getBody(), plugin.getEndpointUrl(),
          plugin.getTags());
    } else {
      ResponseEntity<GeneratorPluginInfo> response = restTemplate.getForEntity(
          plugin.getEndpointUrl() + "/api/2/plugins/generators/{pluginkey}/info",
          GeneratorPluginInfo.class, plugin.getKey());

      return GeneratorPluginConfiguration.of(response.getBody(), plugin.getEndpointUrl(),
          plugin.getTags());
    }
  }

  private void increaseMetric(String serviceKey) {
    GeneratorMetric generatorEntity = Optional
        .ofNullable(generatorMetrics.findByGeneratorKey(serviceKey))
        .orElseThrow(() -> new GenerationException("Generator plugin with key " + serviceKey
            + " is not found in metrics database"));
    generatorEntity.increaseInvocationCount();
    generatorMetrics.save(generatorEntity);
  }

  private GeneratedOutput doGenerate(IUserContext userContext, ModelInfo modelInfo,
      String serviceKey,
      Map<String, String> requestParams) {
    GeneratorPluginConfiguration plugin = getPluginInfo(serviceKey, false);
    GeneratedOutput generatedOutput;
    if (plugin.isApiVersion("2")) {
      generatedOutput = doGenerateWithApiVersion2(modelInfo.getId(), serviceKey, requestParams,
          plugin.getEndpointUrl());
    } else {
      generatedOutput = doGenerateWithApiVersion1(modelInfo, serviceKey, requestParams,
          plugin.getEndpointUrl());
    }
    attachGeneratedOutput(userContext, modelInfo.getId(), serviceKey, requestParams,
        generatedOutput);
    return generatedOutput;
  }

  private GeneratedOutput doGenerateWithApiVersion2(ModelId modelId,
      String serviceKey, Map<String, String> requestParams, String baseUrl) {

    ModelIdToModelContentConverter converter =
        new ModelIdToModelContentConverter(this.modelRepositoryFactory);
    ModelContent content = converter.convert(modelId, Optional.empty());
    
    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Generating with V2. Sending following json content {}",
            new ObjectMapper().writeValueAsString(content));
      }
    } catch (JsonProcessingException e) {
      LOGGER.trace("Error processing JSON for logging", e);
    }

    ResponseEntity<byte[]> response = restTemplate.exchange(
        baseUrl + "/api/2/plugins/generators/{pluginkey}" + attachRequestParams(requestParams),
        HttpMethod.PUT, new HttpEntity<>(content), byte[].class, serviceKey);

    return new GeneratedOutput(response.getBody(), extractFileNameFromHeader(response),
        response.getHeaders().getContentLength());
  }

  private GeneratedOutput doGenerateWithApiVersion1(ModelInfo modelInfo,
      String serviceKey, Map<String, String> requestParams, String baseUrl) {
    if (modelInfo == null) {
      throw new ModelNotFoundException("Model with the given ID does not exist", null);
    }

    if (modelInfo.getType() == ModelType.Datatype
        || modelInfo.getType() == ModelType.Mapping) {
      throw new GenerationException(
          "Provided model is neither an information model nor a function block model!");
    }

    HttpEntity<String> entity = getUserToken().map(token -> {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + token);
      return new HttpEntity<>("parameters", headers);
    }).orElse(null);
    ModelId modelId = modelInfo.getId();
    ResponseEntity<byte[]> response = restTemplate.exchange(
        baseUrl + "/rest/generators/{pluginkey}/generate/{namespace}/{name}/{version}"
            + attachRequestParams(requestParams),
        HttpMethod.GET, entity, byte[].class, serviceKey, modelId.getNamespace(), modelId.getName(),
        modelId.getVersion());

    return new GeneratedOutput(response.getBody(), extractFileNameFromHeader(response),
        response.getHeaders().getContentLength());
  }

  private void attachGeneratedOutput(IUserContext userContext, ModelId modelId, String serviceKey,
      Map<String, String> requestParams, GeneratedOutput response) {
    FileContent fc = new FileContent("generated.json", response.getContent());
    modelRepositoryFactory.getRepositoryByModel(modelId)
        .attachFile(modelId, fc, userContext, tagsForRequest(serviceKey, requestParams));
  }

  private Tag[] tagsForRequest(String serviceKey, Map<String, String> requestParams) {
    Tag[] tags = requestParams.values()
        .stream()
        .map(Tag::new)
        .collect(Collectors.toSet())
        .toArray(new Tag[requestParams.size() + 1]);
    tags[tags.length - 1] = new Tag(serviceKey, serviceKey);
    return tags;
  }

  private Optional<String> getUserToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2Authentication) {
      if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
        OAuth2AuthenticationDetails details =
            (OAuth2AuthenticationDetails) authentication.getDetails();
        return Optional.ofNullable(details.getTokenValue());
      }
    }
    return Optional.empty();
  }

  private String attachRequestParams(Map<String, String> requestParams) {
    if (requestParams.isEmpty()) {
      return "";
    } else {
      StringBuilder requestUrlParams = new StringBuilder("?");
      for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
        String key = iter.next();
        requestUrlParams.append(key);
        requestUrlParams.append("=");
        requestUrlParams.append(requestParams.get(key));
        if (iter.hasNext()) {
          requestUrlParams.append("&");
        }
      }
      return requestUrlParams.toString();
    }
  }

  private String extractFileNameFromHeader(ResponseEntity<byte[]> entity) {
    List<String> values = entity.getHeaders().get("content-disposition");
    if (values.size() > 0) {
      int indexOfFileNameStart = values.get(0).indexOf("=");
      return values.get(0).substring(indexOfFileNameStart + 1);
    }
    return "generated.output";
  }

  @Override
  public Collection<GeneratorPluginConfiguration> getMostlyUsed(int top) {
    List<GeneratorMetric> topResult = new ArrayList<>();

    for (GeneratorMetric entity : this.generatorMetrics.findByClassifier("platform")) {
      topResult.add(entity);
    }

    topResult.sort(new Comparator<GeneratorMetric>() {

      @Override
      public int compare(GeneratorMetric o1, GeneratorMetric o2) {
        if (o1.getInvocationCount() > o2.getInvocationCount()) {
          return -1;
        } else if (o1.getInvocationCount() < o2.getInvocationCount()) {
          return +1;
        } else {
          return 0;
        }
      }
    });

    List<GeneratorPluginConfiguration> result = new ArrayList<>(top);
    int counter = 0;
    for (GeneratorMetric entity : topResult) {
      if (counter < top) {
        try {
          result.add(getPluginInfo(entity.getKey(), false));
          counter++;
        } catch (Throwable t) {
          LOGGER.warn("Generator " + entity.getKey()
              + " appears to be offline or not deployed. Skipping...");
        }

      }
    }

    return result;
  }

  public IGeneratorMetrics getGeneratorMetrics() {
    return generatorMetrics;
  }

  public void setGeneratorMetrics(IGeneratorMetrics generatorMetrics) {
    this.generatorMetrics = generatorMetrics;
  }

  public IModelRepositoryFactory getModelRepositoryFactory() {
    return modelRepositoryFactory;
  }

  public void setModelRepositoryFactory(IModelRepositoryFactory modelRepositoryFactory) {
    this.modelRepositoryFactory = modelRepositoryFactory;
  }

  /**
   * Removes the cached plugin meta data for the given plugin key
   * 
   * @param pluginKey
   */
  public void clearPluginCache(String pluginKey) {
    generatorsPlugins.remove(pluginKey);
  }

}
