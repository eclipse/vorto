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
package org.eclipse.vorto.repository.plugin.importer;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.plugin.importer.ImportValidationResult;
import org.eclipse.vorto.plugin.importer.ImporterPluginInfo;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.importer.AbstractModelImporter;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.plugin.FileContentResource;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RemoteImporter extends AbstractModelImporter {

  private ImporterPluginInfo info;
  
  private String endpointUrl;

  private RestTemplate restTemplate;

  public RemoteImporter(ImporterPluginInfo info, String endpointUrl) {
    super(info.getFileType());
    this.info = info;
    this.restTemplate = new RestTemplate();
    this.endpointUrl = endpointUrl;
  }

  @Override
  public String getKey() {
    return this.info.getKey();
  }

  @Override
  public String getShortDescription() {
    return this.info.getDescription();
  }

  @Override
  protected List<ValidationReport> validate(FileUpload fileUpload, IUserContext user) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    body.add("file", new FileContentResource(fileUpload));

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    ResponseEntity<ImportValidationResult> validationResult = restTemplate.postForEntity(
        this.endpointUrl + "/api/2/plugins/importers/{pluginkey}/file_validation",
        requestEntity, ImportValidationResult.class, this.info.getKey());

    ImportValidationResult result = validationResult.getBody();
    

    if (result.isValid()) {
      ModelInfo modelInfo = new ModelInfo(result.getModelId(), org.eclipse.vorto.model.ModelType.Functionblock);
      
      ModelValidationHelper validationHelper = new ModelValidationHelper(this.modelRepoFactory, this.userRepository, this.tenantService); 
      ValidationReport report = validationHelper.validate(modelInfo, user);
      return Arrays.asList(report);
    } else {
      return Arrays.asList(ValidationReport.invalid(result.getMessage()));
    }

  }

  @Override
  protected List<ModelResource> convert(FileUpload fileUpload, IUserContext user) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", new FileContentResource(fileUpload));

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    ResponseEntity<byte[]> validationResult = restTemplate.postForEntity(
        this.endpointUrl + "/api/2/plugins/importers/{pluginkey}/file_conversion",
        requestEntity, byte[].class, this.info.getKey());

    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addZip(new ZipInputStream(new ByteArrayInputStream(validationResult.getBody()))).read();

    return workspace.get().stream().map(model -> new ModelResource(model))
        .collect(Collectors.toList());
  }

}
