/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.spi.service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;
import javax.annotation.PostConstruct;
import org.eclipse.vorto.codegen.api.FileContent;
import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.spi.config.AbstractGeneratorConfiguration;
import org.eclipse.vorto.codegen.spi.exception.NotFoundException;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.eclipse.vorto.codegen.spi.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.spi.utils.GatewayUtils;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.attachment.Attachment;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.google.common.base.Throwables;

@Component
public class VortoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VortoService.class);
  private static final String TENANT = "default";

  @Autowired
  private AbstractGeneratorConfiguration env;

  @Autowired
  private GeneratorRepository repo;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private IModelRepository modelRepository;

  @Value("${server.config.generatorUser:#{null}}")
  private String generatorUsername;

  @Value("${server.config.generatorPassword:#{null}}")
  private String generatorPassword;


  public IGenerationResult generate(String key, String namespace, String name, String version,
      Map<String, String> parameters, Optional<String> headerAuth) {
    LOGGER.info(String.format("Generating for Platform [%s] and Model [%s.%s:%s]", key, namespace,
        name, version));

    Generator generator =
        repo.get(key).orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", key)));

    InformationModel model = getModel(namespace, name, version, headerAuth).orElseThrow(
        GatewayUtils.notFound(String.format("[Model %s.%s:%s]", namespace, name, version)));

    List<MappingModel> mappings = getMappings(key, namespace, name, version, headerAuth);

    InvocationContext invocationContext =
        new InvocationContext(mappings, repo.newGeneratorLookup(), parameters);
    try {
      final ModelId modelId = new ModelId(name, namespace, version);
      List<Attachment> attachments = modelRepository.getAttachments(modelId).get();
      Optional<Attachment> importedFile = attachments.stream()
          .filter(attachment -> attachment.getTagById(Attachment.TAG_IMPORTED.getLabel()) != null)
          .findAny();
      if (importedFile.isPresent()) {
        byte[] importedFileContent =
            modelRepository.getAttachment(modelId, importedFile.get().getFilename()).get();
        invocationContext.setImportedFile(
            new FileContent(importedFile.get().getFilename(), importedFileContent));
      }
    } catch (Exception ex) {
      // do logging
    }



    return generate(generator.getInstance(), model, invocationContext);
  }

  public IGenerationResult generate(String key, Map<String, String> parameters,
      Optional<String> headerAuth) {
    LOGGER.info(String.format("Generating for Platform [%s]", key));

    Generator generator =
        repo.get(key).orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", key)));
    InformationModel infomodel = InformationModelFactory.eINSTANCE.createInformationModel();
    infomodel.setNamespace("com.mycompany");
    infomodel.setName("Template");
    infomodel.setVersion("0.0.1");
    InvocationContext invocationContext = InvocationContext.simpleInvocationContext(parameters);
    return generate(generator.getInstance(), infomodel, invocationContext);
  }

  private IGenerationResult generate(IVortoCodeGenerator generator, InformationModel model,
      InvocationContext invocationContext) {
    try {
      return generator.generate(ModelConversionUtils.convertToFlatHierarchy(model),
          invocationContext, null);
    } catch (Exception e) {
      LOGGER.error(String.format("Exception on generating [%s.%s:%s] for key[%s]",
          model.getNamespace(), model.getName(), model.getVersion(), generator.getServiceKey()), e);
      GenerationResultZip output = new GenerationResultZip(model, generator.getServiceKey());
      Generated generated =
          new Generated("generation_error.log", "/generated", Throwables.getStackTraceAsString(e));
      output.write(generated);
      return output;
    }
  }

  public Optional<InformationModel> getModel(String namespace, String name, String version,
      Optional<String> headerAuth) {
    Optional<byte[]> modelResources =
        downloadUrl(urlForModel(namespace, name, version), headerAuth);

    if (!modelResources.isPresent()) {
      return Optional.empty();
    }

    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addZip(new ZipInputStream(new ByteArrayInputStream(modelResources.get()))).read();

    return toInformationModel(
        workspace.get().stream().filter(p -> p.getName().equals(name)).findFirst().get());
  }

  private String urlForModel(String namespace, String name, String version) {
    return String.format("%s/api/v1/models/%s/file?includeDependencies=true", env.getVortoRepoUrl(),
        new ModelId(name, namespace, version).getPrettyFormat());
  }

  private Optional<InformationModel> toInformationModel(Model model) {
    if (model instanceof InformationModel) {
      return Optional.of((InformationModel) model);
    } else if (model instanceof FunctionblockModel) {
      return Optional.of(Utils.wrapFunctionBlock((FunctionblockModel) model));
    }

    throw new NotFoundException(
        String.format("[Model %s.%s:%s] is not an Information Model or a Function Block",
            model.getNamespace(), model.getName(), model.getVersion()));
  }

  public List<MappingModel> getMappings(String generatorKey, String namespace, String name,
      String version, Optional<String> headerAuth) {
    Optional<byte[]> mappingResources =
        downloadUrl(urlForMapping(generatorKey, namespace, name, version), headerAuth);

    if (mappingResources.isPresent()) {
      IModelWorkspace workspace = IModelWorkspace.newReader()
          .addZip(new ZipInputStream(new ByteArrayInputStream(mappingResources.get()))).read();
      List<Model> models = workspace.get().stream().filter(p -> p instanceof MappingModel)
          .collect(Collectors.toList());
      return models.stream().map(MappingModel.class::cast).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  private String urlForMapping(String targetPlatform, String namespace, String name,
      String version) {
    return String.format("%s/rest/" + TENANT + "/models/%s/download/mappings/%s",
        env.getVortoRepoUrl(), new ModelId(name, namespace, version).getPrettyFormat(),
        targetPlatform);
  }

  private Optional<byte[]> downloadUrl(String url, Optional<String> headerAuth) {
    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Downloading " + url);
      }

      HttpEntity<String> entity = headerAuth.map(token -> {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return new HttpEntity<String>("parameters", headers);
      }).orElse(null);

      return Optional
          .of(restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class).getBody());
    } catch (RestClientException e) {
      LOGGER.error("Error downloading the URL [" + url + "]", e);
      return Optional.empty();
    }
  }

  public void register(Generator generator) {
    String serviceUrl = getServiceUrl(generator);
    LOGGER.info(String.format(
        "Registering Generator[%s] on URL[%s] to [%s/rest/" + TENANT + "/generators/%s]",
        generator.getInstance().getServiceKey(), serviceUrl, env.getVortoRepoUrl(),
        generator.getInstance().getServiceKey()));

    restTemplate.put(env.getVortoRepoUrl() + "/rest/" + TENANT + "/generators/{serviceKey}",
        getEntity(serviceUrl, Optional.of(getGeneratorCredentials())),
        generator.getInstance().getServiceKey());
  }

  private String getGeneratorCredentials() {
    if (!"".equals(this.generatorUsername) && !"".equals(this.generatorPassword)) {
      return this.generatorUsername + ":" + this.generatorPassword;
    } else {
      return null;
    }

  }

  private HttpEntity<String> getEntity(String serviceUrl, Optional<String> basicCredentials) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    if (basicCredentials.isPresent()) {
      headers.add("Authorization",
          "Basic " + Base64.getEncoder().encodeToString(basicCredentials.get().getBytes()));
    }
    return new HttpEntity<String>(serviceUrl, headers);
  }

  private String getServiceUrl(Generator generator) {
    return String.format("%s/rest/generators/%s/generate", env.getAppServiceUrl(),
        generator.getInstance().getServiceKey());
  }

  @PostConstruct
  public void init() {
    DatatypePackageImpl.init();
    FunctionblockPackageImpl.init();
    InformationModelPackageImpl.init();
  }
}
