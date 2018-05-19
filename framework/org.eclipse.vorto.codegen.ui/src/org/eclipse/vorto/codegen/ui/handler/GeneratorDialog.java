/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.vorto.codegen.api.GeneratorInfo.BinaryConfigurationItem;
import org.eclipse.vorto.codegen.api.GeneratorInfo.ChoiceConfigurationItem;
import org.eclipse.vorto.codegen.api.GeneratorInfo.ConfigurationItem;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.ui.progressmonitor.VortoCodeGenProgressMonitorFactory;
import org.eclipse.vorto.codegen.ui.progressmonitor.VortoProgressMonitor;
import org.eclipse.vorto.codegen.ui.utils.PlatformUtils;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.ui.MessageDisplayFactory;
import org.eclipse.vorto.core.ui.exception.ExceptionHandlerFactory;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.core.ui.model.ModelProjectFactory;

public class GeneratorDialog extends TitleAreaDialog {

	private IVortoCodeGenerator generator;
	private IGeneratorLookup lookupService;
	
	private Map<String,String> parameters = new HashMap<String, String>();

	public GeneratorDialog(Shell parentShell, IVortoCodeGenerator generator, IGeneratorLookup lookupService) {
		super(parentShell);
		this.generator = generator;
		this.lookupService = lookupService;
	}

	@Override
	public void create() {
		super.create();
		setTitle(generator.getInfo().getName());
		setMessage(generator.getInfo().getDescription(), IMessageProvider.INFORMATION);
		Button generateButton = getButton(IDialogConstants.OK_ID);
		generateButton.setText("Generate");
		generateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					IModelElement selectedElement = ModelProjectFactory.getInstance().getModelElementFromSelection();
					if (selectedElement == null) {
						selectedElement = ModelProjectFactory.getInstance().getSelectedModel();
					}

					InformationModel informationModel = getInformationModel(selectedElement.getModel());

					IGenerationResult result = generator.generate(informationModel,
							createInvocationContext(selectedElement.getProject(), generator.getServiceKey()),
							VortoCodeGenProgressMonitorFactory.getCodeGenStatusReporter());
					CodeGenerationHelper.createEclipseProject(ModelIdFactory.newInstance(informationModel),
							generator.getServiceKey(), result);
					VortoProgressMonitor.getDefault().display();
					PlatformUtils.switchPerspective(PlatformUtils.JAVA_PERSPECTIVE);
				} catch (Exception e1) {
					ExceptionHandlerFactory.getHandler().handle(e1);
				}
			}
		});
		getButton(IDialogConstants.CANCEL_ID).setText("Close");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		ScrolledComposite scrolledComposite = new ScrolledComposite(area, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_scrolledComposite.heightHint = 30;
		scrolledComposite.setLayoutData(gd_scrolledComposite);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		if (generator.getInfo().isConfigurable()) {
			createGeneratorConfigUI(area);
		}
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return area;
	}

	private void createGeneratorConfigUI(Composite container) {
		for (ConfigurationItem item : this.generator.getInfo().getConfigurationItems()) {
			
			Group genderGroup = new Group(container, SWT.NONE);
			genderGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
			if (item instanceof ChoiceConfigurationItem) {
				genderGroup.setText(item.getLabel());
				ChoiceConfigurationItem choiceItem = (ChoiceConfigurationItem)item;
				
				for (String choice : choiceItem.getChoices()) {
					Button choiceButton = new Button(genderGroup, SWT.RADIO);
					choiceButton.setText(choice);
					choiceButton.addSelectionListener(new SelectionAdapter() {
					    @Override
					    public void widgetSelected(SelectionEvent event) {
					    	Button btn = (Button) event.getSource();
					    	parameters.put(item.getKey(), btn.getText());
					    }
					});
				}
				
				if (genderGroup.getChildren().length > 0) {
					Button firstControl = (Button)genderGroup.getChildren()[0];
					firstControl.setSelection(true);
				}
				
			
			} 
			if (item instanceof BinaryConfigurationItem) {
				Button checkbox = new Button(genderGroup, SWT.CHECK);
				checkbox.setText(item.getLabel());
				checkbox.addSelectionListener(new SelectionAdapter() {
				    @Override
				    public void widgetSelected(SelectionEvent event) {
				    	Button btn = (Button) event.getSource();
				    	parameters.put(item.getKey(), Boolean.toString(btn.getSelection()));
				       
				    }
				});
				
			}
		} 
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	public IVortoCodeGenerator getGenerator() {
		return generator;
	}

	public IGeneratorLookup getLookupService() {
		return lookupService;
	}

	private InformationModel getInformationModel(Model model) {
		if (model instanceof InformationModel) {
			return (InformationModel) model;
		} else if (model instanceof FunctionblockModel) {
			return Utils.wrapFunctionBlock((FunctionblockModel) model);
		}
		throw new IllegalArgumentException("Cannot generate from selected model");
	}

	private InvocationContext createInvocationContext(IModelProject project, String targetPlatform) {
		return new InvocationContext(project.getMapping(targetPlatform), lookupService, parameters);
	}

}
