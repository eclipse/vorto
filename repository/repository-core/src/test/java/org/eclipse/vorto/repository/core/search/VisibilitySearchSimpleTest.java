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
package org.eclipse.vorto.repository.core.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
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
    List<ModelInfo> model = testInfrastructure.getRepositoryFactory().getRepository( testInfrastructure.createUserContext("alex")).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    // "alex" user updates the only imported model's visibility to public
    ModelId updated =  testInfrastructure.getRepositoryFactory().getRepository( testInfrastructure.createUserContext("alex"))
        .updateVisibility(model.get(0).getId(), "Public");

    // importing another model as private
     testInfrastructure.importModel( testInfrastructure.INFORMATION_MODEL,  testInfrastructure.createUserContext("alex", "playground"));
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testPlainVisibilityPublic() {
    String query = "visibility:Public";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) = 'public'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainVisibilityPublicCaseInsensitive() {
    String query = "visibility:pUBLIC";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) = 'public'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  /**
   * Expected to return 0 model - value incomplete
   */
  @Test
  public void testVisibilityPublicIncomplete() {
    String query = "visibility:Pub";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) = 'pub'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(0,  testInfrastructure.getSearchService().search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPublic() {
    String query = "visibility:*blic";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%blic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:*BLIC";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%blic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPublic() {
    String query = "visibility:?ublic";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '_ublic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:?UBLIC";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '_ublic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPublic() {
    String query = "visibility:Publ*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'publ%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:PUBL*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'publ%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPublic() {
    String query = "visibility:Publi?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'publi_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:pUBLI?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'publi_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPublic() {
    String query = "visibility:Pu*c";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pu%c'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:pU*C";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pu%c'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPublic() {
    String query = "visibility:Pu?lic";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pu_lic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:pU?LIC";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pu_lic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPublic() {
    String query = "visibility:*u?lic";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%u_lic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:*U?LIC";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%u_lic'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPublic() {
    String query = "visibility:P?bl*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'p_bl%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPublicCaseInsensitive() {
    String query = "visibility:p?BL*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'p_bl%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainVisibilityPrivate() {
    String query = "visibility:Private";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) = 'private'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pRIVATE";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) = 'private'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  /**
   * Expected to return 0 model - value incomplete
   */
  @Test
  public void testVisibilityPrivateIncomplete() {
    String query = "visibility:Pri";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) = 'pri'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(0,  testInfrastructure.getSearchService().search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPrivate() {
    String query = "visibility:*vate";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%vate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingMultiWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:*VATE";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%vate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPrivate() {
    String query = "visibility:?rivate";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '_rivate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:?RIVATE";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '_rivate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPrivate() {
    String query = "visibility:Priv*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'priv%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pRIV*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'priv%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPrivate() {
    String query = "visibility:Privat?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'privat_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pRIVAT?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'privat_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPrivate() {
    String query = "visibility:Pr*e";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pr%e'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pR*E";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pr%e'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPrivate() {
    String query = "visibility:Pr?vate";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pr_vate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:pR?VATE";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'pr_vate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPrivate() {
    String query = "visibility:*r?vate";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%r_vate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:*R?VATE";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE '%r_vate'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPrivate() {
    String query = "visibility:P?iva*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'p_iva%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardVisibilityPrivateCaseInsensitive() {
    String query = "visibility:p?IV*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:visibility]) LIKE 'p_iv%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model =  testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals( testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

}
