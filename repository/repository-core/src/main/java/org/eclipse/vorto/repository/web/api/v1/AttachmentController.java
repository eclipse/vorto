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

import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.*;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.api.v1.dto.AttachResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/attachments")
public class AttachmentController extends AbstractRepositoryController {

  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";
  private static final Predicate<Attachment> STATIC_TAG_FILTER = attachment -> attachment.getTags()
      .contains(Attachment.TAG_IMAGE)
      || attachment.getTags().contains(Attachment.TAG_DOCUMENTATION)
      || attachment.getTags().contains(Attachment.TAG_IMPORTED)
      || attachment.getTags().isEmpty();

  private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentController.class);

  @Autowired
  private AttachmentValidator attachmentValidator;

  @Autowired
  private NamespaceService namespaceService;

  @PutMapping(value = "/{modelId:.+}", produces = "application/json")
  @PreAuthorize("hasAuthority('sysadmin')")
  public AttachResult attach(
      @ApiParam(
          value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
          required = true) @PathVariable String modelId,
      @ApiParam(value = "The file to be uploaded as attachmment",
          required = true) @RequestParam("file") MultipartFile file) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);

    // early file size validation
    if (!attachmentValidator.validateAttachmentSize(file.getSize())) {
      return AttachResult.fail(
        modelID,
        file.getOriginalFilename(),
        String.format(
            "The attachment is too large. Maximum size allowed is %dMB",
            attachmentValidator.getMaxFileSizeSetting()
        )
      );
    }

    final String workspaceId = getWorkspaceId(modelID).orElseThrow(
        () -> new ModelNotFoundException("Namespace for model '" + modelId + "' doesn't exist", null));

    try {
      String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");

      getModelRepository(modelID).attachFile(modelID,
          new FileContent(fileName, file.getBytes(), file.getSize()), getUserContext(workspaceId),
          guessTagsFromFileExtension(fileName));

      return AttachResult.success(modelID, fileName);
    } catch (IOException | FatalModelRepositoryException | AttachmentException e) {
      return AttachResult.fail(modelID, file.getOriginalFilename(), e.getMessage());
    }
  }

  @PutMapping(value = "/{modelId:.+}/links", produces = "application/json")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public AttachResult addLink(@ApiParam(
      value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
      required = true) @PathVariable String modelId,
      @ApiParam(value = "The URL to be attached",
          required = true) @RequestBody String url) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);
    getModelRepository(modelID).attachLink(modelID, url);

    return AttachResult.success(modelID, url);
  }

  @GetMapping("/{modelId:.+}/links")
  public Collection<String> getLinks(@ApiParam(
      value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
      required = true) @PathVariable String modelId) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);
    return getModelRepository(modelID).getLinks(modelID);
  }

  @DeleteMapping("/{modelId:.+}/links")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public ResponseEntity<Void> deleteLink(
      @ApiParam(
          value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
          required = true) @PathVariable String modelId,
      @ApiParam(value = "The URL to be deleted",
          required = true) @RequestBody String url) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);
    getModelRepository(modelID).deleteLink(modelID, url);
    return new ResponseEntity<>(HttpStatus.OK);
  }


  // TODO: interim solution until attachment upload dialog supports Label Chooser
  private Tag[] guessTagsFromFileExtension(String fileName) {
    final String _name = fileName.toLowerCase();
    if (_name.endsWith(".jpg") || _name.endsWith(".png")) {
      return new Tag[] {Attachment.TAG_IMAGE};
    } else if (_name.endsWith(".doc") || _name.endsWith(".pdf") || _name.endsWith(".txt")) {
      return new Tag[] {Attachment.TAG_DOCUMENTATION};
    } else {
      return new Tag[0];
    }
  }

  @GetMapping(value = "/{modelId:.+}", produces = "application/json")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public List<Attachment> getAttachments(
      @ApiParam(
          value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
          required = true) @PathVariable String modelId) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);

    try {
      return getModelRepository(modelID).getAttachments(modelID);
    } catch (FatalModelRepositoryException e) {
      return Collections.emptyList();
    }
  }

  @GetMapping(value = "/static/{modelId:.+}", produces = "application/json")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public List<Attachment> getAttachmentsWithStaticTags(
      @ApiParam(
          value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
          required = true) @PathVariable String modelId) {
    return getAttachments(modelId).stream()
        .filter(STATIC_TAG_FILTER)
        .collect(Collectors.toList());
  }

  @GetMapping("/{modelId:.+}/files/{filename:.+}")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public void getAttachment(
      @ApiParam(
          value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
          required = true) @PathVariable String modelId,
      @ApiParam(value = "The name of the attached file that you want to retrieve",
          required = true) @PathVariable String filename,
      final HttpServletResponse response) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);

    try {
      String fileName = URLDecoder.decode(filename, "UTF-8");
      Optional<FileContent> content =
          getModelRepository(modelID).getAttachmentContent(modelID, fileName);

      if (content.isPresent()) {
        response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
        response.setContentType(APPLICATION_OCTET_STREAM);

        IOUtils.copy(new ByteArrayInputStream(content.get().getContent()),
            response.getOutputStream());
        response.flushBuffer();
      } else {
        throw new ModelNotFoundException("Could not find model");
      }
    } catch (IOException e) {
      LOGGER.error("Cannot get model attachment:", e);
    }
  }

  @DeleteMapping("/{modelId:.+}/files/{filename:.+}")
  @PreAuthorize("hasAuthority('sysadmin') or hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId), 'model:owner')")
  public ResponseEntity<Void> deleteAttachment(
      @ApiParam(
          value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany:MagneticSensor:1.0.0",
          required = true) @PathVariable String modelId,
      @ApiParam(value = "The name of the attached file that you want to delete",
          required = true) @PathVariable String filename) {

    ModelId modelID = ModelId.fromPrettyFormat(modelId);

    try {
      String fileName = URLDecoder.decode(filename, "UTF-8");

      if (!getModelRepository(modelID).deleteAttachment(modelID, fileName)) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (UnsupportedEncodingException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private Optional<String> getWorkspaceId(ModelId modelId) {
    return namespaceService.resolveWorkspaceIdForNamespace(modelId.getNamespace());
  }

  private UserContext getUserContext(String tenantId) {
    return UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName(),
        tenantId);
  }
}
