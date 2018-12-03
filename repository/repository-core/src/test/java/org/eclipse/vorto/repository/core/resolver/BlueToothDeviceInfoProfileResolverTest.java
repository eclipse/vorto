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
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.resolver.DefaultResolver;
import org.eclipse.vorto.repository.web.core.dto.BluetoothQuery;
import org.junit.Test;

public class BlueToothDeviceInfoProfileResolverTest extends AbstractIntegrationTest {

  @Test
  public void testResolveInfoModelByDeviceInfoProfileSerialNo() {
    importModel("bluetooth/ColorLight.fbmodel");
    importModel("bluetooth/ColorLightIM.infomodel");
    importModel("bluetooth/ColorLight_bluetooth.mapping");

    DefaultResolver resolver = new DefaultResolver();
    resolver.setRepository(this.modelRepository);
    assertEquals(new ModelId("ColorLightIM", "com.mycompany", "1.0.0"),
        resolver.resolve(new BluetoothQuery("4810")));

    assertNotNull(this.modelRepository.getById(resolver.resolve(new BluetoothQuery("4810"))));
  }

}
