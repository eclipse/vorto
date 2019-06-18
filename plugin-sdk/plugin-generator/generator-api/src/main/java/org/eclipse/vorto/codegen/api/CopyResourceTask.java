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
package org.eclipse.vorto.codegen.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.FileUtils;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class CopyResourceTask<Context> implements ICodeGeneratorTask<Context> {

  private URL basePath;
  private String targetPath;

  public CopyResourceTask(URL basePath, String targetPath) {
    this.basePath = basePath;
    this.targetPath = targetPath;
  }

  public void generate(Context metaData, InvocationContext context,
      final IGeneratedWriter outputter) {
    try {
      Path start = Paths.get(basePath.toURI());
      Files.walkFileTree(start, new FileVisitor<Path>() {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          outputter.write(new Generated(file.getFileName().toFile().getName(),
              getOutputPath(file).isEmpty() ? null : getOutputPath(file),
              FileUtils.readFileToByteArray((file.toFile()))));
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {

          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException ioEx) {
      throw new RuntimeException(ioEx);
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private String getOutputPath(Path file) {
    String parentPath = file.getParent().toString().replace("\\", "/");
    String outputPath = parentPath.substring(
        parentPath.lastIndexOf(this.basePath.getPath()) + this.basePath.getPath().length());
    if (outputPath.startsWith("/")) {
      outputPath = outputPath.substring(1);
    }
    return outputPath.isEmpty() ? this.targetPath : this.targetPath + "/" + outputPath;
  }

}
