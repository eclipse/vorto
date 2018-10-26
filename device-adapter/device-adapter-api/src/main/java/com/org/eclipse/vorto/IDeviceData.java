package org.eclipse.vorto.deviceadapter;

import org.eclipse.vorto.mapping.engine.normalized.FunctionblockData;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;

/**
 * This interface presents the API on how to receive/send data from/to an Adapter. The Adapter
 * that implements this interface is responsible for implementing the functionality.
 */
public interface IDeviceData {
    /**
     * Sets the configuration on the device.
     *
     * @param configurationProperties the configuration properties
     * @param deviceId                the device id
     */
    void setConfiguration(DeviceProperties configurationProperties, String deviceId);

    /**
     * Receive data by using some blocking mechanism.
     *
     * @param deviceID   the device id
     * @param fbProperty the fb property
     * @return the functionblock data
     */
    FunctionblockData receiveFBData(String deviceID, String fbProperty);

    /**
     * Receive fb data async, starts a new read process and calls the callback once done.
     *
     * @param callback the callback
     * @param deviceId the device id
     */
    void receiveFBDataAsync(IDataCallback callback, String deviceId);
}
