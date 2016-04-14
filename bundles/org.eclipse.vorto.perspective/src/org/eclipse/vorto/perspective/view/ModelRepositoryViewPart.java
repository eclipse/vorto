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
package org.eclipse.vorto.perspective.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.containers.LocalFileStorage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.api.repository.GeneratorResource;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.perspective.contentprovider.ModelRepositoryContentProvider;
import org.eclipse.vorto.perspective.dnd.ModelDragListener;
import org.eclipse.vorto.perspective.labelprovider.ModelRepositoryLabelProvider;
import org.eclipse.vorto.perspective.util.ViewPartUtil;
import org.eclipse.xtext.ui.editor.XtextReadonlyEditorInput;

import com.google.common.io.Files;

public class ModelRepositoryViewPart extends ViewPart {

	private static final String TEMP_MODEL_PREFIX = "temp_model_";

	private static final String INFOMODEL_EXT = ".infomodel";

	private static final String FBMODEL_EXT = ".fbmodel";

	private static final String DATATYPE_EXT = ".type";

	private static final String INFOMODEL_EDITOR_ID = "org.eclipse.vorto.editor.infomodel.InformationModel";

	private static final String FUNCTIONBLOCK_EDITOR_ID = "org.eclipse.vorto.editor.functionblock.Functionblock";

	private static final String DATATYPE_EDITOR_ID = "org.eclipse.vorto.editor.datatype.Datatype";

	private static final String VERSION = "Version";

	private static final String NAME = "Name";

	private static final String NAMESPACE = "Namespace";

	private static final String DESCRIPTION = "Description";

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipse.vorto.perspective.views.ModelRepositoryViewPart";

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();

	private TableViewer viewer;

	private Text searchField;

	class NameSorter extends ViewerSorter {
	}

	public ModelRepositoryViewPart() {
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());

		Button btnSearch = createSearchButton(parent);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String filter = searchField.getText();
				try {
					viewer.setInput(modelRepo.search(filter));
				} catch (Exception ex) {
					MessageDisplayFactory.getMessageDisplay().displayError(ex.getCause());
				}
			}
		});

		searchField = createSearchField(parent, btnSearch);

		viewer = createTableViewer(parent, btnSearch);

		addOpenSharedModelInEditorListener(viewer);

		initContextMenu();
	}

	private void addOpenSharedModelInEditorListener(TableViewer viewer) {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page == null) {
			MessageDisplayFactory.getMessageDisplay().displayError("Cannot get active workbench page. Opening shared model in Editor not possible.");
			return;
		}
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				
				ModelResource resource = (ModelResource) ViewPartUtil.getFirstSelectedObject(ModelResource.class, event);
				
				if (resource != null) {
					try {
						// Create temporary file
						File file = File.createTempFile(TEMP_MODEL_PREFIX,
								getExtension(resource.getId().getModelType()));
						
						// Download shared model and put in temporary file
						Files.write(modelRepo.downloadContent(resource.getId()), file);
						
						// Open temporary file in editor
						IEditorPart editor = openFileInEditor(page, file, resource.getId().getModelType());
						
						// Add listener to editor close event, so we can delete the file when editor is closed
						if (editor != null) {
							page.addPartListener(onEditorCloseListener(page, editor, file));
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
	}
	
	private IPartListener onEditorCloseListener(final IWorkbenchPage page, final IWorkbenchPart editor, final File file) {
		return new IPartListener() {
			public void partActivated(IWorkbenchPart part) {}
			public void partBroughtToTop(IWorkbenchPart part) {}
			public void partDeactivated(IWorkbenchPart part) {}
			public void partOpened(IWorkbenchPart part) {}
			
			public void partClosed(IWorkbenchPart part) {
				if (part == editor) {
					if (file.delete()) {
						page.removePartListener(this);
					}
				}
			}
		};
	}

	private IEditorPart openFileInEditor(IWorkbenchPage page, File file, ModelType modelType) {
		if (file.exists()) {
			try {
				return IDE.openEditor(page, new XtextReadonlyEditorInput(new LocalFileStorage(file)), getEditorId(modelType), true);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	private String getEditorId(ModelType modelType) {
		if (modelType == ModelType.Datatype) {
			return DATATYPE_EDITOR_ID;
		} else if (modelType == ModelType.Functionblock) {
			return FUNCTIONBLOCK_EDITOR_ID;
		} else {
			return INFOMODEL_EDITOR_ID;
		}
	}

	private String getExtension(ModelType modelType) {
		if (modelType == ModelType.Datatype) {
			return DATATYPE_EXT;
		} else if (modelType == ModelType.Functionblock) {
			return FBMODEL_EXT;
		} else {
			return INFOMODEL_EXT;
		}
	}

	/**
	 * Create the context menu
	 *
	 */
	private void initContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (viewer.getSelection().isEmpty()) {
					return;
				}
				ModelResource model = (ModelResource) viewer.getStructuredSelection().getFirstElement();
				if (model.getId().getModelType() == ModelType.InformationModel) {
					addListGeneratorsToMenu(manager, model);
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getControl().setMenu(menu);
	}

	/**
	 * Fill context menu
	 *
	 * @param contextMenu
	 */
	protected void addListGeneratorsToMenu(IMenuManager contextMenu, final ModelResource model) {
		contextMenu.add(new Action("Generate code") {
			@Override
			public void run() {
				List<GeneratorResource> codegens = modelRepo.listGenerators();
				GeneratorDialog dialog = new GeneratorDialog(new Shell(), model, codegens);
				dialog.create();
				dialog.open();
			}
		});
	}

	private TableViewer createTableViewer(Composite parent, Button btnSearch) {
		TableViewer viewer = new TableViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(btnSearch, 2);
		fd_table.left = new FormAttachment(searchField, 0, SWT.LEFT);
		fd_table.bottom = new FormAttachment(100, -5);
		fd_table.right = new FormAttachment(100, -10);
		viewer.getTable().setLayoutData(fd_table);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		String[] columnLabels = { "", NAMESPACE, NAME, VERSION, DESCRIPTION };
		int[] columnBounds = { 50, 300, 200, 100, 400 };
		for (int i = 0; i < columnLabels.length; i++) {
			createTableViewerColumn(viewer, columnLabels[i], columnBounds[i]);
		}

		viewer.setContentProvider(new ModelRepositoryContentProvider());
		viewer.setLabelProvider(new ModelRepositoryLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		viewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE, new Transfer[] { LocalSelectionTransfer.getTransfer() },
				new ModelDragListener(viewer));

		return viewer;
	}

	private TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private Text createSearchField(Composite parent, Button btnSearch) {
		Text field = new Text(parent, SWT.BORDER);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 23);
		formData.right = new FormAttachment(btnSearch, -10);
		field.setLayoutData(formData);

		return field;
	}

	private Button createSearchButton(Composite parent) {
		Button button = new Button(parent, SWT.NONE);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(100, -110);
		formData.right = new FormAttachment(100, -10);
		button.setLayoutData(formData);
		button.setText("Search");

		return button;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}