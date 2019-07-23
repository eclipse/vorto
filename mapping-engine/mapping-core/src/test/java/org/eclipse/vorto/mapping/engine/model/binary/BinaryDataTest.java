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
package org.eclipse.vorto.mapping.engine.model.binary;

import org.junit.Test;

public class BinaryDataTest {

    @Test public void testBinaryData() {
        byte[] testData = new byte[]{(byte) 0xff, (byte) 0xff};
        BinaryData data = new BinaryData();
        data.setData(testData);
        byte[] compare = data.getData();
        for (int i=0; i<testData.length;i++){
            assert(testData[i] == compare[i]);
        }
        data = new BinaryData(testData);
        compare = data.getData();
        for (int i=0; i<testData.length;i++){
            assert(testData[i] == compare[i]);
        }
    }
}
