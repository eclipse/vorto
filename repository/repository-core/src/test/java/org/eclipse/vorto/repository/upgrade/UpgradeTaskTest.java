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
package org.eclipse.vorto.repository.upgrade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.impl.DefaultUpgradeService;
import org.junit.Test;

public class UpgradeTaskTest extends AbstractIntegrationTest {

  @Test
  public void testUpgradeFileContent() {
    importModel("Color.type", UserContext.user("alex"));
    ModelResource resource =
        (ModelResource) modelRepository.getEMFResource(modelRepository.search("*").get(0).getId());
    assertNull(resource.getModel().getCategory());
    DefaultUpgradeService service = new DefaultUpgradeService();
    service.addTasks(new AddCategoryUpgradeTask(this.modelRepository));

    service.installUpgrades();

    resource =
        (ModelResource) modelRepository.getEMFResource(modelRepository.search("*").get(0).getId());
    assertEquals("iot", resource.getModel().getCategory());

  }


  private class AddCategoryUpgradeTask extends AbstractUpgradeTask {

    public AddCategoryUpgradeTask(IModelRepository repo) {
      super(repo);
    }

    @Override
    public void doUpgrade() throws UpgradeProblem {
      for (ModelInfo model : modelRepository.search("*")) {
        ModelResource resource = getModel(model.getId());
        if (resource.getModel().getCategory() == null) {
          resource.getModel().setCategory("iot");
          modelRepository.saveModel(resource);
        }

      }
    }

    @Override
    public String getShortDescription() {
      return "Adding category 'iot' to models without category.";
    }

  }

}
