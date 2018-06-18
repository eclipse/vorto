/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getUuid <em>Uuid</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getFunctionblocks <em>Functionblocks</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getCharacteristics <em>Characteristics</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getService()
 * @model
 * @generated
 */
public interface Service extends Model {
	/**
	 * Returns the value of the '<em><b>Uuid</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uuid</em>' attribute.
	 * @see #setUuid(String)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getService_Uuid()
	 * @model default="" id="true" required="true"
	 * @generated
	 */
	String getUuid();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getUuid <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uuid</em>' attribute.
	 * @see #getUuid()
	 * @generated
	 */
	void setUuid(String value);

	/**
	 * Returns the value of the '<em><b>Functionblocks</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functionblocks</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Functionblocks</em>' reference list.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getService_Functionblocks()
	 * @model
	 * @generated
	 */
	EList<FunctionblockModel> getFunctionblocks();

	/**
	 * Returns the value of the '<em><b>Characteristics</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Characteristics</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Characteristics</em>' containment reference list.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getService_Characteristics()
	 * @model containment="true"
	 * @generated
	 */
	EList<Characteristic> getCharacteristics();

} // Service
