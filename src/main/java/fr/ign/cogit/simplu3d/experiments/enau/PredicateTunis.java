package fr.ign.cogit.simplu3d.experiments.enau;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
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
public class PredicateTunis<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	Geometry jtsCurveLimiteSepParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsLimSepDroite = null;

	// C1
	private double distReculVoirie = 2;

	// C2
	private double slope = 1;
	private double hIni = 45;

	// C3
//	private double hMax = 17;

	// C4
	private double distReculLimi = 5.4;
	private double slopeProspect = 2;

	// C7
	private double maximalCES = 0.5;

	private BasicPropertyUnit bPU;

	GeometryFactory gf = new GeometryFactory();

	public PredicateTunis(double distReculVoirie, double slope, double hIni,
			double hMax, double distReculLimi, double slopeProspect,
			double maximalCES, BasicPropertyUnit bPU) {
		super();
		this.distReculVoirie = distReculVoirie;
		this.slope = slope;
		this.hIni = hIni;
//		this.hMax = hMax;
		this.distReculLimi = distReculLimi;
		this.slopeProspect = slopeProspect;
		this.maximalCES = maximalCES;
		this.bPU = bPU;
		init();
	}

	private void init() {

		List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

		List<IOrientableCurve> lCurveLatBot = new ArrayList<>();
		
		List<IOrientableCurve> lCurveLatRight = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			// for (SubParcel sB : cP.getSubParcel()) {

			for (ParcelBoundary sCB : cP
					.getBoundaries()) {

				if (sCB.getType() == ParcelBoundaryType.ROAD) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve) {

						lCurveVoirie.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
					}

				} else if (sCB.getType() != ParcelBoundaryType.ROAD) {
					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve) {

						lCurveLatBot.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
					}

				}
				
				if(sCB.getSide() == ParcelBoundarySide.RIGHT){
					lCurveLatRight.add((IOrientableCurve) sCB.getGeom());
				}

				// }

			}

		}

	
		// System.out.println("NB voirie : " + lCurveVoirie.size());

		// System.out.println("NB other : " + lCurveLatBot.size());

		System.out.println(lCurveVoirie.size());

		try {
			
			
			jtsLimSepDroite= AdapterFactory.toGeometry(gf,
					new GM_MultiCurve<>(lCurveLatRight));
			
			
			if (!lCurveVoirie.isEmpty()) {
				this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveVoirie));
			}

			if (!lCurveLatBot.isEmpty()) {
				this.jtsCurveLimiteSepParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveLatBot));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean check(C c, M m) {
		
	//
		
	//	if(true) return true;
		
		O birth = null;

		// On récupère le nouvel objet s'il existe
		if (!m.getBirth().isEmpty()) {
			birth = m.getBirth().get(0);
		}

		if (birth != null) {
			
			if (!birth.prospectJTS(this.jtsCurveLimiteSepParcel,
					this.slopeProspect, 0)) {
				return false;
			}

			
			if(birth.toGeometry().distance(jtsLimSepDroite) > 7.4){
				return false;
			}
			
			
			
		//	if(true){return true;}
			// /////////// Contrainte C1
			if (birth.toGeometry().distance(this.jtsCurveLimiteFrontParcel) < this.distReculVoirie) {
				return false;
			}

			// /////////// Contrainte C2
			if (!birth.prospectJTS(this.jtsCurveLimiteFrontParcel, this.slope,
					this.hIni)) {
				return false;
			}

			// ///// Contrainte C4
			if (birth.toGeometry().distance(this.jtsCurveLimiteSepParcel) < this.distReculLimi) {
				return false;
			}

			
			
			
			
			/*
			
			if (birth.height * birth.toGeometry().getArea()/3      > 0.3 * this.bPU.getpol2D().area()) {
				return false;
			}*/
			
		
		}

		// contrainte distance entre bâtiment
	//	if(true){return true;}
		
		
		/*
		if (!checkDistanceInterBuildings(c, m, 6)) {
			return false;
		}*/

		// On récupère l'objet mort s'il existe
		O batDeath = null;

		if (!m.getDeath().isEmpty()) {

			batDeath = m.getDeath().get(0);

		}

		// On récupère la liste des bâtiments actuels
		List<O> lBatIni = new ArrayList<>();
		Iterator<O> iTBat = c.iterator();

		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			if (batTemp == batDeath) {
				continue;
			}

			lBatIni.add(batTemp);

		}

		if (birth != null) {
			// /////////// Contrainte C7

			lBatIni.add(birth);
			// On vérifie le CES sur les bâtiments actuels

			/*
			if(lBatIni.size() > 1)
			{return false;}*/
			
			if (!respectBuildArea(lBatIni)) {
				return false;
			}

			/*
			 * ////Contrainte C4 pour un groupe la distance doit être inférieure
			 * à 7,4 m List<List<O>> listGroup = createGroupe(lBatIni);
			 * 
			 * 
			 * 
			 * bouclegroupe : for(List<O> l : listGroup){
			 * 
			 * for(O bat : l){
			 * 
			 * if(bat.toGeometry().distance(jtsCurveLimiteSepParcel) < 7.4){
			 * continue bouclegroupe; }
			 * 
			 * 
			 * }
			 * 
			 * return false;
			 * 
			 * }
			 */

		}

		return true;
	}

	

	private boolean respectBuildArea(List<O> lBatIni) {

		if (lBatIni.isEmpty()) {
			return true;
		}

		int nbElem = lBatIni.size();

		double area = 0;
		
		//Geometry geom = lBatIni.get(0).toGeometry();

		
		for (int i = 0; i < nbElem; i++) {

			Geometry geom = lBatIni.get(i).toGeometry();
	area = area + geom.getArea();
		}

		double airePAr = this.bPU.getCadastralParcels().get(0).getArea();

		return ((area / airePAr) <= this.maximalCES);
	}

	protected boolean checkDistanceInterBuildings(C c, M m, double distanceInterBati) {

		// On récupère les objets ajoutées lors de la proposition
		List<O> lO = m.getBirth();

		// On récupère la boîte (si elle existe) que l'on supprime lors de la
		// modification
		O batDeath = null;

		if (!m.getDeath().isEmpty()) {
			batDeath = m.getDeath().get(0);
		}

		// On parcourt les boîtes existantes dans la configuration courante
		// (avant
		// d'appliquer la modification)
		Iterator<O> iTBat = c.iterator();
		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			// Si c'est une boîte qui est amenée à disparaître après
			// modification,
			// elle n'entre pas en jeu dans les vérifications
			if (batTemp == batDeath) {
				continue;
			}

			// On parcourt les boîtes que l'on ajoute
			for (O ab : lO) {

				// System.out.println("Distance JTS  : " +
				// ab.getRectangle2D().toGeometry().distance(batTemp.getRectangle2D().toGeometry())
				// + "  distance Julien " +(new
				// SquaredDistance(ab.getRectangle2D(), batTemp.getRectangle2D()
				// )).getSquaredDistance() );

				// On regarde si la distance entre les boîtes qui restent et
				// celles que
				// l'on ajoute
				// respecte la distance entre boîtes

				if (((DeformedCuboid)ab).getRectangle2D().toGeometry()
						.distance(((DeformedCuboid)batTemp).getRectangle2D().toGeometry()) < distanceInterBati) {

					// if ((new SquaredDistance(ab.getRectangle2D(),
					// batTemp.getRectangle2D() )).getSquaredDistance() <
					// distanceInterBati) {
					return false;
				}

			}

		}
		return true;

	}

}