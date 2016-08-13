/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web.security;

import javax.transaction.Transactional;

import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
@Transactional
public class UsersDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    private UserRepository userRepository;
	
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
    	User user = userRepository.findByUsername(username);
    
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: "+ username);
        }
        
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
               
        return  new org.springframework.security.core.userdetails.User(
        		  user.getUsername(), 
        		  user.getPassword(),
        		  enabled, 
        		  accountNonExpired,
        		  credentialsNonExpired, 
        		  accountNonLocked,
        		  AuthorityUtils.createAuthorityList("ROLE_"+user.getRole().toString()));
    }

}