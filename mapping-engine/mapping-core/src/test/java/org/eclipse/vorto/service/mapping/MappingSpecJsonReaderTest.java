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
package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.decoder.IPayloadDeserializer;
import org.eclipse.vorto.mapping.engine.decoder.JSONDeserializer;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.runtime.EntityPropertyValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;

public class MappingSpecJsonReaderTest {

  @Test
  public void testReadFromJson() {
    IMappingSpecification spec = IMappingSpecification.newBuilder().fromInputStream(
        MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json"))
        .build();
    assertNotNull(spec);
    assertNotNull(spec.getInfoModel());

  }

  @Test
  public void testMappingFromJson() {
    IMappingSpecification spec = IMappingSpecification.newBuilder().fromInputStream(
        MappingSpecJsonReaderTest.class.getClassLoader().getResourceAsStream("mappingspec.json"))
        .build();

    IDataMapper mapper = IDataMapper.newBuilder().withSpecification(spec).build();

    String json = "{\"t\" : 50}";

    IPayloadDeserializer deserializer = new JSONDeserializer();
    InfomodelValue mappedOutput =
        mapper.mapSource(deserializer.deserialize(json));
    assertEquals(50.0,((EntityPropertyValue)
        mappedOutput.get("outdoorTemperature").getStatusProperty("value").get()).getValue().getPropertyValue("value").get().getValue());
  }
}
