package fr.ign.cogit.simplu3d.experiments.thesis.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class UB16PredicateWithOtherBuildings<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	Geometry jtsCurveLimiteFondParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteLatParcel = null;

	Geometry bufferRoad = null;
	Geometry buffer13 = null;
	Geometry buffer20 = null;
	Geometry buffer20more = null;

	Geometry bufferLimLat = null;

	BasicPropertyUnit bPU;

	double hIni, s;

	GeometryFactory gf = new GeometryFactory();

	public UB16PredicateWithOtherBuildings(BasicPropertyUnit bPU, double hIni, double s) throws Exception {
		this.bPU = bPU;

		System.out.println(bPU.getBuildings().size());

		this.hIni = hIni;
		this.s = s;
		// Pour simplifier la vérification, on extrait les différentes bordures
		// de
		// parcelles
		IMultiCurve<IOrientableCurve> curveLimiteFondParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteFrontParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatParcel = new GM_MultiCurve<>();

		// On parcourt les parcelles du BasicPropertyUnit (un propriétaire peut
		// avoir plusieurs parcelles)
		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			// On parcourt les limites séparaticves
			for (ParcelBoundary sCB : cP.getBoundaries()) {

				// En fonction du type on ajoute à telle ou telle géométrie
				IGeometry geom = sCB.getGeom();

				if (geom == null || geom.isEmpty() || geom.length() < 0.01) {
					continue;
				}

				// Fond de parcel
				if (sCB.getType() == ParcelBoundaryType.BOT) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteFondParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

				// Limite latérale
				if (sCB.getType() == ParcelBoundaryType.LAT) {

					if (geom instanceof IOrientableCurve) {
						curveLimiteLatParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

				// Limite front
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

		if (!curveLimiteFondParcel.isEmpty()) {
			this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf, curveLimiteFondParcel);
		}

		if (!curveLimiteFrontParcel.isEmpty()) {
			this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf, curveLimiteFrontParcel);
		}

		if (!curveLimiteLatParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf, curveLimiteLatParcel);
		}

		bufferRoad = jtsCurveLimiteFrontParcel.buffer(0.5);

		bufferLimLat = jtsCurveLimiteLatParcel.buffer(0.5);

		buffer13 = jtsCurveLimiteFrontParcel.buffer(13);
		buffer20 = jtsCurveLimiteFrontParcel.buffer(20).difference(buffer13);

		 surface = AdapterFactory.toGeometry(gf, bPU.getGeom());
		buffer20more = surface.difference(jtsCurveLimiteFrontParcel.buffer(20));

	}
	
	Geometry surface;

	@Override
	public boolean check(C c, M m) {

		List<O> lO = m.getBirth();

		if (lO.isEmpty()) {
			return true;
		}
		
		
		
		// Cas de l'implantation avec prospect
		boolean co = checkDistanceToLimitAccordingToDistanceToRoad(lO, hIni, s);

		if (!co) {
			return false;
		}
		
	
		
	

		O batDeat = null;

		if (!m.getDeath().isEmpty()) {
			batDeat = m.getDeath().get(0);
		}

		for (O ab : lO) {
			// System.out.println("Oh une naissance");
			
			if(! surface.contains(ab.toGeometry())){
				return false;
			}

			Iterator<O> iTBat = c.iterator();

			while (iTBat.hasNext()) {

				O batTemp = iTBat.next();

				if (batTemp == batDeat) {
					continue;
				}

				if (batTemp.getFootprint().distance(ab.getFootprint()) < 5) {
					return false;
				}

			}

		}
		
	

		// Règles concernant le nouveau bâtiment

		// Distance de 1,5 m à la rue ou alors longer la rue

		boolean checked = this.distanceToRoadRespected(lO, 1.5);

		if (!checked) {
			return false;

		}



		boolean co2 = checkProspectForBuilding2(lO);

		if (!co2) {
			return false;
		}

		return true;

	}

	protected boolean respectBuildArea(List<O> lBatIni) {

		if (lBatIni.isEmpty()) {
			return true;
		}

		int nbElem = lBatIni.size();

		IGeometry geom = lBatIni.get(0).getFootprint();

		for (int i = 1; i < nbElem; i++) {

			geom = geom.union(lBatIni.get(i).getFootprint());

		}

		double aireBuilt = geom.area();

		for (AbstractBuilding ab : this.bPU.getBuildings()) {
			aireBuilt = aireBuilt + ab.getFootprint().area();

		}

		double airePAr = this.bPU.getCadastralParcels().get(0).getArea();

		return ((aireBuilt / airePAr) <= 0.5);
	}

	private boolean checkProspectForBuilding2(List<O> lO) {

		for (O ab : lO) {

			for (AbstractBuilding ab2 : this.bPU.getBuildings()) {

				
				 boolean checked = ab.prospect(ab2.getFootprint(), 1, 1);
				 
				 if (!checked) { return false; }
				 

				 checked = (ab.getFootprint().distance(ab2.getFootprint()) > 5);
				if (!checked) {
					return false;
				}
			}

		}

		return true;

	}

	private boolean checkDistanceToLimitAccordingToDistanceToRoad(List<O> lO, double hIni, double s) {

		for (O ab : lO) {

			boolean checked = ab.prospectJTS(bufferLimLat, s, hIni);

			if (!checked) {
				return false;
			}

			if (ab.toGeometry().intersects(buffer13)) {

				
				Geometry decoup = ab.toGeometry().intersection(buffer13);

				if(jtsCurveLimiteLatParcel != null){
					double dist = decoup.distance(jtsCurveLimiteLatParcel);

					if (dist < 1.9) {
						return false;
					}
					
				}
			
				
				if(jtsCurveLimiteFondParcel != null){
					double dist = decoup.distance(jtsCurveLimiteFondParcel);

					if (dist < 1.9) {
						return false;
					}
				}

				

			}

			if (ab.toGeometry().intersects(buffer20)) {

				Geometry decoup = ab.toGeometry().intersection(buffer20);
				
				if(jtsCurveLimiteFondParcel != null){
					double dist = decoup.distance(jtsCurveLimiteFondParcel);

					if (dist < 3) {
						return false;
					}	
				}

			
	

			}

			if (ab.toGeometry().intersects(buffer20more)) {

				Geometry decoup = ab.toGeometry().intersection(buffer20more);

				if(jtsCurveLimiteFondParcel != null){
					double dist = decoup.distance(jtsCurveLimiteFondParcel);

					if (dist < 6) {
						return false;
					}	
				}

				
			}

		}

		return true;

	}

	protected boolean checkHeight(List<O> lO, double threshold) throws Exception {

	 for (O ab : lO) {

			List<Geometry> ls = createLineStringsFromPol(ab.getFootprint());

			for (Geometry l : ls) {

				// On se place à une distance de plus de 13 m
				if (l.distance(jtsCurveLimiteFrontParcel) < 13) {
					continue;
				}

			

				if (l.getLength() > threshold && this.bufferLimLat.contains(l)) {
					return false;
				}

			}

		}

		return true;

	}

	private boolean distanceToRoadRespected(List<O> lO, double dist) {

		for (O ab : lO) {

			if (ab.toGeometry().distance(this.jtsCurveLimiteFrontParcel) < 1.5) {
				return false;
			}

		}

		return true;

	}

	private List<Geometry> createLineStringsFromPol(IGeometry geom) throws Exception {
		List<Geometry> ls = new ArrayList<>();

		IDirectPositionList dpl1 = new DirectPositionList();
		dpl1.add(geom.coord().get(0));
		dpl1.add(geom.coord().get(1));

		IDirectPositionList dpl2 = new DirectPositionList();
		dpl1.add(geom.coord().get(1));
		dpl1.add(geom.coord().get(2));

		IDirectPositionList dpl3 = new DirectPositionList();
		dpl1.add(geom.coord().get(2));
		dpl1.add(geom.coord().get(3));

		IDirectPositionList dpl4 = new DirectPositionList();
		dpl1.add(geom.coord().get(3));
		dpl1.add(geom.coord().get(4));

		Geometry ls1 = AdapterFactory.toGeometry(gf, new GM_LineString(dpl1));
		Geometry ls2 = AdapterFactory.toGeometry(gf, new GM_LineString(dpl2));
		Geometry ls3 = AdapterFactory.toGeometry(gf, new GM_LineString(dpl3));
		Geometry ls4 = AdapterFactory.toGeometry(gf, new GM_LineString(dpl4));

		ls.add(ls1);

		ls.add(ls2);
		ls.add(ls3);
		ls.add(ls4);

		return ls;
	}

}