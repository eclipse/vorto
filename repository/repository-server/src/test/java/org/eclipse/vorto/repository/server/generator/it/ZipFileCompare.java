package org.eclipse.vorto.repository.server.generator.it;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import java.io.ByteArrayInputStream;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.codegen.testutils.ZipDiff;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

public class ZipFileCompare implements ResultMatcher {
  private ZipInputStream baselineZip = null;
  
  public static ResultMatcher equals(byte[] baseline) {
    return new ZipFileCompare(baseline);
  }
  
  public ZipFileCompare(byte[] baseline) {
    this.baselineZip = new ZipInputStream(new ByteArrayInputStream(baseline));
  }

  @Override
  public void match(MvcResult result) throws Exception {
    ZipInputStream generatedZip =
        new ZipInputStream(new ByteArrayInputStream(result.getResponse().getContentAsByteArray()));
    
    ZipDiff.Result diffResult = new ZipDiff().diff(baselineZip, generatedZip);
    
    assertEquals("Zip Diff Result: Added", diffResult.getAdded().size(), 0);
    assertEquals("Zip Diff Result: Removed", diffResult.getRemoved().size(), 0);
    assertEquals("Zip Diff Result: Changed", diffResult.getChanged().size(), 0);
  }

}
