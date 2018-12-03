package org.eclipse.vorto.repository.core;

import java.security.Principal;

import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.Privilege;

import org.eclipse.vorto.repository.account.Role;

public class PolicyEntry {
	
	private String principalId;
	
	private PrincipalType principalType;
	
	private boolean grantRead = false;
	
	private boolean grantWrite = false;
		
	public static PolicyEntry of(String principalId, PrincipalType type, boolean read, boolean write) {
		PolicyEntry entry  = new PolicyEntry();
		entry.principalId = principalId;
		entry.principalType = type;
		entry.grantRead = read;
		entry.grantWrite = write;
		return entry;
	}

	public static PolicyEntry of(AccessControlEntry ace) {
		PolicyEntry entry = new PolicyEntry();
		Principal principal = ace.getPrincipal();
		
		if (Role.isValid(principal.getName())) {
			entry.principalType = PrincipalType.Role;
			entry.principalId = Role.of(principal.getName()).name();
		} else {
			entry.principalType = PrincipalType.User;
			entry.principalId = principal.getName();
		}
		
		for (Privilege p : ace.getPrivileges()) {
			if (p.getName().equals("jcr:read")) {
				entry.grantRead = true;
			} else if (p.getName().equals("jcr:write")) {
				entry.grantWrite = true;
			}
		}
		
		return entry;
	}
	
	
	public String getPrincipalId() {
		return principalId;
	}


	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}



	public PrincipalType getPrincipalType() {
		return principalType;
	}



	public void setPrincipalType(PrincipalType principalType) {
		this.principalType = principalType;
	}



	public boolean isGrantRead() {
		return grantRead;
	}



	public void setGrantRead(boolean grantRead) {
		this.grantRead = grantRead;
	}



	public boolean isGrantWrite() {
		return grantWrite;
	}



	public void setGrantWrite(boolean grantWrite) {
		this.grantWrite = grantWrite;
	}



	public static enum PrincipalType {
		User, Role
	}

}
