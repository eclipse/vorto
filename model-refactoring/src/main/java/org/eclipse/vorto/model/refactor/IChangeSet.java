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
package org.eclipse.vorto.model.refactor;

import java.util.Set;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;

public interface IChangeSet extends IModelWorkspace {

  Set<Model> getChanges();
}
