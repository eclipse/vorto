package org.eclipse.vorto.repository.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        InputStream is = getClass().getClassLoader()
            .getResourceAsStream("static/error/error-template.html");
        if (Objects.nonNull(is)) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(sb::append);
            htmlTemplate = sb.toString();
        } else {
            LOGGER.error("Template Resource Stream was null.");
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

    @Override
    public String getErrorPath() {
        return ERROR;
    }
}
