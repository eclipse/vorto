package device.mysensor.model;

import java.util.HashMap;
import java.util.Map;

public class ConfigPropertiesFunctionBlock {
    
    /** Configuration properties */
    
    /**
    * 
    */
    @com.google.gson.annotations.SerializedName("configValue")
    private device.mysensor.model.datatypes.UnitEntity configValue;
    /**
    * 
    */
    @com.google.gson.annotations.SerializedName("configBoolean")
    private boolean configBoolean;
    	
    /**
    * Setter for configValue.
    */
    public void setConfigValue(device.mysensor.model.datatypes.UnitEntity configValue) {
    	this.configValue = configValue;
    }
    /**
    * Getter for configValue.
    */
    public device.mysensor.model.datatypes.UnitEntity getConfigValue() {
    	return this.configValue;
    }
    /**
    * Setter for configBoolean.
    */
    public void setConfigBoolean(boolean configBoolean) {
    	this.configBoolean = configBoolean;
    }
    /**
    * Getter for configBoolean.
    */
    public boolean getConfigBoolean() {
    	return this.configBoolean;
    }
    
    public Map getStatusProperties() {
        Map<String, Object> status = new HashMap<String, Object>();
    	return status;
    }
    public Map getConfigurationProperties() {
        Map<String, Object> configuration = new HashMap<String, Object>();
        configuration.put("configValue", this.configValue);
        configuration.put("configBoolean", this.configBoolean);
        return configuration;
    }
}
