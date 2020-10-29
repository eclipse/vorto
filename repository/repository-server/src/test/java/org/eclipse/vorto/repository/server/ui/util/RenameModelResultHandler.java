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
package org.eclipse.vorto.repository.server.ui.util;


import static org.junit.Assert.assertFalse;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Handles expected results after renaming a model.
 */
public class RenameModelResultHandler implements ResultHandler {

  private RemoteWebDriver driver;
  private RenameModelParams params;

  /**
   * Initializes a new {@link ResultHandler} for renaming models, with the given {@link RenameModelParams}.
   *
   * @param driver
   * @param params
   */
  public RenameModelResultHandler(RemoteWebDriver driver, RenameModelParams params) {
    this.driver = driver;
    this.params = params;
  }

  /**
   * Renames the model, waits for the page to reload and verifies the new model's name and namespace
   * conform to the {@link RenameModelParams} this handler was initialized with.
   */
  @Override
  public void succeed() {
    driver.findElementByXPath("//button[contains(., 'Rename')]").click();
    // wait for the model details dialog to show up.
    driver.findElementByXPath(
        String.format(
            "//dd[@class='ng-binding' and .='%s']",
            params.getNewName()
        )
    );
    // model details being reloaded - waiting 5 minutes max
    WebDriverWait wait = new WebDriverWait(driver, 300);
    wait.until(
        ExpectedConditions.visibilityOf(
            // also looks into the ID label to verify the new namespace is present and lowercased
            driver.findElementByXPath(
                String.format(
                    "//dd[contains(.,'%s')]",
                    params.getNewSubNamespace().toLowerCase()
                )
            )
        )
    );
  }

  /**
   * Verifies the model cannot be renamed by checking the {@literal Rename} button's disabled in
   * the modal.
   */
  @Override
  public void fail() {
    // waiting  for the button status to refresh
    WebDriverWait wait = new WebDriverWait(driver, 60);
    wait.until(
        ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(.,'Rename') and (@disabled)]")
        )
    );
  }
}
