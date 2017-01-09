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
package org.eclipse.vorto.perspective.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * This selection provider can be used for view parts containing multiple controls for which particular selection providers should 
 * be set / unset
 *
 */
public class MultipleSelectionProvider implements ISelectionProvider {

	private ISelectionProvider provider;
	private List<ISelectionChangedListener> selectionListeners;
	private ISelection sel = StructuredSelection.EMPTY;

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (selectionListeners == null)
			selectionListeners = new ArrayList<ISelectionChangedListener>(1);
		selectionListeners.add(listener);
		if (provider != null)
			provider.addSelectionChangedListener(listener);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if (selectionListeners != null) {
			selectionListeners.remove(listener);
			if (provider != null)
				provider.removeSelectionChangedListener(listener);
		}
	}

	public ISelection getSelection() {
		return provider != null ? provider.getSelection() : sel;
	}

	public void setSelection(ISelection selection) {
		if (provider != null) {
			provider.setSelection(selection);
		} else {
			sel = selection;
			if (selectionListeners != null) {
				SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
				for (Iterator<ISelectionChangedListener> it = selectionListeners.iterator(); it.hasNext();) {
					it.next().selectionChanged(event);
				}
			}

		}
	}

	public void setSelectionProvider(ISelectionProvider provider) {
		if (this.provider != provider) {
			ISelection currentSelection = null;
			if (selectionListeners != null) {
				if (this.provider != null) {
					for (ISelectionChangedListener listener : selectionListeners) {
						this.provider.removeSelectionChangedListener(listener);
					}
				}

				if (provider != null) {
					for (ISelectionChangedListener listener : selectionListeners) {
						provider.addSelectionChangedListener(listener);
					}

					currentSelection = provider.getSelection();
				} else {
					currentSelection = sel;
				}
			}
			this.provider = provider;
			if (currentSelection != null) {
				setSelection(currentSelection); //enforce selection event
			}
		}
	}

	public ISelectionProvider getSelectionProvider() {
		return provider;
	}
}
