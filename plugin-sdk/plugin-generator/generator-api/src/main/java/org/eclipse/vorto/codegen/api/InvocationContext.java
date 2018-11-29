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
/*******************************************************************************
 * x * Copyright (c) 2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.codegen.api.mapping.DefaultMapped;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.codegen.api.mapping.NullMapped;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EntityPropertySource;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EnumPropertySource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfomodelSource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.Model;

public class InvocationContext {

  private List<MappingRule> mappingRules;

  private Map<String, String> configProperties;

  private IGeneratorLookup lookupService;

  private FileContent importedFile = null;

  private static final IGeneratorLookup NOOP_RUNTIME = new NoopGeneratorLookup();

  private static final IVortoCodeGenerator NOOP_GEN = new NoopGenerator();

  public InvocationContext(List<MappingModel> mappingModels, IGeneratorLookup generatorRuntime,
      Map<String, String> configProperties) {
    this.mappingRules = new ArrayList<MappingRule>();
    for (MappingModel mappingModel : mappingModels) {
      this.mappingRules.addAll(mappingModel.getRules());
    }
    this.lookupService = generatorRuntime != null ? generatorRuntime : NOOP_RUNTIME;

    this.configProperties = configProperties;
  }

  public static InvocationContext simpleInvocationContext() {
    return new InvocationContext(new ArrayList<MappingModel>(), NOOP_RUNTIME,
        Collections.<String, String>emptyMap());
  }

  public static InvocationContext simpleInvocationContext(Map<String, String> configProperties) {
    return new InvocationContext(new ArrayList<MappingModel>(), NOOP_RUNTIME, configProperties);
  }

  /**
   * Finds the mapped element by a stereotype for a given information model
   * 
   * @param informationModel
   * @param stereoType
   * @return mapped element
   */
  public IMapped<InformationModel> getMappedElement(final InformationModel informationModel,
      final String stereoType) {
    for (MappingRule rule : mappingRules) {
      for (Source ruleSource : rule.getSources()) {
        if (ruleSource instanceof InfomodelSource
            && EcoreUtil.equals(((InfomodelSource) ruleSource).getModel(), informationModel)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<InformationModel>(informationModel,
              (StereoTypeTarget) rule.getTarget());
        }
      }
    }
    return new NullMapped<InformationModel>(informationModel);
  }

  /**
   * Finds the mapped element by a stereotype for a given functionblock model
   * 
   * @param functionblockModel
   * @param stereoType
   * @return mapped element
   */
  public IMapped<FunctionblockModel> getMappedElement(final FunctionblockModel functionblockModel,
      final String stereoType) {
    for (MappingRule rule : mappingRules) {
      for (Source ruleSource : rule.getSources()) {
        if (ruleSource instanceof FunctionBlockSource
            && EcoreUtil.equals(((FunctionBlockSource) ruleSource).getModel(), functionblockModel)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<FunctionblockModel>(functionblockModel,
              (StereoTypeTarget) rule.getTarget());
        }
      }
    }
    return new NullMapped<FunctionblockModel>(functionblockModel);
  }

  /**
   * Finds the mapped element by a stereotype for a given enumeration literal
   * 
   * @param enumLiteral
   * @param stereoType
   * @return mapped element
   */
  public IMapped<EnumLiteral> getMappedElement(final EnumLiteral enumLiteral,
      final String stereoType) {
    for (MappingRule rule : mappingRules) {
      for (Source ruleSource : rule.getSources()) {
        if (ruleSource instanceof EnumPropertySource
            && EcoreUtil.equals(((EnumPropertySource) ruleSource).getProperty(), enumLiteral)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<EnumLiteral>(enumLiteral, (StereoTypeTarget) rule.getTarget());
        }
      }
    }
    return new NullMapped<EnumLiteral>(enumLiteral);
  }

  private boolean matchesStereoType(final String stereoType,
      final StereoTypeTarget stereoTypeTarget) {
    return stereoTypeTarget.getName().equalsIgnoreCase(stereoType);
  }

  private boolean matchesProperty(final Property srcProp, final Property tgtProp) {
    FunctionBlock srcFb = (FunctionBlock) srcProp.eContainer().eContainer();
    FunctionBlock tgtFb = (FunctionBlock) tgtProp.eContainer().eContainer();
    FunctionblockModel srcFbModel = (FunctionblockModel) srcFb.eContainer();
    FunctionblockModel tgtFbModel = (FunctionblockModel) tgtFb.eContainer();
    return EcoreUtil.equals(srcProp, tgtProp) && EcoreUtil.equals(srcFbModel, tgtFbModel);
  }

  /**
   * Finds the mapped element by a stereotype for a given property
   * 
   * @param property
   * @param stereoType
   * @return mapped element
   */
  public IMapped<Property> getMappedElement(final Property property, final String stereoType) {

    for (MappingRule rule : mappingRules) {
      for (Source ruleSource : rule.getSources()) {
        if (ruleSource instanceof ConfigurationSource
            && matchesProperty(((ConfigurationSource) ruleSource).getProperty(), property)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
        } else if (ruleSource instanceof StatusSource
            && matchesProperty(((StatusSource) ruleSource).getProperty(), property)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
        } else if (ruleSource instanceof FaultSource
            && matchesProperty(((FaultSource) ruleSource).getProperty(), property)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
        } else if (ruleSource instanceof EntityPropertySource
            && matchesProperty(((EntityPropertySource) ruleSource).getProperty(), property)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<Property>(property, (StereoTypeTarget) rule.getTarget());
        }
      }
    }

    return new NullMapped<Property>(property);
  }

  private boolean matchesOperation(final Operation srcOp, final Operation tgtOp) {
    FunctionBlock srcFb = (FunctionBlock) srcOp.eContainer();
    FunctionBlock tgtFb = (FunctionBlock) tgtOp.eContainer();
    FunctionblockModel srcFbModel = (FunctionblockModel) srcFb.eContainer();
    FunctionblockModel tgtFbModel = (FunctionblockModel) tgtFb.eContainer();
    return EcoreUtil.equals(srcOp, tgtOp) && EcoreUtil.equals(srcFbModel, tgtFbModel);
  }

  /**
   * Finds the mapped element by a stereotype for a given operation
   * 
   * @param operation
   * @param stereoType
   * @return mapped element
   */
  public IMapped<Operation> getMappedElement(final Operation operation, final String stereoType) {
    for (MappingRule rule : mappingRules) {
      for (Source ruleSource : rule.getSources()) {
        if (ruleSource instanceof OperationSource
            && matchesOperation(((OperationSource) ruleSource).getOperation(), operation)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<Operation>(operation, (StereoTypeTarget) rule.getTarget());
        }
      }
    }

    return new NullMapped<Operation>(operation);
  }

  /**
   * Finds the mapped element by a stereotype for a given model attribute
   * 
   * @param operation
   * @param stereoType
   * @return mapped element
   */
  public IMapped<ModelAttribute> getMappedModelAttribute(final Model model,
      final ModelAttribute attribute, final String stereoType) {
    for (MappingRule rule : mappingRules) {
      for (Source ruleSource : rule.getSources()) {
        if (model instanceof InformationModel && ruleSource instanceof InfoModelAttributeSource
            && ((InfoModelAttributeSource) ruleSource).getAttribute().equals(attribute)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
        } else if (model instanceof FunctionblockModel
            && ruleSource instanceof FunctionBlockAttributeSource
            && ((FunctionBlockAttributeSource) ruleSource).getAttribute().equals(attribute)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
        } else if (model instanceof Enum && ruleSource instanceof EnumAttributeSource
            && ((EnumAttributeSource) ruleSource).getAttribute().equals(attribute)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
        } else if (model instanceof Entity && ruleSource instanceof EnumAttributeSource
            && ((EntityAttributeSource) ruleSource).getAttribute().equals(attribute)
            && matchesStereoType(stereoType, (StereoTypeTarget) rule.getTarget())) {
          return new DefaultMapped<ModelAttribute>(attribute, (StereoTypeTarget) rule.getTarget());
        }
      }
    }

    return new NullMapped<ModelAttribute>(attribute);
  }

  /**
   * Finds and returns a registered code generator by its service key
   * 
   * @param key
   * @return code generator, never null
   */
  public IVortoCodeGenerator lookupGenerator(String key) {
    IVortoCodeGenerator gen = this.lookupService.lookupByKey(key);
    if (gen == null) {
      return NOOP_GEN;
    } else {
      return gen;
    }
  }

  /**
   * Configuration Properties that have been passed in to the code generator
   * 
   * @return
   */
  public Map<String, String> getConfigurationProperties() {
    return Collections.unmodifiableMap(this.configProperties);
  }

  static class NoopGeneratorLookup implements IGeneratorLookup {

    @Override
    public IVortoCodeGenerator lookupByKey(String key) {
      return NOOP_GEN;
    }
  }

  static class NoopGenerator implements IVortoCodeGenerator {

    @Override
    public IGenerationResult generate(InformationModel model, InvocationContext context,
        IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
      return null;
    }

    @Override
    public String getServiceKey() {
      return "noop";
    }

    @Override
    public GeneratorInfo getInfo() {
      return null;
    }
  }

  public Optional<FileContent> getImportedFile() {
    return Optional.of(this.importedFile);
  }

  public void setImportedFile(FileContent importedFile) {
    this.importedFile = importedFile;
  }

}
