/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.examples.lwm2m.utils;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Utility class to define target path of Object Generator.
 */
public final class ModuleUtil {
   private static final String  XML_SRC_LOCATION = "/objects/";
   private static final String LESHAN_SRC_LOCATION = "/leshan";

   private ModuleUtil() {
   }

   /**
    * Retrieves path to LWM2M objects.
    * 
    * @return path to LWM2M objects
    */
   public static String getLwm2mObjectsPath() {
      return XML_SRC_LOCATION;
   }
   
   public static String getLeshanPath() {
	   return LESHAN_SRC_LOCATION;
   }

   /**
    * Retrieves artifact id of the LWM2M object generator.
    * 
    * @param informationModel the {@link InformationModel}
    * @return the artifact id of the LWM2M object generator
    */
   public static String getArtifactId( final InformationModel informationModel ) {
      return informationModel.getName() + "-lwm2m-object-generator";
   }
}
