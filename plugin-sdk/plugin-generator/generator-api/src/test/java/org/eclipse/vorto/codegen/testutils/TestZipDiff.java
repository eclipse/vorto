package org.eclipse.vorto.codegen.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestZipDiff {

  @Test
  public void testZipDiff() {
    try {
      ZipDiff.Result result =
          new ZipDiff().diff(loadResource("diagnosis.zip"), loadResource("diagnosis-same.zip"));
      assertEquals(0, result.getAdded().size());
      assertEquals(0, result.getRemoved().size());
      assertEquals(0, result.getChanged().size());
      assertEquals(4, result.getUnchanged().size());
      System.out.println("-erle- : " + result.getUnchanged().size());
    } catch (IOException e) {
      fail("Exception");
    }
  }

  @Test
  public void testZipDiffWithZipInside() {
    try {
      ZipDiff.Result result = new ZipDiff().diff(loadResource("diagnosis-with-zip-inside.zip"),
          loadResource("diagnosis-with-zip-inside2.zip"));
      assertEquals(0, result.getAdded().size());
      assertEquals(0, result.getRemoved().size());
      assertEquals(0, result.getChanged().size());
      assertEquals(12, result.getUnchanged().size());
      System.out.println("-erle- : " + result.getUnchanged().size());
    } catch (IOException e) {
      fail("Exception");
    }
  }
  
  @Test
  public void testZipDiffWithZipInsideChanged() {
    try {
      ZipDiff.Result result = new ZipDiff().diff(loadResource("diagnosis-with-zip-inside.zip"),
          loadResource("diagnosis-with-zip-inside-changed.zip"));
      assertEquals(0, result.getAdded().size());
      assertEquals(0, result.getRemoved().size());
      assertEquals(1, result.getChanged().size());
      assertEquals(11, result.getUnchanged().size());
      System.out.println("-erle- : " + result.getUnchanged().size());
    } catch (IOException e) {
      fail("Exception");
    }
  }

  @Test
  public void testZipDiffWithExtra() {
    try {
      ZipDiff.Result result = new ZipDiff().diff(loadResource("diagnosis.zip"),
          loadResource("diagnosis-with-extra.zip"));
      assertEquals(1, result.getAdded().size());
      assertEquals(0, result.getRemoved().size());
      assertEquals(0, result.getChanged().size());
      assertEquals(4, result.getUnchanged().size());
      System.out.println("-erle- : " + result.getUnchanged().size());
    } catch (IOException e) {
      fail("Exception");
    }
  }

  @Test
  public void testZipDiffWithRemoved() {
    try {
      ZipDiff.Result result = new ZipDiff().diff(loadResource("diagnosis-with-extra.zip"),
          loadResource("diagnosis.zip"));
      assertEquals(0, result.getAdded().size());
      assertEquals(1, result.getRemoved().size());
      assertEquals(0, result.getChanged().size());
      assertEquals(4, result.getUnchanged().size());
      System.out.println("-erle- : " + result.getUnchanged().size());
    } catch (IOException e) {
      fail("Exception");
    }
  }

  @Test
  public void testZipDiffChanged() {
    try {
      ZipDiff.Result result =
          new ZipDiff().diff(loadResource("diagnosis.zip"), loadResource("diagnosis-changed.zip"));
      assertEquals(0, result.getAdded().size());
      assertEquals(0, result.getRemoved().size());
      assertEquals(1, result.getChanged().size());
      assertEquals(3, result.getUnchanged().size());
      System.out.println("-erle- : " + result.getUnchanged().size());
    } catch (IOException e) {
      fail("Exception");
    }
  }

  private ZipInputStream loadResource(String filename) throws IOException {
    return new ZipInputStream(new ByteArrayInputStream(
        IOUtils.toByteArray(this.getClass().getClassLoader().getResource(filename).openStream())));
  }

}
