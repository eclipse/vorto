package org.eclipse.vorto.perspective.listener;

import java.util.Collection;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.vorto.perspective.util.Predicates;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class ErrorDiagnosticListener extends DisplayRunnableExecutioner implements IResourceChangeListener {

	public ErrorDiagnosticListener(Runnable runnable) {
		super(runnable);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		Collection<IMarkerDelta> markerWithErrors = 
			Collections2.filter(Lists.newArrayList(event.findMarkerDeltas(null, true)), Predicates.isVortoModelWithMarkerError);
		if (!markerWithErrors.isEmpty()) {
			executeRunnableOnDisplayThread();
		}
	}
}
