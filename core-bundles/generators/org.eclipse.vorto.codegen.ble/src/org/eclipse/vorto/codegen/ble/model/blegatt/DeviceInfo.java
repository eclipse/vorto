/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Device Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo#getModelNumber <em>Model Number</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getDeviceInfo()
 * @model
 * @generated
 */
public interface DeviceInfo extends EObject {
	/**
	 * Returns the value of the '<em><b>Model Number</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Number</em>' attribute.
	 * @see #setModelNumber(String)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getDeviceInfo_ModelNumber()
	 * @model default=""
	 * @generated
	 */
	String getModelNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo#getModelNumber <em>Model Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Number</em>' attribute.
	 * @see #getModelNumber()
	 * @generated
	 */
	void setModelNumber(String value);

} // DeviceInfo
