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
package org.eclipse.vorto.perspective.preferences;

import java.net.URL;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class VortoPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public VortoPreferencePage() {
	}
	
	public VortoPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public VortoPreferencePage(String title) {
		super(title);
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		Label imageLabel = new Label(parent, SWT.CENTER);
		imageLabel.setImage(getVortoOverviewImage());

		noDefaultAndApplyButton();
		return parent;
	}

	private Image getVortoOverviewImage() {
		try {
			URL url = new URL(
					"platform:/plugin/org.eclipse.vorto.perspective/icons/vorto-overview.png");
			return ImageDescriptor.createFromURL(url).createImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}