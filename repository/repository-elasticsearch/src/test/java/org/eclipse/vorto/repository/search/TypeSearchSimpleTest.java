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
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests model type based tagged searches.<br/> Note that not all types are tested against all
 * possible case and wildcard scenarios here, to keep the number of tests reasonable.
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
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(0, model.size());
  }

  @Test
  public void testPlainFunctionblock() {
    String query = "type:Functionblock";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.FUNCTIONBLOCK_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainFunctionblockCaseInsensitive() {
    String query = "type:fUNCTIONBLOCK";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.FUNCTIONBLOCK_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainInfomodel() {
    String query = "type:InformationModel";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainInfomodelCaseInsensitive() {
    String query = "type:iNFORMATIONmODEL";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * The query here is so ambiguous that it should catch both infomodel and functionblock.
   */
  @Test
  public void testTwoModelsCaseInsensitive() {
    String query = "type:*ON*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
    assertTrue(model.stream().map(SearchTestInfrastructure::getFileName).allMatch(
        s -> s.equals(testInfrastructure.INFORMATION_MODEL) || s
            .equals(testInfrastructure.FUNCTIONBLOCK_MODEL)));
  }

  @Test
  public void testPlainDatatype() {
    String query = "type:Datatype";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainDatatypeCaseInsensitive() {
    String query = "type:dATATYPE";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainMapping() {
    String query = "type:Mapping";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.MAPPING_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainMappingCaseInsensitive() {
    String query = "type:mAPPING";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.MAPPING_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testAllTypesWithSingleWildcard() {
    String query = "type:*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(4, model.size());
  }

  @Test
  public void testThreeTypesWithWildcardsAndRepeatedTagCaseInsensitive() {
    String query = "type:m* type:?ATA*e type:?nf????tioNm*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
    assertTrue(model.stream().map(SearchTestInfrastructure::getFileName)
        .noneMatch(s -> s.equals(testInfrastructure.FUNCTIONBLOCK_MODEL)));
  }

}
