/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    map.put("subject", userAccount.getSubject());
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

  /**
   * This creates a map with boolean key: {@link IOAuthProvider#supportsWebflow()} , and values as a
   * list of {@link OAuthProvider} POJOS.<br/>
   * The {@code providers} property of the context {@link Map} sent back to the UI is the list of
   * providers actually supporting web flow (e.g. Github, Bosch ID...), for backwards-compatibility
   * reasons.<br/>
   * The new {@code unsupportedProviders} property of the context {@link Map} sent back to the UI is
   * the list of providers that do not support web flow (e.g. Bosch Suite OAuth), but the UI needs
   * to know about them, e.g. when creating a technical user.<br/>
   * <b>Note</b>: since Bosch IoT Suite versions are user-agnostic, only one provider is returned
   * through the non-webflows-compatibles, by a (debatable) proprietary "merging" logic.
   * @param request
   * @param user
   * @return
   */
  @RequestMapping(value = {"/context"}, method = RequestMethod.GET)
  public Map<String, Object> globalContext(final HttpServletRequest request, Principal user) {
    
    Map<String, Object> context = new LinkedHashMap<>();

    Map<Boolean, List<OAuthProvider>> allProviders = this.registry.list().stream()
      .collect(
          Collectors.partitioningBy(
              p -> p.supportsWebflow(),
              Collectors.mapping(
                  OAuthProvider::from,
                  Collectors.toList()
              )
          )
      );

    context.put("providers", allProviders.get(true));
    context.put("nonWebflowProviders", OAuthProvider.mergeBoschIotSuiteOAuthProviders(allProviders.get(false)));
    context.put("attachmentAllowedSize", attachmentAllowedSize);
    context.put("supportEmail", supportEmail);
    return context;
  }


}
