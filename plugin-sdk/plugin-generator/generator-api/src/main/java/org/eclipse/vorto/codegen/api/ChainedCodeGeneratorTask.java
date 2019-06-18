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

import java.util.ArrayList;
import java.util.List;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class ChainedCodeGeneratorTask<Element> implements ICodeGeneratorTask<Element> {

  private List<ICodeGeneratorTask<Element>> tasks = new ArrayList<ICodeGeneratorTask<Element>>();

  public void addTask(ICodeGeneratorTask<Element> task) {
    this.tasks.add(task);
  }

  public void generate(Element element, InvocationContext context, IGeneratedWriter outputter) {
    for (ICodeGeneratorTask<Element> task : tasks) {
      task.generate(element, context, outputter);
    }
  }
}
