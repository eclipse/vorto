package org.eclipse.vorto.repository.web.api.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.plugin.generator.GeneratedOutput;
import org.eclipse.vorto.repository.plugin.generator.IGeneratorPluginService;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractGeneratorController extends AbstractRepositoryController {

  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";
  
  @Autowired
  protected IGeneratorPluginService generatorService;
  
  @Autowired
  protected ITenantService tenantService;
  
  protected void generateAndWriteToOutputStream(String modelId, String pluginKey, HttpServletRequest request, HttpServletResponse response) {
    generateAndWriteToOutputStream(modelId, pluginKey, getRequestParams(request), response);
  }
  
  protected void generateAndWriteToOutputStream(String modelId, String pluginKey, Map<String, String> params, HttpServletResponse response) {
    ModelId modelIdToGen = ModelId.fromPrettyFormat(modelId);

    try {
      GeneratedOutput generatedOutput = generatorService.generate(getUserContext(modelIdToGen),
          modelIdToGen, URLDecoder.decode(pluginKey, "utf-8"), params);
      writeToResponse(response, generatedOutput);
    } catch (IOException e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }
  
  protected IUserContext getUserContext(ModelId modelId) {
    Optional<Tenant> tenant = tenantService.getTenantFromNamespace(modelId.getNamespace());
    if (!tenant.isPresent()) {
      throw new ModelNotFoundException("The tenant for '" + modelId + "' could not be found.");
    }

    return UserContext.user(SecurityContextHolder.getContext().getAuthentication(),
        tenant.get().getTenantId());
  }

  protected void writeToResponse(final HttpServletResponse response, GeneratedOutput generatedOutput)
      throws IOException {
    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + generatedOutput.getFileName());
    response.setContentLengthLong(generatedOutput.getSize());
    response.setContentType(APPLICATION_OCTET_STREAM);

    IOUtils.copy(new ByteArrayInputStream(generatedOutput.getContent()),
        response.getOutputStream());
    response.flushBuffer();
  }

  protected Map<String, String> getRequestParams(final HttpServletRequest request) {
    Map<String, String> requestParams = new HashMap<>();
    request.getParameterMap().entrySet().stream().forEach(x -> {
      requestParams.put(x.getKey(), x.getValue()[0]);
    });

    return requestParams;
  }

}
