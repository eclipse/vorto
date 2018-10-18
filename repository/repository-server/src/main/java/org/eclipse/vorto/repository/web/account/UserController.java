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
package org.eclipse.vorto.repository.web.account;

import io.swagger.annotations.ApiParam;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.UserUtils;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/rest/{tenant}/users")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private IUserRepository userRepository;
	    
    @Autowired
	private IUserAccountService accountService;
    
    @Autowired
    private IUpgradeService updateService;

	@Value("#{'${oauth2.roles.default}'.split(',')}")
	private List<String> defaultRoles;

	@RequestMapping(method = RequestMethod.GET,
					value = "/{username:.+}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
	public ResponseEntity<UserDto> getUser(@ApiParam(value = "Username", required = true) @PathVariable String username) {
		
		LOGGER.debug("User {} - {} ", username, userRepository.findByUsername(username));
		User user = userRepository.findByUsername(username);
		return new ResponseEntity<UserDto>(UserDto.fromUser(user, new ArrayList<>(user.getRoles())), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST,
	    		consumes = "application/json")
	public ResponseEntity<Boolean> createUser(Principal user) {
		
		OAuth2Authentication oauth2User = (OAuth2Authentication) user;
		
		if (userRepository.findByUsername(oauth2User.getName()) != null ) {           
			return new ResponseEntity<Boolean>(false, HttpStatus.CREATED);
		}

		LOGGER.info("User: '{}' accepted the terms and conditions.", oauth2User.getName());

		User createdUser = accountService.create(oauth2User.getName(), defaultRoles);
		UserUtils.refreshSpringSecurityUser(createdUser); // change the spring oauth context with the updated user and its roles
		
		return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{username:.+}/updateTask")
	@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
	public ResponseEntity<Boolean> updateUser(Principal user, @ApiParam(value = "Username", required = true) @PathVariable String username) {
		
		User userAccount = userRepository.findByUsername(username);
		if (userAccount == null) {
			return new ResponseEntity<Boolean>(true, HttpStatus.BAD_REQUEST);
		}
		
		updateService.installUserUpgrade(userAccount, () -> user);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{username:.+}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#username,'user:delete')")
	public ResponseEntity<Void> deleteAccount(@PathVariable("username") final String username) {	
		accountService.delete(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{username:.+}", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserDto> createUser(@PathVariable("username") final String userName, @RequestBody List<String> roles) {
		User user = accountService.create(userName, roles);

		return new ResponseEntity<UserDto>(
				UserDto.fromUser(this.userRepository.findByUsername(userName),
						new ArrayList<>(user.getRoles())), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{username:.+}/roles", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserDto> removeUserRole(@PathVariable("username") final String userName, @RequestBody List<String> roles) {

		User user = accountService.removeUserRole(userName, roles);
		return new ResponseEntity<UserDto>(
				UserDto.fromUser(this.userRepository.findByUsername(userName),
						new ArrayList<>(user.getRoles())), HttpStatus.OK);
	}

	@RequestMapping(value = "/{username:.+}/roles", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserDto> showRoles(@PathVariable("username") final String userName) {
		User user = this.userRepository.findByUsername(userName);

		if(Objects.isNull(user)){
			throw new UsernameNotFoundException("User Not Found: " + userName);
		}
		return new ResponseEntity<UserDto>(
				UserDto.fromUser(user, new ArrayList<>(user.getRoles())), HttpStatus.OK);
	}


}
