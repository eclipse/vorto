package org.eclipse.vorto.deviceadapter;

import org.eclipse.vorto.repository.api.content.Infomodel;

/**
 * Class to hold Device information to be passed between the Adapter and the Application using it.
 */
public abstract class DeviceInfo {

    private String deviceID;
    private Infomodel informationModel;

    /**
     * Gets device id.
     *
     * @return the device id
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Gets information model.
     *
     * @return the information model
     */
    public Infomodel getInformationModel() {
        return informationModel;
    }

    /**
     * Instantiates a new Device info.
     *
     * @param deviceID         the device id
     * @param informationModel the information model
     */
    public DeviceInfo(String deviceID, Infomodel informationModel) {
        this.deviceID = deviceID;
        this.informationModel = informationModel;
    }
}
