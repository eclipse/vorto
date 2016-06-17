/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.vorto.core.api.model.datatype.ConstraintRule;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Primitive Param</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getConstraintRule <em>Constraint Rule</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getPrimitiveParam()
 * @model
 * @generated
 */
public interface PrimitiveParam extends Param {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #setType(PrimitiveType)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getPrimitiveParam_Type()
	 * @model
	 * @generated
	 */
	PrimitiveType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #getType()
	 * @generated
	 */
	void setType(PrimitiveType value);

	/**
	 * Returns the value of the '<em><b>Constraint Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint Rule</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint Rule</em>' containment reference.
	 * @see #setConstraintRule(ConstraintRule)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getPrimitiveParam_ConstraintRule()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintRule getConstraintRule();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getConstraintRule <em>Constraint Rule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint Rule</em>' containment reference.
	 * @see #getConstraintRule()
	 * @generated
	 */
	void setConstraintRule(ConstraintRule value);

} // PrimitiveParam
