/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
