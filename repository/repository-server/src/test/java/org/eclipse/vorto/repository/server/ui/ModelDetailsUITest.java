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

import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.server.ui.util.CreateModelParams;
import org.eclipse.vorto.repository.server.ui.util.RenameModelParams;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
  public static final String SET_EDITOR_SYNTAX_FORMAT =
      "angular.element(document.getElementsByClassName('ace_content')[0]).scope().modelEditor.getSession().getDocument().setValue('%s')";

  @Override
  protected void setUpTest() throws Exception {

  }

  @Test
  public void testSaveModelSuccessfulNotification() {
    createModel().succeed();
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    // fullscreen to make save button visible
    driver.manage().window().fullscreen();
    //find the save button and click it to trigger notification
    driver.findElementByXPath("//a[@ng-click='saveModel()']").click();
    // make sure the success message is displayed (wait a little longer)
    // waits 5 minutes max for model saving
    WebDriverWait wait = new WebDriverWait(driver, 300);
    wait.until(ExpectedConditions.visibilityOf(
        driver.findElementByXPath("//span[contains(.,'Model saved successfully')]")));
  }

  @Test
  public void testSaveModelErrorNotification() {
    createModel().succeed();
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    // going full screen
    driver.manage().window().fullscreen();
    WebElement textArea = driver.findElementByXPath("//div[@class='ace_content']");
    driver.executeScript("arguments[0].scrollIntoView();", textArea);
    // changes the editor's text via Javascript
    driver.executeScript(String.format(SET_EDITOR_SYNTAX_FORMAT, "this will break the syntax"));
    //find the save button and click it to trigger notification
    WebElement saveButton = driver.findElementByXPath("//a[@ng-click='saveModel()']");
    driver.executeScript("arguments[0].scrollIntoView();", saveButton);
    saveButton.click();
    WebDriverWait waitForErrorMessage = new WebDriverWait(driver, 10);
    // verifies a div "Cannot parse model" is visible
    // note: this does not seem to work with an xpath selector by class (alert / alert-danger) on
    // the outer div, which makes this test a bit brittle (e.g. if the message changes)
    waitForErrorMessage.until(ExpectedConditions
        .visibilityOfElementLocated(By.xpath("//span[contains(., 'Cannot parse model')]")));
  }

  @Test
  public void testCreateModelWithBadSubNS() {
    CreateModelParams params =
        new CreateModelParams().withName(CreateModelParams.defaults().getName())
            .withNamespace(CreateModelParams.defaults().getNamespace())
            .withType(CreateModelParams.defaults().getType().name())
            .withSubnamespace("1ncorrect.$ubNamespace");
    createModel(params).fail();
    // additional check for validation hint
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    assertTrue(driver.findElementByXPath("//div[@class='invalid-input']").isDisplayed());
  }

  @Test
  public void testCreateModelWithCapitalizedSubNS() {
    CreateModelParams params =
        new CreateModelParams().withName(CreateModelParams.defaults().getName())
            // uppercases the default namespace
            .withNamespace(CreateModelParams.defaults().getNamespace().toUpperCase())
            .withType(CreateModelParams.defaults().getType().name());
    // this will check the namespace is lowercased when redirected to the model details after creating
    createModel(params).succeed();
  }

  /**
   * This creates a model, then tests that loading it in the UI with a capitalized namespace
   * still returns the right model. <br/>
   * Moreover, test that the model's namespace displayed in ID field is lower-cased.
   */
  @Test
  public void testLoadModelWithCapitalizedNS() {
    createModel().succeed();
    ModelId id = new ModelId(CreateModelParams.defaults().getName(),
        CreateModelParams.defaults().getNamespace().toUpperCase(), "1.0.0");
    loadModelDetailsUI(id);
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    // ModelId#getPrettyFormat will return the namespace lowercased
    driver.findElementByXPath(String.format("//dd[contains(., '%s')]", id.getPrettyFormat()));
  }

  /**
   * This creates the garden-variety model, then renames it with a bad sub-namespace notation.
   */
  @Test
  public void testRenameModelWithBadSubNS() {
    createModel().succeed();
    ModelId id = new ModelId(CreateModelParams.defaults().getName(),
        CreateModelParams.defaults().getNamespace(), "1.0.0");
    RenameModelParams params = new RenameModelParams()
        // no name change
        .withNewName(CreateModelParams.defaults().getName())
        .withNewSubNamespace("bad.7ubNamespace");
    renameModel(id, params).fail();
    // additional check for validation hint
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    assertTrue(driver.findElementByXPath("//div[@class='invalid-input']").isDisplayed());
  }

  /**
   * This creates the default model, then renames it by adding a sub-namespace that's capitalized.
   */
  @Test
  public void testRenameModelWithCapitalizedSubNS() {
    createModel().succeed();
    ModelId id = new ModelId(CreateModelParams.defaults().getName(),
        CreateModelParams.defaults().getNamespace(), "1.0.0");
    RenameModelParams params = new RenameModelParams()
        // no name change
        .withNewName(CreateModelParams.defaults().getName())
        .withNewSubNamespace("VALID.SUB_N4MESPACE1");
    renameModel(id, params).succeed();
  }

}
