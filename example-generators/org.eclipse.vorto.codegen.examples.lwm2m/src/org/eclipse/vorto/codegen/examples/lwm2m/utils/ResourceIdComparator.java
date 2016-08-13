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

import java.util.Comparator;

import org.eclipse.vorto.codegen.examples.lwm2m.generated.LWM2M.Object.Resources.Item;

/**
 * Comparator to sort Resource IDs.
 * 
 *
 */
public class ResourceIdComparator implements Comparator<Item> {


   /*
    * (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
   @Override
   public int compare( final Item i1, final Item i2 ) {
      if( i1.getID() > i2.getID() ) {
         return 1;
      }
      else if( i1.getID() < i2.getID() ) {
         return -1;
      }
      else {
         return 0;
      }
   }
}
