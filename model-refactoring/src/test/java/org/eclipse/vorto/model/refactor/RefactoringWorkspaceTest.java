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
package org.eclipse.vorto.model.refactor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class RefactoringWorkspaceTest {

  @BeforeClass
  public static void initParser() {
    ModelWorkspaceReader.init();
  }
  
  @Test
  public void testChangeNamespaceWithoutReferences() {
    IModelWorkspace workspace =
        IModelWorkspace.newReader()
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/Brightness.type"),
                ModelType.Datatype)
            .read();

    ChangeSet changeSet = RefactoringTask.from(workspace).toNamespace("private.vorto.alex").execute();
    
    assertEquals(1,changeSet.get().size());
    assertEquals(1,changeSet.getChanges().size());
    assertEquals("private.vorto.alex",changeSet.get().get(0).getNamespace());

  }
  
  @Test
  public void testChangeNamespaceWithReferences() {
    IModelWorkspace workspace =
        IModelWorkspace.newReader()
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/Brightness.type"),
                ModelType.Datatype)
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/Dimmer.type"),
                ModelType.Datatype)
            .read();

    ChangeSet changeSet = RefactoringTask.from(workspace).toNamespace("private.vorto.alex").execute();
    
    assertEquals(2,changeSet.get().size());
    assertEquals(2,changeSet.getChanges().size());
    
    Entity dimmer = (Entity)changeSet.get().stream().filter(m -> m.getName().equals("Dimmer")).findFirst().get();
    assertEquals(2,dimmer.getReferences().size());
    
    assertEquals("private.vorto.alex",dimmer.getNamespace());
    assertEquals("private.vorto.alex.Brightness",dimmer.getReferences().get(0).getImportedNamespace());
    assertEquals("org.eclipse.vorto.Temperature",dimmer.getReferences().get(1).getImportedNamespace());
    
    ObjectPropertyType type = (ObjectPropertyType)dimmer.getProperties().get(0).getType();
    assertTrue(type.getType() instanceof Enum);
    assertEquals("private.vorto.alex",type.getType().getNamespace());
  }
  
  @Test
  public void testChangeNamespaceIgnoringSpecificNamespace() {
    IModelWorkspace workspace =
        IModelWorkspace.newReader()
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/Brightness.type"),
                ModelType.Datatype)
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/Dimmer.type"),
                ModelType.Datatype)
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/Color.fbmodel"),
                ModelType.Functionblock)
            .read();

    ChangeSet changeSet = RefactoringTask.from(workspace).toNamespace("private.vorto.alex","org.eclipse.vorto").execute();
    
    assertEquals(3,changeSet.get().size());
    assertEquals(2,changeSet.getChanges().size());
    
    FunctionblockModel color = (FunctionblockModel)changeSet.get().stream().filter(m -> m.getName().equals("Color")).findFirst().get();
    assertEquals("org.eclipse.vorto",color.getNamespace());
    
  }
}
