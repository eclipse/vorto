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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.vorto.codegen.ui.handler.PopulateGeneratorsMenu;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.command.DeleteModelAction;
import org.eclipse.vorto.perspective.command.ProjectAction;
import org.eclipse.vorto.perspective.command.ShareModelAction;
import org.eclipse.vorto.perspective.dnd.ModelDropListenerFactory;
import org.eclipse.vorto.perspective.util.ImageUtil;
import org.eclipse.vorto.wizard.infomodel.InfomodelWizard;

import com.google.common.collect.Collections2;

public class InfomodelTreeViewer extends ModelTreeViewer {

	public InfomodelTreeViewer(Composite parent, ILocalModelWorkspace localProjectBrowser) {
		super(parent, localProjectBrowser);
	}

	@Override
	public void init() {
		super.init();

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };

		treeViewer.addDropSupport(operations, transferTypes,
				ModelDropListenerFactory.infomodelViewPartDropListener(treeViewer));
	}

	protected void initContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenuIM");

		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {

				if (!treeViewer.getStructuredSelection().isEmpty()) {
					final IModelElement model = (IModelElement) treeViewer.getStructuredSelection().getFirstElement();

					if (model.getId().getModelType() == ModelType.InformationModel) {
						
						if (modelCanGenerate(model)) {
							MenuManager generatorMenuMgr = new MenuManager("Generate Code");
							generatorMenuMgr.add(new PopulateGeneratorsMenu());
							menuMgr.add(generatorMenuMgr);
						}
						
						menuMgr.add(ShareModelAction.newInstance(treeViewer, model));
						menuMgr.add(DeleteModelAction.newInstance(localModelWorkspace, treeViewer, model));
						menuMgr.add(new ProjectAction("New Mapping Model",ImageUtil.getImage("add_exc.gif"),treeViewer.getLocalModelWorkspace()) {
							@Override
							public void doAction() {
								openMappingWizard();
							}
						});
					}
				}

				manager.add(new ProjectAction("New Information Model", ImageUtil.getImage("add_exc.gif"),
						treeViewer.getLocalModelWorkspace()) {
					@Override
					public void doAction() {
						WizardDialog wizardDialog = new WizardDialog(treeViewer.getControl().getShell(),
								new InfomodelWizard(localModelWorkspace.getProjectBrowser().getSelectedProject()));
						if (wizardDialog.open() == Window.OK) {
							populate(localModelWorkspace.getProjectBrowser().getSelectedProject()
									.getModelElementsByType(ModelType.InformationModel));
						}
					}
				});

			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		this.treeViewer.getControl().setMenu(menu);
	}
	
	// check if model has no syntax errors
	private boolean modelCanGenerate(IModelElement modelElement) {
		Model model = modelElement.getModel();
		if (model instanceof InformationModel) {
			InformationModel infoModel = (InformationModel) model;
			for(FunctionblockProperty property : infoModel.getProperties()) {				
				// for syntax and parsing errors
				if (property.getType().eResource().getErrors().size() > 0) {
					return false;
				}
				
				// for linking errors
				if (getLinkingErrors(property.getType()).size() > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	private Collection<Diagnostic> getLinkingErrors(EObject model) {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(model);
		switch (diagnostic.getSeverity()) {
		  case Diagnostic.ERROR:
			  return diagnostic.getChildren();
		}
		return Collections.emptyList();
	}

	@Override
	protected String getLabel() {
		return "Information Models";
	}
}
