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
package org.eclipse.vorto.perspective.vorto.view;

import org.eclipse.vorto.perspective.view.AbstractModelRepositoryViewPart;

public class ModelRepositoryViewPart extends AbstractModelRepositoryViewPart {

	@Override
	protected String getInfoModelEditorId() {
		return "org.eclipse.vorto.editor.infomodel.InformationModel";
	}

	@Override
	protected String getFunctionblockEditorId() {
		return "org.eclipse.vorto.editor.functionblock.Functionblock";
	}

	@Override
	protected String getDatatypeEditorId() {
		return "org.eclipse.vorto.editor.datatype.Datatype";
	}

}
