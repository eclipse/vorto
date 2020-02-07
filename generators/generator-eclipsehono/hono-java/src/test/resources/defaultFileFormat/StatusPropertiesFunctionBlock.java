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
package device.mysensor.model;

import java.util.HashMap;
import java.util.Map;

public class StatusPropertiesFunctionBlock {
    
    /** Status properties */
    
    /**
    * 
    */
    @com.google.gson.annotations.SerializedName("statusValue")
    private device.mysensor.model.datatypes.UnitEntity statusValue;
    /**
    * 
    */
    @com.google.gson.annotations.SerializedName("statusBoolean")
    private boolean statusBoolean;
    	
    /**
    * Setter for statusValue.
    */
    public void setStatusValue(device.mysensor.model.datatypes.UnitEntity statusValue) {
    	this.statusValue = statusValue;
    }
    /**
    * Getter for statusValue.
    */
    public device.mysensor.model.datatypes.UnitEntity getStatusValue() {
    	return this.statusValue;
    }
    /**
    * Setter for statusBoolean.
    */
    public void setStatusBoolean(boolean statusBoolean) {
    	this.statusBoolean = statusBoolean;
    }
    /**
    * Getter for statusBoolean.
    */
    public boolean getStatusBoolean() {
    	return this.statusBoolean;
    }
    
    public Map getStatusProperties() {
        Map<String, Object> status = new HashMap<String, Object>();
        status.put("statusValue", this.statusValue);
        status.put("statusBoolean", this.statusBoolean);
    	return status;
    }
    public Map getConfigurationProperties() {
        Map<String, Object> configuration = new HashMap<String, Object>();
        return configuration;
    }
}
