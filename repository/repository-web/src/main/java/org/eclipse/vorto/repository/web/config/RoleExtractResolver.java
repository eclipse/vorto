/**
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 *
 * This program and the accompanying materials are made available under the terms of the Bosch Internal Open Source License v4 which accompanies this distribution,
 * and is available at http://bios.intranet.bosch.com/bioslv4.txt
 */
package org.eclipse.vorto.repository.web.config;

import org.eclipse.vorto.repository.sso.DatabaseRoleExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

public class RoleExtractResolver implements AuthoritiesExtractor {

	@Autowired
	private DatabaseRoleExtractor databaseRoleExtractor;

	@Override
	public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
		return AuthorityUtils.createAuthorityList(databaseRoleExtractor.extractAuthorities(map).split(","));
	}

}
