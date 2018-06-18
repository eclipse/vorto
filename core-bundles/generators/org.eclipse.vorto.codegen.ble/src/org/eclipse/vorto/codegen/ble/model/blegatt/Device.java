/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Device</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getDeviceInfo <em>Device Info</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getServices <em>Services</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getInfomodel <em>Infomodel</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getDevice()
 * @model
 * @generated
 */
public interface Device extends Model {
	/**
	 * Returns the value of the '<em><b>Device Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Device Info</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Device Info</em>' containment reference.
	 * @see #setDeviceInfo(DeviceInfo)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getDevice_DeviceInfo()
	 * @model containment="true"
	 * @generated
	 */
	DeviceInfo getDeviceInfo();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getDeviceInfo <em>Device Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Device Info</em>' containment reference.
	 * @see #getDeviceInfo()
	 * @generated
	 */
	void setDeviceInfo(DeviceInfo value);

	/**
	 * Returns the value of the '<em><b>Services</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.codegen.ble.model.blegatt.Service}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Services</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Services</em>' containment reference list.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getDevice_Services()
	 * @model containment="true"
	 * @generated
	 */
	EList<Service> getServices();

	/**
	 * Returns the value of the '<em><b>Infomodel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Infomodel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Infomodel</em>' reference.
	 * @see #setInfomodel(InformationModel)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getDevice_Infomodel()
	 * @model
	 * @generated
	 */
	InformationModel getInfomodel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getInfomodel <em>Infomodel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Infomodel</em>' reference.
	 * @see #getInfomodel()
	 * @generated
	 */
	void setInfomodel(InformationModel value);

} // Device
