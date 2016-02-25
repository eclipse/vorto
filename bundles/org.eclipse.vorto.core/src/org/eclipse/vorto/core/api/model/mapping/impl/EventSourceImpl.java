/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.vorto.core.api.model.datatype.Property;

import org.eclipse.vorto.core.api.model.functionblock.Event;

import org.eclipse.vorto.core.api.model.mapping.EventSource;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Source</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EventSourceImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EventSourceImpl#getEventProperty <em>Event Property</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EventSourceImpl extends FunctionBlockSourceImpl implements EventSource {
	/**
	 * The cached value of the '{@link #getEvent() <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
	protected Event event;

	/**
	 * The cached value of the '{@link #getEventProperty() <em>Event Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventProperty()
	 * @generated
	 * @ordered
	 */
	protected Property eventProperty;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventSourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.EVENT_SOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event getEvent() {
		if (event != null && event.eIsProxy()) {
			InternalEObject oldEvent = (InternalEObject)event;
			event = (Event)eResolveProxy(oldEvent);
			if (event != oldEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.EVENT_SOURCE__EVENT, oldEvent, event));
			}
		}
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetEvent() {
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEvent(Event newEvent) {
		Event oldEvent = event;
		event = newEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.EVENT_SOURCE__EVENT, oldEvent, event));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property getEventProperty() {
		if (eventProperty != null && eventProperty.eIsProxy()) {
			InternalEObject oldEventProperty = (InternalEObject)eventProperty;
			eventProperty = (Property)eResolveProxy(oldEventProperty);
			if (eventProperty != oldEventProperty) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.EVENT_SOURCE__EVENT_PROPERTY, oldEventProperty, eventProperty));
			}
		}
		return eventProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property basicGetEventProperty() {
		return eventProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEventProperty(Property newEventProperty) {
		Property oldEventProperty = eventProperty;
		eventProperty = newEventProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.EVENT_SOURCE__EVENT_PROPERTY, oldEventProperty, eventProperty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.EVENT_SOURCE__EVENT:
				if (resolve) return getEvent();
				return basicGetEvent();
			case MappingPackage.EVENT_SOURCE__EVENT_PROPERTY:
				if (resolve) return getEventProperty();
				return basicGetEventProperty();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MappingPackage.EVENT_SOURCE__EVENT:
				setEvent((Event)newValue);
				return;
			case MappingPackage.EVENT_SOURCE__EVENT_PROPERTY:
				setEventProperty((Property)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MappingPackage.EVENT_SOURCE__EVENT:
				setEvent((Event)null);
				return;
			case MappingPackage.EVENT_SOURCE__EVENT_PROPERTY:
				setEventProperty((Property)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MappingPackage.EVENT_SOURCE__EVENT:
				return event != null;
			case MappingPackage.EVENT_SOURCE__EVENT_PROPERTY:
				return eventProperty != null;
		}
		return super.eIsSet(featureID);
	}

} //EventSourceImpl
