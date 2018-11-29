package org.eclipse.vorto.repository.importer;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.junit.Before;
import org.junit.Test;

public class DependencyManagerTest {

  private DependencyManager dm = null;

  @Before
  public void setUp() {
    this.dm = new DependencyManager();
  }

  @Test
  public void testDependentDatatypes() {
    ModelInfo subunit = create("Subunit", ModelType.Datatype);
    ModelInfo unit = create("Unit", ModelType.Datatype, subunit);
    ModelInfo temp = create("Temperature", ModelType.Datatype, unit);

    dm.addResource(subunit);
    dm.addResource(unit);
    dm.addResource(temp);

    assertEquals(subunit.getId(), dm.getSorted().get(0).getId());
    assertEquals(unit.getId(), dm.getSorted().get(1).getId());
    assertEquals(temp.getId(), dm.getSorted().get(2).getId());
  }

  @Test
  public void testDependentTypesWithFB() {
    ModelInfo subunit = create("Subunit", ModelType.Datatype);
    ModelInfo unit = create("Unit", ModelType.Datatype, subunit);
    ModelInfo temp = create("Temperature", ModelType.Datatype, unit);
    ModelInfo fridge = create("Fridge", ModelType.Functionblock, temp);

    dm.addResource(unit);
    dm.addResource(subunit);
    dm.addResource(temp);
    dm.addResource(fridge);

    assertEquals(subunit.getId(), dm.getSorted().get(0).getId());
    assertEquals(unit.getId(), dm.getSorted().get(1).getId());
    assertEquals(temp.getId(), dm.getSorted().get(2).getId());
    assertEquals(fridge.getId(), dm.getSorted().get(3).getId());
  }

  @Test
  public void testDependentTypesWithFB2() {
    ModelInfo subunit = create("Subunit", ModelType.Datatype);
    ModelInfo unit = create("Unit", ModelType.Datatype, subunit);
    ModelInfo temp = create("Temperature", ModelType.Datatype, unit);
    ModelInfo superFridge = create("SuperFridge", ModelType.Functionblock, temp);
    ModelInfo fridge = create("Fridge", ModelType.Functionblock, superFridge);

    dm.addResource(unit);
    dm.addResource(subunit);
    dm.addResource(temp);
    dm.addResource(fridge);
    dm.addResource(superFridge);

    assertEquals(subunit.getId(), dm.getSorted().get(0).getId());
    assertEquals(unit.getId(), dm.getSorted().get(1).getId());
    assertEquals(temp.getId(), dm.getSorted().get(2).getId());
    assertEquals(superFridge.getId(), dm.getSorted().get(3).getId());
    assertEquals(fridge.getId(), dm.getSorted().get(4).getId());
  }

  protected ModelInfo create(String name, ModelType type, ModelInfo... references) {
    final ModelId id = new ModelId(name, "org.eclipse.vorto", "1.0.0");
    ModelInfo resource = new ModelInfo(id, type);
    resource.setReferences(Arrays.asList(references).stream().map(reference -> reference.getId())
        .collect(Collectors.toList()));
    Arrays.asList(references).stream().forEach(it -> it.addReferencedBy(id));
    return resource;
  }
}
