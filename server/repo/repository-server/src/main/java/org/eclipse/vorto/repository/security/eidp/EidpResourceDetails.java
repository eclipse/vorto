package org.eclipse.vorto.repository.security.eidp;

import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class EidpResourceDetails extends AuthorizationCodeResourceDetails {

	private String resource;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}
