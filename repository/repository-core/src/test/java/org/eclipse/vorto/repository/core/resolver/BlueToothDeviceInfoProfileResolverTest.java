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
