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

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class groups all tests scoped to the model details UI.
 */
public class ModelDetailsUITest extends AbstractUITest {

  @Override
  protected void setUpTest() throws Exception {

  }

  /**
   * This performs the following:
   * <ol>
   *   <li>
   *     Log in with the sysadmin user
   *   </li>
   *   <li>
   *     Creates a namespace with that user
   *   </li>
   *   <li>
   *     Navigates to the welcome page and accepts cookies
   *   </li>
   *   <li>
   *     Creates a model (still with the sysadmin user)
   *   </li>
   *   <li>
   *     Scrolls down the page to "see" the save button
   *   </li>
   *   <li>
   *     Saves that model with no change
   *   </li>
   *   <li>
   *     Waits for the success message to be visible / fails if it isn't in a small amount of time
   *   </li>
   * </ol>
   * @throws Exception
   */
  @Test
  public void verifySuccessMessageUponSavingModel() throws Exception {
    RemoteWebDriver remoteWebDriver = this.seleniumVortoHelper.getRemoteWebDriver();
    seleniumVortoHelper.loginWithUser("user1", "pass");

    // create a namespace (reuse existing test) - TODO generalize and put in abstract instead of basic
    this.seleniumVortoHelper.loginWithUser("user1", "pass");
    this.seleniumVortoHelper.createNamespace(
        SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);

    // check if the namespace was created successfully.
    this.seleniumVortoHelper.openManageNamespacesTab();
    this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath(
        "//td/div[@class='ng-binding' and contains(.,'"
            + SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX
            + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + "')]");
    seleniumVortoHelper.gotoWelcomePage();
    // dismisses cookie consent, since selenium claims the button in the banner would be overlapping with the save button
    // (maybe caused by resolution during test?)
    seleniumVortoHelper.allowCookies();

    remoteWebDriver.findElementByXPath("//a[@ng-click='openCreateModelDialog()']").click();
    remoteWebDriver.findElementByXPath("//input[@name='modelType' and @value='InformationModel']")
        .click();
    remoteWebDriver.findElementByXPath("//button[contains(@ng-click,'next(')]").click();
    Select namespaceComboBox = new Select(
        remoteWebDriver.findElementById(SeleniumVortoHelper.ID_CB_NAMESPACE_ROOT));
    namespaceComboBox.selectByVisibleText(
        SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
    WebElement modelNameTextField = remoteWebDriver.findElementByName("modelName");
    modelNameTextField.sendKeys(SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL);
    remoteWebDriver.findElementByXPath("//button[contains(@ng-click,'next(')]").click();
    remoteWebDriver.findElementByXPath("//button[text()='Create']").click();
    // wait for the model details dialog to show up.
    remoteWebDriver.findElementByXPath(
        "//dd[@class='ng-binding' and .='" + SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL + "']");

    WebElement saveButton = remoteWebDriver
        .findElementByXPath("//a[@class='btn btn-sm btn-primary pull-right']");
    // scrolls down the page to see the button
    remoteWebDriver.executeScript("arguments[0].scrollIntoView();", saveButton);
    // saves the model - no change but button enabled and will perform a back-end save
    saveButton.click();

    // waits a few seconds, then checks if the message is visible
    WebDriverWait wait = new WebDriverWait(remoteWebDriver, 5);
    wait.until(ExpectedConditions
        .visibilityOfElementLocated(By.xpath("//span[text() = ' Model saved successfully']")));

  }
}
