/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 * <p>
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests visibility-based tagged searches.<br/>
 *
 * @author mena-bosch
 */
public class VisibilitySearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    
    testInfrastructure = new SearchTestInfrastructure();
    
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);
    List<ModelInfo> model = testInfrastructure.getRepositoryFactory()
        .getRepository(testInfrastructure.createUserContext("alex")).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    // "alex" user updates the only imported model's visibility to public
    ModelId updated = testInfrastructure.getRepositoryFactory()
        .getRepository(testInfrastructure.createUserContext("alex"))
        .updateVisibility(model.get(0).getId(), "Public");

    // importing another model as private
    testInfrastructure.importModel(testInfrastructure.INFORMATION_MODEL,
        testInfrastructure.createUserContext("alex", "playground"));
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testPlainVisibilityPublic() {
    String query = "visibility:Public";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainVisibilityPublicCaseInsensitive() {
    String query = "visibility:pUBLIC";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * Expected to return 0 model - value incomplete
   */
  @Test
  public void testVisibilityPublicIncomplete() {
    String query = "visibility:Pub";
    assertEquals(0, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPublic() {
    String query = "visibility:*blic";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:*BLIC";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPublic() {
    String query = "visibility:?ublic";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:?UBLIC";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPublic() {
    String query = "visibility:Publ*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:PUBL*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPublic() {
    String query = "visibility:Publi?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:pUBLI?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPublic() {
    String query = "visibility:Pu*c";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:pU*C";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPublic() {
    String query = "visibility:Pu?lic";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:pU?LIC";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPublic() {
    String query = "visibility:*u?lic";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:*U?LIC";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPublic() {
    String query = "visibility:P?bl*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:p?BL*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainVisibilityPrivate() {
    String query = "visibility:Private";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pRIVATE";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * Expected to return 0 model - value incomplete
   */
  @Test
  public void testVisibilityPrivateIncomplete() {
    String query = "visibility:Pri";
    assertEquals(0, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPrivate() {
    String query = "visibility:*vate";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:*VATE";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPrivate() {
    String query = "visibility:?rivate";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:?RIVATE";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPrivate() {
    String query = "visibility:Priv*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pRIV*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPrivate() {
    String query = "visibility:Privat?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pRIVAT?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPrivate() {
    String query = "visibility:Pr*e";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pR*E";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPrivate() {
    String query = "visibility:Pr?vate";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pR?VATE";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPrivate() {
    String query = "visibility:*r?vate";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:*R?VATE";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPrivate() {
    String query = "visibility:P?iva*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:p?IV*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

}
