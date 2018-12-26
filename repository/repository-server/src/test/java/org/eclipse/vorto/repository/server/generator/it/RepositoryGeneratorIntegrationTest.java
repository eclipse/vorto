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
package org.eclipse.vorto.repository.server.generator.it;

import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RepositoryGeneratorIntegrationTest extends AbstractGeneratorIntegrationTest {

    @Test public void testGetRegisteredGeneratorServices() {
        try {
            vortoMockMvc.perform(get("/api/v1/generators").with(userAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(getGenerators().size())));
        } catch (Exception e) {
            fail("failed because of Exception: " + e.toString());
        }
    }

    @Test public void testGetGeneratorInfo() {
        try {
            for (GeneratorServiceInfo genInfo : getGenerators()) {
                System.out.println("Checking for [" + genInfo.getKey() + "]");
                vortoMockMvc.perform(get("/api/v1/generators/" + genInfo.getKey()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", Matchers.is(genInfo.getName())))
                    .andExpect(jsonPath("$.description", Matchers.is(genInfo.getDescription())));
            }
        } catch (Exception e) {
            fail("failed because of Exception: " + e.toString());
        }
    }

    @Test public void testGenerate() {
        try {
            SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
                user("admin").password("pass").authorities(
                    SpringUserUtils.toAuthorityList(Sets.newHashSet(ADMIN, MODEL_CREATOR, USER)));

            createModel("Location.fbmodel", "com.test:Location:1.0.0");
            createModel("TrackingDevice.infomodel", "com.test:TrackingDevice:1.0.0");
            // Creating a model takes a couple seconds
            sleep(5000);

            vortoMockMvc.perform(get("/api/v1/search/models?expression="))
                .andExpect(status().isOk());
            vortoMockMvc.perform(
                get("/api/v1/generators/boschiotsuite/models/com.test:TrackingDevice:1.0.0?language=java")
                    .with(user)).andExpect(status().isOk())
                .andExpect(ZipFileCompare.equals(loadResource("generated-boschiotsuite.zip")));

        } catch (Exception e) {
            fail("failed because of Exception: " + e.toString());
        }
    }

    private Collection<GeneratorServiceInfo> getGenerators() throws Exception {
        MvcResult result = generatorMockMvc.perform(get("/rest/generators")).andReturn();
        return gson.fromJson(new String(result.getResponse().getContentAsByteArray()),
            new TypeToken<List<GeneratorServiceInfo>>() {
            }.getType());
    }

}
