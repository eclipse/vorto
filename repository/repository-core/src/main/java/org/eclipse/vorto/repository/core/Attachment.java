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
package org.eclipse.vorto.repository.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;

public class Attachment {

  private ModelId modelId;
  private String filename;
  private List<Tag> tags = new ArrayList<Tag>();

  public static final Tag TAG_IMPORTED = new Tag("org.eclipse.vorto.tag.import", "Imported");
  public static final Tag TAG_DOCUMENTATION =
      new Tag("org.eclipse.vorto.tag.documentation", "Documentation");
  public static final Tag TAG_IMAGE = new Tag("org.eclipse.vorto.tag.image", "Image");

  public static Attachment newInstance(ModelId modelId, String filename) {
    return new Attachment(modelId, filename, getLink(modelId, filename));
  }

  public static String getLink(ModelId modelId, String filename) {
    StringBuffer link = new StringBuffer("/api/v1/attachments");

    link.append("/");
    link.append(modelId.getPrettyFormat());
    link.append("/files/");
    link.append(filename);

    return link.toString();
  }

  private Attachment(ModelId modelId, String filename, String downloadLink) {
    this.modelId = modelId;
    this.filename = filename;
  }

  public ModelId getModelId() {
    return modelId;
  }

  public void setModelId(ModelId modelId) {
    this.modelId = modelId;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public Optional<Tag> getTagById(String tagId) {
    return this.tags.stream().filter(tag -> tag.getId().equals(tagId)).findAny();
  }

  @Override
  public String toString() {
    return "Attachment [modelId=" + modelId + ", filename=" + filename + ", tags=" + tags + "]";
  }


}
