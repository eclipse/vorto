package com.bosch.otf81sgp.vortopayloadmappingengine;

/**
 * To be used by the Adapter to communicate data changes back to the Application.
 */
public interface IDataCallback {

    /**
     * On status.
     *
     * @param status the status
     * @param ctx    the ctx
     */
    void onStatus(DeviceProperties status, Context ctx);

    /**
     * On configuration.
     *
     * @param deviceProperties the device properties
     * @param ctx              the ctx
     */
    void onConfiguration(DeviceProperties deviceProperties, Context ctx);


}
