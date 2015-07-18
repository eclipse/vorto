package org.eclipse.vorto.perspective.util;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

public class TreeViewerTemplate {

	private final TreeViewer treeViewer;
	
	public TreeViewerTemplate(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}
	
	public void update(final TreeViewerCallback callback) {
		if (!Display.getDefault().isDisposed()) { 
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					try {
						callback.doUpdate(treeViewer);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}
}
