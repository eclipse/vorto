/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.api;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.xml.sax.SAXException;

/**
 * 
 * @author Alexander Edelmann
 *
 */
public class ZipContentExtractCodeGeneratorTask implements ICodeGeneratorTask<ModelId> {

  private static final String MAVEN_POM = "pom.xml";
  private static final String ECLIPSE_PROJECT = ".project";
  private static final String[] PATH = new String[] {"projectDescription", "name"};

  private byte[] zipContent;

  private boolean isMavenContent = false;
  private String eclipseProjectName = null;
  private final EclipseProjectParserHandler handler = new EclipseProjectParserHandler(PATH);
  private SoftReference<SAXParser> parser = null;

  public ZipContentExtractCodeGeneratorTask(byte[] zipContent) {
    this.zipContent = zipContent;
  }

  @Override
  public void generate(ModelId infoModelId, InvocationContext context, IGeneratedWriter outputter) {
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipContent));

    try {

      ZipEntry ze = null;
      while ((ze = zis.getNextEntry()) != null) {
        if (ze.isDirectory()) {
          outputter.write(new Generated(null, ze.getName(), new byte[0]));
        } else {
          int indexOfLastSlash = ze.getName().lastIndexOf("/");
          String fileName = null;
          String folderName = null;
          if (indexOfLastSlash > -1) {
            fileName = ze.getName().substring(indexOfLastSlash + 1);
            folderName = ze.getName().substring(0, indexOfLastSlash);
          } else {
            fileName = ze.getName();
          }

          outputter
              .write(new Generated(fileName, folderName, new String(copyStream(zis), "utf-8")));

          if (fileName.equalsIgnoreCase(MAVEN_POM)) {
            this.isMavenContent = true;
          }
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      if (zis != null) {
        try {
          zis.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private byte[] copyStream(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    int size;
    byte[] buffer = new byte[2048];

    BufferedOutputStream bos = new BufferedOutputStream(out);

    while ((size = in.read(buffer, 0, buffer.length)) != -1) {
      bos.write(buffer, 0, size);
    }
    bos.flush();
    bos.close();
    return out.toByteArray();
  }

  public boolean isMavenContent() {
    return this.isMavenContent;
  }

  /**
   * If the ZIP content to be processed contains an Eclipse project (a file named /.project),
   * returns the name of the Eclipse project extracted from the file
   * 
   * @return the name of the Eclipse project contained in the ZIP file, or null if there is no
   *         .project file or there was an error parsing .project
   */
  public String getEclipseProjectName() {
    return this.eclipseProjectName;
  }

  /**
   * Parse the generated ZIP file to check it for existing Eclipse project components or Maven POM
   * XMLs
   * 
   * @see {@link #isMavenContent()} {@link #getEclipseProjectName()}
   */
  public void preprocess() {
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipContent));
    try {
      ZipEntry ze = null;
      while ((ze = zis.getNextEntry()) != null) {
        String name = ze.getName();
        int mPos = name.length() - MAVEN_POM.length() - 1;
        int ePos = name.length() - ECLIPSE_PROJECT.length() - 1;
        if (MAVEN_POM.equals(name)
            || (mPos > 0 && (name.charAt(mPos) == '/' || name.charAt(mPos) == '\\'))) {
          this.isMavenContent = true;
        } else if (ECLIPSE_PROJECT.equals(name)
            || (ePos > 0 && (name.charAt(mPos) == '/' || name.charAt(ePos) == '\\'))) {
          if (parser == null || parser.get() == null) {
            parser = new SoftReference<>(SAXParserFactory.newInstance().newSAXParser());
          }
          parser.get().parse(zis, handler);
          eclipseProjectName = handler.getValue();
          handler.reset();
        }
      }
    } catch (IOException | SAXException e) {
      // TODO: do we need to log this?
    } catch (ParserConfigurationException e1) {
      // can not happen
    }
  }


}
