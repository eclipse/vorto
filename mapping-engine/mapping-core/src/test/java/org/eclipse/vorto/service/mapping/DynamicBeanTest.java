package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Map;
import org.eclipse.vorto.mapping.engine.internal.DynamicBean;
import org.junit.Test;

public class DynamicBeanTest {

  @Test
  public void testAddSimpleProperty() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data", 5);
    assertEquals(5, bean.getProperty("data"));
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testAddNestedProperty() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/key", 5);
    System.out.println(bean);
    assertNotNull(bean.getProperty("data"));
    assertEquals(5, ((Map) bean.getProperty("data")).get("key"));
  }

  @Test
  public void testAddNestedPropertyToExistingElement() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/key", 5);
    bean.setProperty("data/name", "Alex");
    assertEquals(5, ((Map<?, ?>) bean.getProperty("data")).get("key"));
    assertEquals("Alex", ((Map<?, ?>) bean.getProperty("data")).get("name"));
    System.out.println(bean);
  }

  @Test
  public void testAddSeveralNestedProperties() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/person/name", "Alex");
    System.out.println(bean);

  }

  @Test
  public void testAddArrayItem() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/key", 5);
    bean.setProperty("data/persons[1]/name", "Alex");
    System.out.println(bean);

  }

  @Test
  public void testAddArrayItemToExistingArray() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/key", 5);
    bean.setProperty("data/persons[1]/name", "Alex");
    bean.setProperty("data/persons[1]/male", true);
    System.out.println(bean);
  }

  @Test
  public void testAddArrayItemToExistingArray2() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/key", 5);
    bean.setProperty("data/persons[1]/name", "Alex");
    bean.setProperty("data/persons[1]/male", true);
    bean.setProperty("data/persons[2]/name", "Claudia");
    bean.setProperty("data/persons[2]/male", false);
    System.out.println(bean);
  }

  @Test
  public void testAddArrayItemToExistingArray3() {
    DynamicBean bean = new DynamicBean();
    bean.setProperty("data/key", 5);
    bean.setProperty("data/persons[1]/name", "Alex");
    bean.setProperty("data/persons[2]/name", "Claudia");
    bean.setProperty("data/persons[name='Alex']/male", true);
    System.out.println(bean);
  }
}
