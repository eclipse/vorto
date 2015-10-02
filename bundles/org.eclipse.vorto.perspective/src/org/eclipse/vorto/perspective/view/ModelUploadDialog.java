/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.perspective.view;

import java.util.List;
import java.util.Objects;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.api.repository.UploadResult;
import org.eclipse.vorto.core.model.ModelId;

import com.google.common.base.Strings;

public class ModelUploadDialog extends TitleAreaDialog {

	// private static final String REFERENCES_LABEL = "References";
	private static final String DESCRIPTION_LABEL = "Description";
	private static final String DISPLAY_NAME_LABEL = "DisplayName";
	private static final String MODELTYPE_LABEL = "Modeltype";
	private static final String VERSION_LABEL = "Version";
	private static final String NAME_LABEL = "Name";
	private static final String NAMESPACE_LABEL = "Namespace";
	private static final String WINDOW_TITLE = "Upload result";
	private static final String ERROR_MSG = "ERROR - ";
	private static final String SUCCESS_MSG = "OK - Uploaded model is valid and ready to be checked in.";
	private static final String[] COLUMNS = new String[] { "Namespace", "Name", "Version", "ModelType" };

	private UploadResult uploadResult;

	public ModelUploadDialog(Shell parentShell, UploadResult uploadResult) {
		super(parentShell);
		this.uploadResult = Objects.requireNonNull(uploadResult, "uploadResult should not be null");
	}

	@Override
	public void create() {
		super.create();
		setTitle(WINDOW_TITLE);
		if (uploadResult.statusOk()) {
			setMessage(SUCCESS_MSG, IMessageProvider.INFORMATION);
		} else {
			setMessage(ERROR_MSG + uploadResult.getErrorMessage(), IMessageProvider.ERROR);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		String namespace = null;
		String name = null;
		String version = null;
		String modelType = null;
		String displayName = null;
		String description = null;
		
		ModelResource modelResource = uploadResult.getModelResource();
		if (modelResource != null) {
			ModelId id = modelResource.getId();
			if (id != null) {
				namespace = id.getNamespace();
				name = id.getName();
				version = id.getVersion();
				modelType = id.getModelType().toString();
			}
			displayName = modelResource.getDisplayName();
			description = modelResource.getDescription();
		}

		createField(container, NAMESPACE_LABEL, namespace);
		createField(container, NAME_LABEL, name);
		createField(container, VERSION_LABEL, version);
		createField(container, MODELTYPE_LABEL, modelType);
		createField(container, DISPLAY_NAME_LABEL, displayName);
		createField(container, DESCRIPTION_LABEL, description);
		// createField(container, REFERENCES_LABEL, references);
		createReferencesTable(container, modelResource.getReferences());

		return composite;
	}

	private void createField(Composite container, String label, String value) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText(label);

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(data);
		text.setText(Strings.nullToEmpty(value));
		text.setEditable(false);
	}

	private void createReferencesTable(Composite container, List<ModelId> references) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("References");

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		Table table = new Table(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setLayoutData(data);
		table.setLinesVisible (true);

		for (String columnName : COLUMNS) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnName);
		}

		if (references != null) {
			for (ModelId id : references) {
				TableItem item = new TableItem(table, 0);
				item.setText(0, id.getNamespace());
				item.setText(1, id.getName());
				item.setText(2, id.getVersion());
				item.setText(3, id.getModelType().toString());
			}
		}

		for (int i = 0; i < COLUMNS.length; i++) {
			table.getColumn(i).pack();
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		if (uploadResult.statusOk()) {
			getButton(OK).setEnabled(true);
		} else {
			getButton(OK).setEnabled(false);
		}
	}
}
