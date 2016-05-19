package org.eclipse.vorto.perspective.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.model.nature.VortoProjectNature;
import org.eclipse.vorto.perspective.contentprovider.DefaultTreeModelContentProvider;
import org.eclipse.vorto.perspective.labelprovider.DefaultTreeModelLabelProvider;

public class ProjectSelectionViewPart extends ViewPart {

	public static final String PROJECT_SELECT_VIEW_ID = "org.eclipse.vorto.perspective.view.ProjectSelectionViewPart";

	protected ComboViewer projectSelectionViewer;
	protected TreeViewer datatypeTreeViewer;
	protected TreeViewer functionBlockTreeViewer;
	protected TreeViewer infoModelTreeViewer;
	
	
	private IProject selectedProject = null;
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;	

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

		Composite viewerComposite = createViewerComposite(container, projectSelectionViewer.getCombo());
		
		projectSelectionViewer.setContentProvider(contentProvider);
		
		projectSelectionViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof IProject) {
					IProject project = (IProject) element;
					return project.getName();
				}
				return super.getText(element);
			}
		});
		
		projectSelectionViewer.setInput(getAllVortoProjects());
		
		createActions();
		setInitialSelection();
		
		getSite().setSelectionProvider(projectSelectionViewer);

		/*
		 * datatypeTreeViewer = createTreeViewer(viewerComposite, "Datatypes",
		 * 30); // populate with test data datatypeTreeViewer.setInput(new
		 * TreeSet<IModelElement>(ModelProjectServiceFactory
		 * .getDefault().getProjectsInWorkspace(ModelType.Datatype)));
		 * 
		 * functionBlockTreeViewer = createTreeViewer(viewerComposite,
		 * "Function Blocks", 60); // populate with test data
		 * functionBlockTreeViewer.setInput(new
		 * TreeSet<IModelElement>(ModelProjectServiceFactory
		 * .getDefault().getProjectsInWorkspace(ModelType.Functionblock)));
		 * 
		 * infoModelTreeViewer = createTreeViewer(viewerComposite,
		 * "Information Models", 100); // populate with test data
		 * infoModelTreeViewer.setInput(new
		 * TreeSet<IModelElement>(ModelProjectServiceFactory
		 * .getDefault().getProjectsInWorkspace(ModelType.InformationModel)));
		 */

		// createActions();
		// initializeToolBar();
		// initializeMenu();
	}

	private void setInitialSelection() {
		List<IProject> allVortoProjects = getAllVortoProjects();
		if(allVortoProjects != null && allVortoProjects.size() > 0 ) {
			selectedProject = allVortoProjects.get(0);
		}
	}

	private void createActions() {
		/*projectSelectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
			}
		});*/
		
	}

	private ComboViewer createProjectSelectionViewer(Composite container, String labelStr) {
		Label label = new Label(container, SWT.NONE);
		label.setText(labelStr);

		FormData labelFormData = new FormData();
		labelFormData.top = new FormAttachment(0, 0);
		labelFormData.left = new FormAttachment(0, 0);

		label.setLayoutData(labelFormData);

		ComboViewer comboViewer = new ComboViewer(container, SWT.NONE);
		Combo combo = comboViewer.getCombo();

		FormData viewerFormData = new FormData();
		viewerFormData.top = new FormAttachment(0, 0);
		viewerFormData.left = new FormAttachment(label, 10);
		viewerFormData.right = new FormAttachment(100, 0);

		combo.setLayoutData(viewerFormData);

		return comboViewer;
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

	private void init() {
		contentProvider = ArrayContentProvider.getInstance();
	}
	private TreeViewer createTreeViewer(Composite container, String labelStr, int parentCompositeHeight) {
		Composite composite = new Composite(container, SWT.BORDER);

		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;

		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setText(labelStr);

		FormData lblFormData = new FormData();
		lblFormData.top = new FormAttachment(0, 0);
		lblFormData.left = new FormAttachment(0, 0);
		lblFormData.right = new FormAttachment(100, 0);

		label.setLayoutData(lblFormData);

		TreeViewer treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData viewerFormData = new FormData();
		viewerFormData.top = new FormAttachment(label, 10);
		viewerFormData.left = new FormAttachment(0, 0);
		viewerFormData.right = new FormAttachment(100, 0);
		viewerFormData.bottom = new FormAttachment(100, 0);

		treeViewer.getTree().setLayoutData(viewerFormData);

		treeViewer.setContentProvider(new DefaultTreeModelContentProvider());
		treeViewer.setLabelProvider(new DefaultTreeModelLabelProvider());

		treeViewer.setUseHashlookup(true);

		ColumnViewerToolTipSupport.enableFor(treeViewer);

		return treeViewer;
	}

	private List<IProject> getAllVortoProjects() {

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> allProjects = Arrays.asList(projects);

		List<IProject> vortoProjects = allProjects.stream().filter(new java.util.function.Predicate<IProject>() {

			@Override
			public boolean test(IProject project) {
				try {
					return project.isOpen() && project.hasNature(VortoProjectNature.VORTO_NATURE);
				} catch (CoreException e) {
					e.printStackTrace();
					return false;
				}
			}
		}).collect(Collectors.<IProject> toList());

		return vortoProjects;

	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
