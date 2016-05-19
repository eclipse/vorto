package org.eclipse.vorto.perspective.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.model.nature.VortoProjectNature;
import org.eclipse.vorto.perspective.listener.ProjectSelectionListener;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class ProjectSelectionViewPart extends ViewPart {

	public static final String PROJECT_SELECT_VIEW_ID = "org.eclipse.vorto.perspective.view.ProjectSelectionViewPart"; 
	private ComboViewer comboViewer;
	private Combo combo; 
	
	private IProject selectedProject = null;
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;

	public ProjectSelectionViewPart() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		init();
		Composite container = new Composite(parent, SWT.NONE);
		
		Label lblSelectVortoProject = new Label(container, SWT.NONE);
		lblSelectVortoProject.setBounds(10, 10, 111, 13);
		lblSelectVortoProject.setText("Select Vorto Project:");
		
		comboViewer = new ComboViewer(container, SWT.NONE);
		
		comboViewer.setContentProvider(contentProvider);
		comboViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof IProject) {
					IProject project = (IProject) element;
					return project.getName();
				}
				return super.getText(element);
			}
		});
		
		combo = comboViewer.getCombo();
		combo.setBounds(127, 7, 252, 21);
		comboViewer.setInput(getAllVortoProjects());
		//combo.setItems(getAllVortoProjectNames());

		createActions();
		setInitialSelection();
		
		getSite().setSelectionProvider(comboViewer);
		
		//getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(PROJECT_SELECT_VIEW_ID,new ProjectSelectionListener());
		//initializeToolBar();
		//initializeMenu();
	}

	private void init() {
		contentProvider = ArrayContentProvider.getInstance();
		
		
	}

	private void setInitialSelection() {
		List<IProject> allVortoProjects = getAllVortoProjects();
		if(allVortoProjects != null && allVortoProjects.size() > 0 ) {
			selectedProject = allVortoProjects.get(0);
		}
	}

	private List<IProject> getAllVortoProjects() {
		
		
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
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
		}).collect(Collectors.<IProject>toList());
		
		return vortoProjects;
		
		/*for (IProject iProject : allProjects) {
			try {
				if(iProject.isOpen()){
					iProject.hasNature(VortoProjectNature.VORTO_NATURE);
					if(iProject.hasNature(VortoProjectNature.VORTO_NATURE)) {
						System.out.println("Name:" + iProject.getName());
					} else {
						System.out.println("Null:" + iProject.getName());
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}*/
		
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
		
		vortoProjects.stream().forEach(prj -> prj.getName());*/
		
		/*List<String> vortoProjectNames = FluentIterable
				.from(allProjects).filter(new Predicate<IProject>() {
					@Override
					public boolean apply(IProject project) {
						try {
							return project.isOpen() && project.hasNature(VortoProjectNature.VORTO_NATURE);
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
		return vortoProjectNames.toArray(new String[vortoProjectNames.size()]);*/
		
		
		/*List<String> vortoProjectNames = Lists.transform(vortoProjects, new Function<IProject, String>(){

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
		
		/*String[] someNames = {"One", "Two", "Three"};
		return someNames;*/
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
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
			}
		});
		
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
