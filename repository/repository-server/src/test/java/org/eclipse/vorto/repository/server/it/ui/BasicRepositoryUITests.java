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

import com.google.common.collect.Sets;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.eclipse.vorto.repository.domain.Role.*;


/**
 * Tests some of the basic functionality of the vorto repository.
 */
public class BasicRepositoryUITests extends AbstractUITest{

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
        this.seleniumVortoHelper.allowCookies();
    }

    /**
     * Tests if the copyright footer is there.
     */
    @Test
    public void testCopyrightFooter() {
        RemoteWebDriver webDriver = this.seleniumVortoHelper.getRemoteWebDriver();
        this.seleniumVortoHelper.allowCookies();
        WebElement copyrightFooter = webDriver.findElementByXPath("//li[@class='copyright']");
        Assert.assertTrue(copyrightFooter.isDisplayed());
        String copyrightText = copyrightFooter.getText();
        Assert.assertTrue("Text was: " + copyrightText, copyrightText.contains("Revision:"));
    }

    /**
     * Test if the login button is there and can be clicked.
     */
    @Test
    public void testLoginButton() {
        final RemoteWebDriver webDriver = this.seleniumVortoHelper.getRemoteWebDriver();
        webDriver.get(rootUrl);
        webDriver.findElementByLinkText("Login").click();
        webDriver.findElementsByXPath("//a[@href='github/login']");
        webDriver.findElementsByXPath("//a[@href='eidp/login']");
    }

    /**
     * Tests to create a namespace via the manage page.
     */
    @Test
    public void testCreateNamespace() {
        this.seleniumVortoHelper.loginWithUser("user1", "pass");
        this.seleniumVortoHelper.createNamespace(  SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
        this.seleniumVortoHelper.openManageNamespacesTab();
        // check if the namespace was created successfully.
        this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//td/div[@class='ng-binding' and contains(.,'" + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + "')]");
    }

    /**
     * Tests to create a model.
     */
    @Test
    public void testCreateInfoModel() {
        RemoteWebDriver remoteWebDriver = this.seleniumVortoHelper.getRemoteWebDriver();
        // there should be no create button before logging in.
        List<WebElement> createModelButtonList = this.seleniumVortoHelper.getRemoteWebDriver().findElementsByXPath("//a[@ng-click='openCreateModelDialog()']");
        Assert.assertTrue(createModelButtonList.isEmpty());
        // create a namespace (reuse existing test)
        testCreateNamespace();
        // now the create button should be available
        this.seleniumVortoHelper.gotoWelcomePage();
        remoteWebDriver.findElementByXPath("//a[@ng-click='openCreateModelDialog()']").click();
        remoteWebDriver.findElementByXPath("//input[@name='modelType' and @value='InformationModel']").click();
        remoteWebDriver.findElementByXPath("//button[contains(@ng-click,'next(')]").click();
        Select namespaceComboBox = new Select(remoteWebDriver.findElementById(SeleniumVortoHelper.ID_CB_NAMESPACE_ROOT));
        namespaceComboBox.selectByVisibleText(SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
        WebElement modelNameTextField = remoteWebDriver.findElementByName("modelName");
        modelNameTextField.sendKeys(SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL);
        remoteWebDriver.findElementByXPath("//button[contains(@ng-click,'next(')]").click();
        remoteWebDriver.findElementByXPath("//button[text()='Create']").click();
        // wait for the model details dialog to show up.
        remoteWebDriver.findElementByXPath("//dd[@class='ng-binding' and .='" + SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL + "']");
    }

    /**
     * Tests if non-published models become visible when logged in.
     */
    @Test
    public void testVisibleModels() throws Exception {
        // create an info model
        testCreateInfoModel();
        // check if the model is visible in the overview
        this.seleniumVortoHelper.gotoWelcomePage();
        this.seleniumVortoHelper.selectModelStateInComboBox(null);
        WebElement linkToModel = this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//a[@href='./#!/details/" + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + ":" + SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL + ":1.0.0']");
        // login with user2 who should not be able to see the unpublished model
        this.seleniumVortoHelper.loginWithUser("user2", "pass");
        this.seleniumVortoHelper.gotoWelcomePage();
        // select model state: all.
        this.seleniumVortoHelper.selectModelStateInComboBox(null);
        WebDriverWait wait5Secs = new WebDriverWait(this.seleniumVortoHelper.getRemoteWebDriver(),5);
        wait5Secs.until(ExpectedConditions.invisibilityOf(linkToModel));
        // TODO figure out a better way to ensure that the model is not visible
        this.seleniumVortoHelper.loginWithUser("user1", "pass");
        this.seleniumVortoHelper.selectModelStateInComboBox(null);
        linkToModel = this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//a[@href='./#!/details/" + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + ":" + SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL + ":1.0.0']");
        wait5Secs.until(ExpectedConditions.visibilityOf(linkToModel));
    }

    @Override
    protected void setUpTest() throws Exception {
//        // load some test models.
//        createModel("Zone.type", "com.test:Zone:1.0.0");
//        setPublic("com.test:Zone:1.0.0");
//        createModel("Color2.type", "com.test:Color:1.0.0");
//        setPublic("com.test:Color:1.0.0");
//        createModel("Fb_withDictionary.fbmodel", "com.test:InstalledSoftware:1.0.0");
//        setPublic("com.test:InstalledSoftware:1.0.0");
//        createModel("Lamp.fbmodel", "com.test:Lamp:1.0.0");
//        setPublic("com.test:Lamp:1.0.0");
//        createModel("StreetLamp.infomodel", "com.test:StreetLamp:1.0.0");

        mock.setAuthorityListForUser(SpringUserUtils.toAuthorityList(
                Sets.newHashSet(USER, SYS_ADMIN, TENANT_ADMIN, MODEL_CREATOR, MODEL_PROMOTER, MODEL_REVIEWER)), "user1");
        mock.setAuthorityListForUser(SpringUserUtils.toAuthorityList(
                Sets.newHashSet(USER)), "user2");
    }

    @After
    public void cleanupTestData() {
        deleteNamespaces(SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
    }

    private void deleteNamespaces(String user1PrivateNamespace) {

    }
}
