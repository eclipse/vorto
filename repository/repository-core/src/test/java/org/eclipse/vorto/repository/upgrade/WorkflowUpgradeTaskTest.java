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
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.upgrade.impl.WorkflowUpgradeTask;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

@Ignore
public class WorkflowUpgradeTaskTest extends AbstractIntegrationTest {

  private WorkflowUpgradeTask task = null;

  @Override
  public void beforeEach() throws Exception {
    super.beforeEach();
    IUserContext admin = createUserContext("admin");

    getRepoManager(admin).restore(
        IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));

    task = new WorkflowUpgradeTask(repositoryFactory,
        new DefaultWorkflowService(repositoryFactory, accountService, null));
    task.setUpgradeTaskCondition(new IUpgradeTaskCondition() {

      @Override
      public boolean shouldExecuteTask() {
        return true;
      }
    });
  }

  @Test
  public void testUpgradeFull() {
    IUserContext admin = createUserContext("admin");
    IModelRepository modelRepository = getModelRepository(admin);
    
    List<ModelInfo> models = modelRepository.search("state:Draft");

    assertEquals(0, models.size());

    task.doUpgrade();

    models = modelRepository.search("state:Draft");

    assertEquals(4, models.size());
  }

  @Test
  public void testUpgradePartialModels() {
    IUserContext admin = createUserContext("admin");
    IModelRepository modelRepository = getModelRepository(admin);
    
    List<ModelInfo> models = modelRepository.search("*");

    ModelInfo model = models.get(0);
    model.setState("Released");
    modelRepository.updateMeta(model);

    task.doUpgrade();

    assertEquals(1, modelRepository.search("state:Released").size());
    assertEquals(model.getId(), modelRepository.search("state:Released").get(0).getId());
    assertEquals(3, modelRepository.search("state:Draft").size());
  }

}
