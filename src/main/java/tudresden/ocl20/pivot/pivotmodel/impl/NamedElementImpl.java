/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2007 Matthias Braeuer (braeuer.matthias@web.de). * All rights
 * reserved. * * This work was done as a project at the Chair for Software
 * Technology, * Dresden University Of Technology, Germany
 * (http://st.inf.tu-dresden.de). * It is understood that any modification not
 * identified as such is not * covered by the preceding statement. * * This work
 * is free software; you can redistribute it and/or modify it * under the terms
 * of the GNU Library General Public License as published * by the Free Software
 * Foundation; either version 2 of the License, or * (at your option) any later
 * version. * * This work is distributed in the hope that it will be useful, but
 * WITHOUT * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public *
 * License for more details. * * You should have received a copy of the GNU
 * Library General Public License * along with this library; if not, you can
 * view it online at * http://www.fsf.org/licensing/licenses/gpl.html. * * To
 * submit a bug report, send a comment, or get the latest news on this *
 * project, please visit the website: http://dresden-ocl.sourceforge.net. * For
 * more information on OCL and related projects visit the OCL Portal: *
 * http://st.inf.tu-dresden.de/ocl * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * *
 * 
 * $Id$
 */
package tudresden.ocl20.pivot.pivotmodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import tudresden.ocl20.pivot.pivotmodel.NamedElement;
import tudresden.ocl20.pivot.pivotmodel.PivotModelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Named Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getName
 * <em>Name</em>}</li>
 * <li>
 * {@link tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getQualifiedName
 * <em>Qualified Name</em>}</li>
 * <li>{@link tudresden.ocl20.pivot.pivotmodel.impl.NamedElementImpl#getOwner
 * <em>Owner</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class NamedElementImpl extends EObjectImpl implements
    NamedElement {

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!--
   * begin-user-doc --> This is set to the empty string in order to prevent
   * <code>NullPointerExceptions</code> where the name attribute is checked.
   * <!-- end-user-doc -->
   * 
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = ""; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getQualifiedName()
   * <em>Qualified Name</em>}' attribute. <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * 
   * @see #getQualifiedName()
   * @generated
   * @ordered
   */
  protected static final String QUALIFIED_NAME_EDEFAULT = null;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected NamedElementImpl() {

    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  protected EClass eStaticClass() {

    return PivotModelPackage.Literals.NAMED_ELEMENT;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public String getName() {

    return name;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public void setName(String newName) {

    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET,
          PivotModelPackage.NAMED_ELEMENT__NAME, oldName, name));
  }

  private String qualifiedName = null;

  /**
   * The current implementation simply recursively goes up the owner hierarchy,
   * adding each owner's name, separated by <code>::</code>.
   * 
   * TODO: Consider using a StringBuilder and collecting the name parts
   * iteratively TODO: Consider caching the name for large models
   * 
   * @generated NOT
   */
  public String getQualifiedName() {
    if (qualifiedName == null) {
      if (this.getOwner() != null && this.getOwner() != this)
        qualifiedName = this.getOwner().getQualifiedName() + "::" + this.getName();
      else
        qualifiedName = this.getName();
    }
    return qualifiedName;
  }

  /**
   * Subclasses should override this method if they have an owner that
   * contributes to their qualified name. This is important because the equality
   * of two named elements is, by default, solely determined based on their
   * qualified name. The default implementation simply returns <code>null</code>
   * .
   * 
   * @generated NOT
   */
  public NamedElement getOwner() {

    return null;
  }

  /**
   * The default implementation in this class throws a
   * {@link CloneNotSupportedException}. Subclasses may override to implement
   * cloning behaviour. Because <code>NamedElement</code> is at the root of the
   * inheritance tree, we do not make this method abstract. This avoids having
   * to change all subclasses.
   * 
   * @throws CloneNotSupportedException if this element does not support cloning
   * 
   * @generated NOT
   */
  @Override
  public NamedElement clone() throws CloneNotSupportedException {

    throw new CloneNotSupportedException(
        "Element " + this + " does not support cloning."); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * <p>
   * Returns the name of this {@link NamedElement} as a {@link List} of
   * {@link String}s containing the name of the name spaces and of this
   * {@link NamedElement}.
   * </p>
   * 
   * @return A {@link List} containing the qualified name as {@link String}s.
   * @generated NOT
   */
  public List<String> getQualifiedNameList() {

    List<String> result;

    result = new ArrayList<String>();

    /* Add the name of this element. */
    result.add(this.getName());

    /* Add the names of the owners until the owner is null. */
    NamedElement owner;
    owner = this.getOwner();

    while (owner != null) {
      result.add(0, owner.getName());
      owner = owner.getOwner();
    }

    return result;
  }

  /**
   * Convenience method for subclasses that initializes a cloned
   * <code>NamedElement</code> with the properties of this
   * <code>NamedElement</code>.
   */
  protected NamedElement initialize(NamedElement clone) {

    if (!hasVolatileName()) {
      clone.setName(getName());
    }

    return clone;
  }

  /**
   * Helper method that can be overridden in subclasses to signal that a name is
   * volatile, i.e., its value is automatically calculated (and, hence, the
   * {@link #setName(String)} method might throw an exception). This is used by
   * {@link #initialize(NamedElement)} to decide whether to set the name in a
   * cloned element. The default implementation returns <code>false</code>
   * indicating that the name should be set.
   * 
   * @return <code>false</code> by default, subclasses may override
   */
  protected boolean hasVolatileName() {

    return false;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {

    switch (featureID) {
      case PivotModelPackage.NAMED_ELEMENT__NAME:
        return getName();
      case PivotModelPackage.NAMED_ELEMENT__QUALIFIED_NAME:
        return getQualifiedName();
      case PivotModelPackage.NAMED_ELEMENT__OWNER:
        return getOwner();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue) {

    switch (featureID) {
      case PivotModelPackage.NAMED_ELEMENT__NAME:
        setName((String) newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public void eUnset(int featureID) {

    switch (featureID) {
      case PivotModelPackage.NAMED_ELEMENT__NAME:
        setName(NAME_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {

    switch (featureID) {
      case PivotModelPackage.NAMED_ELEMENT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
            .equals(name);
      case PivotModelPackage.NAMED_ELEMENT__QUALIFIED_NAME:
        return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null
            : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
      case PivotModelPackage.NAMED_ELEMENT__OWNER:
        return getOwner() != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * Changed EMF implementation to avoid call to super class that includes the
   * hash code of the object. In addition, the getter methods are used to get
   * attribute values. This is important if repository-specific subclasses have
   * alternative ways of obtaining their attribute values.
   * 
   * @generated NOT
   */
  @Override
  public String toString() {

    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(
        "name", getName()).toString(); //$NON-NLS-1$
  }

} // NamedElementImpl
