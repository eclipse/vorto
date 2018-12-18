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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.datatype.impl.TypeImpl;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

/**
 * Construct local scope for cross reference type. This is used when convert from xmi to dsl, xmi
 * does not contain referecing EObject type. However we can constuct the cross reference based on
 * type href info e.g. "using|com.bosch.Color"
 * 
 */
public class DatatypeScopeHelper {

  public static IScope getDataTypeReferenceScope(IQualifiedNameConverter converter, EObject context,
      EReference reference) {
    IScope scope = IScope.NULLSCOPE;

    if (context instanceof ObjectPropertyType
        && DatatypeScopeHelper.isFromVirtualFile((ObjectPropertyType) context)) {
      scope = DatatypeScopeHelper.getObjectPropertyScope(converter, (ObjectPropertyType) context,
          reference);

    } else if (context instanceof ReturnObjectType) {
      scope = DatatypeScopeHelper.getReturnObjectTypeScope(converter, (ReturnObjectType) context,
          reference);

    } else if (context instanceof RefParam) {

      scope = DatatypeScopeHelper.getRefParamTypeScope(converter, (RefParam) context, reference);

    }
    return scope;
  }

  public static IScope getObjectPropertyScope(IQualifiedNameConverter converter,
      ObjectPropertyType objectPropertyType, EReference reference) {

    try {
      return getTypeScope(converter, objectPropertyType.getType(), reference);
    } catch (Exception e) {
    } catch (Error e) {
    }

    return IScope.NULLSCOPE;
  }

  public static IScope getReturnObjectTypeScope(IQualifiedNameConverter converter,
      ReturnObjectType returnObjectType, EReference reference) {
    try {
      return getTypeScope(converter, returnObjectType.getReturnType(), reference);
    } catch (Exception e) {
      e.printStackTrace();
    } catch (Error e) {
    }

    return IScope.NULLSCOPE;
  }

  public static IScope getRefParamTypeScope(IQualifiedNameConverter converter, RefParam refParam,
      EReference reference) {
    try {
      return getTypeScope(converter, refParam.getType(), reference);
    } catch (Exception e) {

    } catch (Error e) {
    }

    return IScope.NULLSCOPE;
  }

  private static IScope getTypeScope(IQualifiedNameConverter converter, Type type,
      EReference reference) {
    List<IEObjectDescription> objects = new ArrayList<IEObjectDescription>();

    String objectPropertyTypeName =
        getObjectPropertyNameFromProxyUri(((TypeImpl) type).eProxyURI().toString());

    objects.add(
        EObjectDescription.create(converter.toQualifiedName(objectPropertyTypeName), reference));
    return new ProxyUriReferenceBasedScope(IScope.NULLSCOPE, objects);
  }

  public static boolean isFromVirtualFile(ObjectPropertyType objectPropertyType) {
    return objectPropertyType.eResource().getURI().toString().contains("NON_EXIST_DSL_FILE");
  }

  // Get name from string 'using|com.bosch.Color;1.0.0
  private static String getObjectPropertyNameFromProxyUri(String proxyUri) {
    String importPart = proxyUri.substring("using|".length() + 1);
    importPart = importPart.substring(0, importPart.indexOf(";"));
    String name = importPart.substring(importPart.lastIndexOf(".") + 1);
    return name;
  }
}
