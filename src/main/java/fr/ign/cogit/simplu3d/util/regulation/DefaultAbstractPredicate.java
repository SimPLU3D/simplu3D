package fr.ign.cogit.simplu3d.util.regulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.util.JTS;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

/**
 * An abstract class that defines a set of optimized operators to check rules
 * during simulation
 * 
 * @author mbrasebin
 * 
 **/
public abstract class DefaultAbstractPredicate<O extends ISimPLU3DPrimitive, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	private BasicPropertyUnit currentBPU;

	// The geometry factory for jts operations
	protected GeometryFactory gf = new GeometryFactory();

	// JTS cached geometries

	// The geometry of the current BPU
	protected Geometry bPUGeom = null;

	// Cached geometries according to the different limits categories
	protected Geometry jtsCurveLimiteFondParcel = null;
	protected Geometry jtsCurveLimiteFrontParcel = null;
	protected Geometry jtsCurveLimiteLatParcel = null;
	protected Geometry jtsCurveLimiteLatParcelRight = null;
	protected Geometry jtsCurveLimiteLatParcelLeft = null;

	// Bâtiments dans les parcelles de l'autre côté de la route
	protected Geometry jtsCurveOppositeLimit = null;

	protected DefaultAbstractPredicate(BasicPropertyUnit bPU, Environnement env) throws Exception {
		this.currentBPU = bPU;
		prepareCachedGeometries(bPU, env);
	}

	/**
	 * Prepare a set of cached geometries for a given bPU and an environnement
	 * 
	 * @param bPU the considered basic property unti
	 * @param env the geographic environnement
	 * @throws Exception geometric errors
	 */
	protected void prepareCachedGeometries(BasicPropertyUnit bPU, Environnement env) throws Exception {

		// The GeOXygene intermediate geoemtries
		IMultiCurve<IOrientableCurve> curveLimiteFondParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteFrontParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatRightParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatLeftParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveOppositeLimit = new GM_MultiCurve<>();

		// For each parcel
		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			// for each voundaries
			for (ParcelBoundary sCB : cP.getBoundaries()) {

				// According to the type of boundary we add the geometry into the right geometry
				IGeometry geom = sCB.getGeom();

				if (geom == null || geom.isEmpty() || geom.length() < 0.01) {
					continue;
				}
				// Bot boundary
				if (sCB.getType() == ParcelBoundaryType.BOT) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteFondParcel.add((IOrientableCurve) geom);
					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}
				}

				// Lateral boundary
				if (sCB.getType() == ParcelBoundaryType.LAT) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteLatParcel.add((IOrientableCurve) geom);
						if (sCB.getSide() == ParcelBoundarySide.LEFT) {
							curveLimiteLatLeftParcel.add((IOrientableCurve) geom);
						} else if (sCB.getSide() == ParcelBoundarySide.RIGHT) {
							curveLimiteLatRightParcel.add((IOrientableCurve) geom);
						}
					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}
				}
				// Front boundary
				if (sCB.getType() == ParcelBoundaryType.ROAD) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteFrontParcel.add((IOrientableCurve) geom);
					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}
				}
			}
		}

		// Oposite boundary
		for (ParcelBoundary currentBoundary : bPU.getCadastralParcels().get(0)
				.getBoundariesByType(ParcelBoundaryType.ROAD)) {

			if (currentBoundary.getOppositeBoundary() != null) {
				IGeometry geom = currentBoundary.getOppositeBoundary().getGeom();
				if (geom instanceof IOrientableCurve) {
					curveOppositeLimit.add((IOrientableCurve) geom);
				} else {
					System.out
							.println("Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
				}
			}

		}

		// Generating the JTS geometries
		if (!curveOppositeLimit.isEmpty()) {
			this.jtsCurveOppositeLimit = AdapterFactory.toGeometry(gf, curveOppositeLimit);
		}

		if (!curveLimiteFondParcel.isEmpty()) {
			this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf, curveLimiteFondParcel);
		}

		if (!curveLimiteFrontParcel.isEmpty()) {
			this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf, curveLimiteFrontParcel);
		}

		if (!curveLimiteLatParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf, curveLimiteLatParcel);
		}

		if (!curveLimiteLatLeftParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcelLeft = AdapterFactory.toGeometry(gf, curveLimiteLatLeftParcel);
		}

		if (!curveLimiteLatRightParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcelRight = AdapterFactory.toGeometry(gf, curveLimiteLatRightParcel);
		}

		this.bPUGeom = AdapterFactory.toGeometry(gf, bPU.getGeom());

	}
	
	
	
	/////GETTERS

	public BasicPropertyUnit getCurrentBPU() {
		return currentBPU;
	}

	public Geometry getbPUGeom() {
		return bPUGeom;
	}

	public Geometry getJtsCurveLimiteFondParcel() {
		return jtsCurveLimiteFondParcel;
	}

	public Geometry getJtsCurveLimiteFrontParcel() {
		return jtsCurveLimiteFrontParcel;
	}

	public Geometry getJtsCurveLimiteLatParcel() {
		return jtsCurveLimiteLatParcel;
	}

	public Geometry getJtsCurveLimiteLatParcelRight() {
		return jtsCurveLimiteLatParcelRight;
	}

	public Geometry getJtsCurveLimiteLatParcelLeft() {
		return jtsCurveLimiteLatParcelLeft;
	}

	public Geometry getJtsCurveOppositeLimit() {
		return jtsCurveOppositeLimit;
	}
	

	/**
	 * Return all the objects after modification
	 * 
	 * @param c the current configuration
	 * @param m the modification
	 * @return a list of objects from the tested class
	 */
	public List<O> getAllObjectsAfterModifcation(C c, M m) {
		List<O> listOfObjects = new ArrayList<>();
		Iterator<O> iTBat = c.iterator();

		while (iTBat.hasNext()) {
			listOfObjects.add(iTBat.next());
		}

		// The equals() method has to be defined
		listOfObjects.removeAll(m.getDeath());
		listOfObjects.addAll(m.getBirth());

		return listOfObjects;
	}

	
	////METHOD TO TEST ON ALL OBJECTS (with getAllObjectsAfterModifcation() method for example)
	/**
	 * 
	 * 
	 * @param allObjects        the list of all object after modifications
	 * @param numberMaxOfObject the maximal numer of objects
	 * @return Check if the number of objects is lesser than numberMaxOfObject
	 */
	public boolean checkNumberOfBuildings(List<O> allObjects, int numberMaxOfObject) {

		return allObjects.size() < numberMaxOfObject;
	}

	
	/**
	 * 
	 * 
	 * @param allObjects        the list of all object after modifications
	 * @param distanceInterBati minimal distance between two buildings
	 * @return Check if distance between objects is greater than distanceInterBati
	 */
	public boolean checkDistanceBetweenObjects(List<O> allObjects, Double distanceInterBati) {

		int nbObjects = allObjects.size();

		for (int i = 0; i < nbObjects; i++) {
			O cI = allObjects.get(i);

			for (int j = i + 1; j < nbObjects; j++) {
				O cJ = allObjects.get(j);

				double distance = cI.toGeometry().distance(cJ.toGeometry());

				if (distance < distanceInterBati) {
					return false;
				}

			}
		}

		return true;
	}
	
	

	/**
	 * 
	 * 
	 * @param object            the object to test
	 * @param distanceInterBati distance between buildings to respect
	 * @return Check the distance between an object and existing buildings
	 */
	public boolean checkDistanceBetweenObjectandBuildings(O object, double distanceInterBati) {

		// Distance between existig building and cuboid
		for (Building b : currentBPU.getBuildings()) {

			if (JTS.toJTS(b.getFootprint()).distance(object.toGeometry()) <= distanceInterBati) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * 
	 * @param objects           a list of objects to test
	 * @param distanceInterBati distance between buildings to respect
	 * @return Check the distance between an object and existing buildings
	 */
	public boolean checkDistanceBetweenObjectandBuildings(List<O> objects, double distanceInterBati) {

		for (O object : objects) {
			if (!checkDistanceBetweenObjectandBuildings(object, distanceInterBati)) {
				return false;
			}
		}
		return true;
	}
	
	

	/**
	 * 
	 * @param objects the list of objects to test
	 * @return Asses the built area on a parcel from a list of objects
	 */
	private double assesBuiltArea(List<O> objects) {
		// On calcule la surface couverte par l'ensemble des cuboid
		int nbElem = objects.size();

		Geometry geom = objects.get(0).toGeometry();

		for (int i = 1; i < nbElem; i++) {

			geom = geom.union(objects.get(i).toGeometry());

		}

		return geom.getArea();
	}


	
	/**
	 * 
	 * @param objects  list of objects (after modification) from class 0
	 * @param maxValue  maxValue the maximal value of the built ratio
	 * @return Check if the builtraio (with existing buildings) is lesser than a maxValue
	 */
	public boolean checkBuiltRatio(List<O> objects, double maxValue) {

		double builtArea = assesBuiltArea(objects);

		List<AbstractSimpleBuilding> lBatIni = new ArrayList<>();
		for (ISimPLU3DPrimitive s : objects) {
			lBatIni.add((AbstractSimpleBuilding) s);
		}

		// On récupère la superficie de la basic propertyUnit
		double airePAr = 0;
		for (CadastralParcel cP : currentBPU.getCadastralParcels()) {
			airePAr = airePAr + cP.getArea();
		}

		return ((builtArea / airePAr) <= maxValue);
	}
	
	////METHOD TO TEST ON NEW OBJECTS ONLY (with m.getBirth)
	
	
	/**
	 * 
	 * 
	 * @param object   an object of O class
	 * @param geometry the geometry in which the object mus lay
	 * @return Indicates if a geometry intersects a zone
	 */
	public boolean checkIfContainedInGeometry(O object, Geometry geometry) {
		if (geometry != null) {
			if (geometry.contains(object.toGeometry())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * 
	 * @param objects  a list of class O objects
	 * @param geometry the geometry in which the object mus lay
	 * @return Indicates if a geometry intersects a zone
	 */
	public boolean checkIfContainedInGeometry(List<O> objects, Geometry geometry) {

		for (O currentObj : objects) {
			if (!checkIfContainedInGeometry(currentObj, geometry)) {
				return false;
			}
		}
		return true;
	}



	/**
	 * 
	 * @param object   object to consider
	 * @param geom     the geometry from wich the distance is calculated
	 * @param dist     the distance value
	 * @param supOrInf if the cubiod must be superior or inferior to the limit
	 * @return Check if the distance between an object and a geometry is lesser or
	 *         more than dist
	 */
	public boolean checkDistanceToGeometry(O object, Geometry geom, double dist, boolean supOrInf) {

		if (dist == 99.0) {
			return true;
		}
		// On vérifie la contrainte de recul par rapport au fond de parcelle
		// Existe t il ?
		if (geom != null) {
			Geometry objectGeom = object.toGeometry();
			if (objectGeom == null) {
				System.out.println("Geometry object is null " + DefaultAbstractPredicate.class.toString());
			}
			// determining if the distance in inferior or superior
			if (supOrInf) {
				// this distance must be superior
				if (objectGeom.distance(geom) < dist) {
					// elle n'est pas respectée, on retourne faux
					return false;
				}
			} else {
				// this distance must be inferior
				if (objectGeom.distance(geom) > dist) {
					// elle n'est pas respectée, on retourne faux
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param objects  a list of objects to consider
	 * @param geom     the geometry from wich the distance is calculated
	 * @param dist     the distance value
	 * @param supOrInf if the cubiod must be superior or inferior to the limit
	 * @return Check if the distance between an object and a geometry is lesser or
	 *         more than dist
	 */
	public boolean checkDistanceToGeometry(List<O> objects, Geometry geom, double dist, boolean supOrInf) {

		for (O object : objects) {
			if (!checkDistanceToGeometry(object, geom, dist, supOrInf)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param object  object of the class O
	 * @param geom    the geometry to consider
	 * @param distMin the maximum distance between the object and a a geometry
	 * @return Check if the distance between an object and a geometry is bigger than
	 *         distMin
	 */
	public boolean checkDistanceToGeometry(O object, Geometry geom, double distMin) {

		return checkDistanceToGeometry(object, geom, distMin, true);

	}


	
	/**
	 * 
	 * @param objects a list of objects to consider
	 * @param geom the geometry from wich the distance is calculated
	 * @param distMin the minimum distance value
	 * @return  Check if the distance between an object and a geometry is greater than
	 *         distMin
	 */ 
	public boolean checkDistanceToGeometry(List<O> objects, Geometry geom, double distMin) {

		for (O object : objects) {
			if (!checkDistanceToGeometry(object, geom, distMin, true)) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 
	 * 
	 * @param object   an object from class o
	 * @param geometry a geometry to test
	 * @return Indicates if a geometry intersects a zone
	 */
	public boolean checkIfIntersectsGeometry(O object, Geometry geometry) {
		if (geometry != null) {
			if (object.toGeometry().intersects(geometry)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * 
	 * @param objects  a list of objects from class o
	 * @param geometry a geometry to test
	 * @return Indicates if a geometry intersects a zone
	 */
	public boolean checkIfIntersectsGeometry(List<O> objects, Geometry geometry) {

		for (O object : objects) {
			if (!checkIfIntersectsGeometry(object, geometry)) {
				return false;
			}
		}

		return true;
	}


	/**
	 *
	 * @param objects the list of objects to test
	 * @return Check if the list of geometry is contained inside the BPUGeometry
	 */
	public boolean checkIfInsideBPU(List<O> objects) {
		return checkIfContainedInGeometry(objects, bPUGeom);
	}

	/**
	 *
	 * @param object an object to test
	 * @return Check if the list of geometry is contained inside the BPUGeometry
	 */
	public boolean checkIfInsideBPU(O object) {
		return checkIfContainedInGeometry(object, bPUGeom);
	}

	/**
	 * 
	 * @param objects the list of objects to test
	 * @param distanceMin minimal distance
	 * @param lBoundaryType types of boundary
	 * @return check if the distance to the types of boundaries is greater than distanceMin
	 */
	public boolean checkDistanceToLimitByType(List<O> objects, double distanceMin,
			List<ParcelBoundaryType> lBoundaryType) {
		for (O o : objects) {
			if (!checkDistanceToLimitByType(o, distanceMin, lBoundaryType)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param object an object
	 * @param distanceMin minimal distance  
	 * @param lBoundaryType  types of boundary
	 * @return  check if the distance to the types of boundaries is greater than distanceMin
	 */
	public boolean checkDistanceToLimitByType(O object, double distanceMin, List<ParcelBoundaryType> lBoundaryType) {

		for (ParcelBoundaryType type : lBoundaryType) {
			Geometry geom = null;
			switch (type) {

			case BOT:
				geom = jtsCurveLimiteFondParcel;
				break;
			case LAT:
				geom = jtsCurveLimiteLatParcel;
				break;
			case ROAD:
				geom = jtsCurveLimiteFrontParcel;
				break;
			default:
				System.out.println(DefaultAbstractPredicate.class + "  Cas non traité : " + type);
				continue;

			}

			if (!this.checkDistanceToGeometry(object, geom, distanceMin)) {
				return false;
			}

		}
		return true;

	}

	
	/**
	 * 
	 * @param objects the list of objects to test
	 * @param distanceMin minimal distance
	 * @param  lBoundaryType types of boundary
	 * @return  check if the distance to the sides of boundaries is greater than distanceMax
	 */ 
	public boolean checkDistanceToLimitBySide(List<O> objects, double distanceMin,
			List<ParcelBoundarySide> lBoundaryType) {
		for (O o : objects) {
			if (!checkDistanceToLimitBySide(o, distanceMin, lBoundaryType)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * 
	 * @param object an object to test
	 * @param distanceMin minimal distance
	 * @param  lBoundaryType types of boundary
	 * @return  check if the distance to the sides of boundaries is greater than distanceMin
	 */ 
	public boolean checkDistanceToLimitBySide(O object, double distanceMin, List<ParcelBoundarySide> lBoundaryType) {

		for (ParcelBoundarySide side : lBoundaryType) {
			Geometry geom = null;
			switch (side) {

			case LEFT:
				geom = jtsCurveLimiteLatParcelLeft;
				break;
			case RIGHT:
				geom = jtsCurveLimiteLatParcelRight;
				break;
			default:
				System.out.println(DefaultAbstractPredicate.class + "  Cas non traité : " + side);
				continue;
			}

			if (!this.checkDistanceToGeometry(object, geom, distanceMin)) {
				return false;
			}

		}
		return true;

	}

	/**
	 * 
	 * @param objects the list of objects to test
	 * @param distanceMin  minimal distance
	 * @return  check if the distance to the sides of boundaries is greater than distanceMin
	 */
	public boolean checkDistanceToOppositeLimit(List<O> objects, double distanceMin) {
		for (O o : objects) {
			if (!checkDistanceToOppositeLimit(o, distanceMin)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param object an object to test
	 * @param distanceMin minimal distance
	 * @return  check if the distance to the sides of boundaries is greater than distanceMax
	 */
	public boolean checkDistanceToOppositeLimit(O object, double distanceMin) {

		return this.checkDistanceToGeometry(object, jtsCurveOppositeLimit, distanceMin);

	}
	
	

	

}
