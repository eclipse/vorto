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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import org.eclipse.vorto.repository.core.IModelSearchService;

public abstract class AbstractUpgradeTask implements IUpgradeTask {

	protected IModelSearchService modelSearchService;
	
	public AbstractUpgradeTask(IModelSearchService modelSearchService) {
		this.modelSearchService = modelSearchService;
	}
	
	public Optional<IUpgradeTaskCondition> condition() {
		return Optional.empty();
	}

  public IModelSearchService getModelSearchService() {
    return modelSearchService;
  }

  public void setModelSearchService(IModelSearchService modelSearchService) {
    this.modelSearchService = modelSearchService;
  }
}
