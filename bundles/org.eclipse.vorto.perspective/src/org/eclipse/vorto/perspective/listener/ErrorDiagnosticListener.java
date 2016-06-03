package org.eclipse.vorto.perspective.listener;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.swt.widgets.Display;

public class ErrorDiagnosticListener implements IResourceChangeListener {

	private Runnable callback;
	
	public ErrorDiagnosticListener(Runnable runnable) {
		this.callback = runnable;
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		IMarkerDelta[] deltas = event.findMarkerDeltas(null, true);
		for (IMarkerDelta delta : deltas) {
			// Commented code below is for determining whether an error appeared or disappeared, but we don't need this for now.
			// we only need to know that a marker with error arrived.
			if ( delta.getAttribute("severity").equals(IMarker.SEVERITY_ERROR)) {
				executeCallback();
			}
			
			/*
			if ( delta.getAttribute("severity").equals(IMarker.SEVERITY_ERROR) && delta.getMarker().exists()) {
				System.out.println("Error EXIST on " + delta.getResource().getName());
				executeCallback();
			} else if ( delta.getAttribute("severity").equals(IMarker.SEVERITY_ERROR) && !delta.getMarker().exists()) {
				System.out.println("Error REMOVED on " + delta.getResource().getName());
				executeCallback();
			}*/
		}
	}
	
	public void executeCallback() {
		if (!Display.getDefault().isDisposed()) {
			Display.getDefault().syncExec(callback);
		}
	}
}
