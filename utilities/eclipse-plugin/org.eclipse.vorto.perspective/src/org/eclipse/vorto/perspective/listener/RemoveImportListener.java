package org.eclipse.vorto.perspective.listener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.perspective.util.Predicates;

public class RemoveImportListener extends DisplayRunnableExecutioner implements IResourceChangeListener {

	public RemoveImportListener(Runnable runnable) {
		super(runnable);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			
			event.getDelta().accept(new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (Predicates.isVortoModelChanged.apply(delta)) {
						// -erle- : perhaps add another test here to see if it was really an import that was removed.
						// This requires that we know the diff between old and new versions which was somehow left out
						// of this API.
						executeRunnableOnDisplayThread();
						return false;
					}
					
					return true;
				}
			});
		} catch (CoreException e) {
			throw new ResourceListenerException("In RemoveImportListener.", e);
		}
	}

}
