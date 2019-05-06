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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.junit.Test;

public class ModelRepositoryTest2 extends AbstractIntegrationTest {

  @Test
  public void testReferencedByApi() {
    IUserContext creator = createUserContext("creator", "playground");
    importModel("Color.type", creator);
    importModel("Color4.type", creator);
    importModel("Colorlight.fbmodel", creator);
    importModel("Colorlight2.fbmodel", creator);
    importModel("Colorlight3.fbmodel", creator);
    
    List<ModelInfo> referencedBy = repositoryFactory.getRepository(creator).getModelsReferencing(new ModelId("Color", "org.eclipse.vorto.examples.type", "1.0.0"));
    assertEquals(2, referencedBy.size());
    assertTrue(referencedBy.stream().anyMatch(model -> model.getId().getName().equals("ColorLight")));
    assertTrue(referencedBy.stream().anyMatch(model -> model.getId().getName().equals("ColorLight2")));
  }
  
}
