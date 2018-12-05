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

import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.repository.core.IModelRepository;
import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class FunctionblockModelParser extends AbstractModelParser {

  public FunctionblockModelParser(String fileName, IModelRepository repository,
      ErrorMessageProvider errorMessageProvider) {
    super(fileName, repository, errorMessageProvider);
  }

  @Override
  protected Injector getInjector() {
    return new FunctionblockStandaloneSetup().createInjectorAndDoEMFRegistration();
  }
}
