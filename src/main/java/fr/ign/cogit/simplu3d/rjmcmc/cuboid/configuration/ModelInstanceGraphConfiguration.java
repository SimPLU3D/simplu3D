package fr.ign.cogit.simplu3d.rjmcmc.cuboid.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceObject;
import fr.ign.cogit.simplu3d.model.application.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.Building;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SubParcel;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.cache.CacheModelInstance;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;

/**
 * A specific graph configuration. It can be used in a DirectRejection approach
 * or in a RejectionSampler approach.
 * 
 * @author JPerret
 * @param <T>
 */
public class ModelInstanceGraphConfiguration<T extends AbstractSimpleBuilding>
		extends
		AbstractGraphConfiguration<T, ModelInstanceGraphConfiguration<T>, ModelInstanceModification<T>> {
	/**
	 * Logger.
	 */
	static Logger LOGGER = Logger
			.getLogger(ModelInstanceGraphConfiguration.class.getName());

	private IModelInstance modelInstance = null;
	private BasicPropertyUnit propertyUnit = null;

	private List<T> lAB = new ArrayList<>();
	private List<IModelInstanceObject> mIEBat = new ArrayList<>();
	private List<IModelInstanceElement> mIEFootP = new ArrayList<>();

	private List<IModelInstanceObject> currentModelInstanceObjectList = null;

	public List<IModelInstanceObject> getCurrentModelInstanceObjectList() {
		return this.currentModelInstanceObjectList;
	}

	public List<IModelInstanceObject> getBuildings() {
		return this.mIEBat;
	}

	public ModelInstanceGraphConfiguration(BasicPropertyUnit bPU,
			IModelInstance mI, UnaryEnergy<T> unary_energy,
			BinaryEnergy<T, T> binary_energy) {
		this.unaryEnergy = unary_energy;
		this.binaryEnergy = binary_energy;
		this.globalEnergy = null;
//		this.unary = this.binary = this.global = 0;
//		this.graph = new SimpleWeightedGraph<GraphVertex<T>, GraphEdge>(
//				GraphEdge.class);
//		this.vertexMap = new HashMap<T, GraphVertex<T>>();
//		this.dirty = false;
//		this.useCache = false;

		this.propertyUnit = bPU;
		this.modelInstance = mI;
	}

	// @Override
	// public void apply(BirthDeathModification<T> m) {
	// super.apply(m);
	// this.currentModelInstanceObjectList = this.update(m);
	// }

	public List<IModelInstanceObject> update(ModelInstanceModification<T> m) {
		return this.update(m.getBirth(), m.getDeath());
	}

	public List<IModelInstanceObject> cancelUpdate(ModelInstanceModification<T> m) {
		return this.update(m.getDeath(), m.getBirth());
	}

	public List<IModelInstanceObject> update(final List<T> lBorn,
			final List<T> lDeath) {
		LOGGER.debug("Cache content Before update");
		for (T o : this.lAB) {
			LOGGER.debug(o);
		}
		LOGGER.debug("*******Born list*******");
		for (AbstractBuilding ab : lBorn) {
			LOGGER.debug(ab);
		}
		LOGGER.debug("**********Kill list********");
		for (AbstractBuilding ab : lDeath) {
			LOGGER.debug(ab);
		}
		List<T> birthCopy = new ArrayList<T>(lBorn);
		List<T> deathCopy = new ArrayList<T>(lDeath);
		int nbElem = deathCopy.size();
		for (int i = 0; i < nbElem; i++) {
			T a = deathCopy.get(i);
			boolean isRem = birthCopy.remove(a);
			if (isRem) {
				deathCopy.remove(i);
				i--;
				nbElem--;
				LOGGER.error("Object both in birth and death");
			}
		}
		List<IModelInstanceObject> lIME = addElements(birthCopy);
		killElements(deathCopy);
		// LOGGER.debug(this.toString());
		return lIME;
	}

	private List<IModelInstanceObject> addElements(List<T> lBorn) {
		List<IModelInstanceObject> lIME = new ArrayList<>();
		for (T aB : lBorn) {
			if (lAB.contains(aB)) {
				LOGGER.error("Object already in cache " + aB);
				continue;
			}
			lAB.add(aB);
			try {
				IModelInstanceObject iME = (IModelInstanceObject) modelInstance
						.addModelInstanceElement(aB);
				AbstractBuilding abTemp = (AbstractBuilding) iME.getObject();
				createLink(abTemp, propertyUnit);
				mIEBat.add(iME);
				mIEFootP.add(modelInstance.addModelInstanceElement(abTemp
						.getFootprint()));
				lIME.add(iME);
			} catch (TypeNotFoundInModelException e) {
				e.printStackTrace();
			}
		}
		for (T aB : lBorn) {
			if (lAB.indexOf(aB) == -1) {
				LOGGER.error("Could not find : " + aB);
			}
		}
		return lIME;
	}

	private void killElements(List<T> lDeath) {
		for (T a : lDeath) {
			int ind = lAB.indexOf(a);
			if (ind == -1) {
				LOGGER.error(CacheModelInstance.class.getCanonicalName()
						+ " : Objet à tuer absent du cache");
				LOGGER.error("Object to remove " + a);
				LOGGER.error("Cache content = ");
				for (T o : this.lAB) {
					LOGGER.error(o);
				}
				System.exit(0);
				continue;
			}
			// L'élément n'est pas dans la nouvelle liste
			lAB.remove(ind);
			IModelInstanceObject iIO = (IModelInstanceObject) mIEBat
					.remove(ind);
			AbstractBuilding aBTemp = (AbstractBuilding) iIO.getObject();
			removeLink(aBTemp, propertyUnit);
			modelInstance.removeModelInstanceElement(iIO);
			aBTemp.setGeom(null);
			IModelInstanceObject iFP = (IModelInstanceObject) mIEFootP
					.remove(ind);
			modelInstance.removeModelInstanceElement(iFP);
		}
	}

	private void createLink(AbstractBuilding aB, BasicPropertyUnit bPU) {
		if (aB instanceof Building) {
			bPU.getBuildings().add((Building) aB);
			aB.setbPU(bPU);
		}
		for (CadastralParcel cP : bPU.getCadastralParcel()) {
			for (SubParcel cB : cP.getSubParcel()) {
				cB.getBuildingsParts().add(aB);
			}
		}
	}

	private void removeLink(AbstractBuilding aB, BasicPropertyUnit bPU) {
		if (aB instanceof Building) {
			bPU.getBuildings().remove((Building) aB);
			aB.setbPU(null);
		}
		for (CadastralParcel cP : bPU.getCadastralParcel()) {
			for (SubParcel cB : cP.getSubParcel()) {
				cB.getBuildingsParts().remove(aB);
			}
		}
	}

	@Override
	public ModelInstanceModification<T> newModification() {
		return new ModelInstanceModification<>();
	}
}
