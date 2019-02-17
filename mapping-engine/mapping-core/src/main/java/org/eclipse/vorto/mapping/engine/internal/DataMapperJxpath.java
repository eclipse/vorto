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
package org.eclipse.vorto.mapping.engine.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.ObjectContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.text.StrSubstitutor;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.MappingContext;
import org.eclipse.vorto.mapping.engine.MappingException;
import org.eclipse.vorto.mapping.engine.internal.functions.CustomFunctionsLibrary;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.model.runtime.PropertyValue;

/**
 * 
 * Extend this class in order to implement a platform mapper that maps normlized vorto model to the
 * target platform specific data model
 *
 */
public class DataMapperJxpath implements IDataMapper {

  private IMappingSpecification specification;

  private JxPathFactory jxpathHelper = null;

  private JexlEngine jexlEngine = null;

  private static final String STEREOTYPE_SOURCE = "source";
  private static final String STEREOTYPE_TARGET = "target";
  private static final String ATTRIBUTE_XPATH = "xpath";
  private static final String ATTRIBUTE_CONDITION = "condition";

  public DataMapperJxpath(IMappingSpecification mappingSpecification,
      CustomFunctionsLibrary functionLibrary) {
    this.specification = mappingSpecification;
    this.jxpathHelper = new JxPathFactory(functionLibrary);
    this.jexlEngine = createJexlEngine(functionLibrary);
  }

  private static JexlEngine createJexlEngine(CustomFunctionsLibrary functionLibrary) {
    JexlEngine jexl = new JexlEngine();
    jexl.setFunctions(functionLibrary.getConditionFunctions());
    return jexl;
  }

  public InfomodelValue map(Object input, MappingContext mappingContext) {

    JXPathContext context = jxpathHelper.newContext(input);

    InfomodelValue normalized = new InfomodelValue(specification.getInfoModel());

    final Infomodel deviceInfoModel = specification.getInfoModel();

    for (ModelProperty fbProperty : deviceInfoModel.getFunctionblocks()) {
      FunctionblockValue mappedFb = mapFunctionBlock(fbProperty, context);
      if (mappedFb != null) {
        normalized.withFunctionblock(fbProperty.getName(), mappedFb);
      }
    }

    return normalized;
  }

  private FunctionblockValue mapFunctionBlock(ModelProperty fbProperty, JXPathContext context) {

    FunctionblockModel fbModel = specification.getFunctionBlock(fbProperty.getName());
    
    if (!matchesCondition(fbModel,context)) {
    	return null;
    }

    FunctionblockValue fbData = new FunctionblockValue(fbModel);

    for (ModelProperty statusProperty : fbModel.getStatusProperties()) {

      try {
        Object mapped = this.mapProperty(statusProperty, context);
        if (mapped != null) {
          fbData.withStatusProperty(statusProperty.getName(), mapped);
        }
      } catch (JXPathNotFoundException ex) {
        if (statusProperty.isMandatory()) {
          return null;
        }
      } catch (JXPathInvalidAccessException ex) {
        if (ex.getCause() instanceof JXPathNotFoundException) {
          if (statusProperty.isMandatory()) {
            return null;
          }
        }
        throw new MappingException("A problem occured during mapping", ex);
      }

    }

    for (ModelProperty configProperty : fbModel.getConfigurationProperties()) {

      try {
        Object mapped = this.mapProperty(configProperty, context);
        if (mapped != null) {
          fbData.withConfigurationProperty(configProperty.getName(), mapped);
        }
      } catch (JXPathNotFoundException ex) {
        if (configProperty.isMandatory()) {
          return null;
        }
      } catch (JXPathInvalidAccessException ex) {
        if (ex.getCause() instanceof JXPathNotFoundException) {
          if (configProperty.isMandatory()) {
            return null;
          }
        }
        throw new MappingException("A problem occured during mapping", ex);
      }
    }

    return onlyReturnIfPopulated(fbData);
  }

  private boolean matchesCondition(FunctionblockModel fbModel, JXPathContext context) {
	Optional<Stereotype> conditionStereotype = fbModel.getStereotype("condition");
    if (conditionStereotype.isPresent() && conditionStereotype.get().hasAttribute("value")) {
    	Expression e =
  	          jexlEngine.createExpression(normalizeCondition(conditionStereotype.get().getAttributes().get("value")));
  	      JexlContext jc = new ObjectContext<Object>(jexlEngine, context.getContextBean());
  	      jc.set("this", context.getContextBean());
  	      return (boolean) e.evaluate(jc);
    } else {
    	return true;
    }
  }

private FunctionblockValue onlyReturnIfPopulated(FunctionblockValue fbData) {
    if (!fbData.getConfiguration().isEmpty() || !fbData.getStatus().isEmpty()) {
      return fbData;
    } else {
      return null;
    }
  }

  private Object mapProperty(ModelProperty property, JXPathContext input) {
    Optional<Stereotype> sourceStereotype = property.getStereotype(STEREOTYPE_SOURCE);
    if (sourceStereotype.isPresent() && hasXpath(sourceStereotype.get().getAttributes())) {
      String expression =
          replacePlaceHolders(sourceStereotype.get().getAttributes().get(ATTRIBUTE_XPATH),
              sourceStereotype.get().getAttributes());

      if (matchesPropertyCondition(sourceStereotype.get(), input)) {
        return input.getValue(expression);
      }
    }

    return null;

  }

  private boolean matchesPropertyCondition(Stereotype stereotype, JXPathContext context) {
    if (stereotype.hasAttribute(ATTRIBUTE_CONDITION)) {
      Expression e =
          jexlEngine.createExpression(normalizeCondition(stereotype.getAttributes().get(ATTRIBUTE_CONDITION)));
      JexlContext jc = new ObjectContext<Object>(jexlEngine, context.getContextBean());
      jc.set("this", context.getContextBean());
      return (boolean) e.evaluate(jc);
    } else {
      return true;
    }
  }

  private String normalizeCondition(final String expression) {
    return expression.replaceAll("/", "\\.");
  }

  private boolean hasXpath(Map<String, String> stereotypeAttributes) {
    return stereotypeAttributes.containsKey(ATTRIBUTE_XPATH)
        && !stereotypeAttributes.get(ATTRIBUTE_XPATH).equals("");
  }

  private String replacePlaceHolders(String expression, Map<String, String> mappedAttributes) {
    StrSubstitutor sub = new StrSubstitutor(mappedAttributes);
    return sub.replace(expression);
  }

  @Override
  public InfomodelValue mapSource(Object input) {
    return this.map(input, MappingContext.empty());
  }

  @Override
  public Object mapTarget(PropertyValue newValue, Optional<PropertyValue> oldValue,
      String infomodelProperty) {
    FunctionblockModel fbm = this.specification.getFunctionBlock(infomodelProperty);
    if (fbm == null) {
      throw new IllegalArgumentException(
          "No property with the given name could be found in Information Model");
    }

    Optional<Stereotype> targetStereotype = newValue.getMeta().getStereotype(STEREOTYPE_TARGET);
    if (!targetStereotype.isPresent()) {
      throw new MappingException("No mapping rule defined for property");
    }

    Map<String, Object> jxpathContext = new HashMap<String, Object>();
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("newValue", newValue.getValue());
    param.put("oldValue", oldValue.isPresent() ? oldValue.get().getValue() : null);

    jxpathContext.put("ctx", param);
    final String functionName =
        "convert" + newValue.getMeta().getName().substring(0, 1).toUpperCase()
            + newValue.getMeta().getName().substring(1);

    final String xpath = infomodelProperty.toLowerCase() + ":" + functionName + "(ctx)";
    JXPathContext context = jxpathHelper.newContext(jxpathContext);
    try {
      return context.getValue(xpath);
    } catch (Exception ex) {
      throw new MappingException("Problem occurred during mapping", ex);
    }
  }

}
