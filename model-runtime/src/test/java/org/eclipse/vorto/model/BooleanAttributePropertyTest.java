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
package org.eclipse.vorto.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BooleanAttributePropertyTest {

    @Test public void testBooleanAttributePropertyType() {
        BooleanAttributeProperty property =
            new BooleanAttributeProperty(BooleanAttributePropertyType.READABLE, false);
        assertEquals(property.getType(),BooleanAttributePropertyType.READABLE);
        assertFalse(property.isValue());
        property.setType(BooleanAttributePropertyType.WRITABLE);
        assertEquals(property.getType(),BooleanAttributePropertyType.WRITABLE);
        property.setValue(true);
        assertTrue(property.isValue());
        assertTrue(property.toString().equals("BooleanAttributeProperty [type=WRITABLE, value=true]"));
    }
}
