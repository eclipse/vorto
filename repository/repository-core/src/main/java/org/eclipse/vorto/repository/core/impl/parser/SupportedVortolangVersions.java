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
package org.eclipse.vorto.repository.core.impl.parser;

import java.util.Arrays;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;

public enum SupportedVortolangVersions {
  BASE("1.0");
  String version;

  SupportedVortolangVersions(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public static void validate(Model model, String fileName) throws ValidationException {
    if (Arrays.stream(SupportedVortolangVersions.values())
        .map(SupportedVortolangVersions::getVersion).noneMatch(v -> v.equals(model.getLang()))) {
      throw new ValidationException(
          String.format("Vortolang version [%s] is not supported", model.getLang()),
          AbstractModelParser.getModelInfo(model, fileName)
              .orElse(AbstractModelParser.getModelInfoFromFilename(fileName))
      );
    }
  }

}
