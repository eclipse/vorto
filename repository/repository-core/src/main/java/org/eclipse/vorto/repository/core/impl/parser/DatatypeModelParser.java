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
package org.eclipse.vorto.repository.core.impl.parser;

import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class DatatypeModelParser extends AbstractModelParser {

  public DatatypeModelParser(String fileName, IModelRetrievalService modelRetrievalService,
      ErrorMessageProvider errorMessageProvider) {
    super(fileName, modelRetrievalService, errorMessageProvider);
  }

  @Override
  protected Injector getInjector() {
    return new DatatypeStandaloneSetup().createInjectorAndDoEMFRegistration();
  }

}
