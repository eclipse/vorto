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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration
@SpringBootTest(classes = VortoRepository.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//https://github.com/spring-projects/spring-boot/issues/12280
public class GeneratorsControllerIntegrationTest {

    MockMvc mockMvc;
    TestModel testModel;

    @Autowired protected WebApplicationContext wac;

    static {
      System.setProperty("github_clientid", "foo");
      System.setProperty("github_clientSecret", "foo");
      System.setProperty("eidp_clientid", "foo");
      System.setProperty("eidp_clientSecret", "foo");
    }
    
    @Before public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).
            apply(springSecurity()).build();
    }
    
    @Test
    public void placeholder() {}


}
