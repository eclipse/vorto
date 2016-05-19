package org.eclipse.vorto.perspective.listener;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.vorto.perspective.view.ProjectSelectionViewPart;

public class ProjectSelectionListener implements ISelectionListener {
	
	private IProject selectedProject;

	@Override
	public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
		if (sourcepart instanceof ProjectSelectionViewPart) {
			
			if(selection instanceof IStructuredSelection) {
				IStructuredSelection iSelection = (IStructuredSelection) selection;
				Object firstElement = iSelection.getFirstElement();
				if(firstElement instanceof IProject) {
					System.out.println("Its Project....");
				}
					
				if(firstElement != null)
					System.out.println("ISElection Selected:::::" + firstElement.toString());
			}
			/*if(selection instanceof Project) {
				IProject project = (Project) selection;
				System.out.println("Selected:::::" + project.getName());
				selectedProject = project;
			}*/
				
		}
	}

}
