/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.core.ui.model.ModelParserFactory;
import org.eclipse.vorto.core.ui.model.VortoModelProject;
import org.eclipse.vorto.perspective.listener.RemoveModelProjectListener;
import org.eclipse.vorto.perspective.util.ImageUtil;
import org.eclipse.vorto.perspective.util.NullModelProject;
import org.eclipse.vorto.perspective.view.ILocalModelWorkspace.IModelProjectBrowser;
import org.eclipse.vorto.wizard.vorto.VortoProjectWizard;
import org.eclipse.vorto.wizard.vorto.VortoProjectWizardPage;

public class ProjectSelectionViewPart extends ViewPart implements ILocalModelWorkspace, IModelProjectBrowser {

	public static final String PROJECT_SELECT_VIEW_ID = "org.eclipse.vorto.perspective.view.ProjectSelectionViewPart";

	protected ComboViewer projectSelectionViewer;

	private IModelProject selectedProject = null;

	protected DatatypeTreeViewer datatypeTreeViewer;
	protected FunctionblockTreeViewer functionBlockTreeViewer;
	protected InfomodelTreeViewer infoModelTreeViewer;
	
	private IResourceChangeListener removeModelProjectListener = null;

	public ProjectSelectionViewPart() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);

		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;

		container.setLayout(layout);

		projectSelectionViewer = createProjectSelectionViewer(container, "Select Vorto Project");

		Composite modelPanel = createViewerComposite(container, projectSelectionViewer.getCombo());
		
		projectSelectionViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof IModelProject) {
					IModelProject project = (IModelProject) element;
					return project.getProject().getName();
				}
				return super.getText(element);
			}
		});

		projectSelectionViewer.setContentProvider(ArrayContentProvider.getInstance());
		projectSelectionViewer.setInput(getModelProjects());

		projectSelectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection iSelection = (IStructuredSelection) event.getSelection();
					IModelProject project = (IModelProject)iSelection.getFirstElement();
					selectedProject = project;
					
					populate(selectedProject);
				}
			}
		});
		
		hookListeners();
		
		datatypeTreeViewer = new DatatypeTreeViewer(modelPanel,this);
		functionBlockTreeViewer = new FunctionblockTreeViewer(modelPanel,this);
		infoModelTreeViewer = new InfomodelTreeViewer(modelPanel,this);
		
		getSite().setSelectionProvider(infoModelTreeViewer.treeViewer);
		
		if (!getModelProjects().isEmpty()) {
			setSelectedProject(getModelProjects().iterator().next());
		}
		
	}
	
	protected void hookListeners() {
		addWorkspaceChangeEventListenr();
	}
	
	protected void addWorkspaceChangeEventListenr() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		this.removeModelProjectListener = new RemoveModelProjectListener(this);
		workspace.addResourceChangeListener(removeModelProjectListener,IResourceChangeEvent.PRE_DELETE);
	}
	
	private void setSelectedProject(final IModelProject project) {
		projectSelectionViewer.setSelection(new StructuredSelection(project),true);
	}
	
	public void populate(IModelProject modelProject) {
		datatypeTreeViewer.populate(modelProject.getModelElementsByType(ModelType.Datatype));
		functionBlockTreeViewer.populate(modelProject.getModelElementsByType(ModelType.Functionblock));
		infoModelTreeViewer.populate(modelProject.getModelElementsByType(ModelType.InformationModel));
	}
	
	public void refresh() {
		for (IModelProject project : getModelProjects()) {
			populate(project);
		}
	}

	private ComboViewer createProjectSelectionViewer(final Composite container, String labelStr) {
		
		Label label = new Label(container, SWT.NONE);
		label.setText(labelStr);

		FormData labelFormData = new FormData();
		labelFormData.top = new FormAttachment(0, 0);
		labelFormData.left = new FormAttachment(0, 0);

		label.setLayoutData(labelFormData);

		ComboViewer comboViewer = new ComboViewer(container, SWT.READ_ONLY);
		
		Combo combo = comboViewer.getCombo();

		FormData viewerFormData = new FormData();
		viewerFormData.top = new FormAttachment(0, 0);
		viewerFormData.left = new FormAttachment(label, 10);
		viewerFormData.right = new FormAttachment(70,10);
		combo.setLayoutData(viewerFormData);
		
		Button button = new Button(container, SWT.NONE);
		button.setImage(ImageUtil.getImage("add_exc.gif"));
		button.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		        switch (e.type) {
		        case SWT.Selection:
		        	WizardDialog wizardDialog = new WizardDialog(container.getShell(),new VortoProjectWizard());
		        	if (wizardDialog.open() == Window.OK) {
		        		projectSelectionViewer.setInput(getModelProjects());
		        		VortoProjectWizardPage page = (VortoProjectWizardPage)wizardDialog.getCurrentPage();
		        		IModelProject project = getModelProjectFromName(page.getProjName());
		        		selectedProject = project;
		        		projectSelectionViewer.setSelection(new StructuredSelection(project),true);
		        	}
		          break;
		        }
		      }
		    });
		
		FormData buttonFormdata = new FormData();
		buttonFormdata.top = new FormAttachment(0, 0);
		buttonFormdata.left = new FormAttachment(combo, 10);
		buttonFormdata.right = new FormAttachment(100, 0);

		button.setLayoutData(buttonFormdata);
				
		return comboViewer;
	}
	
	private IModelProject getModelProjectFromName(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return new VortoModelProject(project, ModelParserFactory.getInstance().getModelParser());
	}

	private Composite createViewerComposite(Composite container, Control previousControl) {
		Composite viewerComposite = new Composite(container, SWT.NONE);

		FormData formData = new FormData();
		formData.top = new FormAttachment(previousControl, 10);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		viewerComposite.setLayoutData(formData);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.spacing = 5;
		viewerComposite.setLayout(fillLayout);

		return viewerComposite;
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
	public void removeProject(IModelProject modelProject) {
		final Collection<IModelProject> projects = this.getModelProjects();
		projects.remove(modelProject);
		
		if (!Display.getDefault().isDisposed()) {
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					try {
						projectSelectionViewer.setInput(projects);
						if (!projects.isEmpty()) {
							setSelectedProject(projects.iterator().next());
						} else {
							populate(new NullModelProject());
							selectedProject = null;
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(removeModelProjectListener);
	}
	
	public IModelProject getSelectedProject() {
		return this.selectedProject;
	}

	@Override
	public Collection<IModelProject> getModelProjects() {
		List<IModelProject> vortoModelProjects = new ArrayList<IModelProject>();

		for (IProject projectInWorkspace : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (projectInWorkspace.isOpen() && VortoModelProject.isVortoModelProject(projectInWorkspace)) {
				vortoModelProjects.add(
						new VortoModelProject(projectInWorkspace, ModelParserFactory.getInstance().getModelParser()));
			}
		}
		
		return vortoModelProjects;
	}

	@Override
	public IModelProjectBrowser getProjectBrowser() {
		return this;
	}

	@Override
	public Shell getShell() {
		return projectSelectionViewer.getControl().getShell();
	}
}
