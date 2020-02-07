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
package org.eclipse.vorto.repository.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.eclipse.vorto.repository.oauth.internal.Resource;
import org.junit.Test;

public class ResourceIdentifierTest {
  
  @Test
  public void testResourceIdentifier() {
    Optional<Resource> resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle:Datatype1:1.0.0");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle:Datatype1:1.0.0", resource.get().getName());
    
    resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle:Datatype1:1.0.0-Snapshot");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle:Datatype1:1.0.0-Snapshot", resource.get().getName());

    resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle:Datatype1:1.0.0/anyOtherApi");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle:Datatype1:1.0.0", resource.get().getName());

    resource = ResourceIdentificationHelper.identifyResource("/api/v1/search/models");
    assertFalse(resource.isPresent());

    resource = ResourceIdentificationHelper.identifyResource("/api/v1/models/vorto.private.erle");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle", resource.get().getName());

    resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle/anyOtherApi");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle", resource.get().getName());
  }
}
