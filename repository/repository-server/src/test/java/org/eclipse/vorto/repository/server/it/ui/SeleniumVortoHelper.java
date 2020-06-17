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

import com.sun.mail.iap.ByteArray;
import org.apache.commons.fileupload.util.Streams;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Helper class for vorto repository Selenium tests.
 */
public class SeleniumVortoHelper {

    public static String ID_CB_MODEL_STATE = "dropdownState";

    public static String ID_CB_NAMESPACE_ROOT = "namespaceRoot";

    public static String USER1_PRIVATE_NAMESPACE = "vorto.private.user1";

    public static String USER1_EMPTY_INFO_MODEL = "EmptyInfoModel";

    private final RemoteWebDriver webDriver;

    private final String rootUrl;

    public SeleniumVortoHelper(RemoteWebDriver webDriver, String rootUrl) {
        this.webDriver = webDriver;
        this.rootUrl = rootUrl;
    }


    public void allowCookies() {
        webDriver.get(rootUrl);
        WebElement cookieFooter = webDriver.findElementById("cookieconsent:desc");
        Assert.assertTrue(cookieFooter.isDisplayed());
        WebElement allowCookieButton = webDriver.findElementByLinkText("Allow cookies");
        allowCookieButton.click();
        WebDriverWait wait5Secs = new WebDriverWait(webDriver,5);
        wait5Secs.until(ExpectedConditions.invisibilityOf(cookieFooter));
    }

    /**
     * Select the desired model state in the model state combo box ("null" = All states)
     *
     * @param modelState
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
        webDriver.findElementByLinkText("Manage").click();
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
        // Clear pre-filled text field.
        WebDriverWait wait5Secs = new WebDriverWait(webDriver, 5);
        wait5Secs.until(ExpectedConditions.elementToBeClickable(namespaceInputField));
        namespaceInputField.click();
        namespaceInputField.clear();
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

    public void loginWithUser(String username, String password) {
        webDriver.get(rootUrl + "/login");
        webDriver.findElementByName("username").sendKeys(username);
        webDriver.findElementByName("password").sendKeys(password);
        webDriver.findElementByName("submit").click();
    }

    public RemoteWebDriver getRemoteWebDriver() {
        return this.webDriver;
    }

    public String getTestFileContent(String fileName) throws IOException {
        return Streams.asString(SeleniumVortoHelper.class.getResourceAsStream(fileName));
    }

    public void gotoWelcomePage() {
        this.webDriver.get(rootUrl);
    }
}
