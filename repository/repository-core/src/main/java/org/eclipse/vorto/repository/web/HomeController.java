/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.web;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.oauth.OAuthUser;
import org.eclipse.vorto.repository.web.oauth.OAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class HomeController {
 
  @Value("${repo.attachment.allowed.fileSize}")
  private String attachmentAllowedSize;

  @Value("${server.config.updateDate:#{'2000-01-01 12:00:00'}}")
  private String updateDate;

  @Value("${server.config.supportEmail:#{null}}")
  private String supportEmail;

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IOAuthProviderRegistry registry;

  @ApiOperation(value = "Returns the currently logged in User")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 200, message = "OK")})
  @RequestMapping(value = {"/user", "/me"}, method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> getUser(Principal user,
      final HttpServletRequest request) throws ParseException {
    
    Map<String, Object> map = new LinkedHashMap<>();
    
    if (user == null)
      return new ResponseEntity<Map<String, Object>>(map, HttpStatus.UNAUTHORIZED);
    
    IOAuthProvider provider = registry.getByPrincipal(user);
        
    OAuthUser oauthUser = provider.createUser((OAuth2Authentication) user);
   
    User userAccount = accountService.getUser(oauthUser.getUserId());

    Date updateCutoff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(updateDate);

    map.put("name", oauthUser.getUserId());
    map.put("displayName", oauthUser.getDisplayName());
    map.put("isRegistered", Boolean.toString(userAccount != null));
    map.put("roles", oauthUser.getRoles());
    map.put("needUpdate", Boolean.toString(needUpdate(userAccount, updateCutoff)));
    map.put("logOutUrl", provider.getWebflowConfiguration().get().getLogoutUrl(request));
    map.put("provider", new OAuthProvider(provider.getId(), provider.getLabel(), provider.getWebflowConfiguration().get()));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  private boolean needUpdate(User user, Date updateCutoff) {
    return user != null && user.getLastUpdated().before(updateCutoff)
        && new Date().after(updateCutoff);
  }

  @RequestMapping(value = {"/context"}, method = RequestMethod.GET)
  public Map<String, Object> globalContext(final HttpServletRequest request, Principal user) {
    
    Map<String, Object> context = new LinkedHashMap<>();
    
    List<OAuthProvider> availableProviders = this.registry.list().stream().filter(p -> p.supportsWebflow()).map(p -> new OAuthProvider(p.getId(),p.getLabel(), p.getWebflowConfiguration().get())).collect(Collectors.toList());

    context.put("providers", availableProviders);
    context.put("attachmentAllowedSize", attachmentAllowedSize);
    context.put("supportEmail", supportEmail);
    return context;
  }

//  public String getBaseUrl(HttpServletRequest request) {
//    if (request.getRequestURI().equals("/") || request.getRequestURI().equals("")) {
//      return request.getRequestURL().toString();
//    } else {
//      return request.getRequestURL().toString().replace(request.getRequestURI(), "");
//    }
//
//  }


}
