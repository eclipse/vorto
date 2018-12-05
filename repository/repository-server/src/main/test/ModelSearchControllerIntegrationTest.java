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
import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.eclipse.vorto.repository.account.Role.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration
@SpringBootTest(classes = VortoRepository.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//https://github.com/spring-projects/spring-boot/issues/12280
public class ModelSearchControllerIntegrationTest {

    MockMvc mockMvc;

    TestModel testModel;

    @Autowired protected WebApplicationContext wac;

    @Before public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).
            apply(springSecurity()).build();
        testModel = TestModel.TestModelBuilder.aTestModel().build();
        testModel.createModel(mockMvc);
    }

    @Test public void testModelSearch() throws Exception {
        mockMvc.perform(get("/api/v1/search/models?expression="))
            .andExpect(status().isOk());
    }
}
