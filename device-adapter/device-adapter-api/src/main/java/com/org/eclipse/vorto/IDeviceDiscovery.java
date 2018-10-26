package org.eclipse.vorto.deviceadapter;

/**
 * The interface Device discovery.
 */
public interface IDeviceDiscovery {
    /**
     * List available devices.
     *
     * @param callback     the callback
     * @param scantimeInMs the scantime in ms
     */
    void listAvailableDevices(IDeviceDiscoveryCallback callback, int scantimeInMs);
}
