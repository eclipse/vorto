/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.utils;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class NamespaceUtils {

    public static String[] components(String namespace) {
        String[] breakdown = namespace.split("\\.");
        List<String> components = Lists.newArrayList();
        for (int i = 1; i <= breakdown.length; i++) {
            components.add(String.join(".", Arrays.copyOfRange(breakdown, 0, i)));
        }
        return components.toArray(new String[components.size()]);
    }

    public static boolean in(String str, String[] strings) {
        return Arrays.stream(strings).anyMatch(str::equals);
    }

}
