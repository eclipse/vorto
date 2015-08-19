/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Info Model Fb Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionblock <em>Functionblock</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelFbElement()
 * @model
 * @generated
 */
public interface InfoModelFbElement extends InfoModelChild {
	/**
	 * Returns the value of the '<em><b>Functionblock</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functionblock</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Functionblock</em>' reference.
	 * @see #setFunctionblock(FunctionblockProperty)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelFbElement_Functionblock()
	 * @model
	 * @generated
	 */
	FunctionblockProperty getFunctionblock();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionblock <em>Functionblock</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Functionblock</em>' reference.
	 * @see #getFunctionblock()
	 * @generated
	 */
	void setFunctionblock(FunctionblockProperty value);

} // InfoModelFbElement
