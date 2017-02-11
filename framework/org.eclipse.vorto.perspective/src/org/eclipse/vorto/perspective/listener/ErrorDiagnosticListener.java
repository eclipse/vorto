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
package org.eclipse.vorto.perspective.listener;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.vorto.perspective.util.Predicates;

import com.google.common.collect.Collections2;

public class ErrorDiagnosticListener extends DisplayRunnableExecutioner implements IResourceChangeListener {

	public ErrorDiagnosticListener(Runnable runnable) {
		super(runnable);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		Collection<IMarkerDelta> markerWithErrors = Collections2
				.filter(Arrays.asList(event.findMarkerDeltas(null, true)), Predicates.isVortoModelWithMarkerError);
		if (!markerWithErrors.isEmpty()) {
			executeRunnableOnDisplayThread();
		}
	}
}
