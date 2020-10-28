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

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = {"local-ui-test"})
@SpringBootTest(classes = VortoRepository.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// https://github.com/spring-projects/spring-boot/issues/12280
@TestPropertySource(properties = {"repo.configFile = vorto-repository-config-h2.json"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(initializers = AbstractUITest.Initializer.class)
public abstract class AbstractUITest {

  @LocalServerPort
  protected int port;

  protected String rootUrl;

  protected SeleniumVortoHelper seleniumVortoHelper;

  @Autowired
  protected AuthenticationProviderMock mock;

  @Autowired
  protected UserRepository userRepository;

  @Rule
  public BrowserWebDriverContainer chrome =
      new BrowserWebDriverContainer()
          .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, new File("./target/"))
          .withCapabilities(DesiredCapabilities.chrome());

  @BeforeClass
  public static void configureOAuthConfiguration() {
    System.setProperty("eclipse_clientid", "foo");
    System.setProperty("eclipse_clientSecret", "foo");
    System.setProperty("github_clientid", "foo");
    System.setProperty("github_clientSecret", "foo");
    System.setProperty("eidp_clientid", "foo");
    System.setProperty("eidp_clientSecret", "foo");
    System.setProperty("suite_clientid", "foo");
    System.setProperty("suite_clientSecret", "foo");
    System.setProperty("line.separator", "\n");
  }

  @Before
  public void prepareTests() throws Exception {
    setRootUrl();
    setUpTest();
    chrome.getWebDriver().manage().deleteAllCookies();
  }

  private void setRootUrl() throws Exception {
    rootUrl = String.format("http://host.testcontainers.internal:%d", port);
    chrome.getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    this.seleniumVortoHelper = new SeleniumVortoHelper(chrome.getWebDriver(), rootUrl);
  }

  /**
   * Exposes the repository (random) port on the host system to the test container after initialization.
   */
  public static class Initializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      applicationContext.addApplicationListener(
          (ApplicationListener<EmbeddedServletContainerInitializedEvent>) event -> {
            Testcontainers.exposeHostPorts(event.getEmbeddedServletContainer().getPort());
          });
    }
  }

  protected abstract void setUpTest() throws Exception;

  // methods below constitute basic tests on their own, but can also be conveniently reused by
  // more elaborate tests

  public void testCreateNamespace() {
    this.seleniumVortoHelper.loginWithUser("user1", "pass");
    this.seleniumVortoHelper.createNamespace(
        SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE);
    this.seleniumVortoHelper.openManageNamespacesTab();
    // check if the namespace was created successfully.
    this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath(
        "//td/div[@class='ng-binding' and contains(.,'"
            + SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX
            + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE + "')]");
  }

  public void testCreateInfoModel() {
    RemoteWebDriver remoteWebDriver = this.seleniumVortoHelper.getRemoteWebDriver();
    // there should be no create button before logging in.
    List<WebElement> createModelButtonList = this.seleniumVortoHelper.getRemoteWebDriver()
        .findElementsByXPath("//a[@ng-click='openCreateModelDialog()']");
    Assert.assertTrue(createModelButtonList.isEmpty());
    // create a namespace (reuse existing test)
    testCreateNamespace();
    // now the create button should be available
    this.seleniumVortoHelper.gotoWelcomePage();
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
  }

}
