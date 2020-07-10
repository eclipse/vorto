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
package org.eclipse.vorto.repository.server.ui;

import com.google.common.collect.Sets;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.eclipse.vorto.repository.domain.NamespaceRole.DEFAULT_NAMESPACE_ROLES;
import static org.eclipse.vorto.repository.domain.RepositoryRole.DEFAULT_REPOSITORY_ROLES;

/**
 * Tests some of the basic functionality of the Vorto repository. To keep the tests independent the repository is
 * wiped after each test method.
 *
 */
public class BasicRepositoryUITest extends AbstractUITest {

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
        this.seleniumVortoHelper.createNamespace( SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
        this.seleniumVortoHelper.openManageNamespacesTab();
        // check if the namespace was created successfully.
        this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//td/div[@class='ng-binding' and contains(.,'" + SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + "')]");
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
        namespaceComboBox.selectByVisibleText(SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
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
    public void testVisibleModels() {
        // create an info model
        testCreateInfoModel();
        // login with user2 who should not be able to see the unpublished model
        this.seleniumVortoHelper.loginWithUser("user2", "pass");
        this.seleniumVortoHelper.gotoWelcomePage();
        // select model state: all.
        this.seleniumVortoHelper.selectModelStateInComboBox(null);
        // make sure that the model is not visible to user2
        this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//div[@id='searchResult']/div[count(*) = 0]");
        this.seleniumVortoHelper.loginWithUser("user1", "pass");
        this.seleniumVortoHelper.selectModelStateInComboBox(null);
        WebElement linkToModel = this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//a[@href='./#/details/" + SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + ":" + SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL + ":1.0.0']");
        WebDriverWait wait5Secs = new WebDriverWait(this.seleniumVortoHelper.getRemoteWebDriver(), 5);
        wait5Secs.until(ExpectedConditions.elementToBeClickable(linkToModel));
    }

    /**
     * Tests if a user is able to see the private model of another user after added as collaborator.
     *
     */
    @Test
    public void testAddCollaboratorToNamespace() throws Exception {
        testVisibleModels();
        this.seleniumVortoHelper.addUserToNamespace("user2", SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE, "model_publisher", "model_promoter", "model_creator", "model_reviewer");
        this.seleniumVortoHelper.loginWithUser("user2", "pass");
        this.seleniumVortoHelper.selectModelStateInComboBox(null);
        this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath("//a[@href='./#/details/" + SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + ":" + SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL + ":1.0.0']");
    }


    protected void setUpTest() {
        mock.setAuthorityListForUser(SpringUserUtils.toAuthorityList(
                Sets.newHashSet(DEFAULT_NAMESPACE_ROLES[0], DEFAULT_NAMESPACE_ROLES[5], DEFAULT_NAMESPACE_ROLES[1], DEFAULT_NAMESPACE_ROLES[2], DEFAULT_NAMESPACE_ROLES[3], DEFAULT_NAMESPACE_ROLES[4], DEFAULT_REPOSITORY_ROLES[0])), "user1");
        mock.setAuthorityListForUser(SpringUserUtils.toAuthorityList(
                Sets.newHashSet(DEFAULT_NAMESPACE_ROLES[0])), "user2");
        userRepository.save(User.create("user1", "GITHUB", null, false));
        userRepository.save(User.create("user2", "GITHUB", null, false));
    }
}
