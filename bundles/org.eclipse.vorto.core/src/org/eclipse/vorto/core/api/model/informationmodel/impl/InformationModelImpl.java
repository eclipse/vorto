/**
 */
package org.eclipse.vorto.core.api.model.informationmodel.impl;

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

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;

import org.eclipse.vorto.core.api.model.model.ModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Information Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getReferences <em>References</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getDisplayname <em>Displayname</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InformationModelImpl extends MinimalEObjectImpl.Container implements InformationModel {
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
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<FunctionblockProperty> properties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InformationModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InformationModelPackage.Literals.INFORMATION_MODEL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, InformationModelPackage.INFORMATION_MODEL__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InformationModelPackage.INFORMATION_MODEL__NAMESPACE, oldNamespace, namespace));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InformationModelPackage.INFORMATION_MODEL__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getReferences() {
		if (references == null) {
			references = new EObjectContainmentEList<ModelReference>(ModelReference.class, this, InformationModelPackage.INFORMATION_MODEL__REFERENCES);
		}
		return references;
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
			eNotify(new ENotificationImpl(this, Notification.SET, InformationModelPackage.INFORMATION_MODEL__DISPLAYNAME, oldDisplayname, displayname));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InformationModelPackage.INFORMATION_MODEL__DESCRIPTION, oldDescription, description));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InformationModelPackage.INFORMATION_MODEL__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FunctionblockProperty> getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList<FunctionblockProperty>(FunctionblockProperty.class, this, InformationModelPackage.INFORMATION_MODEL__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case InformationModelPackage.INFORMATION_MODEL__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
			case InformationModelPackage.INFORMATION_MODEL__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
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
			case InformationModelPackage.INFORMATION_MODEL__NAME:
				return getName();
			case InformationModelPackage.INFORMATION_MODEL__NAMESPACE:
				return getNamespace();
			case InformationModelPackage.INFORMATION_MODEL__VERSION:
				return getVersion();
			case InformationModelPackage.INFORMATION_MODEL__REFERENCES:
				return getReferences();
			case InformationModelPackage.INFORMATION_MODEL__DISPLAYNAME:
				return getDisplayname();
			case InformationModelPackage.INFORMATION_MODEL__DESCRIPTION:
				return getDescription();
			case InformationModelPackage.INFORMATION_MODEL__CATEGORY:
				return getCategory();
			case InformationModelPackage.INFORMATION_MODEL__PROPERTIES:
				return getProperties();
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
			case InformationModelPackage.INFORMATION_MODEL__NAME:
				setName((String)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__VERSION:
				setVersion((String)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection<? extends ModelReference>)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__DISPLAYNAME:
				setDisplayname((String)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__CATEGORY:
				setCategory((String)newValue);
				return;
			case InformationModelPackage.INFORMATION_MODEL__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends FunctionblockProperty>)newValue);
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
			case InformationModelPackage.INFORMATION_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case InformationModelPackage.INFORMATION_MODEL__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case InformationModelPackage.INFORMATION_MODEL__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case InformationModelPackage.INFORMATION_MODEL__REFERENCES:
				getReferences().clear();
				return;
			case InformationModelPackage.INFORMATION_MODEL__DISPLAYNAME:
				setDisplayname(DISPLAYNAME_EDEFAULT);
				return;
			case InformationModelPackage.INFORMATION_MODEL__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case InformationModelPackage.INFORMATION_MODEL__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
				return;
			case InformationModelPackage.INFORMATION_MODEL__PROPERTIES:
				getProperties().clear();
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
			case InformationModelPackage.INFORMATION_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case InformationModelPackage.INFORMATION_MODEL__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case InformationModelPackage.INFORMATION_MODEL__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case InformationModelPackage.INFORMATION_MODEL__REFERENCES:
				return references != null && !references.isEmpty();
			case InformationModelPackage.INFORMATION_MODEL__DISPLAYNAME:
				return DISPLAYNAME_EDEFAULT == null ? displayname != null : !DISPLAYNAME_EDEFAULT.equals(displayname);
			case InformationModelPackage.INFORMATION_MODEL__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case InformationModelPackage.INFORMATION_MODEL__CATEGORY:
				return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
			case InformationModelPackage.INFORMATION_MODEL__PROPERTIES:
				return properties != null && !properties.isEmpty();
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
		result.append(", displayname: ");
		result.append(displayname);
		result.append(", description: ");
		result.append(description);
		result.append(", category: ");
		result.append(category);
		result.append(')');
		return result.toString();
	}

} //InformationModelImpl
