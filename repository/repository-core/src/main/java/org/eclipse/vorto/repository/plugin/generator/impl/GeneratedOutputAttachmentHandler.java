/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 * <p>
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.plugin.generator.impl;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.Tag;
import org.eclipse.vorto.repository.core.impl.PrivilegedUserContextProvider;
import org.eclipse.vorto.repository.plugin.generator.GeneratedOutput;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kolotu
 */
@Component
public class GeneratedOutputAttachmentHandler {

  private IModelRepositoryFactory modelRepositoryFactory;

  private static final Function<FileContent, GeneratedOutput> MAP_TO_GENERATED_OUTPUT =
      f -> new GeneratedOutput(f.getContent(), f.getFileName(), f.getSize());

  @Autowired
  public GeneratedOutputAttachmentHandler(IModelRepositoryFactory modelRepositoryFactory) {
    this.modelRepositoryFactory = modelRepositoryFactory;
  }

  Optional<GeneratedOutput> getGeneratedOutputFromAttachment(ModelInfo modelInfo,
      Map<String, String> requestParams, GeneratorPluginConfiguration plugin,
      IModelRepository repository) {

    List<Attachment> attachments = repository
        .getAttachmentsByTags(modelInfo.getId(),
            Sets.newHashSet(
                GeneratedOutputAttachmentHandler.tagsForRequest(plugin, requestParams)));
    if (Objects.nonNull(attachments)) {
      return attachments.stream()
          .findFirst()
          .map(Attachment::getFilename)
          .flatMap(fileName -> repository.getAttachmentContent(modelInfo.getId(), fileName))
          .map(MAP_TO_GENERATED_OUTPUT);
    }
    return Optional.empty();
  }

  GeneratedOutput attachGeneratedOutput(IUserContext userContext, ModelId modelId,
      String serviceKey, Map<String, String> requestParams, GeneratedOutput response,
      GeneratorPluginConfiguration plugin) {

    IUserContext userContextForAttaching = getUserContextForCreatingAttachment(userContext);
    String filename = buildAttachmentFilename(modelId, serviceKey, requestParams, response, plugin);
    FileContent fc = new FileContent(filename, response.getContent());
    modelRepositoryFactory.getRepositoryByModel(modelId, userContextForAttaching)
        .attachFile(modelId, fc, userContextForAttaching,
            GeneratedOutputAttachmentHandler.tagsForRequest(plugin, requestParams));
    return new GeneratedOutput(response.getContent(), filename, response.getSize());
  }

  private IUserContext getUserContextForCreatingAttachment(IUserContext userContext) {
    if (userContext.isAnonymous()) {
      return PrivilegedUserContextProvider.systemAdminContext();
    }
    return PrivilegedUserContextProvider.systemAdminContext(userContext.getUsername());
  }

  private String buildAttachmentFilename(ModelId modelId, String serviceKey,
      Map<String, String> requestParams, GeneratedOutput response,
      GeneratorPluginConfiguration plugin) {

    String fileExtension = getOutputFileExtension(response.getFileName());
    StringBuilder sb = new StringBuilder();
    sb.append("generated_").append(serviceKey).append('_').append(plugin.getPluginVersion())
        .append('_').append(modelId.getName());
    requestParams.values().forEach(param -> sb.append('_').append(param));
    return sb.append(fileExtension).toString();
  }

  private String getOutputFileExtension(String filename) {
    int i = filename.lastIndexOf('.');
    if (i > 0) {
      return filename.substring(i);
    }
    return "";
  }

  static Tag[] tagsForRequest(GeneratorPluginConfiguration plugin,
      Map<String, String> requestParams) {
    Tag[] tags = requestParams.values()
        .stream()
        .map(Tag::new)
        .collect(Collectors.toSet())
        .toArray(new Tag[requestParams.size() + 2]);
    tags[tags.length - 1] = new Tag(plugin.getKey() + '_' + plugin.getPluginVersion());
    tags[tags.length - 2] = new Tag("generated");
    return tags;
  }

}
