/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt.impl;

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

import org.eclipse.vorto.codegen.ble.model.blegatt.Device;
import org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo;
import org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage;
import org.eclipse.vorto.codegen.ble.model.blegatt.Service;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Device</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getReferences <em>References</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getDisplayname <em>Displayname</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getDeviceInfo <em>Device Info</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getServices <em>Services</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl#getInfomodel <em>Infomodel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeviceImpl extends MinimalEObjectImpl.Container implements Device {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected String namespace = NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReferences() <em>References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelReference> references;

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
	 * The cached value of the '{@link #getDeviceInfo() <em>Device Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeviceInfo()
	 * @generated
	 * @ordered
	 */
	protected DeviceInfo deviceInfo;

	/**
	 * The cached value of the '{@link #getServices() <em>Services</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServices()
	 * @generated
	 * @ordered
	 */
	protected EList<Service> services;

	/**
	 * The cached value of the '{@link #getInfomodel() <em>Infomodel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInfomodel()
	 * @generated
	 * @ordered
	 */
	protected InformationModel infomodel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeviceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DEVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeviceInfo(DeviceInfo newDeviceInfo, NotificationChain msgs) {
		DeviceInfo oldDeviceInfo = deviceInfo;
		deviceInfo = newDeviceInfo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__DEVICE_INFO, oldDeviceInfo, newDeviceInfo);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeviceInfo(DeviceInfo newDeviceInfo) {
		if (newDeviceInfo != deviceInfo) {
			NotificationChain msgs = null;
			if (deviceInfo != null)
				msgs = ((InternalEObject)deviceInfo).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DEVICE__DEVICE_INFO, null, msgs);
			if (newDeviceInfo != null)
				msgs = ((InternalEObject)newDeviceInfo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DEVICE__DEVICE_INFO, null, msgs);
			msgs = basicSetDeviceInfo(newDeviceInfo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__DEVICE_INFO, newDeviceInfo, newDeviceInfo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Service> getServices() {
		if (services == null) {
			services = new EObjectContainmentEList<Service>(Service.class, this, ModelPackage.DEVICE__SERVICES);
		}
		return services;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModel getInfomodel() {
		if (infomodel != null && infomodel.eIsProxy()) {
			InternalEObject oldInfomodel = (InternalEObject)infomodel;
			infomodel = (InformationModel)eResolveProxy(oldInfomodel);
			if (infomodel != oldInfomodel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.DEVICE__INFOMODEL, oldInfomodel, infomodel));
			}
		}
		return infomodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModel basicGetInfomodel() {
		return infomodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInfomodel(InformationModel newInfomodel) {
		InformationModel oldInfomodel = infomodel;
		infomodel = newInfomodel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__INFOMODEL, oldInfomodel, infomodel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamespace(String newNamespace) {
		String oldNamespace = namespace;
		namespace = newNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__NAMESPACE, oldNamespace, namespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getReferences() {
		if (references == null) {
			references = new EObjectContainmentEList<ModelReference>(ModelReference.class, this, ModelPackage.DEVICE__REFERENCES);
		}
		return references;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__DESCRIPTION, oldDescription, description));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__DISPLAYNAME, oldDisplayname, displayname));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEVICE__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.DEVICE__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
			case ModelPackage.DEVICE__DEVICE_INFO:
				return basicSetDeviceInfo(null, msgs);
			case ModelPackage.DEVICE__SERVICES:
				return ((InternalEList<?>)getServices()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.DEVICE__NAME:
				return getName();
			case ModelPackage.DEVICE__NAMESPACE:
				return getNamespace();
			case ModelPackage.DEVICE__VERSION:
				return getVersion();
			case ModelPackage.DEVICE__REFERENCES:
				return getReferences();
			case ModelPackage.DEVICE__DESCRIPTION:
				return getDescription();
			case ModelPackage.DEVICE__DISPLAYNAME:
				return getDisplayname();
			case ModelPackage.DEVICE__CATEGORY:
				return getCategory();
			case ModelPackage.DEVICE__DEVICE_INFO:
				return getDeviceInfo();
			case ModelPackage.DEVICE__SERVICES:
				return getServices();
			case ModelPackage.DEVICE__INFOMODEL:
				if (resolve) return getInfomodel();
				return basicGetInfomodel();
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
			case ModelPackage.DEVICE__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.DEVICE__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case ModelPackage.DEVICE__VERSION:
				setVersion((String)newValue);
				return;
			case ModelPackage.DEVICE__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection<? extends ModelReference>)newValue);
				return;
			case ModelPackage.DEVICE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ModelPackage.DEVICE__DISPLAYNAME:
				setDisplayname((String)newValue);
				return;
			case ModelPackage.DEVICE__CATEGORY:
				setCategory((String)newValue);
				return;
			case ModelPackage.DEVICE__DEVICE_INFO:
				setDeviceInfo((DeviceInfo)newValue);
				return;
			case ModelPackage.DEVICE__SERVICES:
				getServices().clear();
				getServices().addAll((Collection<? extends Service>)newValue);
				return;
			case ModelPackage.DEVICE__INFOMODEL:
				setInfomodel((InformationModel)newValue);
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
			case ModelPackage.DEVICE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.DEVICE__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case ModelPackage.DEVICE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ModelPackage.DEVICE__REFERENCES:
				getReferences().clear();
				return;
			case ModelPackage.DEVICE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ModelPackage.DEVICE__DISPLAYNAME:
				setDisplayname(DISPLAYNAME_EDEFAULT);
				return;
			case ModelPackage.DEVICE__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
				return;
			case ModelPackage.DEVICE__DEVICE_INFO:
				setDeviceInfo((DeviceInfo)null);
				return;
			case ModelPackage.DEVICE__SERVICES:
				getServices().clear();
				return;
			case ModelPackage.DEVICE__INFOMODEL:
				setInfomodel((InformationModel)null);
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
			case ModelPackage.DEVICE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.DEVICE__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case ModelPackage.DEVICE__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case ModelPackage.DEVICE__REFERENCES:
				return references != null && !references.isEmpty();
			case ModelPackage.DEVICE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ModelPackage.DEVICE__DISPLAYNAME:
				return DISPLAYNAME_EDEFAULT == null ? displayname != null : !DISPLAYNAME_EDEFAULT.equals(displayname);
			case ModelPackage.DEVICE__CATEGORY:
				return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
			case ModelPackage.DEVICE__DEVICE_INFO:
				return deviceInfo != null;
			case ModelPackage.DEVICE__SERVICES:
				return services != null && !services.isEmpty();
			case ModelPackage.DEVICE__INFOMODEL:
				return infomodel != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", namespace: ");
		result.append(namespace);
		result.append(", version: ");
		result.append(version);
		result.append(", description: ");
		result.append(description);
		result.append(", displayname: ");
		result.append(displayname);
		result.append(", category: ");
		result.append(category);
		result.append(')');
		return result.toString();
	}

} //DeviceImpl
