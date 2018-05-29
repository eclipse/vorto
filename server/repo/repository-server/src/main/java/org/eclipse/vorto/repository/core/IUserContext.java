package org.eclipse.vorto.repository.core;

public interface IUserContext {
	String getUsername();
	String getHashedUsername();
	
	boolean isAnonymous();
}
