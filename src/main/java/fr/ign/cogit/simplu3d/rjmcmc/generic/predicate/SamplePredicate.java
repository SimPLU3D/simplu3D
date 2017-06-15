package fr.ign.cogit.simplu3d.rjmcmc.generic.predicate;

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
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

public class SamplePredicate<O extends ISimPLU3DPrimitive, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	// Des valeurs pour les différentes contraintes
	private double distReculVoirie = 0.0;
	private double distReculFond = 0.0;
	private double distReculLat = 0.0;
	private double distanceInterBati = 0.0;
	private double maximalCES = 0.0;

	// BasicPropertyUnit utilisée
	private BasicPropertyUnit currentBPU;

	/**
	 * Constructeur de la classe
	 * 
	 * @param currentBPU
	 *            : l'unité foncière sur laquelle on construit (généralement : 1
	 *            parcelle, ça peut être utile pour accélérer les traitements,
	 *            mais ce n'est pas fait ici)
	 * @param distReculVoirie
	 *            : distance de recul par rapport à la voirie (la référence ici
	 *            est la limite séparative donnant sur la rue annotée comme
	 *            ROAD)
	 * @param distReculFond
	 *            : distance de recul par rapport aux bordures annotées comme
	 *            fond de parcelle annotée comme BOT
	 * @param distReculLat
	 *            : distance de recul par rapport aux bordures annotée comme LAT
	 * @param distanceInterBati
	 *            : distance entre 2 boîtes
	 * @param maximalCES
	 *            : CES maximum
	 * @throws Exception
	 */
	public SamplePredicate(BasicPropertyUnit currentBPU, double distReculVoirie, double distReculFond,
			double distReculLat, double distanceInterBati, double maximalCES) throws Exception {
		// On appelle l'autre connstructeur qui renseigne un certain nombre de
		// géométries
		this(currentBPU);
		this.currentBPU = currentBPU;
		this.distReculVoirie = distReculVoirie;
		this.distReculFond = distReculFond;
		this.distReculLat = distReculLat;
		this.distanceInterBati = distanceInterBati;
		this.maximalCES = maximalCES;
	}

	// On stocke la géométrie des différentes bordures de la parcelle courante
	// Elles servent de référence pour un certain nombre de contraintes
	Geometry jtsCurveLimiteFondParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteLatParcel = null;

	Geometry surface = null;
	/**
	 * Ce constructeur initialise les géométries curveLimiteFondParcel,
	 * curveLimiteFrontParcel & curveLimiteLatParcel car elles seront utilisées
	 * pour exprimer certaines contraintes
	 * 
	 * @param bPU
	 * @throws Exception
	 */
	private SamplePredicate(BasicPropertyUnit bPU) throws Exception {
		super();
		this.currentBPU = bPU;

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

		GeometryFactory gf = new GeometryFactory();
		
		
		this.surface  = AdapterFactory.toGeometry(gf,bPU.getGeom());

		if (!curveLimiteFondParcel.isEmpty()) {
			this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf, curveLimiteFondParcel);
		}

		if (!curveLimiteFrontParcel.isEmpty()) {
			this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf, curveLimiteFrontParcel);
		}

		if (!curveLimiteLatParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf, curveLimiteLatParcel);
		}
	}

	/**
	 * Cette méthode est executée à chaque fois que le système suggère une
	 * nouvelle proposition. C => contient la configuration courante (en termes
	 * de cuboids proposés) M => les modifications que l'on souhaite apporter à
	 * la configuration courante. Normalement, il n'y a jamais plus d'une
	 * naissance ou d'une mort dans M, mais là dans le code on fait comme si
	 * c'était possible, mais ça peut être simplifié
	 */
	@Override
	public boolean check(C c, M m) {

		// Il s'agit des objets de la classe Cuboid
		List<O> lO = m.getBirth();


		// On vérifie les règles sur tous les pavés droits, dès qu'il y en a un
		// qui
		// ne respecte pas une règle, on rejette
		// On traite les contraintes qui ne concernent que les nouveux bâtiments
		for (O cuboid : lO) {

			// On vérifie la contrainte de recul par rapport au fond de parcelle
			// Existe t il ?
			if (jtsCurveLimiteFondParcel != null) {
				Geometry geom = cuboid.toGeometry();
				if (geom == null) {
					System.out.println("Nullll");
				}
				// On vérifie la distance (on récupère le foot
				if (this.jtsCurveLimiteFondParcel.distance(geom) < this.distReculFond) {
					// elle n'est pas respectée, on retourne faux
					return false;

				}

			}

			// On vérifie la contrainte de recul par rapport au front de
			// parcelle
			// (voirie)
			// Existe t il ?
			if (this.jtsCurveLimiteFrontParcel != null) {
				// On vérifie la distance
				if (this.jtsCurveLimiteFrontParcel.distance(cuboid.toGeometry()) < this.distReculVoirie) {
					// elle n'est pas respectée, on retourne faux
					return false;

				}

			}

			// On vérifie la contrainte de recul par rapport aux bordures de la
			// parcelle
			// Existe t il ?
			if (jtsCurveLimiteLatParcel != null) {
				// On vérifie la distance
				if (this.jtsCurveLimiteLatParcel.distance(cuboid.toGeometry()) < this.distReculLat) {
					// elle n'est pas respectée, on retourne faux
					return false;

				}

			}

			// Autres règles :

			// Pour la hauteur => c'est plutôt dans le fichier de configuration.
			// sinon on peut la mesurer comme ça : cuboid.height(1, 2) => mais
			// c'est
			// plus performant dans le fichier de configuration

			// Pour les bandes de constructibilité : on imaginera qu'il y a un
			// polygone ou un multisurface mp

			// IMultiSurface<IOrientableSurface> mS = null; // à définir
			// mS.contains(cuboid.footprint);
			
			
			if(! surface.contains(cuboid.toGeometry())){
					return false;
			}

		}

	
		// Pour produire des boîtes séparées et vérifier que la distance inter
		// bâtiment est respectée
		try {
			if (!checkDistanceInterBuildings(c, m)) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Pour vérifier que le CES (surface bâti) est respecté
		if (!respectMaximalBuiltArea(c, m)) {
			return false;
		}

		// On a réussi tous les tests, on renvoie vrai
		return true;

	}

	/**
	 * Code pour vérifier que la distance entre bâtiments est vérifiée
	 * 
	 * @param c
	 * @param m
	 * @return
	 * @throws Exception
	 */
	private boolean checkDistanceInterBuildings(C c, M m) throws Exception {
		GeometryFactory gf = new GeometryFactory();
		// On récupère les objets ajoutées lors de la proposition
		List<O> lO = m.getBirth();

		// On récupère la boîte (si elle existe) que l'on supprime lors de la
		// modification
		O batDeath = null;

		if (!m.getDeath().isEmpty()) {
			batDeath = m.getDeath().get(0);
		}

		// On regarde la distance entre les bâtiments existants
		// et les boîtes que l'on ajoute
		for (Building b : currentBPU.getBuildings()) {
			for (O ab : lO) {
				Geometry geomBat = AdapterFactory.toGeometry(gf, b.getFootprint());
				if (geomBat.distance(ab.toGeometry()) < distanceInterBati) {
					return false;
				}
			}
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

				// On regarde si la distance entre les boîtes qui restent et
				// celles que
				// l'on ajoute
				// respecte la distance entre boîtes
				if (batTemp.toGeometry().distance(ab.toGeometry()) < distanceInterBati) {
					return false;
				}

			}

		}
		return true;

	}

	/**
	 * Vérification du nom dépassement du CES
	 * 
	 * @param c
	 * @param m
	 * @return
	 */
	private boolean respectMaximalBuiltArea(C c, M m) {
		// On fait la liste de tous les objets après modification
		List<O> lCuboid = new ArrayList<>();

		// On ajoute tous les nouveaux objets
		lCuboid.addAll(m.getBirth());

		// On récupère la boîte (si elle existe) que l'on supprime lors de la
		// modification
		O cuboidDead = null;

		if (!m.getDeath().isEmpty()) {
			cuboidDead = m.getDeath().get(0);
		}

		// On parcourt les objets existants moins celui qu'on supprime
		Iterator<O> iTBat = c.iterator();
		while (iTBat.hasNext()) {

			O cuboidTemp = iTBat.next();

			// Si c'est une boîte qui est amenée à disparaître après
			// modification,
			// elle n'entre pas en jeu dans les vérifications
			if (cuboidTemp == cuboidDead) {
				continue;
			}

			lCuboid.add(cuboidTemp);

		}

		// C'est vide la règle est respectée
		if (lCuboid.isEmpty()) {
			return true;
		}

		// On calcule la surface couverte par l'ensemble des cuboid
		int nbElem = lCuboid.size();

		Geometry geom = lCuboid.get(0).toGeometry();

		for (int i = 1; i < nbElem; i++) {

			geom = geom.union(lCuboid.get(i).toGeometry());

		}

		List<AbstractSimpleBuilding> lBatIni = new ArrayList<>();
		for (ISimPLU3DPrimitive s : lCuboid) {
			lBatIni.add((AbstractSimpleBuilding) s);
		}

		/*
		List<List<AbstractSimpleBuilding>> groupes = CuboidGroupCreation.createGroup(lBatIni, 0.5);

		if (groupes.size() > 1) {
			return false;
		}*/

		// On récupère la superficie de la basic propertyUnit
		double airePAr = 0;
		for (CadastralParcel cP : currentBPU.getCadastralParcels()) {
			airePAr = airePAr + cP.getArea();
		}

		return ((geom.getArea() / airePAr) <= maximalCES);
	}
}
