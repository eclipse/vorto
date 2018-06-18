/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.datatype.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Characteristic Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getOffset <em>Offset</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getLength <em>Length</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getProperty <em>Property</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getDatatype <em>Datatype</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristicProperty()
 * @model
 * @generated
 */
public interface CharacteristicProperty extends EObject {
	/**
	 * Returns the value of the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offset</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offset</em>' attribute.
	 * @see #setOffset(int)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristicProperty_Offset()
	 * @model
	 * @generated
	 */
	int getOffset();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getOffset <em>Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offset</em>' attribute.
	 * @see #getOffset()
	 * @generated
	 */
	void setOffset(int value);

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
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristicProperty_Length()
	 * @model
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getLength <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(int value);

	/**
	 * Returns the value of the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' reference.
	 * @see #setProperty(Property)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristicProperty_Property()
	 * @model
	 * @generated
	 */
	Property getProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getProperty <em>Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' reference.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(Property value);

	/**
	 * Returns the value of the '<em><b>Datatype</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Datatype</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Datatype</em>' attribute.
	 * @see #setDatatype(String)
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage#getCharacteristicProperty_Datatype()
	 * @model
	 * @generated
	 */
	String getDatatype();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getDatatype <em>Datatype</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Datatype</em>' attribute.
	 * @see #getDatatype()
	 * @generated
	 */
	void setDatatype(String value);

} // CharacteristicProperty
