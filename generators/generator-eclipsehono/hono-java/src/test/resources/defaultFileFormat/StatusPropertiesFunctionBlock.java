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
