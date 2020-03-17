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
 * @author kolotu This class handles the querying and the saving of attachments that contain the
 * output of generators.
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

  /**
   * This method generates the Tags that are used to save and query the attachment.
   *
   * @param plugin        - Info about the generator
   * @param requestParams - Parameters of the request
   * @return an array containing all Tags used for saving / querying the attachment.
   */
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

  /**
   * This method queries the repository for an attachment on the given model that contains the
   * generated output of the given generator.
   *
   * @param modelInfo     - Info of the model that is used to invoke the generator
   * @param requestParams - Parameters of the incoming request
   * @param plugin        - Info about the generator that shall be invoked
   * @param repository    - The repository containing the model
   * @return an optional containing the generated output, if a matching attachment exists on the
   * model, otherwise Optional.empty().
   */
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

  /**
   * This method creates a new attachment on the model with the given output of a generator.
   *
   * @param userContext   - The current user context
   * @param modelId       - the ID of the model used to invoke the generator
   * @param serviceKey    - the service key of the generator
   * @param requestParams - the parameters of the request
   * @param response      - the output of the generator
   * @param plugin        - the info about the invoked generator
   * @return the generated output that has been saved as attachment. The returned value has a
   * different file name than the one in the response parameter, as attachments of generator outputs
   * follow a naming convention.
   */
  GeneratedOutput attachGeneratedOutput(IUserContext userContext, ModelId modelId,
      String serviceKey, Map<String, String> requestParams, GeneratedOutput response,
      GeneratorPluginConfiguration plugin) {

    String filename = buildAttachmentFilename(modelId, serviceKey, requestParams, response, plugin);
    FileContent fc = new FileContent(filename, response.getContent());
    modelRepositoryFactory.getRepositoryByModel(modelId, userContext)
        .attachFileInElevatedSession(modelId, fc, userContext,
            GeneratedOutputAttachmentHandler.tagsForRequest(plugin, requestParams));
    return new GeneratedOutput(response.getContent(), filename, response.getSize());
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

}
