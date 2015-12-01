/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getPropertyAttribute()
 * @model
 * @generated
 */
public interface PropertyAttribute extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.PropertyAttributeType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PropertyAttributeType
	 * @see #setType(PropertyAttributeType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getPropertyAttribute_Type()
	 * @model
	 * @generated
	 */
	PropertyAttributeType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PropertyAttributeType
	 * @see #getType()
	 * @generated
	 */
	void setType(PropertyAttributeType value);

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
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getPropertyAttribute_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // PropertyAttribute
