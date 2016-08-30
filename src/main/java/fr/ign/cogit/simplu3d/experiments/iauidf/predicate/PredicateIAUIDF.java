package fr.ign.cogit.simplu3d.experiments.iauidf.predicate;

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
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundarySide;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.ParallelCuboid2;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.simple.SimpleCuboid2;
import fr.ign.geometry.SquaredDistance;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

public class PredicateIAUIDF<O extends Cuboid, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	private BasicPropertyUnit currentBPU;
	private Regulation r1, r2;

	Geometry jtsCurveLimiteFondParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteLatParcel = null;

	Geometry jtsCurveLatRightArt71 = null;

	public static ParcelBoundarySide RIGHT_OF_LEFT_FOR_ART_71 = ParcelBoundarySide.RIGHT;

	public PredicateIAUIDF(BasicPropertyUnit bPU, Regulation r1, Regulation r2) throws Exception {
		super();
		this.currentBPU = bPU;
		this.r1 = r1;
		this.r2 = r2;
		init();

	}

	/**
	 * Initialisation : stocke les limites séparatives dans les géométries jts
	 * jtsCurveLimiteSepParcel et jtsCurveLimiteFrontParcel.
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		// Pour simplifier la vérification, on extrait les différentes bordures
		// de
		// parcelles
		IMultiCurve<IOrientableCurve> curveLimiteFondParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteFrontParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLatRightLeftArt71 = new GM_MultiCurve<>();

		// On parcourt les parcelles du BasicPropertyUnit (un propriétaire peut
		// avoir plusieurs parcelles)
		for (CadastralParcel cP : currentBPU.getCadastralParcels()) {

			// On parcourt les limites séparaticves
			for (ParcelBoundary sCB : cP.getBoundaries()) {

				// En fonction du type on ajoute à telle ou telle géométrie

				// Fond de parcel
				if (sCB.getType() == ParcelBoundaryType.BOT) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
						curveLimiteFondParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

				// Limite latérale
				if (sCB.getType() == ParcelBoundaryType.LAT) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
						curveLimiteLatParcel.add((IOrientableCurve) geom);

						if (r1 != null && r1.getArt_71() == 2
								&& (sCB.getSide() != PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71)
								|| r2 != null && r2.getArt_71() == 2
										&& (sCB.getSide() != PredicateIAUIDF.RIGHT_OF_LEFT_FOR_ART_71)) {

							curveLatRightLeftArt71.add((IOrientableCurve) geom);

						}

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

				// Limite front
				if (sCB.getType() == ParcelBoundaryType.ROAD) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
						curveLimiteFrontParcel.add((IOrientableCurve) geom);

					} else {
						System.out.println(
								"Classe SamplePredicate : quelque chose n'est pas un ICurve : " + geom.getClass());
					}

				}

			}

		}

		GeometryFactory gf = new GeometryFactory();

		if (!curveLimiteFondParcel.isEmpty()) {
			this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf, curveLimiteFondParcel);
		}

		if (!curveLimiteFrontParcel.isEmpty()) {
			this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf, curveLimiteFrontParcel);
		}

		if (!curveLimiteLatParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf, curveLimiteLatParcel);
		}

		if (!curveLatRightLeftArt71.isEmpty()) {

			this.jtsCurveLatRightArt71 = AdapterFactory.toGeometry(gf, curveLatRightLeftArt71);
		}

	}

	@Override
	public boolean check(C c, M m) {
		

		

		// Pour produire des boîtes séparées et vérifier que la distance inter
		// bâtiment est respectée
		// ART_8 Distance minimale des constructions par rapport aux autres sur
		// une même propriété imposée en mètre 88= non renseignable, 99= non
		// réglementé
		if (!checkDistanceInterBuildings(c, m, r1.getArt_8())) { //r1.getArt_8()
			return false;
		}
		



		O birth = null;

		if (!m.getBirth().isEmpty()) {
			birth = m.getBirth().get(0);
			// IMultiSurface<IOrientableSurface> gm = new GM_MultiSurface<>();
			// gm.add(birth.getFootprint());
			// if(Exec.debugSurface.size() < 100){
			// Exec.debugSurface.add(gm);
			// }

		}

		// Vérification des règles au niveau de la parcelle

		// ART_9 Pourcentage d'emprise au sol maximum autorisé Valeur comprise
		// de 0 à 1, 88= non renseignable, 99= non réglementé
		// ART_13 Part minimale d'espaces libre de toute construction exprimée
		// par rapport à la surface totale de la parcelle Valeur comprise de 0 à
		// 1, 88 si non renseignable, 99 si non règlementé
		// ART_14 Coefficient d'occupation du sol 88= non renseignable, 99= non
		// réglementé
		if (!checkParcelRegulation(r1, c, m)) {
			return false;
		}
	

		// Vérification des règles au niveau des bandes (localement)

		// ART_6 Distance minimale des constructions par rapport à la voirie
		// ART_72 Distance minimale des constructions par rapport aux limites
		// ART_73 Distance minimale des constructions par rapport à la limte
		// ART_74 Distance minimum des constructions par rapport aux limites

		// @TODO : il faudrait déterminer dans quel bande est le nouvel objet
		// pour pointer sur la bonne réglementation

		if (birth != null) {

			if (birth instanceof ParallelCuboid) {

				if (r1.getArt_71() != 2) {

					if (!checkBandRegulation(r1, birth)) {
						return false;
					}

				} else {
					if (!checkBandRegulationSpecArt71(r1, birth)) {

						return false;
					}

				}

			} else if (birth instanceof ParallelCuboid2) {

				if (!checkBandRegulationSpecArt71(r2, birth)) {

					return false;
				}

				// System.out.println("Je retourne true");

			} else if (birth instanceof SimpleCuboid2) {

				if (r2.getArt_71() != 2) {
					if (!checkBandRegulation(r2, birth))
						return false;
				}

			} else if (birth instanceof SimpleCuboid) {
				
				if (r1.getArt_71() != 2) {

					if (!checkBandRegulation(r1, birth)) {
						return false;
					}

				}
				
			} else {
				System.out.println("Predicate IAUIDF - Unexpected class during object birth : "
						+ birth.getClass().getCanonicalName());
			}

		}

		// System.out.println("Je retourne true");
		return true;
	}

	public boolean checkParcelRegulation(Regulation r, C c, M m) {

		// On fait la liste de tous les objets après modification
		List<O> lCuboid = new ArrayList<>();

		// On récupère la boîte (si elle existe) que l'on supprime lors de la
		// modification
		O cuboidDead = null;

		if (!m.getDeath().isEmpty()) {
			cuboidDead = m.getDeath().get(0);
		}

		Iterator<O> iTBat = c.iterator();

		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			if (batTemp == cuboidDead) {
				continue;
			}

			lCuboid.add(batTemp);

		}

		// On ajoute tous les nouveaux objets
		lCuboid.addAll(m.getBirth());

		double areaBuilt = 0;
		double shonBuilt = 0;

		for (O cubTemp : lCuboid) {
			if (cubTemp == cuboidDead) {
				continue;
			}

			double area = cubTemp.getArea(); // .toGeometry().getArea();
			int nbEtage = 1 + (int) (cubTemp.height / 3);

			areaBuilt += area;
			shonBuilt += area * nbEtage;
		}

		double areaBPU = this.currentBPU.getArea();

		// ART_9 Pourcentage d'emprise au sol maximum autorisé Valeur comprise
		// de 0 à 1, 88= non renseignable, 99= non réglementé

		double reg9 = r.getArt_9();
		if (reg9 != 99 & reg9 != 88) {
			if (areaBuilt / areaBPU > reg9) {
				return false;
			}
		}
		// ART_13 Part minimale d'espaces libre de toute construction exprimée
		// par rapport à la surface totale de la parcelle Valeur comprise de 0 à
		// 1, 88 si non renseignable, 99 si non règlementé
		double reg13 = r.getArt_13();
		if (reg13 != 99 & reg13 != 88) {
			if (areaBuilt / areaBPU > (1 - reg13)) {
				return false;
			}
		}

		// ART_14 Coefficient d'occupation du sol 88= non renseignable, 99= non
		// réglementé
		// ART_13 Part minimale d'espaces libre de toute construction exprimée
		// par rapport à la surface totale de la parcelle Valeur comprise de 0 à
		// 1, 88 si non renseignable, 99 si non règlementé
		double reg14 = r.getArt_14();
		if (reg14 != 0.0 & reg14 != 99 & reg14 != 88) {
			if (shonBuilt / areaBPU > reg14) {
				return false;
			}
		}
		
		
		
		//EXTRA RULE :   la distance entre deux bâtiments sur une même parcelle 
		// est égale à la hauteur divisée par deux du bâtiment le plus haut
		
		int nbCuboid = 	lCuboid.size();
		
		
		
		double hMax = -1;
		
		for (O o : lCuboid) {
			hMax = Math.max(hMax, o.getHeight());
		}
		
		//on divise par 2 le hMax
		hMax = hMax * 0.5;
		
		for(int i=0;i<nbCuboid;i ++){
			Cuboid cI = lCuboid.get(i);
			
			for(int j=i+1;j<nbCuboid;j ++){
				Cuboid cJ = lCuboid.get(j);
				
				double distance = cI.getFootprint().distance(cJ.getFootprint());
				
				if(distance < hMax){
					return false;
				}
				
				
			}	
		}

		return true;

	}

	private boolean checkBandRegulationSpecArt71(Regulation r, O cuboid) {

		if (r == null || !r.getEpsilonBuffer().contains(cuboid.toGeometry())) {
			return false;
		}

		/*
		 * if (r == null || !r.getEpsilonBuffer().contains(cuboid.toGeometry()))
		 * { return false; }
		 */

		// ART_72 Distance minimale des constructions par rapport aux limites
		// séparatives imposée en mètre 88= non renseignable, 99= non réglementé

		// On vérifie la contrainte de recul par rapport aux bordures de la
		// parcelle
		// Existe t il ?
		double r_art72 = r.getArt_72();
		if (jtsCurveLatRightArt71 != null && r_art72 != 88.0 && r_art72 != 99.0) {
			// On vérifie la distance
			if (this.jtsCurveLatRightArt71.distance(cuboid.toGeometry()) < r_art72) {
				// elle n'est pas respectée, on retourne faux
				return false;

			}

		}

		// if(true)return true;

		// ART_73 Distance minimale des constructions par rapport à la limte
		// séparative de fond de parcelle 88= non renseignable, 99= non
		// réglementé

		double r_art73 = r.getArt_73();
		if (jtsCurveLimiteFondParcel != null && r_art73 != 88.0 && r_art73 != 99.0) {
			// On vérifie la distance (on récupère le foot
			if (this.jtsCurveLimiteFondParcel.distance(cuboid.toGeometry()) < r_art73) {
				// elle n'est pas respectée, on retourne faux
				return false;

			}

		}

		// ART_74 Distance minimum des constructions par rapport aux limites
		// séparatives, exprimée par rapport à la hauteur du bâtiment
		// 0 : NON
		// 1 : Retrait égal à la hauteur
		// 2 : Retrait égal à la hauteur divisé par deux
		// 3 : Retrait égal à la hauteur divisé par trois
		// 4 : Retrait égal à la hauteur divisé par quatre
		// 5 : Retrait égal à la hauteur divisé par cinq
		// 6 : Retrait égal à la hauteur divisé par deux moins trois mètres
		// 7 : Retrait égal à la hauteur moins trois mètres divisé par deux
		// 8 : retrait égal à la hauteur divisé par deux moins un mètre
		// 9 : retrait égal aux deux tiers de la hauteur
		// 10 : retrait égal aux trois quarts de la hauteur

		int r_art74 = r.getArt_74();
		if (r_art74 != 0) {
			double slope = 0;
			double hIni = 0;
			switch (r_art74) {
			case 1:
				slope = 1;
				break;
			case 2:
				slope = 2;
				break;
			case 3:
				slope = 3;
				break;
			case 4:
				slope = 4;
				break;
			case 5:
				slope = 5;
				break;
			case 6:
				// 6 : Retrait égal à la hauteur divisé par deux moins trois
				// mètres
				hIni = 2;
				slope = 6;
				break;
			case 7:
				// 7 : Retrait égal à la hauteur moins trois mètres divisé par
				// deux
				hIni = 3;
				slope = 2;
				break;
			case 8:
				// 8 : retrait égal à la hauteur divisé par deux moins un mètre
				hIni = 2;
				slope = 2;
				break;
			case 9:
				slope = 3 / 2;
				break;
			case 10:
				slope = 4 / 3;
				break;

			}

			if (this.jtsCurveLimiteFondParcel != null
					&& !cuboid.prospectJTS(this.jtsCurveLimiteFondParcel, slope, hIni)) {
				return false;
			}

			if (this.jtsCurveLatRightArt71 != null && !cuboid.prospectJTS(this.jtsCurveLatRightArt71, slope, hIni)) {
				return false;
			}
		}

		// System.out.println("Je return true " + cuboid.toString());
		return true;
	}

	public boolean checkBandRegulation(Regulation r, O cuboid) {

		if (r == null || !r.getEpsilonBuffer().contains(cuboid.toGeometry())) {
			return false;
		}

		/*
		 * 
		 * 
		 * 
		 * // On vérifie que la boite est bien dans la bande (optionnel ? ) //
		 * if(! r.getJTSBand().contains(cuboid.toGeometry())){ // return false;
		 * // }
		 * 
		 * // ART_6 Distance minimale des constructions par rapport à la voirie
		 * // imposée en mètre 88= non renseignable, 99= non réglementé // On
		 * vérifie la contrainte de recul par rapport au front de parcelle //
		 * (voirie) // Existe t il ? int r_art6 = r.getArt_6(); if (
		 * this.jtsCurveLimiteFrontParcel != null && r_art6 != 88 && r_art6 !=
		 * 99) { // On vérifie la distance if
		 * (this.jtsCurveLimiteFrontParcel.distance(cuboid.toGeometry()) <
		 * r_art6) { // elle n'est pas respectée, on retourne faux return false;
		 * 
		 * }
		 * 
		 * }
		 * 
		 * // ART_72 Distance minimale des constructions par rapport aux limites
		 * // séparatives imposée en mètre 88= non renseignable, 99= non
		 * réglementé
		 * 
		 * // On vérifie la contrainte de recul par rapport aux bordures de la
		 * // parcelle // Existe t il ? int r_art72 = r.getArt_72(); if
		 * (jtsCurveLimiteLatParcel != null && r_art72 != 88 && r_art72 != 99) {
		 * // On vérifie la distance if
		 * (this.jtsCurveLimiteLatParcel.distance(cuboid.toGeometry()) <
		 * r_art72) { // elle n'est pas respectée, on retourne faux return
		 * false;
		 * 
		 * }
		 * 
		 * }
		 * 
		 * // ART_73 Distance minimale des constructions par rapport à la limte
		 * // séparative de fond de parcelle 88= non renseignable, 99= non //
		 * réglementé
		 * 
		 * int r_art73 = r.getArt_73(); if (jtsCurveLimiteFondParcel != null &&
		 * r_art73 != 88 && r_art73 != 99) { // On vérifie la distance (on
		 * récupère le foot if
		 * (this.jtsCurveLimiteFondParcel.distance(cuboid.toGeometry()) <
		 * r_art73) { // elle n'est pas respectée, on retourne faux return
		 * false;
		 * 
		 * }
		 * 
		 * }
		 */

		// ART_74 Distance minimum des constructions par rapport aux limites
		// séparatives, exprimée par rapport à la hauteur du bâtiment
		// 0 : NON
		// 1 : Retrait égal à la hauteur
		// 2 : Retrait égal à la hauteur divisé par deux
		// 3 : Retrait égal à la hauteur divisé par trois
		// 4 : Retrait égal à la hauteur divisé par quatre
		// 5 : Retrait égal à la hauteur divisé par cinq
		// 6 : Retrait égal à la hauteur divisé par deux moins trois mètres
		// 7 : Retrait égal à la hauteur moins trois mètres divisé par deux
		// 8 : retrait égal à la hauteur divisé par deux moins un mètre
		// 9 : retrait égal aux deux tiers de la hauteur
		// 10 : retrait égal aux trois quarts de la hauteur

		int r_art74 = r.getArt_74();
		if (r_art74 != 0) {
			double slope = 0;
			double hIni = 0;
			switch (r_art74) {
			case 1:
				slope = 1;
				break;
			case 2:
				slope = 2;
				break;
			case 3:
				slope = 3;
				break;
			case 4:
				slope = 4;
				break;
			case 5:
				slope = 5;
				break;
			case 6:
				// 6 : Retrait égal à la hauteur divisé par deux moins trois
				// mètres
				hIni = 2;
				slope = 6;
				break;
			case 7:
				// 7 : Retrait égal à la hauteur moins trois mètres divisé par
				// deux
				hIni = 3;
				slope = 2;
				break;
			case 8:
				// 8 : retrait égal à la hauteur divisé par deux moins un mètre
				hIni = 2;
				slope = 2;
				break;
			case 9:
				slope = 3 / 2;
				break;
			case 10:
				slope = 4 / 3;
				break;

			}

			if (this.jtsCurveLimiteFondParcel != null
					&& !cuboid.prospectJTS(this.jtsCurveLimiteFondParcel, slope, hIni)) {
				return false;
			}

			if (this.jtsCurveLimiteLatParcel != null
					&& !cuboid.prospectJTS(this.jtsCurveLimiteLatParcel, slope, hIni)) {
				return false;
			}
		}

		// System.out.println("Je return true " + cuboid.toString());
		return true;

	}

	private boolean checkDistanceInterBuildings(C c, M m, double distanceInterBati) {

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

				// On prend en compte la hauteur max si elle est supérieure à la
				// contrainte de distance
				double distComp = Math.max(distanceInterBati, Math.max(ab.height(), batTemp.height()));

				if ((new SquaredDistance(ab.getRectangle2D(), batTemp.getRectangle2D())).getSquaredDistance() < distComp
						* distComp) {
					return false;
				}

			}

		}
		return true;

	}

}
