package org.eclipse.vorto.repository;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

public class RestModelRepositoryExtensionFactory implements
		IExecutableExtensionFactory {

	@Override
	public Object create() throws CoreException {
		RestModelRepository modelRepository = new RestModelRepository(new ConnectionInfoSupplier() {
			public String connectionUrl() {
				// TODO : Get this value from preferences
				return "http://localhost:8080/infomodelrepository";
			}
		});
		
		modelRepository.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				//MessageDisplayFactory.getMessageDisplay().display((String) arg);
				// TODO : find a way to display messages on Console here
				System.out.println((String) arg);
			}			
		});
		
		return modelRepository;
	}

}
