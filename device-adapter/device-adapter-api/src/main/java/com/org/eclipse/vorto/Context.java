package com.bosch.otf81sgp.vortopayloadmappingengine;

import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;

/**
 * Context to be passed with callbacks to the application, to help interpret the result.
 */
class Context {

   private String deviceID;
   private Infomodel infomodel;
   private FunctionblockModel functionblockModel;

   /**
    * Instantiates a new Context.
    *
    * @param deviceID           the device id
    * @param infomodel          the infomodel
    * @param functionblockModel the functionblock model
    */
   public Context(String deviceID, Infomodel infomodel, FunctionblockModel functionblockModel) {
      this.deviceID = deviceID;
      this.infomodel = infomodel;
      this.functionblockModel = functionblockModel;
   }

   /**
    * Gets device id.
    *
    * @return the device id
    */
   public String getDeviceID() {
      return deviceID;
   }

   /**
    * Gets infomodel.
    *
    * @return the infomodel
    */
   public Infomodel getInfomodel() {
      return infomodel;
   }

   /**
    * Gets functionblock model.
    *
    * @return the functionblock model
    */
   public FunctionblockModel getFunctionblockModel() {
      return functionblockModel;
   }
}
