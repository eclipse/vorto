package org.eclipse.vorto.perspective.listener;

import org.eclipse.swt.widgets.Display;

public class DisplayRunnableExecutioner {

	private Runnable callback;

	public DisplayRunnableExecutioner(Runnable runnable) {
		this.callback = runnable;
	}

	public void executeRunnableOnDisplayThread() {
		if (!Display.getDefault().isDisposed()) {
			Display.getDefault().syncExec(callback);
		}
	}

}
