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
package org.eclipse.vorto.codegen.testutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipDiff {

  private static Collection<String> zipFileExtensions =
      Arrays.asList(".zip", ".ear", ".war", ".rar", ".jar");

  public Result diff(ZipInputStream baselineZip, ZipInputStream newZip) {
    try {
      Map<String, ZipEntry> baseline = getZipEntries("", baselineZip);
      
      Map<String, ZipEntry> compare = getZipEntries("", newZip);

      Collection<String> added = compare.entrySet().stream().filter(notIn(baseline))
          .map(entry -> entry.getKey()).collect(Collectors.toList());

      Collection<String> removed = baseline.entrySet().stream().filter(notIn(compare))
          .map(entry -> entry.getKey()).collect(Collectors.toList());

      Collection<String> unchanged = baseline.entrySet().stream().filter(in(compare))
          .map(entry -> entry.getKey()).collect(Collectors.toList());

      Collection<String> changed = baseline.entrySet().stream().filter(changedIn(compare))
          .map(entry -> entry.getKey()).collect(Collectors.toList());
      
      return new Result(added, removed, changed, unchanged);
    } catch (IOException e) {
      throw new ZipDiffException("Exception while taking a diff of two zip files.", e);
    }
  }

  private Predicate<Map.Entry<String, ZipEntry>> notIn(Map<String, ZipEntry> items) {
    return (item) -> {
      ZipEntry entry = items.get(item.getKey());
      if ((entry == null || (entry.isDirectory() != item.getValue().isDirectory())) && !item.getValue().isDirectory()) {
        return true;
      }
      return false;
    };
  }

  private Predicate<Map.Entry<String, ZipEntry>> changedIn(Map<String, ZipEntry> items) {
    return (item) -> {
      ZipEntry entry = items.get(item.getKey());
      if (entry != null && entry.isDirectory() == item.getValue().isDirectory()
          && entry.getCrc() != item.getValue().getCrc()) {
        return true;
      }

      return false;
    };
  }
  
  private Predicate<Map.Entry<String, ZipEntry>> in(Map<String, ZipEntry> items) {
    return (item) -> {
      ZipEntry entry = items.get(item.getKey());
      if (entry != null && entry.isDirectory() == item.getValue().isDirectory()
          && entry.getCrc() == item.getValue().getCrc()) {
        return true;
      }

      return false;
    };
  }

  private Map<String, ZipEntry> getZipEntries(String prefix, ZipInputStream zip)
      throws IOException {
    Map<String, ZipEntry> zipEntries = new HashMap<String, ZipEntry>();
    ZipEntry entry = zip.getNextEntry();
    while (entry != null) {
      if (isZipFile(entry)) {
        zipEntries
            .putAll(getZipEntries(prefix + entry.getName() + "/", getZipInputStream(zip, entry)));
      } else {
        zipEntries.put(prefix + entry.getName(), entry);
      }
      zip.closeEntry();
      entry = zip.getNextEntry();
    }
    return zipEntries;
  }

  private boolean isZipFile(ZipEntry entry) {
    if (entry == null || entry.getName() == null) {
      return false;
    }

    return zipFileExtensions.stream().anyMatch(extension -> entry.getName().endsWith(extension));
  }

  private ZipInputStream getZipInputStream(ZipInputStream zip, ZipEntry entry) throws IOException {
    byte[] zipContents = new byte[(int) entry.getSize()];
    for(int i=0 ; i < entry.getSize(); i++) {
      zipContents[i] = (byte) zip.read();
    }
    return new ZipInputStream(new ByteArrayInputStream(zipContents));
  }

  public class Result {
    private Collection<String> added;
    private Collection<String> removed;
    private Collection<String> changed;
    private Collection<String> unchanged;

    public Result(Collection<String> added, Collection<String> removed, Collection<String> changed,
        Collection<String> unchanged) {
      this.added = added;
      this.removed = removed;
      this.changed = changed;
      this.unchanged = unchanged;
    }

    public Collection<String> getAdded() {
      return added;
    }

    public void setAdded(Collection<String> added) {
      this.added = added;
    }

    public Collection<String> getRemoved() {
      return removed;
    }

    public void setRemoved(Collection<String> removed) {
      this.removed = removed;
    }

    public Collection<String> getChanged() {
      return changed;
    }

    public void setChanged(Collection<String> changed) {
      this.changed = changed;
    }

    public Collection<String> getUnchanged() {
      return unchanged;
    }

    public void setUnchanged(Collection<String> unchanged) {
      this.unchanged = unchanged;
    }
  }
}
