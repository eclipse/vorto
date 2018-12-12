/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.server.it;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.junit.Test;

public class RepositoryJavaClientTest extends AbstractIntegrationTest  {

  private IRepositoryClient repositoryClient = null;
  
  @Override
  protected void setUpTest() throws Exception {
    createModel("Color.type", "org.eclipse.vorto.examples.type:Color:1.0.0");
    this.repositoryClient = IRepositoryClient.newBuilder().setBaseUrl("http://localhost:" + port).build();
  }
  
  @Test
  public void testSearchModel() {
    assertEquals(1,repositoryClient.search("*").size());
  }

}
