package org.eclipse.vorto.repository;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.repository.preferences.PreferenceConstants;

public class RestModelRepositoryExtensionFactory implements
		IExecutableExtensionFactory {

	@Override
	public Object create() throws CoreException {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		RestModelRepository modelRepository = new RestModelRepository(new ConnectionInfoSupplier() {
			public String connectionUrl() {
				return store.getString(PreferenceConstants.P_REMOTE_REPO_URL);
			}
		});
		
		modelRepository.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				MessageDisplayFactory.getMessageDisplay().display(
						"Upload Status : " + arg.toString());
			}			
		});
		
		return modelRepository;
	}

}
