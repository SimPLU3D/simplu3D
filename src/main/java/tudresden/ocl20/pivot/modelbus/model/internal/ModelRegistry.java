/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2007 Matthias Braeuer (braeuer.matthias@web.de).            *
 * All rights reserved.                                                      *
 *                                                                           *
 * This work was done as a project at the Chair for Software Technology,     *
 * Dresden University Of Technology, Germany (http://st.inf.tu-dresden.de).  *
 * It is understood that any modification not identified as such is not      *
 * covered by the preceding statement.                                       *
 *                                                                           *
 * This work is free software; you can redistribute it and/or modify it      *
 * under the terms of the GNU Library General Public License as published    *
 * by the Free Software Foundation; either version 2 of the License, or      *
 * (at your option) any later version.                                       *
 *                                                                           *
 * This work is distributed in the hope that it will be useful, but WITHOUT  *
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or     *
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public     *
 * License for more details.                                                 *
 *                                                                           *
 * You should have received a copy of the GNU Library General Public License *
 * along with this library; if not, you can view it online at                *
 * http://www.fsf.org/licensing/licenses/gpl.html.                           *
 *                                                                           *
 * To submit a bug report, send a comment, or get the latest news on this    *
 * project, please visit the website: http://dresden-ocl.sourceforge.net.    *
 * For more information on OCL and related projects visit the OCL Portal:    *
 * http://st.inf.tu-dresden.de/ocl                                           *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * $Id$
 */
package tudresden.ocl20.pivot.modelbus.model.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.osgi.util.NLS;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.IModelRegistry;
import tudresden.ocl20.pivot.model.event.IModelRegistryListener;
import tudresden.ocl20.pivot.model.event.ModelRegistryEvent;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelbus.internal.ModelBusMessages;

/**
 * <p>
 * Standard implementation of the {@link IModelRegistry} interface that uses a
 * simple list for storing references to {@link IModel models}.
 * </p>
 * 
 * @author Matthias Braeuer
 */
public class ModelRegistry implements IModelRegistry {

	/** {@link Logger} for this class. */
	private static final Logger LOGGER =
			ModelBusPlugin.getLogger(ModelRegistry.class);

	/** Keeps track of the active {@link IModel}. */
	private IModel activeModel;

	/** The list of registered {@link IModel}s. */
	private Set<IModel> models;

	/** A list of listeners. */
	private ListenerList listeners;

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelRegistry#addMetamodel(tudresden.ocl20
	 * .pivot.modelbus.IModel)
	 */
	public void addModel(IModel model) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("addModel(model=" + model + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// no else.

		if (model == null) {
			throw new IllegalArgumentException(
					"The parameter 'model' must not be null."); //$NON-NLS-1$
		}
		// no else.

		/* Lazily create the list of models. */
		if (this.models == null) {
			this.models = new HashSet<IModel>();
		}
		// no else.

		/*
		 * Check if model is already contained in the registry; this is meant to be
		 * captured and dealt with on the UI, e.g., by showing an error message;
		 * this is better than silently do nothing.
		 */
		if (this.models.contains(model)) {
			LOGGER
					.warn("Model '" + model.getDisplayName() + "' is already loaded. The model will be replaced."); //$NON-NLS-1$//$NON-NLS-2$
		}
		// no else.

		/* Add the model. */
		this.models.add(model);

		/* Inform listeners. */
		this.fireModelAdded(model);

		/* If no model has been active, make this the active one. */
		if (this.activeModel == null) {
			this.setActiveModel(model);
		}
		// no else.

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("addModel() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelRegistry#addModelBusListener(tudresden
	 * .ocl20.pivot.modelbus.event.IModelRegistryListener)
	 */
	public void addModelRegistryListener(IModelRegistryListener listener) {

		this.getListeners().add(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelRegistry#dispose()
	 */
	public void dispose() {

		this.activeModel = null;

		if (this.models != null) {
			this.models.clear();
			this.models = null;
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelRegistry#getActiveModel()
	 */
	public IModel getActiveModel() {

		return this.activeModel;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.IModelRegistry#getModels()
	 */
	public IModel[] getModels() {

		IModel[] result;

		if (this.models == null) {
			result = new IModel[] {};
		}

		else {
			result = this.models.toArray(new IModel[this.models.size()]);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.model.IModelRegistry#removeModel(tudresden
	 * .ocl20.pivot.modelbus.model.IModel)
	 */
	public boolean removeModel(IModel model) {

		if (model == null) {
			throw new IllegalArgumentException("Parameter model must not be null");
		}
		// no else.

		boolean result;

		if (this.models != null) {

			result = this.models.remove(model);
			model.dispose();

			this.fireModelRemoved(model);

			/* Probably update the active model. */
			if (model.equals(this.getActiveModel())) {

				this.activeModel = null;

				if (this.models.size() == 1) {
					this.activeModel = this.models.iterator().next();
				}
				// no else.

				this.fireActiveModelChanged(this.getActiveModel());
			}
			// no else.
		}

		else {
			result = false;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.model.IModelRegistry#removeModel(java.lang
	 * .String)
	 */
	public IModel removeModel(String displayName) {

		if (displayName == null) {
			throw new IllegalArgumentException(
					"The parameter displayName must not be null.");
		}
		// no else.

		IModel result;
		result = null;

		if (this.models != null) {

			for (IModel model : this.models) {

				if (model.getDisplayName().equals(displayName)) {

					result = model;
					this.removeModel(model);
					break;
				}
				// no else.
			}
			// end for.
		}
		// no else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelRegistry#removeModelBusListener(tudresden
	 * .ocl20.pivot.modelbus.event.IModelRegistryListener)
	 */
	public void removeModelRegistryListener(IModelRegistryListener listener) {

		if (this.listeners != null) {
			this.listeners.remove(listener);
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelRegistry#setActiveModel(tudresden.
	 * ocl20.pivot.modelbus.IModel)
	 */
	public void setActiveModel(IModel model) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setActiveModel(model=" + model + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// no else.

		if (model == null) {
			throw new IllegalArgumentException("The active model must not be null."); //$NON-NLS-1$
		}
		// no else.

		/* Check that the new active model is managed by this registry. */
		if (this.models == null || !this.models.contains(model)) {
			throw new IllegalArgumentException(
					"The model '" + model.getDisplayName() //$NON-NLS-1$
							+ "' must be added to the model registry first before it can be set active."); //$NON-NLS-1$
		}
		// no else.

		/* Only update if the active model has changed. */
		if (this.activeModel == null || !this.activeModel.equals(model)) {

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(NLS.bind(ModelBusMessages.ModelRegistry_SettingActiveModel,
						model.getDisplayName()));
			}
			// no else.

			this.activeModel = model;
			this.fireActiveModelChanged(model);
		}
		// no else.

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setActiveModel() - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/**
	 * <p>
	 * A helper method that informs all listeners about a a new active
	 * {@link IModel}.
	 * </p>
	 * 
	 * @param model
	 *          The {@link IModel} that has been set active.
	 */
	private void fireActiveModelChanged(IModel model) {

		ModelRegistryEvent event;
		event = null;

		if (this.listeners != null) {

			Object[] listeners;
			listeners = this.listeners.getListeners();

			for (int index = 0; index < listeners.length; index++) {

				/* Lazily create the event. */
				if (event == null) {
					event = new ModelRegistryEvent(this, model);
				}
				// no else.

				((IModelRegistryListener) listeners[index]).activeModelChanged(event);
			}
			// end for.
		}
		// no else.
	}

	/**
	 * <p>
	 * A helper method that informs all listeners about an added {@link IModel}.
	 * </p>
	 * 
	 * @param model
	 *          The {@link IModel} that has been added.
	 */
	private void fireModelAdded(IModel model) {

		ModelRegistryEvent event;
		event = null;

		if (this.listeners != null) {

			Object[] listeners;
			listeners = this.listeners.getListeners();

			for (int index = 0; index < listeners.length; index++) {

				/* Lazily create the event. */
				if (event == null) {
					event = new ModelRegistryEvent(this, model);
				}
				// no else.

				((IModelRegistryListener) listeners[index]).modelAdded(event);
			}
			// end for.
		}
		// no else.
	}

	/**
	 * <p>
	 * A helper method that informs all listeners about a removed {@link IModel}.
	 * </p>
	 * 
	 * @param model
	 *          The {@link IModel} that has been removed.
	 */
	private void fireModelRemoved(IModel model) {

		ModelRegistryEvent event;
		event = null;

		if (this.listeners != null) {

			Object[] listeners;
			listeners = this.listeners.getListeners();

			for (int index = 0; index < listeners.length; index++) {

				/* Lazily create the event. */
				if (event == null) {
					event = new ModelRegistryEvent(this, model);
				}
				// no else.

				((IModelRegistryListener) listeners[index]).modelRemoved(event);
			}
			// end for.
		}
		// no else.
	}

	/**
	 * <p>
	 * A helper method that lazily creates the {@link ListenerList}.
	 * </p>
	 */
	private ListenerList getListeners() {

		if (this.listeners == null) {
			this.listeners = new ListenerList(ListenerList.IDENTITY);
		}
		// no else.

		return this.listeners;
	}
}