/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.backup.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Item;
import javax.jcr.PathNotFoundException;
import javax.jcr.Session;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.backup.IModelBackupService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelBackupService implements IModelBackupService {

	@Autowired
	private Session session;

	@Autowired
	private IModelRepository modelRepository;

	@Override
	public byte[] backup() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		((org.modeshape.jcr.api.Session) session).exportDocumentView("/", baos, false, false);
		baos.close();
		return baos.toByteArray();

	}

	@Override
	public void restore(byte[] backup) throws Exception {
		removeAll();
		((org.modeshape.jcr.api.Session) session).getWorkspace().importXML("/", new ByteArrayInputStream(backup),
				ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	private void removeAll() throws Exception {
		Set<String> rootNodes = new HashSet<>();
		for (ModelInfo resource : this.modelRepository.search("*")) {
			int namespaceIdx = resource.getId().getNamespace().indexOf(".");
			if (namespaceIdx != -1) {
				final String org = resource.getId().getNamespace().substring(0,namespaceIdx);
		        rootNodes.add(org);
			} else {
				rootNodes.add(resource.getId().getNamespace());
			}
		}

		for (String rootNode : rootNodes) {
			try {
				Item item = session.getItem("/" + rootNode);
				item.remove();
			} catch (PathNotFoundException ex) {
			}
		}

		this.session.save();
	}

}
