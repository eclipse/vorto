package org.eclipse.vorto.deviceadapter;

import package org.eclipse.vorto.deviceadapter.DeviceInfo;

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
