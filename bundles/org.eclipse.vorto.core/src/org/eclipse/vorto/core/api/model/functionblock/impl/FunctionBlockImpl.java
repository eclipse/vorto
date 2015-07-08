/**
 */
package org.eclipse.vorto.core.api.model.functionblock.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getDisplayname <em>Displayname</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getFault <em>Fault</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl#getOperations <em>Operations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FunctionBlockImpl extends MinimalEObjectImpl.Container implements FunctionBlock {
	/**
	 * The default value of the '{@link #getDisplayname() <em>Displayname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayname()
	 * @generated
	 * @ordered
	 */
	protected static final String DISPLAYNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDisplayname() <em>Displayname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayname()
	 * @generated
	 * @ordered
	 */
	protected String displayname = DISPLAYNAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String CATEGORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected String category = CATEGORY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConfiguration() <em>Configuration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfiguration()
	 * @generated
	 * @ordered
	 */
	protected Configuration configuration;

	/**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected Status status;

	/**
	 * The cached value of the '{@link #getFault() <em>Fault</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFault()
	 * @generated
	 * @ordered
	 */
	protected Fault fault;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> events;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<Operation> operations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FunctionBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FunctionblockPackage.Literals.FUNCTION_BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDisplayname() {
		return displayname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisplayname(String newDisplayname) {
		String oldDisplayname = displayname;
		displayname = newDisplayname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__DISPLAYNAME, oldDisplayname, displayname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategory(String newCategory) {
		String oldCategory = category;
		category = newCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConfiguration(Configuration newConfiguration, NotificationChain msgs) {
		Configuration oldConfiguration = configuration;
		configuration = newConfiguration;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION, oldConfiguration, newConfiguration);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConfiguration(Configuration newConfiguration) {
		if (newConfiguration != configuration) {
			NotificationChain msgs = null;
			if (configuration != null)
				msgs = ((InternalEObject)configuration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION, null, msgs);
			if (newConfiguration != null)
				msgs = ((InternalEObject)newConfiguration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION, null, msgs);
			msgs = basicSetConfiguration(newConfiguration, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION, newConfiguration, newConfiguration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStatus(Status newStatus, NotificationChain msgs) {
		Status oldStatus = status;
		status = newStatus;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__STATUS, oldStatus, newStatus);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatus(Status newStatus) {
		if (newStatus != status) {
			NotificationChain msgs = null;
			if (status != null)
				msgs = ((InternalEObject)status).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTION_BLOCK__STATUS, null, msgs);
			if (newStatus != null)
				msgs = ((InternalEObject)newStatus).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTION_BLOCK__STATUS, null, msgs);
			msgs = basicSetStatus(newStatus, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__STATUS, newStatus, newStatus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Fault getFault() {
		return fault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFault(Fault newFault, NotificationChain msgs) {
		Fault oldFault = fault;
		fault = newFault;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__FAULT, oldFault, newFault);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFault(Fault newFault) {
		if (newFault != fault) {
			NotificationChain msgs = null;
			if (fault != null)
				msgs = ((InternalEObject)fault).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTION_BLOCK__FAULT, null, msgs);
			if (newFault != null)
				msgs = ((InternalEObject)newFault).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTION_BLOCK__FAULT, null, msgs);
			msgs = basicSetFault(newFault, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTION_BLOCK__FAULT, newFault, newFault));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectContainmentEList<Event>(Event.class, this, FunctionblockPackage.FUNCTION_BLOCK__EVENTS);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Operation> getOperations() {
		if (operations == null) {
			operations = new EObjectContainmentEList<Operation>(Operation.class, this, FunctionblockPackage.FUNCTION_BLOCK__OPERATIONS);
		}
		return operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION:
				return basicSetConfiguration(null, msgs);
			case FunctionblockPackage.FUNCTION_BLOCK__STATUS:
				return basicSetStatus(null, msgs);
			case FunctionblockPackage.FUNCTION_BLOCK__FAULT:
				return basicSetFault(null, msgs);
			case FunctionblockPackage.FUNCTION_BLOCK__EVENTS:
				return ((InternalEList<?>)getEvents()).basicRemove(otherEnd, msgs);
			case FunctionblockPackage.FUNCTION_BLOCK__OPERATIONS:
				return ((InternalEList<?>)getOperations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FunctionblockPackage.FUNCTION_BLOCK__DISPLAYNAME:
				return getDisplayname();
			case FunctionblockPackage.FUNCTION_BLOCK__DESCRIPTION:
				return getDescription();
			case FunctionblockPackage.FUNCTION_BLOCK__CATEGORY:
				return getCategory();
			case FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION:
				return getConfiguration();
			case FunctionblockPackage.FUNCTION_BLOCK__STATUS:
				return getStatus();
			case FunctionblockPackage.FUNCTION_BLOCK__FAULT:
				return getFault();
			case FunctionblockPackage.FUNCTION_BLOCK__EVENTS:
				return getEvents();
			case FunctionblockPackage.FUNCTION_BLOCK__OPERATIONS:
				return getOperations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FunctionblockPackage.FUNCTION_BLOCK__DISPLAYNAME:
				setDisplayname((String)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__CATEGORY:
				setCategory((String)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION:
				setConfiguration((Configuration)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__STATUS:
				setStatus((Status)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__FAULT:
				setFault((Fault)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__OPERATIONS:
				getOperations().clear();
				getOperations().addAll((Collection<? extends Operation>)newValue);
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
			case FunctionblockPackage.FUNCTION_BLOCK__DISPLAYNAME:
				setDisplayname(DISPLAYNAME_EDEFAULT);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION:
				setConfiguration((Configuration)null);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__STATUS:
				setStatus((Status)null);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__FAULT:
				setFault((Fault)null);
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__EVENTS:
				getEvents().clear();
				return;
			case FunctionblockPackage.FUNCTION_BLOCK__OPERATIONS:
				getOperations().clear();
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
			case FunctionblockPackage.FUNCTION_BLOCK__DISPLAYNAME:
				return DISPLAYNAME_EDEFAULT == null ? displayname != null : !DISPLAYNAME_EDEFAULT.equals(displayname);
			case FunctionblockPackage.FUNCTION_BLOCK__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case FunctionblockPackage.FUNCTION_BLOCK__CATEGORY:
				return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
			case FunctionblockPackage.FUNCTION_BLOCK__CONFIGURATION:
				return configuration != null;
			case FunctionblockPackage.FUNCTION_BLOCK__STATUS:
				return status != null;
			case FunctionblockPackage.FUNCTION_BLOCK__FAULT:
				return fault != null;
			case FunctionblockPackage.FUNCTION_BLOCK__EVENTS:
				return events != null && !events.isEmpty();
			case FunctionblockPackage.FUNCTION_BLOCK__OPERATIONS:
				return operations != null && !operations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (displayname: ");
		result.append(displayname);
		result.append(", description: ");
		result.append(description);
		result.append(", category: ");
		result.append(category);
		result.append(')');
		return result.toString();
	}

} //FunctionBlockImpl
