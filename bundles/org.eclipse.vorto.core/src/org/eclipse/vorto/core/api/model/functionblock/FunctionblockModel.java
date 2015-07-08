/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.emf.common.util.EList;

import org.eclipse.vorto.core.api.model.datatype.Entity;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getFunctionblock <em>Functionblock</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEntities <em>Entities</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEnums <em>Enums</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel()
 * @model
 * @generated
 */
public interface FunctionblockModel extends Model {
	/**
	 * Returns the value of the '<em><b>Functionblock</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functionblock</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Functionblock</em>' containment reference.
	 * @see #setFunctionblock(FunctionBlock)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_Functionblock()
	 * @model containment="true"
	 * @generated
	 */
	FunctionBlock getFunctionblock();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getFunctionblock <em>Functionblock</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Functionblock</em>' containment reference.
	 * @see #getFunctionblock()
	 * @generated
	 */
	void setFunctionblock(FunctionBlock value);

	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.Entity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_Entities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getEntities();

	/**
	 * Returns the value of the '<em><b>Enums</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.Enum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enums</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enums</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_Enums()
	 * @model containment="true"
	 * @generated
	 */
	EList<org.eclipse.vorto.core.api.model.datatype.Enum> getEnums();

} // FunctionblockModel
