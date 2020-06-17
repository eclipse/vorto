/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.server.it.ui;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Profile("local-ui-test")
@Primary
@Service
public class AuthenticationProviderMock implements AuthenticationProvider {

    private Map<String, List<GrantedAuthority>> userAuhtoritiesMap = new HashMap<>();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails myUser = getUser(authentication.getPrincipal().toString());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(myUser, "", getGrantedAuthoritiesForUser(myUser.getUsername()));
        usernamePasswordAuthenticationToken.setDetails(new HashMap<>());
        OAuth2Authentication myAuth = new OAuth2Authentication(new OAuth2Request(null, "foo", getGrantedAuthoritiesForUser(myUser.getUsername()),true,null,null,null,null,null ),usernamePasswordAuthenticationToken);
        return myAuth;
    }

    private UserDetails getUser(String principal) {
        return new User(principal, "pass", getGrantedAuthoritiesForUser(principal));
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return true;
    }

    private List<GrantedAuthority> getGrantedAuthoritiesForUser(String userName) {
        List<GrantedAuthority> authorityList = this.userAuhtoritiesMap.get(userName);
        if (authorityList == null)
            authorityList = new LinkedList<>();
        return authorityList;

    }

    public void setAuthorityListForUser(List<GrantedAuthority> authorityList, String userName) {
        this.userAuhtoritiesMap.put(userName, authorityList);
    }




}