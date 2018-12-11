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
package org.eclipse.vorto.repository.core.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.resolver.DefaultResolver;
import org.eclipse.vorto.repository.web.core.dto.LWM2MQuery;
import org.junit.Test;

public class Lwm2mObjectIdResolverTest extends AbstractIntegrationTest {

  @Test
  public void testResolveForExistingObjectId() {
    importModel("lwm2m/ColorLight.fbmodel");
    importModel("lwm2m/ColorLight_lwm2m.mapping");

    DefaultResolver lwm2mResolver = new DefaultResolver();
    lwm2mResolver.setRepository(this.modelRepository);
    assertEquals(new ModelId("ColorLight", "com.mycompany.fb", "1.0.0"),
        lwm2mResolver.resolve(new LWM2MQuery("2")));

    assertNotNull(this.modelRepository.getById(lwm2mResolver.resolve(new LWM2MQuery("2"))));
  }

  @Test
  public void testResolveResourceId() {
    importModel("lwm2m/ColorLight.fbmodel");
    importModel("lwm2m/ColorLight_lwm2m.mapping");

    DefaultResolver lwm2mResolver = new DefaultResolver();
    lwm2mResolver.setRepository(this.modelRepository);
    assertNull(lwm2mResolver.resolve(new LWM2MQuery("3")));
  }
}
