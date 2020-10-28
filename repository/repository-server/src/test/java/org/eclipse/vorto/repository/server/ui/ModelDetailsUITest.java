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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * This class groups all tests scoped to the model details UI.
 */
public class ModelDetailsUITest extends AbstractUITest {

  /**
   * The ace editor is rendered as div and span elements, which seems to make it impossible to
   * invoke click and sendKeys methods on its elements. <br/>
   * This hack infers the AngularJS scope from a specific DOM element (here, the editor's content
   * div), then retrieves the actual editor as a Javascript variable programmatically and injects
   * the desired value into it.
   */
  public static final String SET_EDITOR_SYNTAX_FORMAT = "angular.element(document.getElementsByClassName('ace_content')[0]).scope().modelEditor.getSession().getDocument().setValue('%s')";

  @Override
  protected void setUpTest() throws Exception {

  }

  @Test
  public void testSaveModelSuccessfulNotification() {
    testCreateInfoModel();
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    // fullscreen to make save button visible
    driver.manage().window().fullscreen();
    //find the save button and click it to trigger notification
    driver.findElementByXPath("//a[@ng-click='saveModel()']").click();
    // make sure the success message is displayed (wait a little longer)
    driver.findElementByXPath("//span[contains(.,'Model saved successfully')]");
  }

  @Test
  public void testSaveModelErrorNotification() {
    testCreateInfoModel();
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    // going full screen
    driver.manage().window().fullscreen();
    WebElement textArea = driver.findElementByXPath("//div[@class='ace_content']");
    driver.executeScript("arguments[0].scrollIntoView();", textArea);
    driver.executeScript(
        String.format(SET_EDITOR_SYNTAX_FORMAT, "this will break the syntax")
    );
    // TODO error message
  }

}
