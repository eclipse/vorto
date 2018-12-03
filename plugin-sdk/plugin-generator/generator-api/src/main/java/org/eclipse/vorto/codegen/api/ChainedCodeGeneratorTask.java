/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper Generator Task that is able to execute many generator tasks for the same model element
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
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
