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

import java.util.Objects;
import org.eclipse.vorto.model.ModelType;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Handles expected results after creating a model.
 */
public class CreateModelResultHandler implements ResultHandler {

    private RemoteWebDriver driver;
    private CreateModelParams params;

    /**
     * Initializes a new {@link ResultHandler} for creating models, optionally with the given {@link CreateModelParams}.
     *
     * @param driver
     * @param params
     */
    public CreateModelResultHandler(RemoteWebDriver driver, CreateModelParams... params) {
        this.driver = driver;
        if (!Objects.isNull(params) && params.length > 0) {
            this.params = params[0];
        } else {
            this.params = CreateModelParams.defaults();
        }
    }

    /**
     * Clicks "Next" (if the model's an Informationmodel), then "Create", in the wizard modal, and
     * verifies the model's name appears once the details reload.
     */
    @Override public void succeed() {
        if (params.getType() == ModelType.InformationModel) {
            driver.findElementByXPath("//button[contains(@ng-click,'next(')]").click();
        }
        driver.findElementByXPath("//button[contains(., 'Create')]").click();
        // wait for the model details dialog to show up.
        driver.findElementByXPath(
            String.format("//dd[@class='ng-binding' and .='%s']", params.getName()));
    }

    /**
     * Cannot click "Next" through the model creation wizard.
     */
    @Override public void fail() {
        // could verify the visibility of the validation div as well, but it's better done at caller
        // level
        assertFalse(driver.findElementByXPath("//button[contains(@ng-click,'next(')]").isEnabled());
    }


}
