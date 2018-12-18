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
package org.eclipse.vorto.editor.datatype;

import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

public class QualifiedNameWithVersionProvider extends DefaultDeclarativeQualifiedNameProvider {

  public QualifiedName qualifiedName(Model e) {
    QualifiedName namespace = getConverter().toQualifiedName(e.getNamespace());
    return QualifiedName.create(namespace.getSegments()).append(e.getName());
  }
}
