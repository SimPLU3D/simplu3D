/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2007 Ronny Brandt (Ronny_Brandt@web.de).                    *
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
 */
package tudresden.ocl20.pivot.modelbus.modelinstance.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.event.ModelRegistryEvent;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstance.IModelInstanceRegistry;
import tudresden.ocl20.pivot.modelinstance.event.IModelInstanceRegistryListener;
import tudresden.ocl20.pivot.modelinstance.event.ModelInstanceRegistryEvent;

/**
 * <p>
 * The {@link ModelInstanceRegistry} manages all {@link IModelInstance}s that
 * are currently imported into Dresden OCL2 for Eclipse.
 * </p>
 * 
 * @author Ronny Brandt
 */
public class ModelInstanceRegistry implements IModelInstanceRegistry {

	/** {@link Logger} for this class. */
	private static final Logger LOGGER = ModelBusPlugin
			.getLogger(ModelInstanceRegistry.class);

	/** The {@link IModelInstance}s mapped to the corresponding {@link IModel}. */
	private Map<IModel, Set<IModelInstance>> modelInstances;

	/** Map of active {@link IModelInstance}s according to their {@link IModel}. */
	private Map<IModel, IModelInstance> activeModelInstances;

	/** The listeners of this {@link ModelInstanceRegistry}. */
	private ListenerList listeners;

	/*
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.modelbus.event.IModelRegistryListener#
	 * activeModelChanged
	 * (tudresden.ocl20.pivot.modelbus.event.ModelRegistryEvent)
	 */
	public void activeModelChanged(ModelRegistryEvent event) {

		/* Do nothing. Only listens to removed models. */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceRegistry#
	 * addModelInstance
	 * (tudresden.ocl20.pivot.modelbus.modelinstance.IModelInstance)
	 */
	public void addModelInstance(IModelInstance modelInstance) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("addModelInstance(modelInstance = " + modelInstance + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// no else.

		if (modelInstance == null) {
			throw new IllegalArgumentException(
					"The parameter 'modelInstance' must not be null.");
		}
		// no else.

		/* Lazy initialization of map. */
		if (this.modelInstances == null) {
			this.modelInstances = new HashMap<IModel, Set<IModelInstance>>();
		}
		// no else.

		Set<IModelInstance> instances;
		instances = this.modelInstances.get(modelInstance.getModel());

		if (instances == null) {
			instances = new HashSet<IModelInstance>();
		}
		// no else.

		IModelInstance similarModelInstance;
		similarModelInstance = this.getSimilarModelInstance(modelInstance);

		if (similarModelInstance != null) {
			LOGGER.warn("ModelInstance '"
					+ modelInstance.getDisplayName()
					+ "' has already been loaded. The ModelInstance will be replaced.");
			instances.remove(similarModelInstance);
		}
		// no else.

		instances.add(modelInstance);

		this.modelInstances.put(modelInstance.getModel(), instances);
		this.fireModelInstanceAdded(modelInstance);

		/* Check if an active model instance of the model already exists. */
		if (this.activeModelInstances == null
				|| this.activeModelInstances.get(modelInstance.getModel()) == null
				|| instances.size() == 1) {
			this.setActiveModelInstance(modelInstance.getModel(), modelInstance);
		}
		// no else.

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("addModelInstance(IModelInstance) - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.IModelInstanceRegistry#
	 * addModelInstanceRegistryListener
	 * (tudresden.ocl20.pivot.modelbus.event.IModelInstanceRegistryListener)
	 */
	public void addModelInstanceRegistryListener(
			IModelInstanceRegistryListener listener) {

		this.getListeners().add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tudresden.ocl20.pivot.modelbus.IModelInstanceRegistry#dispose()
	 */
	public void dispose() {

		if (this.modelInstances != null) {
			this.modelInstances.clear();
			this.modelInstances = null;
		}
		// no else.

		if (this.activeModelInstances != null) {
			this.activeModelInstances.clear();
			this.activeModelInstances = null;
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelInstanceRegistry#getActiveModelInstance
	 * (tudresden.ocl20.pivot.modelbus.IModel)
	 */
	public IModelInstance getActiveModelInstance(IModel model) {

		IModelInstance result;

		if (this.activeModelInstances != null) {
			result = this.activeModelInstances.get(model);
		}

		else {
			result = null;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.IModelInstanceRegistry#getModelInstances
	 * (tudresden.ocl20.pivot.modelbus.IModel)
	 */
	public IModelInstance[] getModelInstances(IModel model) {

		IModelInstance[] result;

		if (this.modelInstances == null) {
			result = new IModelInstance[] {};
		}

		else {
			Set<IModelInstance> instances;
			instances = this.modelInstances.get(model);

			if (instances == null) {
				result = new IModelInstance[] {};
			}

			else {
				result = instances
						.toArray(new IModelInstance[instances.size()]);
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.event.IModelRegistryListener#modelAdded(
	 * tudresden.ocl20.pivot.modelbus.event.ModelRegistryEvent)
	 */
	public void modelAdded(ModelRegistryEvent event) {

		/* Do nothing. Only listens to removed models. */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tudresden.ocl20.pivot.modelbus.event.IModelRegistryListener#modelRemoved
	 * (tudresden.ocl20.pivot.modelbus.event.ModelRegistryEvent)
	 */
	public void modelRemoved(ModelRegistryEvent event) {

		if (this.modelInstances != null
				&& this.modelInstances.containsKey(event.getAffectedModel())) {

			ArrayList<IModelInstance> instancesToRemove = new ArrayList<IModelInstance>(
					this.modelInstances.get(event.getAffectedModel()));

			for (IModelInstance modelInstance : instancesToRemove) {

				this.removeModelInstance(modelInstance);
			}
			// end for.
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceRegistry#
	 * removeModelInstance
	 * (tudresden.ocl20.pivot.modelbus.modelinstance.IModelInstance)
	 */
	public boolean removeModelInstance(IModelInstance modelInstance) {

		boolean result;

		if (modelInstance == null) {
			throw new IllegalArgumentException(
					"Parameter modelInstance must not be null.");
		}
		// no else.

		if (this.modelInstances != null) {

			Set<IModelInstance> modelInstancesOfModel;
			modelInstancesOfModel = this.modelInstances.get(modelInstance
					.getModel());

			if (modelInstancesOfModel != null) {

				result = modelInstancesOfModel.remove(modelInstance);

				if (result) {

					if (modelInstancesOfModel.size() > 0) {
						this.modelInstances.put(modelInstance.getModel(),
								modelInstancesOfModel);
					}

					else {
						this.modelInstances.remove(modelInstance.getModel());
					}

					this.fireModelInstanceRemoved(modelInstance);

					/* Probably add the active IModelInstances as well. */
					if (modelInstance.equals(this
							.getActiveModelInstance(modelInstance.getModel()))) {

						if (modelInstancesOfModel.size() == 1) {
							this.setActiveModelInstance(
									modelInstance.getModel(),
									modelInstancesOfModel.iterator().next());
						}

						else {
							this.setActiveModelInstance(
									modelInstance.getModel(), null);
						}
						// end else.
					}
					// no else.
				}
				// no else.
			}

			else {
				result = false;
			}
			// end else.
		}

		else {
			result = false;
		}
		// end else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceRegistry#
	 * removeModelInstance(java.lang.String)
	 */
	public IModelInstance removeModelInstance(String displayName) {

		if (displayName == null) {
			throw new IllegalArgumentException(
					"The parameter displayName must not be null.");
		}
		// no else.

		IModelInstance result;
		result = null;

		if (this.modelInstances != null) {

			for (Set<IModelInstance> modelInstancesOfModel : this.modelInstances
					.values()) {

				for (IModelInstance modelInstance : modelInstancesOfModel) {

					if (modelInstance.getDisplayName().equals(displayName)) {
						result = modelInstance;

						if (this.removeModelInstance(modelInstance)) {
							break;
						}

						else {
							result = null;
						}
					}
					// no else.

					if (result != null) {
						break;
					}
					// no else.
				}
				// end for (iteration on model instances). */
			}
			// end for (iteration on model's model instances. */
		}
		// no else.

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.IModelInstanceRegistry#
	 * removeModelInstanceRegistryListener
	 * (tudresden.ocl20.pivot.modelbus.event.IModelInstanceRegistryListener)
	 */
	public void removeModelInstanceRegistryListener(
			IModelInstanceRegistryListener listener) {

		if (this.listeners != null) {
			this.listeners.remove(listener);
		}
		// no else.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetudresden.ocl20.pivot.modelbus.modelinstance.IModelInstanceRegistry#
	 * setActiveModelInstance(tudresden.ocl20.pivot.modelbus.model.IModel,
	 * tudresden.ocl20.pivot.modelbus.modelinstance.IModelInstance)
	 */
	public void setActiveModelInstance(IModel model,
			IModelInstance modelInstance) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setActiveModelInstance(model = " + model + ", modelInstance = " + modelInstance + ") - enter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// no else.

		/*
		 * ModelInstance parameter can be null to reset the active model
		 * instance.
		 */
		if (model == null) {
			throw new IllegalArgumentException(
					"Parameter model must not be null.");
		}

		if (this.modelInstances == null) {
			throw new IllegalArgumentException(
					"The model instance '"
							+ modelInstance.getDisplayName()
							+ "' must be added to the model instance registry first before it can be set active.");
		}
		// no else.

		if (this.modelInstances.get(model) == null && modelInstance != null) {
			throw new IllegalArgumentException(
					"The model instance '"
							+ modelInstance.getDisplayName()
							+ "' must be added to the model instance registry first before it can be set active.");
		}
		// no else.

		if (modelInstance != null
				&& !this.modelInstances.get(model).contains(modelInstance)) {
			throw new IllegalArgumentException(
					"The model instance '"
							+ modelInstance.getDisplayName()
							+ "' must be added to the model instance registry before it can be set active.");
		}
		// no else.

		if (this.activeModelInstances == null) {
			this.activeModelInstances = new HashMap<IModel, IModelInstance>();
		}
		// no else.

		if (modelInstance == null) {
			this.activeModelInstances.remove(model);
		}

		else {
			IModelInstance instance;
			instance = activeModelInstances.get(model);

			if (instance == null || !instance.equals(modelInstance)) {
				this.activeModelInstances.put(model, modelInstance);
			}
			// no else.
		}
		// end else.

		this.fireActiveModelInstanceChanged(modelInstance);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setActiveModelInstance(IModel, IModelInstance) - exit"); //$NON-NLS-1$
		}
		// no else.
	}

	/**
	 * <p>
	 * Informs all listeners that the active {@link IModelInstance} has been
	 * changed.
	 * </p>
	 * 
	 * @param modelInstance
	 *            The new active {@link IModelInstance}.
	 */
	private void fireActiveModelInstanceChanged(IModelInstance modelInstance) {

		ModelInstanceRegistryEvent event;
		event = null;

		if (this.listeners != null) {

			Object[] listeners;
			listeners = this.listeners.getListeners();

			for (int index = 0; index < listeners.length; index++) {

				/* Lazily create the event. */
				if (event == null) {
					event = new ModelInstanceRegistryEvent(this, modelInstance);
				}
				// no else.

				((IModelInstanceRegistryListener) listeners[index])
						.activeModelInstanceChanged(event);
			}
			// end for.
		}
		// no else.
	}

	/**
	 * <p>
	 * Informs the listeners that a new {@link IModelInstance} has been added.
	 * </p>
	 * 
	 * @param modelInstance
	 *            The {@link IModelInstance} that has been added.
	 */
	private void fireModelInstanceAdded(IModelInstance modelInstance) {

		ModelInstanceRegistryEvent event;
		event = null;

		if (this.listeners != null) {
			Object[] listeners;
			listeners = this.listeners.getListeners();

			for (int index = 0; index < listeners.length; index++) {

				/* Lazily create the event. */
				if (event == null) {
					event = new ModelInstanceRegistryEvent(this, modelInstance);
				}
				// no else.

				((IModelInstanceRegistryListener) listeners[index])
						.modelInstanceAdded(event);
			}
			// end for.
		}
		// no else.
	}

	/**
	 * <p>
	 * Informs the listeners that a new {@link IModelInstance} has been removed.
	 * </p>
	 * 
	 * @param modelInstance
	 *            The {@link IModelInstance} that has been removed.
	 */
	private void fireModelInstanceRemoved(IModelInstance modelInstance) {

		ModelInstanceRegistryEvent event;
		event = null;

		if (this.listeners != null) {
			Object[] listeners;
			listeners = this.listeners.getListeners();

			for (int index = 0; index < listeners.length; index++) {

				/* Lazily create the event. */
				if (event == null) {
					event = new ModelInstanceRegistryEvent(this, modelInstance);
				}
				// no else.

				((IModelInstanceRegistryListener) listeners[index])
						.modelInstanceRemoved(event);
			}
			// end for.
		}
		// no else.
	}

	/**
	 * <p>
	 * Returns the {@link ListenerList} of this {@link ModelInstanceRegistry}.
	 * </p>
	 * 
	 * @return The {@link ListenerList} of this {@link ModelInstanceRegistry}.
	 */
	private ListenerList getListeners() {

		if (this.listeners == null) {
			this.listeners = new ListenerList(ListenerList.IDENTITY);
		}
		// no else.

		return this.listeners;
	}

	/**
	 * <p>
	 * A helper method that checks if a given {@link IModelInstance} already
	 * exsits for a given {@link IModel}.
	 * </p>
	 * 
	 * @param modelInstance
	 *            The {@link IModelInstance}.
	 * @return A found {@link IModelInstance} if the given
	 *         {@link IModelInstance} or an {@link IModelInstance} with the same
	 *         display name is already registered. Else <code>null</code>.
	 */
	private IModelInstance getSimilarModelInstance(IModelInstance modelInstance) {

		IModelInstance result;

		Set<IModelInstance> modelInstances;
		modelInstances = this.modelInstances.get(modelInstance.getModel());

		if (modelInstances != null) {

			if (modelInstances.contains(modelInstance)) {
				result = modelInstance;
			}

			else {
				result = null;

				for (IModelInstance aModelInstance : modelInstances) {
					if (aModelInstance.getDisplayName().equals(
							modelInstance.getDisplayName())) {
						result = aModelInstance;
						break;
					}
					// no else.
				}
				// end for.
			}
			// end else.
		}

		else {
			result = null;
		}

		return result;
	}
}