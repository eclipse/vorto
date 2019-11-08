package org.eclipse.vorto.repository.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VortoErrorHandler extends AbstractErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VortoErrorHandler.class);
    private static final String ERROR = "/error";

    private String htmlTemplate;

    public VortoErrorHandler(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(ERROR)
    public String handleError(HttpServletRequest request) {
        if (StringUtils.isBlank(htmlTemplate)) {
            loadHtmlTemplate();
        }
        Map<String, Object> errors = getErrorAttributes(request, false);
        if (Objects.nonNull(htmlTemplate) && Objects.nonNull(errors)) {
            return MessageFormat.format(htmlTemplate, getErrorDataAsStringArray(errors));
        }
        return initializationError(errors);
    }

    private void loadHtmlTemplate() {
        URL url = getClass().getClassLoader().getResource("static/error/error-template.html");
        if (Objects.nonNull(url)) {
            File file = new File(url.getFile());
            htmlTemplate = readHtmlTemplate(file);
        }
    }

    private String[] getErrorDataAsStringArray(Map<String, Object> errors) {
        return new String[]{
                        errors.getOrDefault("status", "").toString(),
                        errors.getOrDefault("error", "").toString(),
                        errors.getOrDefault("timestamp", "").toString(),
                        errors.getOrDefault("message", "").toString(),
                        errors.getOrDefault("path", "").toString(),
                };
    }

    private String initializationError(Map<String, Object> errors) {
        if (StringUtils.isBlank(htmlTemplate)) {
            LOGGER.error("The HTML template could not be initialized.");
        }
        if (Objects.isNull(errors)) {
            LOGGER.error("Error data is not present.");
        }
        LOGGER.error("Throwing exception");
        throw new IllegalStateException("Error page could not be loaded.");
    }

    private String readHtmlTemplate(File file) {
        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            br.lines().forEach(sb::append);
            return sb.toString();
        } catch (Exception e) {
            LOGGER.error("Exception while loading the error page HTML template", e);
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return ERROR;
    }
}
