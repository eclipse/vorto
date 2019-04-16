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
package org.eclipse.vorto.repository.upgrade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.core.impl.ModelRepository;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.upgrade.AbstractUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.modeshape.jcr.JcrSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PermissionsUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

  private static final Logger logger = LoggerFactory.getLogger(PermissionsUpgradeTask.class);

  @Value("${server.upgrade.permissions:false}")
  private boolean shouldUpgrade;

  private IModelRepositoryFactory repositoryFactory;

  private IUpgradeTaskCondition upgradeTaskCondition = new IUpgradeTaskCondition() {

    @Override
    public boolean shouldExecuteTask() {
      return shouldUpgrade;
    }
  };

  private static final String FILE_NODES = "*.type | *.fbmodel | *.infomodel | *.mapping ";

  public PermissionsUpgradeTask(@Autowired IModelRepositoryFactory repositoryFactory) {
    super(repositoryFactory.getModelSearchService());
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public void doUpgrade() throws UpgradeProblem {
    setAdminUserContext();
    
    Map<String, List<ModelInfo>> searchResult = getModelSearchService().search("*");
    
    for(Map.Entry<String, List<ModelInfo>> entry : searchResult.entrySet()) {
      ModelRepository modelRepository = (ModelRepository) repositoryFactory.getRepository(entry.getKey());
      
      modelRepository.doInSession(jcrSession -> {
        JcrSession session = (JcrSession) jcrSession;
        
        for (ModelInfo modelInfo : entry.getValue()) { 
          ModelIdHelper helper = new ModelIdHelper(modelInfo.getId());
          
          try {
            Node folderNode = session.getNode(helper.getFullPath());
            if (!folderNode.getNodes(FILE_NODES).hasNext()) {
              logger.warn("folder "+folderNode.getName()+" has no files. Skipping ...");
              continue;
            }
            Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();
            fileNode.addMixin("mode:accessControllable");

            folderNode.addMixin("mix:referenceable");
            folderNode.addMixin("vorto:meta");
            folderNode.addMixin("mix:lastModified");
            folderNode.setProperty("vorto:name", fileNode.hasProperty("vorto:name")?fileNode.getProperty("vorto:name").getString() : "");
            folderNode.setProperty("vorto:namespace", fileNode.getProperty("vorto:namespace").getString() );
            folderNode.setProperty("vorto:type", fileNode.getProperty("vorto:type").getString() );

            if (fileNode.hasProperty("vorto:references")) {
              List<javax.jcr.Value> newReferences = new ArrayList<javax.jcr.Value>();
              try {
                javax.jcr.Value[] referenceValues =
                    fileNode.getProperty("vorto:references").getValues();
                for (javax.jcr.Value value : referenceValues) {
                  Node referencedNode = session.getNodeByIdentifier(value.getString());
                  
                  Node referencedFolder = referencedNode.getParent();
                  referencedFolder.addMixin("mix:referenceable");
                  referencedFolder.addMixin("vorto:meta");
                  newReferences.add(session.getValueFactory().createValue(referencedFolder));
                }
                folderNode.setProperty("vorto:references",
                    newReferences.toArray(new javax.jcr.Value[newReferences.size()]));
                fileNode.getProperty("vorto:references").remove();
                session.save();
              } catch (Exception ex) {
                logger.error("problem with model "+modelInfo.getId().getPrettyFormat(),ex);
              }
            }
          } catch (PathNotFoundException e) {
            logger.error("problem in permission upgrade task",e);
          } catch (RepositoryException e) {
            logger.error("problem in permission upgrade task",e);
          }
        }
        
        for (ModelInfo modelInfo : entry.getValue()) {
          setPermissions(repositoryFactory.getPolicyManager(entry.getKey(), createAuthentication()), modelInfo);
        }
        
        return null;
      });
    }
  }

  private void setPermissions(IModelPolicyManager policyManager, ModelInfo modelInfo) {
    if (modelInfo.getState() != null
        && modelInfo.getState().equalsIgnoreCase(SimpleWorkflowModel.STATE_DRAFT.getName())) {
      logger.info("Setting permissions for model " + modelInfo.toString());
      policyManager.addPolicyEntry(modelInfo.getId(),
          PolicyEntry.of(modelInfo.getAuthor(), PrincipalType.User, Permission.FULL_ACCESS),PolicyEntry.of(Role.SYS_ADMIN.name(), PrincipalType.Role, Permission.FULL_ACCESS));
    }
  }

  public Optional<IUpgradeTaskCondition> condition() {
    return Optional.of(upgradeTaskCondition);
  }

  @Override
  public String getShortDescription() {
    return "Task for setting only-user permissions for models which are in draft state";
  }

  public IUpgradeTaskCondition getUpgradeTaskCondition() {
    return upgradeTaskCondition;
  }

  public void setUpgradeTaskCondition(IUpgradeTaskCondition upgradeTaskCondition) {
    this.upgradeTaskCondition = upgradeTaskCondition;
  }
}
