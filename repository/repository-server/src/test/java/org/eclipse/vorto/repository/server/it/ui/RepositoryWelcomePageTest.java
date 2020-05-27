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
package org.eclipse.vorto.repository.server.it.ui;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests some of the basic functionality of the vorto repository.
 */
public class RepositoryWelcomePageTest extends AbstractUITest{

    /**
     * Tests if the title is correct.
     */
    @Test
    public void testTitle() {
        final RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get(rootUrl);
        String title = webDriver.getTitle();
        Assert.assertEquals("Vorto Repository - Manages standardized IoT device descriptions", title);
    }

    /**
     * Test if the cookie footer appears and disappears after clicking on allow.
     */
    @Test
    public void testCookieFooter() {
        final RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get(rootUrl);
        SeleniumVortoHelper.allowCookies(webDriver);
    }

    /**
     * Tests if the copyright footer is there.
     */
    @Test
    public void testCopyrightFooter() {
        final RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get(rootUrl);
        SeleniumVortoHelper.allowCookies(webDriver);
        WebElement copyrightFooter = webDriver.findElementByXPath("//li[@class='copyright']");
        Assert.assertTrue(copyrightFooter.isDisplayed());
        String copyrightText = copyrightFooter.getText();
        Assert.assertTrue("Text was: " + copyrightText, copyrightText.contains("Revision:"));
    }

    /**
     * Test if the loogin button is there and can be clicked.
     */
    @Test
    public void testLoginButton() {
        final RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get(rootUrl);
        WebElement loginButton = webDriver.findElementByLinkText("Login");
        loginButton.click();
        webDriver.findElementsByXPath("//a[@href='github/login']");
        webDriver.findElementsByXPath("//a[@href='eidp/login']");
    }

    /**
     * Tests if the model editor becomes visible when clicking on a model.
     */
    @Test
    public void testViewModel() throws Exception {
        final RemoteWebDriver webDriver = chrome.getWebDriver();
        webDriver.get(rootUrl);
        //Select "all" models
        SeleniumVortoHelper.selectModelStateInComboBox(webDriver, null);

        String fb = obtainAccessToken("user1", "pass");

        // find link to functionblock.
        WebElement linkToFunctionBlock = webDriver.findElementByXPath("//a[@href='./#/details/com.test:InstalledSoftware:1.0.0']");
        linkToFunctionBlock.click();
        Assert.assertTrue(webDriver.findElementById("xtext-editor").isDisplayed());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "fooClientIdPassword");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = repositoryServer.perform(post("/oauweth/token")
                .params(params)
                .with(httpBasic("fooClientIdPassword","secret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }


    @Override
    protected void setUpTest() throws Exception {
        // load some test models.
        createModel("Zone.type", "com.test:Zone:1.0.0");
        setPublic("com.test:Zone:1.0.0");
        createModel("Color2.type", "com.test:Color:1.0.0");
        setPublic("com.test:Color:1.0.0");
        createModel("Fb_withDictionary.fbmodel", "com.test:InstalledSoftware:1.0.0");
        setPublic("com.test:InstalledSoftware:1.0.0");
        createModel("Lamp.fbmodel", "com.test:Lamp:1.0.0");
        setPublic("com.test:Lamp:1.0.0");
        createModel("StreetLamp.infomodel", "com.test:StreetLamp:1.0.0");
        setPublic("com.test:StreetLamp:1.0.0");
    }
}
