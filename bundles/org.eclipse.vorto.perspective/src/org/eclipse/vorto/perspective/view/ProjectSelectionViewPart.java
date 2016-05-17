package org.eclipse.vorto.perspective.view;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.nature.VortoProjectNature;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.contentprovider.DefaultTreeModelContentProvider;
import org.eclipse.vorto.perspective.labelprovider.DefaultTreeModelLabelProvider;

public class ProjectSelectionViewPart extends ViewPart {

	public static final String PROJECT_SELECT_VIEW_ID = "org.eclipse.vorto.perspective.view.ProjectSelectionViewPart"; 

	protected ComboViewer projectSelectionViewer;
	protected TreeViewer datatypeTreeViewer;
	protected TreeViewer functionBlockTreeViewer;
	protected TreeViewer infoModelTreeViewer;

	
	public ProjectSelectionViewPart() {
	}

	
	/**
	 * Create contents of the view part.
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
		
		datatypeTreeViewer = createTreeViewer(viewerComposite, "Datatypes", 30);
		// populate with test data
		datatypeTreeViewer.setInput(new TreeSet<IModelElement>(ModelProjectServiceFactory
				.getDefault().getProjectsInWorkspace(ModelType.Datatype)));
		
		functionBlockTreeViewer = createTreeViewer(viewerComposite, "Function Blocks", 60);
		// populate with test data
		functionBlockTreeViewer.setInput(new TreeSet<IModelElement>(ModelProjectServiceFactory
						.getDefault().getProjectsInWorkspace(ModelType.Functionblock)));
		
		infoModelTreeViewer = createTreeViewer(viewerComposite, "Information Models", 100);
		// populate with test data
		infoModelTreeViewer.setInput(new TreeSet<IModelElement>(ModelProjectServiceFactory
						.getDefault().getProjectsInWorkspace(ModelType.InformationModel)));
		
		//createActions();
		//initializeToolBar();
		//initializeMenu();
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
		combo.setItems(getAllVortoProjectNames());
		
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
	
	private TreeViewer createTreeViewer(Composite container, String labelStr, int parentCompositeHeight) {
		Composite composite = new Composite (container, SWT.BORDER);
		
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
		
		TreeViewer treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
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

	private String[] getAllVortoProjectNames() {
		
		
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		List<IProject> allProjects = Arrays.asList(projects);
		
		for (IProject iProject : allProjects) {
			try {
				if(iProject.isOpen()){
					IProjectNature nature = iProject.getNature(VortoProjectNature.VORTO_NATURE);
					if(nature != null) {
						System.out.println("Name:" + iProject.getName());
					} else {
						System.out.println("Null:" + iProject.getName());
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		/*Collection<IProject> vortoProjects = Collections2.filter(allProjects, new Predicate<IProject>() {

			@Override
			public boolean apply(IProject project) {
				try {
					return project.getNature(VortoProjectNature.VORTO_NATURE) != null;
				} catch (CoreException e) {
					return false;
				}
			}
		});
		
		vortoProjects.stream().forEach(prj -> prj.getName());
		
		List<String> vortoProjectNames = FluentIterable
				.from(allProjects).filter(new Predicate<IProject>() {
					@Override
					public boolean apply(IProject project) {
						try {
							return project.getNature(VortoProjectNature.VORTO_NATURE) != null;
						} catch (CoreException e) {
							e.printStackTrace();
							return false;
						}
					}
				}).transform(new Function<IProject, String>() {
					@Override
					public String apply(IProject project) {
						return project.getName();
					}

				}).toList();
		        
		vortoProjectNames.stream().forEach(System.out::println);
		
		
		List<String> vortoProjectNames = Lists.transform(vortoProjects, new Function<IProject, String>(){

			@Override
			public String apply(IProject project) {
				try {
					if(project.getNature(VortoProjectNature.VORTO_NATURE) != null) {
						return project.getName();
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
				return null;
			}});
		
		Collections2.filter(vortoProjectNames, Predicates.isNull());
		
		for (String projectName : vortoProjectNames) {
			System.out.println("Names:::" + projectName);
		}*/
		
		String[] someNames = {"One", "Two", "Three"};
		return someNames;
		/*if(vortoProjectNames != null && vortoProjectNames.size() > 0) { 
			return vortoProjectNames.toArray(new String[vortoProjectNames.size()]);
		} else {
			return someNames;
		}*/
		

		
		/*Iterable<IProject> allVortoProjects = Iterables.filter(Arrays.asList(projects), new Predicate<IProject>() {
			@Override
			public boolean apply(IProject project) {
				
				try {
					return project.getNature(VortoProjectNature.VORTO_NATURE) != null;
				} catch (CoreException e) {
					e.printStackTrace();
				}
				return false;
			}
		});*/
		
		
		/*for (IProject iProject : allVortoProjects) {
			System.out.println("Project Names:::" + iProject.getName());
		}*/
		
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
