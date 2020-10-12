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
package org.eclipse.vorto.repository.web.admin;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModeshapeDoctor;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.diagnostics.ModeshapeAclEntry;
import org.eclipse.vorto.repository.diagnostics.ModeshapeContentData;
import org.eclipse.vorto.repository.diagnostics.ModeshapeNodeData;
import org.eclipse.vorto.repository.diagnostics.ModeshapeProperty;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This controller shares the same base path as {@link org.eclipse.vorto.repository.web.api.v1.NamespaceController},
 * but it lives in its own package scoped to administrator functionalities. <br/>
 * It only provides one endpoint to diagnose all namespaces (formerly "tenants"). <br/>
 * This is only available to sysadmin users, so all namespaces are diagnosed without filter. <br/>
 */
@RestController
@RequestMapping(value = "/rest/namespaces/diagnostics")
public class DiagnosticsController {

  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";
  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";

  @Autowired
  private IModelRepositoryFactory repoFactory;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @GetMapping
  @PreAuthorize("hasAuthority('sysadmin')")
  public Collection<Diagnostic> diagnose() {
    Collection<Diagnostic> diagnostics = new ArrayList<>();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    diagnostics = namespaceRepository.findAll().stream().flatMap(
        namespace -> repoFactory
            .getDiagnosticsService(namespace.getWorkspaceId(), auth)
            .diagnoseAllModels()
            .stream()
            .map(d -> d.setWorkspaceId(namespace.getWorkspaceId()))
    )
        .collect(Collectors.toList());

    return diagnostics;
  }

  @GetMapping(value = "modeshape/model/{modelIdString}", produces = "application/json")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ModeshapeNodeData readNodeData(@PathVariable String modelIdString) {
    ModelId modelId = ModelId.fromPrettyFormat(modelIdString);

    return namespaceRepository.findAll().stream()
        .filter(namespace -> namespace.owns(modelId.getNamespace())).map(Namespace::getWorkspaceId)
        .findAny()
        .map(workspaceId -> loadNodeData(modelId, workspaceId))
        .orElseThrow(() -> new ModelNotFoundException("Namespace not found: " + modelIdString));
  }

  @GetMapping(value = "modeshape/node/{workspaceId}", produces = "application/json")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ModeshapeNodeData readNodeData(@PathVariable String workspaceId, @RequestParam String path) {
    return repoFactory.getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication())
        .readModeshapeNodeData(path);
  }

  @GetMapping("modeshape/node/{workspaceId}/data")
  @PreAuthorize("hasAuthority('sysadmin')")
  public void readNodeContentData(@PathVariable String workspaceId, @RequestParam String path, final HttpServletResponse response) {
    ModeshapeContentData contentData = repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication())
        .readModeshapeNodeContent(path);

    writeByteArrayToResponse(response, contentData);
  }

  @DeleteMapping("modeshape/node/{workspaceId}")
  @PreAuthorize("hasAuthority('sysadmin')")
  public void deleteNode(@PathVariable String workspaceId, @RequestParam String path) {
    repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication())
        .deleteModeshapeNode(path);
  }

  @PutMapping("modeshape/node/{workspaceId}/property")
  public ModeshapeNodeData setNodeProperty(@PathVariable String workspaceId, @RequestParam String path,
      @RequestBody ModeshapeProperty property) {

    IModeshapeDoctor doctor = repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication());
    doctor.setPropertyOnNode(path, property);
    return doctor.readModeshapeNodeData(path);
  }

  @PutMapping("modeshape/node/{workspaceId}/acl")
  public ModeshapeNodeData setAclEntry(@PathVariable String workspaceId, @RequestParam String path,
      @RequestBody ModeshapeAclEntry aclEntry) {

    IModeshapeDoctor doctor = repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication());
    doctor.setAclEntryOnNode(path, aclEntry);
    return doctor.readModeshapeNodeData(path);
  }

  @DeleteMapping("modeshape/node/{workspaceId}/acl")
  public void deleteNodeProperty(@PathVariable String workspaceId, @RequestParam String path,
      @RequestBody ModeshapeAclEntry aclEntry) {

    repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication())
        .deleteACLOnNode(path, aclEntry);
  }

  @DeleteMapping("modeshape/node/{workspaceId}/property")
  public void deleteNodeProperty(@PathVariable String workspaceId, @RequestParam String path,
      @RequestBody ModeshapeProperty property) {

    repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication())
        .deletePropertyOnNode(path, property);
  }

  private void writeByteArrayToResponse(HttpServletResponse response, ModeshapeContentData contentData) {
    response.setContentType(APPLICATION_OCTET_STREAM);
    try {
      IOUtils.copy(new ByteArrayInputStream(contentData.getData()),
          response.getOutputStream());
          response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + contentData.getFilename());
          response.flushBuffer();
    } catch (IOException e) {
      throw new IllegalStateException("Error writing response.", e);
    }
  }

  private ModeshapeNodeData loadNodeData(ModelId modelId, String workspaceId) {
    return repoFactory
        .getModeshapeDoctor(workspaceId, SecurityContextHolder.getContext().getAuthentication())
        .readModeshapeNodeData(modelId);
  }

}
