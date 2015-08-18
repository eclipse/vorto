/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Nested Entity Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getRef <em>Ref</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getTail <em>Tail</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getNestedEntityExpression()
 * @model
 * @generated
 */
public interface NestedEntityExpression extends EntityExpressionRef {
	/**
	 * Returns the value of the '<em><b>Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' containment reference.
	 * @see #setRef(EntityExpressionRef)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getNestedEntityExpression_Ref()
	 * @model containment="true"
	 * @generated
	 */
	EntityExpressionRef getRef();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getRef <em>Ref</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' containment reference.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(EntityExpressionRef value);

	/**
	 * Returns the value of the '<em><b>Tail</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tail</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tail</em>' reference.
	 * @see #setTail(Property)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getNestedEntityExpression_Tail()
	 * @model
	 * @generated
	 */
	Property getTail();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getTail <em>Tail</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tail</em>' reference.
	 * @see #getTail()
	 * @generated
	 */
	void setTail(Property value);

} // NestedEntityExpression
