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

@Component
public class AttachmentValidator {

  private static int ONE_KB = 1024;

  @Value("#{'${repo.attachment.allowed.extension}'.split(',')}")
  private List<String> allowedExtension;

  @Value("${repo.attachment.allowed.fileSize: 2}")
  private int fileSize;

  public void validateAttachment(FileContent file, ModelId modelId) throws AttachmentException {
    validateFileLength(file, modelId);

    if (getFileSizeInMegaBytes(file.getSize()) > fileSize) {
      throw new AttachmentException(modelId,
          "File size exceeded. Allowed max size: " + fileSize + " MB.");
    }
    if (!allowedExtension.stream().anyMatch(isExtensionAllowed(file.getFileName()))) {
      throw new AttachmentException(modelId,
          "File type not supported. Supported File types :" + allowedExtension);
    }

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

  private long getFileSizeInMegaBytes(long size) {
    return size / (ONE_KB * ONE_KB);
  }

  private Predicate<String> isExtensionAllowed(String fileName) {
    return extension -> FilenameUtils.getExtension(fileName).equals(extension.trim());

  }
}
