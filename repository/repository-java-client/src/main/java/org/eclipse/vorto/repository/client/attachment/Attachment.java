/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.client.attachment;

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
    return new Attachment(modelId, filename);
  }

  private Attachment(ModelId modelId, String filename) {
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
