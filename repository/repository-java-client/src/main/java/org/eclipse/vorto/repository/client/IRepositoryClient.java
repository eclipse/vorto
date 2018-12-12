package org.eclipse.vorto.repository.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.client.attachment.Attachment;
import org.eclipse.vorto.repository.client.generation.GeneratedOutput;
import org.eclipse.vorto.repository.client.generation.GenerationException;
import org.eclipse.vorto.repository.client.generation.GeneratorInfo;
import org.eclipse.vorto.repository.client.generation.GeneratorNotFoundException;

public interface IRepositoryClient {

  /**
   * Gets all available generators keys of generators that are currently registered in the
   * repository.
   * 
   * @return a list of generator keys
   */
  Set<String> getAvailableGeneratorKeys();

  /**
   * Gives further information about the generator for a given generator key.
   * 
   * @param generatorKey
   * @return generator information
   * 
   * @throws GeneratorNotFoundException if no generator can be found for the given key
   */
  GeneratorInfo getInfo(String generatorKey);

  /**
   * Generates platform-specific code for a given Vorto model id using a code generator
   * 
   * @param modelId
   * @param generatorKey
   * @param invocationParams
   * @return the generated platform - specific source code
   * 
   * @throws GeneratorNotFoundException if no generator can be found for the provided key
   * @throws ModelNotFoundException if no model exists in the repository with the specified model id
   * @throws GenerationException if something goes wrong during the generation
   */
  GeneratedOutput generate(ModelId modelId, String generatorKey,
      Map<String, String> invocationParams);
  
  
  /**
   * Searches the repository by a query expression. Use {@link IModelRepository#newQuery()} as a
   * helper to formulate your query
   * 
   * @param query expression containing the criteria for the search
   * @return a list of model info objects, never null
   */
  Collection<ModelInfo> search(String expression);

  /**
   * Finds a model by the given model id.
   * 
   * @param modelId
   * @return model info that was found in the repository or null if a model does not exist with the
   *         given id
   */
  ModelInfo getById(ModelId modelId);

  /**
   * Gets the actual information model content for a given model id.
   * 
   * @param modelId model id to get its content for
   * @param resultClass expected model class, either {@link Infomodel}, {@link FunctionblockModel},
   *        {@link EntityModel} or {@link EnumModel}
   * @return model content
   */
  ModelContent getContent(ModelId modelId);

  /**
   * Gets the actual model content for a given model id including the meta data for the given target
   * platform.
   * 
   * @param modelId model id to get its content for
   * @param resultClass expected model class, either {@link Infomodel}, {@link FunctionblockModel},
   *        {@link EntityModel} or {@link EnumModel}
   * @param key of the target platform
   * @return model content
   */
  ModelContent getContent(ModelId modelId, String targetPlatformKey);

  /**
   * Gets the actual model content for a given model id including the meta data for the given
   * mapping model ID
   * 
   * @param modelId model id to get its content for
   * @param resultClass expected model class, either {@link Infomodel}, {@link FunctionblockModel},
   *        {@link EntityModel} or {@link EnumModel}
   * @param mappingModelId model id of the mapping to look up
   * @return model content
   */
  ModelContent getContent(ModelId modelId, ModelId mappingModelId);

  /**
   * Get a list of file attachments for a model
   * 
   * @param modelId model id to get attachments for
   * @return attachments List of attachment
   */
  List<Attachment> getAttachments(ModelId modelId);

  /**
   * Get the contents of an attached file to a model
   * 
   * @param modelId the model where the file is attached
   * @param filename the filename of the attached file
   * @return content The contents of the file
   */
  byte[] downloadAttachment(ModelId modelId, String attachmentFileName);
  
  static RepositoryClientBuilder newBuilder() {
    return RepositoryClientBuilder.newBuilder();
  }
}
