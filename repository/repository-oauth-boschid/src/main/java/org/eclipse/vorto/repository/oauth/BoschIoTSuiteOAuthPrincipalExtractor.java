package org.eclipse.vorto.repository.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class BoschIoTSuiteOAuthPrincipalExtractor implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        Map<String, String> ext = (Map<String, String>) map.get("orig_id");
        return ext.get("sub");
    }
}
