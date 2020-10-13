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

import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for vorto repository Selenium tests.
 */
public class SeleniumVortoHelper {

    public static String ID_CB_MODEL_STATE = "dropdownState";

    public static String ID_CB_NAMESPACE_ROOT = "namespaceRoot";

    public static String PRIVATE_NAMESPACE_PREFIX = "vorto.private.";

    public static String USER1_PRIVATE_NAMESPACE = "user1";

    public static String USER1_EMPTY_INFO_MODEL = "EmptyInfoModel";

    private final RemoteWebDriver webDriver;

    private final String rootUrl;

    private static Map<String, String> ROLE_TO_LINK_MAP = new HashMap<>();

    static {
        ROLE_TO_LINK_MAP.put("model_creator", "//input[@ng-model='user.roleModelCreator']");
        ROLE_TO_LINK_MAP.put("model_promoter", "//input[@ng-model='user.roleModelPromoter']");
        ROLE_TO_LINK_MAP.put("model_reviewer", "//input[@ng-model='user.roleModelReviewer']");
        ROLE_TO_LINK_MAP.put("model_publisher", "//input[@ng-model='user.roleAdmin']");
    }

    /**
     * @param webDriver The remote web driver to use.
     * @param rootUrl the base url of the repository
     */
    public SeleniumVortoHelper(RemoteWebDriver webDriver, String rootUrl) {
        this.webDriver = webDriver;
        this.rootUrl = rootUrl;
    }

    /**
     * Locates and accepts the allow cookies footer. (System under tests needs an internet connections)
     */
    public void allowCookies() {
        webDriver.get(rootUrl);
        WebDriverWait wait5Secs = new WebDriverWait(webDriver,5);
        WebElement cookieFooter = webDriver.findElementById("cookieconsent:desc");
        wait5Secs.until(ExpectedConditions.visibilityOf(cookieFooter));
        wait5Secs.until(ExpectedConditions.elementToBeClickable(cookieFooter));
        Assert.assertTrue(cookieFooter.isDisplayed());
        WebElement allowCookieButton = webDriver.findElementByLinkText("Allow cookies");
        allowCookieButton.click();
        wait5Secs.until(ExpectedConditions.invisibilityOf(cookieFooter));
    }

    /**
     * Select the desired model state in the model state combo box ("null" = All states)
     *
     * @param modelState the model state to select.
     */
    public void selectModelStateInComboBox(ModelState modelState)  {
        Select stateComboBox = new Select(webDriver.findElementById(ID_CB_MODEL_STATE));
        String stateText;
        if (modelState == null) {
            stateText = "all";
        } else {
            stateText = modelState.getName();
        }
        stateComboBox.selectByValue(stateText);
    }

    /**
     * Opens the manage tab
     */
    public void openManageTab() {
        webDriver.get(rootUrl);
        // check for loading finished state
        webDriver.findElementByXPath("//div[@id='searchResult']/div[not(contains(@class,'ng-hide'))]");
        WebElement manageLink = webDriver.findElementByXPath("//a[@href='./#/manage']");
        // workaround for unstable "click()"
        manageLink.sendKeys(Keys.ENTER);
    }

    /**
     * Create a namespace by navigating to the 'manage' tab and using the create namespace dialog.
     *
     * @param myNamespace name of the namespace to create.
     */
    public void createNamespace(String myNamespace) {
        openManageTab();
        openManageNamespacesTab();
        webDriver.findElementByXPath("//button[contains(.,'Create a namespace')]").click();
        WebElement namespaceInputField = webDriver.findElementByXPath("//input[@name='namespace']");
        // Click and clear pre-filled text field.
        WebDriverWait wait5Secs = new WebDriverWait(webDriver, 5);
        wait5Secs.until(ExpectedConditions.elementToBeClickable(namespaceInputField));
        Assert.assertTrue(namespaceInputField.isDisplayed());
        Assert.assertTrue(namespaceInputField.isEnabled());
        namespaceInputField.click();
        namespaceInputField.clear();
        // Enter the name of the namespace to create in th text field.
        namespaceInputField.sendKeys(myNamespace);
        webDriver.findElementByXPath("//button[@ng-click='createOrUpdateNamespace()']").click();
    }

    /**
     * Opens the manage namespace tab.
     */
    public void openManageNamespacesTab() {
        openManageTab();
        webDriver.findElementByXPath("//uib-tab-heading[contains(.,'Namespaces')]").click();
    }

    /**
     * Login a user (see @{@link AuthenticationProviderMock})
     *
     * @param username the user name.
     * @param password the password.
     */
    public void loginWithUser(String username, String password) {
        webDriver.get(rootUrl + "/login");
        webDriver.findElementByName("username").sendKeys(username);
        webDriver.findElementByName("password").sendKeys(password);
        webDriver.findElementByName("submit").click();
    }

    public RemoteWebDriver getRemoteWebDriver() {
        return this.webDriver;
    }

    /**
     * open the repository start page.
     */
    public void gotoWelcomePage() {
        this.webDriver.get(rootUrl);
    }

    /**
     * Adds a user to a namespace.
     *
     * @param userToAdd the user to add.
     * @param namespace the namespace.
     */
    public void addUserToNamespace(String userToAdd, String namespace, String... roles) {
        openManageNamespacesTab();
        WebElement namespaceFilterField = this.webDriver.findElementById("namespaceFilter");
        if(namespaceFilterField.isEnabled())
            namespaceFilterField.sendKeys(namespace);
        // xpath selects the row with the namespace in it and finds the link to open the collaborators dialog
        WebElement manageUsersLink = this.webDriver.findElementByXPath("//div[@class='ng-binding' and contains(.,'" + namespace + "')]/../../td/a[@ng-click='manageUsers(namespace)']");
        manageUsersLink.click();
        this.webDriver.findElementByXPath("//button[@ng-click='createOrUpdateUser(newUser())']").click();
        WebElement userIdSearchBox = this.webDriver.findElementById("userId");
        userIdSearchBox.clear();
        userIdSearchBox.sendKeys(userToAdd);
        // tick all checkboxes (could be made configurable)
        this.webDriver.findElementById(userToAdd).click();
        selectRolesForUserInNamespaceDialog(roles);
        this.webDriver.findElementById("submitButton").click();
        this.webDriver.findElementByXPath("//button[contains(.,'Cancel')]").click();
    }

    private void selectRolesForUserInNamespaceDialog(String[] roles) {
        for (int i=0; i < roles.length; i++) {
            this.webDriver.findElementByXPath(ROLE_TO_LINK_MAP.get(roles[i])).click();
        }
    }
}
