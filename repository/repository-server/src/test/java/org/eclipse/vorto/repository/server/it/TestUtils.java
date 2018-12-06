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

import org.apache.commons.text.RandomStringGenerator;

public class TestUtils {

  private static final RandomStringGenerator onlylettergenerator =
      new RandomStringGenerator.Builder().withinRange('a', 'z').build();

  public static String createRandomString(int length) {
    return onlylettergenerator.generate(length);
  }
}
