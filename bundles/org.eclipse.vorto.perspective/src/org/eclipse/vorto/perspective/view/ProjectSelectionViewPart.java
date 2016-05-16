package org.eclipse.vorto.perspective.view;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.vorto.core.model.nature.IoTProjectNature;
import org.eclipse.vorto.core.model.nature.VortoProjectNature;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;

public class ProjectSelectionViewPart extends ViewPart {

	public static final String PROJECT_SELECT_VIEW_ID = "org.eclipse.vorto.perspective.view.ProjectSelectionViewPart"; 

	public ProjectSelectionViewPart() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Label lblSelectVortoProject = new Label(container, SWT.NONE);
		lblSelectVortoProject.setBounds(10, 10, 111, 13);
		lblSelectVortoProject.setText("Select Vorto Project:");
		
		ComboViewer comboViewer = new ComboViewer(container, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setBounds(127, 7, 340, 26);
		
		combo.setItems(getAllVortoProjectNames());

		createActions();
		//initializeToolBar();
		initializeMenu();
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
