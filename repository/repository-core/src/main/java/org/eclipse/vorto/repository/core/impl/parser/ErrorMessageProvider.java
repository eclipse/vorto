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
package org.eclipse.vorto.repository.core.impl.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Component
public class ErrorMessageProvider {

  private Map<Pattern, String> translationTable = getTranslationTable();

  private Map<Pattern, String> getTranslationTable() {
    Type typeOfHashMap = new TypeToken<Map<String, String>>() {}.getType();
    Gson gson = new GsonBuilder().create();
    try {
      Map<String, String> translationMap = gson.fromJson(
          new InputStreamReader(new ClassPathResource("error-messages.json").getInputStream()),
          typeOfHashMap);
      return translationMap.entrySet().stream().map(entry -> {
        return new AbstractMap.SimpleEntry<Pattern, String>(Pattern.compile(entry.getKey()),
            entry.getValue());
      }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    } catch (JsonIOException | JsonSyntaxException | IOException e) {
      throw new ParsingException("Error loading error translation file", e);
    }
  }

  public String convertError(String message) {
    for (Pattern pattern : translationTable.keySet()) {
      Matcher matcher = pattern.matcher(message);
      if (matcher.find()) {
        String[] groups = new String[matcher.groupCount()];
        for (int i = 1; i <= matcher.groupCount(); i++) {
          groups[i - 1] = matcher.group(i);
        }
        return String.format(translationTable.get(pattern), (Object[]) groups);
      }
    }

    return message;
  }

}
