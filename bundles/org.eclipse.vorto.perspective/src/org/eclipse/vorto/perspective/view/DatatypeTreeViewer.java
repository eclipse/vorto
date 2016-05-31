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

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.command.DeleteModelAction;
import org.eclipse.vorto.perspective.command.ProjectAction;
import org.eclipse.vorto.perspective.command.ShareModelAction;
import org.eclipse.vorto.perspective.dnd.ModelDragListener;
import org.eclipse.vorto.perspective.dnd.ModelDropListenerFactory;
import org.eclipse.vorto.perspective.util.ImageUtil;
import org.eclipse.vorto.wizard.datatype.EntityWizard;
import org.eclipse.vorto.wizard.datatype.EnumWizard;

public class DatatypeTreeViewer extends ModelTreeViewer {

	public DatatypeTreeViewer(Composite parent, ILocalModelWorkspace localModelBrowser) {
		super(parent, localModelBrowser);
	}

	@Override
	protected void init() {
		super.init();

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		treeViewer.addDragSupport(operations, transferTypes, new ModelDragListener(treeViewer));
		treeViewer.addDropSupport(operations, transferTypes,
				ModelDropListenerFactory.datatypeViewPartDropListener(treeViewer));
	}

	@Override
	protected String getLabel() {
		return "Datatypes";
	}

	protected void initContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenuDt");

		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				if (!treeViewer.getStructuredSelection().isEmpty()) {
					IModelElement model = (IModelElement) treeViewer.getStructuredSelection().getFirstElement();

					if (model.getId().getModelType() == ModelType.Datatype) {
						menuMgr.add(new ShareModelAction() {

							@Override
							protected TreeViewer getViewer() {
								return treeViewer;
							}

							@Override
							protected IModelElement getSelectedElement() {
								return model;
							}
						});
						menuMgr.add(new DeleteModelAction(localModelWorkspace) {
							
							@Override
							protected TreeViewer getViewer() {
								return treeViewer;
							}
							
							@Override
							protected IModelElement getSelectedElement() {
								return model;
							}
						});
						menuMgr.add(new ProjectAction("New Mapping Model",ImageUtil.getImage("add_exc.gif"),treeViewer.getLocalModelWorkspace()) {
							@Override
							public void doAction() {
								openMappingWizard();
							}
						});
					}
				}

				manager.add(new ProjectAction("New Entity",ImageUtil.getImage("add_exc.gif"),treeViewer.getLocalModelWorkspace()) {
					@Override
					public void doAction() {
						WizardDialog wizardDialog = new WizardDialog(treeViewer.getControl().getShell(),
								new EntityWizard(treeViewer.getLocalModelWorkspace().getProjectBrowser().getSelectedProject()));
						if (wizardDialog.open() == Window.OK) {
							populate(treeViewer.getLocalModelWorkspace().getProjectBrowser().getSelectedProject()
									.getModelElementsByType(ModelType.Datatype));
						}
					}
				});

				manager.add(new ProjectAction("New Enum",ImageUtil.getImage("add_exc.gif"),treeViewer.getLocalModelWorkspace()) {
					@Override
					public void doAction() {
						WizardDialog wizardDialog = new WizardDialog(treeViewer.getControl().getShell(),
								new EnumWizard(treeViewer.getLocalModelWorkspace().getProjectBrowser().getSelectedProject()));
						if (wizardDialog.open() == Window.OK) {
							populate(treeViewer.getLocalModelWorkspace().getProjectBrowser().getSelectedProject()
									.getModelElementsByType(ModelType.Datatype));
						}
					}
				});
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		this.treeViewer.getControl().setMenu(menu);
	}
}
