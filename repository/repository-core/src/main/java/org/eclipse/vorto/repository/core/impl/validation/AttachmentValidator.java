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
package org.eclipse.vorto.repository.core.impl.validation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.AttachmentException;
import org.eclipse.vorto.repository.core.FileContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AttachmentValidator {

  private static int ONE_KB = 1024;

  @Value("#{'${repo.attachment.allowed.extension}'.split(',')}")
  private List<String> allowedExtension;

  @Value("${repo.attachment.allowed.fileSize: 2}")
  private int fileSize;

  public void validateAttachment(FileContent file, ModelId modelId) throws AttachmentException {
    validateFileLength(file, modelId);

    if (validateAttachmentSize(file.getSize())) {
      throw new AttachmentException(modelId,
          "File size exceeded. Allowed max size: " + fileSize + " MB.");
    }
    if (!allowedExtension.stream().anyMatch(isExtensionAllowed(file.getFileName()))) {
      throw new AttachmentException(modelId,
          "File type not supported. Supported File types :" + allowedExtension);
    }
  }

  /**
   * This is invoked at controller level to provide the end-user with a human-readable error message
   * when uploading e.g. an image to a model. <br/>
   * Contrary to {@link AttachmentValidator#validateAttachment(FileContent, ModelId)}, this only
   * validate the size of a {@link MultipartFile} against the {@code repo.attachment.allowed.fileSize}
   * configured value.<br/>
   * Another important point worth noting is that the validation here is much more precise than it
   * used to be, meaning that instead of truncating down the real size of the file to an integer and
   * comparing it with the configured value in MB, it converts the configured value to an exact
   * number of bytes, to be compared with the actual size in bytes too.<br/>
   * Therefore, this is also used at repository level now, aka invoked in the implementation of
   * {@link AttachmentValidator#validateAttachment(FileContent, ModelId)}.
   * @param fileSize
   * @return
   */
  public boolean validateAttachmentSize(long fileSize) {
    return fileSize < this.fileSize * 1024 * 1024;
  }


  /**
   * Invoked to specify maximum allowed in controller responses.
   * @return
   */
  public int getMaxFileSizeSetting() {
    return fileSize;
  }

  private void validateFileLength(FileContent file, ModelId modelId) throws AttachmentException {
    String fileName;
    try {
      fileName = URLDecoder.decode(file.getFileName(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AttachmentException(modelId, e);
    }
    if (fileName.length() > 100) {
      throw new AttachmentException(modelId, "Name of File exceeds 100 Characters");
    }
  }

  private Predicate<String> isExtensionAllowed(String fileName) {
    return extension -> FilenameUtils.getExtension(fileName).equals(extension.trim());

  }
}
