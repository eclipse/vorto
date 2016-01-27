/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Property;

import org.eclipse.vorto.core.api.model.functionblock.Event;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EventSource#getEvent <em>Event</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EventSource#getEventProperty <em>Event Property</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEventSource()
 * @model
 * @generated
 */
public interface EventSource extends FunctionBlockSource {
	/**
	 * Returns the value of the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event</em>' reference.
	 * @see #setEvent(Event)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEventSource_Event()
	 * @model
	 * @generated
	 */
	Event getEvent();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EventSource#getEvent <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event</em>' reference.
	 * @see #getEvent()
	 * @generated
	 */
	void setEvent(Event value);

	/**
	 * Returns the value of the '<em><b>Event Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Property</em>' reference.
	 * @see #setEventProperty(Property)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEventSource_EventProperty()
	 * @model
	 * @generated
	 */
	Property getEventProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EventSource#getEventProperty <em>Event Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Property</em>' reference.
	 * @see #getEventProperty()
	 * @generated
	 */
	void setEventProperty(Property value);

} // EventSource
