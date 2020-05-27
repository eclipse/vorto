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

import org.eclipse.vorto.repository.server.it.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;
import java.util.concurrent.TimeUnit;

@ActiveProfiles( profiles={"local-ui-test"})
@ContextConfiguration(initializers = AbstractUITest.Initializer.class)
public abstract class AbstractUITest extends AbstractIntegrationTest {

    protected String rootUrl;

    @Rule
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("./target/"))
                    .withCapabilities(DesiredCapabilities.chrome());

    @Before
    public void setRootUrl() throws Exception {
        rootUrl = String.format("http://host.testcontainers.internal:%d", port);
        chrome.getWebDriver().manage().deleteAllCookies();
        chrome.getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /**
     * Exposes the repository (random) port on the host system to the test container after initialization.
     */
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.addApplicationListener((ApplicationListener<EmbeddedServletContainerInitializedEvent>) event -> {
                Testcontainers.exposeHostPorts(event.getEmbeddedServletContainer().getPort());
            });
        }
    }
}
