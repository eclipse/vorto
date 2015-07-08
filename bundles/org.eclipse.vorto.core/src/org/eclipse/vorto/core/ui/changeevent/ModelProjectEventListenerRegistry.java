/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.core.ui.changeevent;

import java.util.ArrayList;
import java.util.List;

public class ModelProjectEventListenerRegistry {
	private List<ModelProjectEventListener> eventReceivers = new ArrayList<ModelProjectEventListener>();
	private static ModelProjectEventListenerRegistry instance = new ModelProjectEventListenerRegistry();

	private ModelProjectEventListenerRegistry() {

	}

	public static ModelProjectEventListenerRegistry getInstance() {
		if (instance == null) {
			instance = new ModelProjectEventListenerRegistry();
		}
		return instance;
	}

	public void add(ModelProjectEventListener listener) {
		this.eventReceivers.add(listener);
	}

	public void remove(ModelProjectEventListener listener) {
		this.eventReceivers.remove(listener);
	}

	public void sendChangeEvent(ModelProjectChangeEvent projectChangeEvent) {
		for (ModelProjectEventListener listener : eventReceivers) {
			listener.onProjectChanged(projectChangeEvent);
		}
	}

	public void sendDeleteEvent(ModelProjectDeleteEvent deleteEvent) {
		for (ModelProjectEventListener listener : eventReceivers) {
			listener.onProjectDeleted(deleteEvent);
		}
	}

	public void sendAddedEvent(NewModelProjectEvent addedEvent) {
		for (ModelProjectEventListener listener : eventReceivers) {
			listener.onProjectAdded(addedEvent);
		}
	}
}
