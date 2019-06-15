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
package org.eclipse.vorto.repository.core.impl.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import com.google.common.collect.Lists;

public class TypeImportValidation implements IModelValidator {

  private static final String FB_PROPERTY_PATTERN = "(\\^?[a-zA-Z_][a-zA-Z0-9_]*)\\s+[aA][sS]\\s+((\\w+\\.)*\\w+)\\s*";

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    if (!(modelResource instanceof ModelResource)) {
      return; // do not check types if passed resource is not a parse emf model resource
    }
    Collection<String> unImportedReferences = Lists.newArrayList();

    ModelResource emfModel = (ModelResource) modelResource;
    Model model = emfModel.getModel();

    if (model == null)
      return;

    if (model instanceof Entity) {
      unImportedReferences
          .addAll(getUnimportedProperties(((Entity) model).getProperties(), model.getReferences()));
    } else if (model instanceof FunctionblockModel) {
      unImportedReferences.addAll(validateFunctionBlock((FunctionblockModel) model));
    } else if (model instanceof InformationModel) {
      unImportedReferences.addAll(getUnimportedFunctionblocks(
          ((InformationModel) model).getProperties(), model.getReferences()));
    }

    unImportedReferences.forEach(ref -> System.out.println("Missing : " + ref));

    if (!unImportedReferences.isEmpty()) {
      throw new ValidationException(errorMessage(unImportedReferences), modelResource);
    }
  }

  private Collection<String> validateFunctionBlock(FunctionblockModel model)
      throws ValidationException {
    FunctionBlock functionBlock = model.getFunctionblock();

    Collection<String> unImportedReferences = Lists.newArrayList();

    if (functionBlock.getConfiguration() != null) {
      unImportedReferences.addAll(getUnimportedProperties(
          functionBlock.getConfiguration().getProperties(), model.getReferences()));
    }

    if (functionBlock.getStatus() != null) {
      unImportedReferences.addAll(getUnimportedProperties(functionBlock.getStatus().getProperties(),
          model.getReferences()));
    }

    if (functionBlock.getFault() != null) {
      unImportedReferences.addAll(
          getUnimportedProperties(functionBlock.getFault().getProperties(), model.getReferences()));
    }

    if (functionBlock.getEvents() != null) {
      functionBlock.getEvents().forEach(event -> {
        unImportedReferences
            .addAll(getUnimportedProperties(event.getProperties(), model.getReferences()));
      });
    }

    if (functionBlock.getOperations() != null) {
      unImportedReferences.addAll(
          getUnimportedOperationReturnTypes(functionBlock.getOperations(), model.getReferences()));
      functionBlock.getOperations().forEach(operation -> {
        unImportedReferences
            .addAll(getUnimportedRefParams(operation.getParams(), model.getReferences()));
      });
    }

    return unImportedReferences;
  }

  private Collection<String> getUnimportedProperties(EList<Property> properties,
      EList<ModelReference> importedRefs) {
    return getUnimportedReferences(properties, importedRefs, this::getPropertyTypeSignature,
        property -> property.getType() instanceof ObjectPropertyType);
  }

  private Collection<String> getUnimportedRefParams(EList<Param> params,
      EList<ModelReference> importedRefs) {
    return getUnimportedReferences(params, importedRefs, this::getRefParamTypeSignature,
        param -> param instanceof RefParam);
  }

  private Collection<String> getUnimportedOperationReturnTypes(EList<Operation> operations,
      EList<ModelReference> importedRefs) {
    return getUnimportedReferences(operations, importedRefs, this::getReturnObjectTypeSignature,
        operation -> operation.getReturnType() instanceof ReturnObjectType);
  }

  private Collection<String> getUnimportedFunctionblocks(
      EList<FunctionblockProperty> functionBlocks, EList<ModelReference> importedRefs) {
    return getUnimportedReferences(functionBlocks, importedRefs,
        this::getFunctionBlockTypeSignature, null);
  }

  private <K> Collection<String> getUnimportedReferences(Collection<K> references,
      EList<ModelReference> importedRefs, Function<K, String> sigFunc, Predicate<K> primaryFilter) {
    assert (sigFunc != null);

    if (references == null) {
      return Collections.emptyList();
    }

    if (importedRefs == null) {
      return references.stream().map(sigFunc).collect(Collectors.toList());
    }

    Predicate<K> notInImports = inImports(importedRefs, sigFunc).negate();

    Predicate<K> filter = primaryFilter != null ? primaryFilter.and(notInImports) : notInImports;

    return references.stream().filter(filter).map(sigFunc).collect(Collectors.toList());
  }

  private String errorMessage(Collection<String> unImportedTypes) {
    return String.format(
        "The following property (properties) %s has not been imported (not in the \"using\" statement).",
        unImportedTypes.stream().collect(Collectors.joining(",", "[", "]")));
  }

  private <K> Predicate<K> inImports(EList<ModelReference> importedRefs,
      Function<K, String> signatureFunction) {
    return (reference) -> importedRefs.stream()
        .anyMatch(importedRef -> sameReference(importedRef, signatureFunction.apply(reference)));
  }

  private boolean sameReference(ModelReference reference, String signature) {
    String referenceSignature = reference.getImportedNamespace();

    // if signature is fully qualified, compare directly to the reference namespace, else just
    // compare
    // to the last part of the reference namespace
    if (signature.split(Pattern.quote(".")).length > 1) {
      return signature.equals(referenceSignature);
    } else {
      String[] referenceTypeNameComponents = referenceSignature.split(Pattern.quote("."));
      return signature.equals(referenceTypeNameComponents[referenceTypeNameComponents.length - 1]);
    }
  }

  private String getReturnObjectTypeSignature(Operation operation) {
    // Getting last child as composite node may contain 'multiple' keyword
    return NodeModelUtils
        .getTokenText(NodeModelUtils.getNode(operation.getReturnType()).getLastChild());
  }

  private String getRefParamTypeSignature(Param refParam) {
    // Getting last child as composite node may contain 'multiple' keyword
    return getNamespace(
        NodeModelUtils.getTokenText(NodeModelUtils.getNode(refParam).getLastChild()));
  }

  private String getPropertyTypeSignature(Property property) {
    return NodeModelUtils
        .getTokenText(NodeModelUtils.getNode((ObjectPropertyType) property.getType()));
  }

  private String getFunctionBlockTypeSignature(FunctionblockProperty fb) {
    return getNamespace(NodeModelUtils.getTokenText(NodeModelUtils.getNode(fb)));
  }

  private String getNamespace(String text) {
    Pattern pattern = Pattern.compile(FB_PROPERTY_PATTERN);
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      return matcher.group(2);
    }
    return text;
  }
}
