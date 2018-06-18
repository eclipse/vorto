/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Characteristic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getUuid <em>Uuid</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsReadable <em>Is Readable</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsWritable <em>Is Writable</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsEventable <em>Is Eventable</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getLength <em>Length</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic()
 * @model
 * @generated
 */
public interface Characteristic extends EObject {
	/**
	 * Returns the value of the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uuid</em>' attribute.
	 * @see #setUuid(String)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_Uuid()
	 * @model required="true"
	 * @generated
	 */
	String getUuid();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getUuid <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uuid</em>' attribute.
	 * @see #getUuid()
	 * @generated
	 */
	void setUuid(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' reference list.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_Properties()
	 * @model
	 * @generated
	 */
	EList<CharacteristicProperty> getProperties();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Is Readable</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Readable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Readable</em>' attribute.
	 * @see #setIsReadable(boolean)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_IsReadable()
	 * @model default="true" required="true"
	 * @generated
	 */
	boolean isIsReadable();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsReadable <em>Is Readable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Readable</em>' attribute.
	 * @see #isIsReadable()
	 * @generated
	 */
	void setIsReadable(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Writable</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Writable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Writable</em>' attribute.
	 * @see #setIsWritable(boolean)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_IsWritable()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isIsWritable();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsWritable <em>Is Writable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Writable</em>' attribute.
	 * @see #isIsWritable()
	 * @generated
	 */
	void setIsWritable(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Eventable</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Eventable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Eventable</em>' attribute.
	 * @see #setIsEventable(boolean)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_IsEventable()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isIsEventable();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsEventable <em>Is Eventable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Eventable</em>' attribute.
	 * @see #isIsEventable()
	 * @generated
	 */
	void setIsEventable(boolean value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see #setLength(int)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_Length()
	 * @model
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getLength <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(int value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristic_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // Characteristic
