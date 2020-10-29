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
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.Strings;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.server.ui.util.CreateModelParams;
import org.eclipse.vorto.repository.server.ui.util.CreateModelResultHandler;
import org.eclipse.vorto.repository.server.ui.util.CreateNamespaceParams;
import org.eclipse.vorto.repository.server.ui.util.RenameModelParams;
import org.eclipse.vorto.repository.server.ui.util.RenameModelResultHandler;
import org.eclipse.vorto.repository.web.VortoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
    chrome.getWebDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
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

  /**
   * Creates a namespace with the given {@link CreateNamespaceParams} or
   * {@link CreateNamespaceParams#DEFAULT} if none specified. <br/>
   * Verifies the lower-cased namespace is returned by the UI as created.
   *
   * @param params
   */
  public void testCreateNamespace(CreateNamespaceParams... params) {
    CreateNamespaceParams usedParams = null;
    if (Objects.isNull(params) || params.length == 0) {
      usedParams = CreateNamespaceParams.defaults();
    } else {
      usedParams = params[0];
    }
    this.seleniumVortoHelper.loginWithUser(usedParams.getUsername(), usedParams.getPassword());
    this.seleniumVortoHelper.createNamespace(usedParams.getNamespaceName());
    this.seleniumVortoHelper.openManageNamespacesTab();
    // check if the namespace was created successfully.
    this.seleniumVortoHelper.getRemoteWebDriver().findElementByXPath(
        String.format(
            "//td/div[@class='ng-binding' and contains(.,'%s')]",
            usedParams.getNamespaceName().toLowerCase()
        )
    );

  }

  /**
   * Creates a model with the desired {@link CreateModelParams}, or {@link CreateModelParams#DEFAULT}
   * if none given. <br/>
   * TODO verify the UI logic differences between creating an information model and easier models.
   * <br/>
   * On success, verifies the namespace is lower-cased by the UI when returned.
   *
   * @param params
   */
  public CreateModelResultHandler createModel(CreateModelParams... params) {
    CreateModelParams usedParams;
    if (Objects.isNull(params) || params.length == 0) {
      usedParams = CreateModelParams.defaults();
    } else {
      usedParams = params[0];
    }
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
    remoteWebDriver.findElementByXPath(
        String.format(
            "//input[@name='modelType' and @value='%s']",
            usedParams.getType()
        )
    )
        .click();
    remoteWebDriver.findElementByXPath("//button[contains(@ng-click,'next(')]").click();
    Select namespaceComboBox = new Select(
        remoteWebDriver.findElementById(SeleniumVortoHelper.ID_CB_NAMESPACE_ROOT));
    namespaceComboBox.selectByVisibleText(usedParams.getNamespace().toLowerCase());
    WebElement modelNameTextField = remoteWebDriver.findElementByName("modelName");
    modelNameTextField.sendKeys(usedParams.getName());
    // sub-ns
    if (!Strings.isNullOrEmpty(usedParams.getSubNamespace())) {
      // this is actually the sub-namespace text field
      remoteWebDriver.findElementByName("modelNamespace").sendKeys(usedParams.getSubNamespace());
    }

    return new CreateModelResultHandler(remoteWebDriver, usedParams);

  }

  /**
   * This does the following:
   * <ol>
   *   <li>
   *     Loads the model's details page for the given {@link ModelId}
   *   </li>
   *   <li>
   *     Goes fullscreen
   *   </li>
   *   <li>
   *     Clicks the {@literal Rename} button
   *   </li>
   *   <li>
   *     Clears the new sub-namespace input and fills with data from given {@link RenameModelParams}
   *   </li>
   *   <li>
   *     Clears the new name input and fills with data from given {@link RenameModelParams}
   *   </li>
   *   <li>
   *     Returns the {@link RenameModelResultHandler} associated with this action.
   *   </li>
   * </ol>
   *
   * @param id
   * @param params
   * @return
   */
  public RenameModelResultHandler renameModel(ModelId id, RenameModelParams params) {
    loadModelDetailsUI(id);
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    driver.manage().window().fullscreen();
    driver.findElementByXPath("//a[contains(., 'Rename')]").click();
    // this is the sub-namespace text input, contrary to the advertised tag name
    driver.findElementsByName("namespace").get(0).clear();
    driver.findElementsByName("namespace").get(0).sendKeys(params.getNewSubNamespace());
    driver.findElementsByName("name").get(0).clear();
    driver.findElementsByName("name").get(0).sendKeys(params.getNewName());
    return new RenameModelResultHandler(driver, params);
  }

  /**
   * This does the following:
   * <ol>
   *   <li>
   *     Navigates to the model details UI page for that model ID
   *   </li>
   *   <li>
   *     Goes fullscreen
   *   </li>
   *   <li>
   *     Waits for the model to load, and verifies it is loaded by checking the {@code h2} element
   *     of the DOM bearing the model's name as a title.
   *   </li>
   * </ol>
   *
   * @param id
   */
  public void loadModelDetailsUI(ModelId id) {
    seleniumVortoHelper.goToModelDetails(id.getPrettyFormat());
    RemoteWebDriver driver = seleniumVortoHelper.getRemoteWebDriver();
    driver.manage().window().fullscreen();
    // loading a model through the UI can take a long time - giving it 5 mins and fetching the
    // h2 header with the model name
    WebDriverWait waitForDetailsLoading = new WebDriverWait(driver, 300);
    waitForDetailsLoading.until(
        ExpectedConditions
            .visibilityOf(
                driver.findElementByXPath(String.format("//h2[contains(., '%s')]", id.getName())))
    );

  }

}
