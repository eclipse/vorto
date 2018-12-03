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
