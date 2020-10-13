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
package org.eclipse.vorto.repository.web.core.async;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Attachment;

/**
 * Fetches attachments for a given model asynchronously. <br/>
 * Attachments are retrieved differently according to whether the user has the {@literal sysadmin}
 * repository role or not: {@literal sysadmin} users receive all the attachments, whereas standard
 * users only receive attachments tagged with {@link Attachment#TAG_IMAGE},
 * {@link Attachment#TAG_DOCUMENTATION}, {@link Attachment#TAG_IMPORTED} or with no tags at all.
 */
public class AsyncModelAttachmentsFetcher extends AsyncModelTaskRunner {

  private final ModelId id;
  private final Collection<Attachment> target;
  private final Predicate<Attachment> filter;

  public AsyncModelAttachmentsFetcher(Collection<Attachment> target, ModelId id, boolean sysadmin) {
    this.target = target;
    this.id = id;
    this.filter = a -> sysadmin ? true : a.getTags()
        .contains(Attachment.TAG_IMAGE)
        || a.getTags().contains(Attachment.TAG_DOCUMENTATION)
        || a.getTags().contains(Attachment.TAG_IMPORTED)
        || a.getTags().isEmpty();
  }

  @Override
  public void run() {
    super.run();
    target.addAll(factory.getRepositoryByModelWithoutSessionHelper(id).getAttachments(id).stream()
        .filter(filter).collect(
            Collectors.toList()));
  }
}
