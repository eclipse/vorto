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

package org.eclipse.vorto.editor.scope;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.SimpleScope;

public class ProxyUriReferenceBasedScope extends SimpleScope {

  public ProxyUriReferenceBasedScope(IScope parent, Iterable<IEObjectDescription> descriptions) {
    super(parent, descriptions);
  }

  @Override
  protected Iterable<IEObjectDescription> getLocalElementsByEObject(final EObject object,
      final URI uri) {
    return getAllLocalElements();
  }

}
