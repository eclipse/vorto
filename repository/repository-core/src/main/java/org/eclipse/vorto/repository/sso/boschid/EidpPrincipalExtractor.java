package org.eclipse.vorto.repository.sso.boschid;

import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

public class EidpPrincipalExtractor implements PrincipalExtractor {
 
	@Override
	public Object extractPrincipal(Map<String, Object> map) {
		return (String) map.get("sub");
	}

}
