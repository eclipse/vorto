package com.bosch.otf81sgp.vortopayloadmappingengine;

import com.bosch.otf81sgp.vortopayloadmappingengine.DeviceInfo;

/**
 * The interface Device discovery callback.
 */
public interface IDeviceDiscoveryCallback {

    /**
     * On device found.
     *
     * @param deviceInfo the device info
     */
    void onDeviceFound(DeviceInfo deviceInfo);
}
