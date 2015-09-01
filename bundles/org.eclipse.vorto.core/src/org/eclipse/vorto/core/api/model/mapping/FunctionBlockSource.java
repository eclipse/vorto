/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Block Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource#getModel <em>Model</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockSource()
 * @model
 * @generated
 */
public interface FunctionBlockSource extends Source {

	/**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #setModel(FunctionblockModel)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockSource_Model()
	 * @model
	 * @generated
	 */
	FunctionblockModel getModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(FunctionblockModel value);
} // FunctionBlockSource
