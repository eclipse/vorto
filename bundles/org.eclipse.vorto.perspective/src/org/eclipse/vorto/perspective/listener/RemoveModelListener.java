package org.eclipse.vorto.perspective.listener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.perspective.util.Predicates;

public class RemoveModelListener extends DisplayRunnableExecutioner implements IResourceChangeListener {

	public RemoveModelListener(Runnable runnable) {
		super(runnable);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) {
					if (Predicates.isVortoModelResourceRemoved.apply(delta)) {
						executeRunnableOnDisplayThread();
						return false;
					}
					return true;
				}
			}, IResourceDelta.REMOVED);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
}