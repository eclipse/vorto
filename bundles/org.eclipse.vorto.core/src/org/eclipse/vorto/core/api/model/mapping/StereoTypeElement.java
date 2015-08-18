/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stereo Type Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeElement#getStereoTypes <em>Stereo Types</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getStereoTypeElement()
 * @model
 * @generated
 */
public interface StereoTypeElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Stereo Types</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.StereoType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stereo Types</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stereo Types</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getStereoTypeElement_StereoTypes()
	 * @model containment="true"
	 * @generated
	 */
	EList<StereoType> getStereoTypes();

} // StereoTypeElement
