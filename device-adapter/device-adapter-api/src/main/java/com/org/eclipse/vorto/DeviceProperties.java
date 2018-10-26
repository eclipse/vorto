package org.eclipse.vorto.deviceadapter;

import java.util.HashMap;

/**
 * Class to handle device properties passed in between functions. Used to formulate the requested
 * changes to the device.
 */
public class DeviceProperties {

    private String deviceID;
    private HashMap<String, String> properties = new HashMap();
    private String fbProperty;

    private DeviceProperties(Builder builder) {
        deviceID = builder.deviceID;
        properties = builder.properties;
        setFbProperty(builder.fbProperty);
    }

    /**
     * Gets properties.
     *
     * @return the properties hash map
     */
    public HashMap<String, String> getProperties() {
        return properties;
    }

    /**
     * Gets fb property.
     *
     * @return the fb property
     */
    public String getFbProperty() {
        return fbProperty;
    }

    /**
     * Sets fb property.
     *
     * @param fbProperty the fb property
     */
    public void setFbProperty(String fbProperty) {
        this.fbProperty = fbProperty;
    }


    /**
     * The type Builder.
     */
    public static final class Builder {
        private String deviceID;
        private HashMap<String, String> properties = new HashMap<>();
        private String fbProperty;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
        }

        /**
         * Device id builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder deviceID(String val) {
            deviceID = val;
            return this;
        }

        /**
         * Property builder.
         *
         * @param key   the key
         * @param value the value
         * @return the builder
         */
        public Builder property(String key, String value){
            properties.put(key, value);
            return this;
        }

        /**
         * Fb property builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder fbProperty(String val) {
            fbProperty = val;
            return this;
        }

        /**
         * Build device properties.
         *
         * @return the device properties
         */
        public DeviceProperties build() {
            return new DeviceProperties(this);
        }
    }
}
