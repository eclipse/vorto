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
package org.eclipse.vorto.model.runtime;

import org.junit.Test;

import static org.junit.Assert.*;

import org.eclipse.vorto.model.BooleanAttributeProperty;
import org.eclipse.vorto.model.BooleanAttributePropertyType;

public class BooleanAttributePropertyTest {

    @Test public void testBooleanAttributePropertyType() {
        BooleanAttributeProperty property =
            new BooleanAttributeProperty(BooleanAttributePropertyType.READABLE, false);
        assert (property.getType() == BooleanAttributePropertyType.READABLE);
        assert (property.isValue() == false);
        property.setType(BooleanAttributePropertyType.WRITABLE);
        assert (property.getType() == BooleanAttributePropertyType.WRITABLE);
        property.setValue(true);
        assert (property.isValue());
        assert (property.toString().equals("BooleanAttributeProperty [type=WRITABLE, value=true]"));
    }
}
