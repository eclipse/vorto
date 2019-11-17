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
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.junit.Test;

public class ModelRepositorySearchTest extends AbstractIntegrationTest {
  
  @Test
  public void testSearchWithNull() {
    importModel("Color.type");
    assertEquals(1,searchService.search(null).size());
  }

  @Test
  public void testSearchWithEmptyExpression() {
    importModel("Color.type");
    assertEquals(1, searchService.search("").size());
  }

  @Test
  public void testSearchWithSpecialCharacter() {
    importModel("Color.type");
    assertEquals(0,
        searchService.search("!$@").size());
  }
  
  @Test
  public void testSearchAllModelsWithWildCard() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(4, searchService.search("*").size());
    
    ModelInfo model = searchService.search("*").stream().filter(m -> m.getId().getName().equals("ColorLight")).findAny().get();
    assertEquals(1,model.getReferences().size());
  }

  @Test
  public void testSearchByFreetext1() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(2,
        searchService.search("color").size());
  }
  
  @Test
  public void testSearchByFreetext2() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(2,
        searchService.search("light").size());
  }
  
  @Test
  public void testSearchByFreetext3() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(1,
        searchService.search("tch").size());
  }
  
  @Test
  public void testSearchByFreetext3ContainingWildCard() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(1,
        searchService.search("tch*").size());
    assertEquals("Switcher",searchService.search("tch*").get(0).getId().getName());
  }

  @Test
  public void testSearchModelByModelName() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(1, searchService.search("name:Color").size());
  }

  @Test
  public void testSearchModelByNameWildcard() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    
    assertEquals(2, searchService.search("name:Color*").size());
  }

  @Test
  public void testSearchModelByNamespace() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("ColorLightIM.infomodel");
    importModel("HueLightStrips.infomodel");

    assertEquals(1, searchService.search("namespace:org.eclipse.vorto.examples.fb").size());
    assertEquals(1, searchService.search("namespace:com.mycompany.fb").size());
    assertEquals(2, searchService.search("namespace:com.mycompany   version:1.0.0").size());

  }

  @Test
  public void testSearchModelByType() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("ColorLightIM.infomodel");
    importModel("HueLightStrips.infomodel");

    assertEquals(2, searchService.search("Functionblock").size());
  }

  @Test
  public void testSearchModelByNameAndType() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("ColorLightIM.infomodel");
    importModel("HueLightStrips.infomodel");

    assertEquals(0, searchService.search("name:Switcher InformationModel").size());
    assertEquals(1, searchService.search("name:Switcher Functionblock").size());
  }

  @Test
  public void testSearchModelWithFilters5() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("ColorLightIM.infomodel");
    importModel("HueLightStrips.infomodel");

    assertEquals(1, searchService.search("Functionblock name:Color*").size());
  }

  @Test
  public void testSearchByAuthor1() {
    IUserContext alex = createUserContext("alex", "playground");
    importModel("Color.type", alex);
    importModel("Colorlight.fbmodel", alex);
    importModel("Switcher.fbmodel", createUserContext("admin", "playground"));
    importModel("ColorLightIM.infomodel", createUserContext("admin", "playground"));
    importModel("HueLightStrips.infomodel", createUserContext("admin", "playground"));

    assertEquals(2, searchService.search("author:" + alex.getUsername()).size());
  }
  
  @Test
  public void testSearchByAuthor2() {
    IUserContext erle = createUserContext("erle", "playground");
    IUserContext admin = createUserContext("admin", "playground");
    importModel("Color.type", erle);
    importModel("Colorlight.fbmodel", erle);
    importModel("Switcher.fbmodel", admin);

    assertEquals(2, searchService.search("author:" + createUserContext("erle", "playground").getUsername()).size());
    assertEquals(1, searchService.search("author:" + createUserContext("admin", "playground").getUsername()).size());
  }


}
