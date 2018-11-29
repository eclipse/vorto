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

/**
 * A {@link ICodeGeneratorTask} use generation templates which contain the context specific outcome
 * logic
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 * 
 */
public interface ITemplate<Element> {

  /**
   * gets the generation template for the specified context
   * 
   * @param element the information model element to be converted by the template
   * @param context
   * @return generated content for the specified context
   */
  String getContent(Element element, InvocationContext context);

}
