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

import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Helper class for vorto repository Selenium tests.
 */
public class SeleniumVortoHelper {

    public static String ID_CB_MODEL_STATE = "dropdownState";

    public static void allowCookies(RemoteWebDriver webDriver) {
        WebElement cookieFooter = webDriver.findElementById("cookieconsent:desc");
        Assert.assertTrue(cookieFooter.isDisplayed());
        WebElement allowCookieButton = webDriver.findElementByLinkText("Allow cookies");
        allowCookieButton.click();
        WebDriverWait wait5Secs = new WebDriverWait(webDriver,5);
        wait5Secs.until(ExpectedConditions.invisibilityOf(cookieFooter));
    }

    public static void selectModelStateInComboBox(RemoteWebDriver webDriver, ModelState modelState)  {
        Select stateComboBox = new Select(webDriver.findElementById(ID_CB_MODEL_STATE));
        String stateText;
        if (modelState == null) {
            stateText = "all";
        } else {
            stateText = modelState.getName();
        }
        stateComboBox.selectByValue(stateText);
    }

}
