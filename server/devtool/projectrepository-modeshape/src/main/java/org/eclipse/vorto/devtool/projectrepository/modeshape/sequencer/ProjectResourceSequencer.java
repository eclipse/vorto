/**
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */

package org.eclipse.vorto.devtool.projectrepository.modeshape.sequencer;

import java.io.IOException;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.modeshape.jcr.api.nodetype.NodeTypeManager;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.springframework.core.io.ClassPathResource;

public class ProjectResourceSequencer extends Sequencer {

	@Override
	public void initialize(NamespaceRegistry registry, NodeTypeManager nodeTypeManager)
			throws RepositoryException, IOException {
		registerNodeTypes(new ClassPathResource("project-resource-sequencer.cnd").getInputStream(), nodeTypeManager,
				true);
	}

	@Override
	public boolean execute(Property property, Node node, Context context) throws Exception {

		return false;
	}
}
