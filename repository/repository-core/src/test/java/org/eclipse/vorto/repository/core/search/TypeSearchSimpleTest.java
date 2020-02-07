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
package org.eclipse.vorto.repository.core.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests model type based tagged searches.<br/> Note that not all types are tested against all
 * possible case and wildcard scenarios here, to keep the number of tests reasonable.<br/> Also,
 * some queries cannot be tested as full strings, because the order of appearance of {@literal
 * OR}-separated values in a {@literal CONTAINS} constraint, when different values containing at
 * least one wildcard are tested for the same tag, cannot be predicted.<br/> See {@link
 * SearchTestInfrastructure#assertContains(String, String...)} regarding that.
 *
 * @author mena-bosch
 */
public class TypeSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    // importing one model for each supported type here
    // function block
    testInfrastructure.importModel(testInfrastructure.FUNCTIONBLOCK_MODEL);

    // info model
    testInfrastructure.importModel(testInfrastructure.INFORMATION_MODEL);

    // data type
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);

    // mapping
    testInfrastructure.importModel(testInfrastructure.MAPPING_MODEL);

  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testNoModel() {
    String query = "type:*potato?*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) LIKE '%potato_%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(0, model.size());
  }

  @Test
  public void testPlainFunctionblock() {
    String query = "type:Functionblock";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'functionblock'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.FUNCTIONBLOCK_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainFunctionblockCaseInsensitive() {
    String query = "type:fUNCTIONBLOCK";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'functionblock'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.FUNCTIONBLOCK_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainInfomodel() {
    String query = "type:InformationModel";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'informationmodel'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainInfomodelCaseInsensitive() {
    String query = "type:iNFORMATIONmODEL";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'informationmodel'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, model.get(0).getFileName());
  }

  /**
   * The query here is so ambiguous that it should catch both infomodel and functionblock.
   */
  @Test
  public void testTwoModelsCaseInsensitive() {
    String query = "type:*ON*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) LIKE '%on%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(2, model.size());
    assertTrue(model.stream().map(ModelInfo::getFileName).allMatch(
        s -> s.equals(testInfrastructure.INFORMATION_MODEL) || s
            .equals(testInfrastructure.FUNCTIONBLOCK_MODEL)));
  }

  @Test
  public void testPlainDatatype() {
    String query = "type:Datatype";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'datatype'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainDatatypeCaseInsensitive() {
    String query = "type:dATATYPE";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'datatype'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainMapping() {
    String query = "type:Mapping";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'mapping'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.MAPPING_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testPlainMappingCaseInsensitive() {
    String query = "type:mAPPING";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) = 'mapping'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.MAPPING_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testAllTypesWithSingleWildcard() {
    String query = "type:*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:type]) LIKE '%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(4, model.size());
  }

  @Test
  public void testThreeTypesWithWildcardsAndRepeatedTagCaseInsensitive() {
    String query = "type:m* type:?ATA*e type:?nf????tioNm*";
    // no guarantee on order: checking query by multiple "contains" assertions
    testInfrastructure.assertContains(
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)),
        "SELECT * FROM [nt:file] WHERE (CONTAINS ([vorto:type], ",
        "_nf____tionm%",
        "_ata%e",
        "m%"
    );
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(3, model.size());
    assertTrue(model.stream().map(ModelInfo::getFileName)
        .noneMatch(s -> s.equals(testInfrastructure.FUNCTIONBLOCK_MODEL)));
  }

}
