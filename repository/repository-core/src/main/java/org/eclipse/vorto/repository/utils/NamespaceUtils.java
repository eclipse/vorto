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
