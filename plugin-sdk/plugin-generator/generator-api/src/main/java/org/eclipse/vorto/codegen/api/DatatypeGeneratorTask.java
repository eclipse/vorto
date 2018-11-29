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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Generator task that traverses all types used in an information model and generates their platform
 * specific representations
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class DatatypeGeneratorTask implements ICodeGeneratorTask<InformationModel> {

  private IFileTemplate<Entity> entityTemplate;
  private IFileTemplate<Enum> enumTemplate;

  public DatatypeGeneratorTask(IFileTemplate<Entity> entityTemplate,
      IFileTemplate<Enum> enumTemplate) {
    this.entityTemplate = entityTemplate;
    this.enumTemplate = enumTemplate;
  }

  @Override
  public void generate(InformationModel ctx, InvocationContext context, IGeneratedWriter writer) {
    Set<Type> allTypesUsedInModel = new HashSet<>();
    for (FunctionblockProperty prop : ctx.getProperties()) {
      allTypesUsedInModel.addAll(getTypes(prop.getType()));
    }

    for (Type type : allTypesUsedInModel) {
      if (type instanceof Entity) {
        writer.write(new Generated(entityTemplate.getFileName((Entity) type),
            entityTemplate.getPath((Entity) type),
            entityTemplate.getContent((Entity) type, context)));
      } else if (type instanceof Enum) {
        writer.write(new Generated(enumTemplate.getFileName((Enum) type),
            enumTemplate.getPath((Enum) type), enumTemplate.getContent((Enum) type, context)));
      }
    }
  }

  private static Set<Type> getTypes(FunctionblockModel model) {
    Set<Type> allTypes = new LinkedHashSet<>();
    TreeIterator<EObject> iterator = model.eAllContents();
    while (iterator.hasNext()) {
      EObject current = iterator.next();
      if (current instanceof RefParam) {
        addTypeAndReferences(((RefParam) current).getType(), allTypes);
      } else if (current instanceof ReturnObjectType) {
        addTypeAndReferences(((ReturnObjectType) current).getReturnType(), allTypes);
      } else if (current instanceof ObjectPropertyType) {
        addTypeAndReferences(((ObjectPropertyType) current).getType(), allTypes);
      }
    }
    return allTypes;
  }

  private static void addTypeAndReferences(Type type, Set<Type> container) {
    if (!container.contains(type)) {
      container.add(type);
      Set<Type> references = getTypesOfType(type, container);
      container.addAll(references);
    }
  }

  private static Set<Type> getTypesOfType(Type type, Set<Type> container) {
    TreeIterator<EObject> iterator = type.eAllContents();
    while (iterator.hasNext()) {
      EObject current = iterator.next();
      if (current instanceof ObjectPropertyType) {
        if (!container.contains(current)) {
          container.add(((ObjectPropertyType) current).getType());
          Set<Type> moreTypes = getTypesOfType(((ObjectPropertyType) current).getType(), container);
          container.addAll(moreTypes);
        }

      }
    }

    return container;
  }
}
