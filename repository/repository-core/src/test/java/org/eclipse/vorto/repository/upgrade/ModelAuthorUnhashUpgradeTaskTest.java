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
package org.eclipse.vorto.repository.upgrade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelSearchService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.upgrade.impl.ModelAuthorUnhashUpgradeTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import com.google.common.collect.Maps;

public class ModelAuthorUnhashUpgradeTaskTest {

  private IUserRepository userRepository = Mockito.mock(IUserRepository.class);

  private IModelRepositoryFactory factory = Mockito.mock(IModelRepositoryFactory.class);

  private OAuth2Authentication mockUser = Mockito.mock(OAuth2Authentication.class);
  
  private IModelRepository modelRepository = Mockito.mock(IModelRepository.class);

  private UsernamePasswordAuthenticationToken mockedAuthToken =
      Mockito.mock(UsernamePasswordAuthenticationToken.class);

  private UserContext userContext = UserContext.user("erleczars.mantos", "playground");

  private List<ModelInfo> models = getModelList();

  @Before
  public void initMocks() {
    
    IModelSearchService modelSearchService = Mockito.mock(IModelSearchService.class);
    
    Map<String, List<ModelInfo>> searchResult = new HashMap<>();
    searchResult.put("playground", models);
    
    Mockito.when(modelSearchService.search("author:" + userContext.getHashedUsername()))
        .thenReturn(searchResult);
    
    Mockito
        .when(modelSearchService.search(
            AdditionalMatchers.not(Matchers.eq("author:" + userContext.getHashedUsername()))))
        .thenReturn(Maps.newHashMap());
    
    Mockito.when(factory.getModelSearchService()).thenReturn(modelSearchService);
    Mockito.when(factory.getRepository(Matchers.anyString())).thenReturn(modelRepository);
    Mockito.when(mockedAuthToken.getDetails()).thenReturn(getDetails());
    Mockito.when(mockUser.getUserAuthentication()).thenReturn(mockedAuthToken);
    
  }

  private List<ModelInfo> getModelList() {
    return IntStream.range(0, 10).mapToObj((i) -> {
      ModelInfo model = new ModelInfo();
      model.setId(new ModelId("Erle" + i, "com.erle", "1.0.0"));
      model.setState("start");
      model.setAuthor(userContext.getHashedUsername());
      return model;
    }).collect(Collectors.toList());
  }

  private Map<String, Object> getDetails() {
    Map<String, Object> details = new HashMap<String, Object>();
    details.put("email", "erleczars.mantos@bosch-si.com");
    return details;
  }

  @Test
  public void testUpgradeTask() {
    ModelAuthorUnhashUpgradeTask upgradeTask = new ModelAuthorUnhashUpgradeTask();
    upgradeTask.setModelRepositoryFactory(factory);
    upgradeTask.setUserRepository(userRepository);

    models.forEach((model) -> {
      assertEquals(userContext.getHashedUsername(), model.getAuthor());
    });

    upgradeTask.doUpgrade(User.create("erle", new Tenant("playground"), Role.USER), () -> mockUser);

    ArgumentCaptor<ModelInfo> argument = ArgumentCaptor.forClass(ModelInfo.class);
    Mockito.verify(modelRepository, Mockito.times(models.size())).updateMeta(argument.capture());
    assertEquals("erle", argument.getValue().getAuthor());
    assertEquals("start", argument.getValue().getState());

    ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    Mockito.verify(userRepository).save(userArgument.capture());
    assertNotNull(userArgument.getValue());
    System.out.println(userArgument.getValue().getLastUpdated());
  }

}
