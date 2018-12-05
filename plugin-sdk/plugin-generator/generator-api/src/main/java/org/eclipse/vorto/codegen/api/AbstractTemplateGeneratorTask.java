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
package org.eclipse.vorto.codegen.api;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public abstract class AbstractTemplateGeneratorTask<T>
    implements ICodeGeneratorTask<T> {

  public void generate(final T fragmentModel,
      final InvocationContext context, final IGeneratedWriter writer) {

    Generated generated = new Generated(getFileName(fragmentModel), getPath(fragmentModel),
        getTemplate().getContent(fragmentModel, context));
    writer.write(generated);
  }

  public abstract String getFileName(final T fragment);

  public abstract String getPath(final T fragment);

  public abstract ITemplate<T> getTemplate();

}
