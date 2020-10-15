/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.diagnostics.ModeshapeAclEntry;
import org.eclipse.vorto.repository.diagnostics.ModeshapeContentData;
import org.eclipse.vorto.repository.diagnostics.ModeshapeNodeData;
import org.eclipse.vorto.repository.diagnostics.ModeshapeProperty;

public interface IModeshapeDoctor {

    ModeshapeNodeData readModeshapeNodeData(ModelId modelId);

    ModeshapeNodeData readModeshapeNodeData(String path);

    ModeshapeContentData readModeshapeNodeContent(String path);

    void deleteModeshapeNode(String path);

    void setPropertyOnNode(String path, ModeshapeProperty property);

    void setAclEntryOnNode(String path, ModeshapeAclEntry aclEntry);

    void deletePropertyOnNode(String path, ModeshapeProperty property);

    void deleteAclEntryOnNode(String path, ModeshapeAclEntry aclEntry);

}
