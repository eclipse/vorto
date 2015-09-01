package org.eclipse.vorto.repository;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.repository.preferences.ConnectionInfoFactory;

public class RestModelRepositoryExtensionFactory implements
		IExecutableExtensionFactory {

	@Override
	public Object create() throws CoreException {	
		RestModelRepository modelRepository = new RestModelRepository(ConnectionInfoFactory.getConnectionInfo());
		modelRepository.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				MessageDisplayFactory.getMessageDisplay().display(
						"Upload Status : " + arg.toString());
			}			
		});
		
		return modelRepository;
	}

}
