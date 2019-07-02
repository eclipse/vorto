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
package org.eclipse.vorto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.model.FunctionblockModel.FunctionblockModelBuilder;
import org.eclipse.vorto.model.ModelProperty.Builder;

public abstract class AbstractModel extends DefaultMappedElement implements IModel {

  
  protected String vortolang;
  protected ModelId id;
  protected ModelType type;
  protected String displayName;
  protected String description;
  protected String category;
  protected String fileName;

  protected List<ModelId> references = new ArrayList<>();
  
  private static final String VERSION = "1.0";

  public AbstractModel(ModelId modelId, ModelType modelType) {
	this.vortolang = VERSION;
    this.id = modelId;
    this.type = modelType;
    this.fileName = modelId.getName()+modelType.getExtension();
  }

  public AbstractModel() {
  }
  
  public String getVortolang() {
	  return vortolang;
  }
  
  public void setVortolang(String vortolang) {
	  this.vortolang = vortolang;
  }

  public ModelId getId() {
    return id;
  }

  public void setId(ModelId id) {
    this.id = id;
  }

  public ModelType getType() {
    return type;
  }

  public void setType(ModelType type) {
    this.type = type;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ModelId> getReferences() {
    return references;
  }

  public void setReferences(List<ModelId> references) {
    this.references = references;
  }

  public String getFileName() {
    return fileName;
  }
  
  /**
   * Full Qualified file name of the model
   * 
   * @return full-qualified file name in the form of <namespace>-<name>-<version>.<extension>
   */
  public String getFullQualifiedFileName() {
    return id.getPrettyFormat().replace(":", "-").replace("\\.", "_") + type.getExtension();
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }



  public static abstract class Builder<T extends AbstractModel> {
    protected T model;
    
    public Builder(T model) {
      this.model = model;
      vortolang("1.0");
    }
    
    public Builder<T> vortolang(String lang) {
      this.model.setVortolang(lang);
      return this;
    }
    
    public Builder<T> description(String description) {
      this.model.setDescription(description);
      return this;
    }
    
    public Builder<T> displayname(String displayname) {
      this.model.setDisplayName(displayname);
      return this;
    }
    
    public Builder<T> reference(ModelId reference) {
      this.model.references.add(reference);
      return this;
    }
    
    public Builder<T> category(String category) {
      this.model.setCategory(category);
      return this;
    }
    
    public Builder<T> withStereotype(String stereoTypeName, Map<String,String> attributes,String targetPlatformKey) {
      model.setTargetPlatformKey(targetPlatformKey);
      model.addStereotype(Stereotype.create(stereoTypeName, attributes));
      return this;
    }
    
    public Builder<T> withTargetPlatform(String targetPlatformKey) {
      model.setTargetPlatformKey(targetPlatformKey);
      return this;
    }
    
    public T build() {
      return this.model;
    }
  }

}
