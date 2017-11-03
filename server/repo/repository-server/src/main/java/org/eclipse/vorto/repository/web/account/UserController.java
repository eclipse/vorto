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

import java.security.Principal;
import java.sql.Timestamp;

import javax.validation.Valid;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.UserAccount;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.account.impl.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="User Controller", description="REST API to manage user accounts")
@RestController
@RequestMapping(value = "/rest")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private IUserRepository userRepository;
	    
    @Autowired
	private IUserAccountService accountService;
    
    @Autowired(required=false)
    private PasswordEncoder passwordEncoder;
		
	@ApiOperation(value = "Returns a specified User")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not found"), 
							@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = RequestMethod.GET,
					value = "/users/{username:.+}")
	public ResponseEntity<UserDto> getUser(@ApiParam(value = "Username", required = true) @PathVariable String username) {
		
		LOGGER.debug("User {} - {} ", username, userRepository.findByUsername(username));
		
		return new ResponseEntity<UserDto>(UserDto.fromUser(userRepository.findByUsername(username)), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Creates a new User")
	@RequestMapping(method = RequestMethod.POST,
				value = "/users",
	    		consumes = "application/json")
	public ResponseEntity<Boolean> registerUserAccount(@ApiParam(value = "User Data Transfer Object", required = true) @RequestBody @Valid UserAccount account) {
		
		if (userRepository.findByEmail(account.getEmail()) != null &&
				userRepository.findByUsername(account.getUsername()) != null ) {           
				return new ResponseEntity<Boolean>(false, HttpStatus.CREATED);
			}
		
		LOGGER.debug("Register new user account with information: {}", account);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountService.create(account); 	
		
		return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Update an existing User")
	@RequestMapping(method = RequestMethod.PUT,
					value = "/users/{username}")
	public ResponseEntity<UserDto> updateUser(	Principal currentlyLoggedInUser,
											@ApiParam(value = " Username", required = true) @PathVariable String username, 
											@ApiParam(value = "User Data Transfer Object", required = true) @RequestBody UserDto userDto) {
		if (!isUpdateAllowed(currentlyLoggedInUser, username, userDto)) {
			return new ResponseEntity<UserDto>(HttpStatus.UNAUTHORIZED);
		}
		
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User does not exists");
		}
		
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
		
		if (UserUtils.isAdmin(currentlyLoggedInUser)) {
			user.setHasWatchOnRepository(userDto.isHasWatchOnRepository());
		}
		
		userRepository.save(user);
		
		User updatedUser = userRepository.findByUsername(userDto.getUsername());
		
		return new ResponseEntity<UserDto>(UserDto.fromUser(updatedUser), HttpStatus.OK);
	}
	
	private boolean isUpdateAllowed(Principal currentlyLoggedInUser, String username, UserDto userDto) {
		if(username == null || currentlyLoggedInUser == null || userDto == null) {
			return false;
		}
		
		if (UserUtils.isAdmin(currentlyLoggedInUser)) {
			/*
			 * As an admin, your restriction is you cannot name someone else an admin and 
			 * you cannot rename yourself to someone else
			 */
			if ("admin".equals(username)) {
				return "admin".equals(userDto.getUsername());
			} else {
				return !"admin".equals(userDto.getUsername());
			}
		} else {
			return UserUtils.sameUser(currentlyLoggedInUser, username) && 
					username.equals(userDto.getUsername()) && 
					!"admin".equals(userDto.getUsername());
		}
	}
	
	/* checking uniqueness of specific values
	 */        
	@ApiOperation(value = "Compares an Email-Address with all already existing Email-Addresses")
	@RequestMapping(method = RequestMethod.POST,
    				value = "/users/unique/email")
	public ResponseEntity<Boolean> checkEmailAdressAlreadyExists(@ApiParam(value = "Email-Address", required = true)  @RequestBody String email) {		

    	boolean emailExists = false;
    	if (userRepository.findByEmail(email) == null){
    		 emailExists = false;
		} else {
			 emailExists = true;
		}
    	
		return new ResponseEntity<Boolean>(emailExists, HttpStatus.OK);
	}
    
	@ApiOperation(value = "Compares a username with all already existing usernames")
    @RequestMapping(method = RequestMethod.POST,
					value = "/users/unique/username",
					consumes = "application/json")
	public ResponseEntity<Boolean> checkUsernameAlreadyExists(@ApiParam(value = "Username", required = true)  @RequestBody String username) {		
    	
    	boolean userExists = false;
		
    	if (userRepository.findByUsername(username) == null){
			userExists = false;
		} else {
			
			userExists = true;
		}
    	
    	LOGGER.debug("username exists: "+userExists);
		return new ResponseEntity<Boolean>(userExists, HttpStatus.OK);	
	}
	
	@ApiOperation(value = "Deletes the user's user account")
	@RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#username,'user:delete')")
	public ResponseEntity<Void> deleteAccount(@PathVariable("username") final String username) {	
		accountService.delete(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
