package fr.ign.cogit.simplu3d.experiments.PLUCities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.PrescriptionType;
import fr.ign.cogit.simplu3d.model.SubParcel;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.mix.MultipleBuildingsCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.util.CuboidGroupCreation;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

public class PredicatePLUCities<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	// Des valeurs pour les différentes contraintes
	private double distReculVoirie = 0.0;
	private boolean align = false;
	private double distReculFond = 0.0;
	private double distReculLat = 0.0;
	private double distanceInterBati = 0.0;
	private double maximalCES = 0.0;
	private double maximalHauteur = 0.0;
	private boolean singleBuild = false;
	private int nbCuboid=0;
	private IFeatureCollection<Prescription> prescriptions;

	// BasicPropertyUnit utilisée
	private BasicPropertyUnit currentBPU;

	/**
	 * Constructeur de la classe
	 * 
	 * @param currentBPU        : l'unité foncière sur laquelle on construit
	 *                          (généralement : 1 parcelle, ça peut être utile pour
	 *                          accélérer les traitements, mais ce n'est pas fait
	 *                          ici)
	 * @param distReculVoirie   : distance de recul par rapport à la voirie (la
	 *                          référence ici est la limite séparative donnant sur
	 *                          la rue annotée comme ROAD)
	 * @param distReculFond     : distance de recul par rapport aux bordures
	 *                          annotées comme fond de parcelle annotée comme BOT
	 * @param distReculLat      : distance de recul par rapport aux bordures annotée
	 *                          comme LAT
	 * @param distanceInterBati : distance entre 2 boîtes
	 * @param maximalCES        : CES maximum
	 * @throws Exception
	 */

	public PredicatePLUCities(BasicPropertyUnit currentBPU, double distReculVoirie, double distReculFond,
			double distReculLat, double distanceInterBati, double maximalCES, double maximalhauteur, int nbcuboid,boolean singleBati) throws Exception {
		// On appelle l'autre constructeur qui renseigne un certain nombre de
		// géométries
		this(currentBPU);
		this.currentBPU = currentBPU;
		this.distReculVoirie = distReculVoirie;
		this.distReculFond = distReculFond;
		this.distReculLat = distReculLat;
		this.distanceInterBati = distanceInterBati;
		this.maximalCES = maximalCES;
		this.maximalHauteur = maximalhauteur;
		this.nbCuboid = nbcuboid;
		this.singleBuild = singleBati;
	}

	public PredicatePLUCities(BasicPropertyUnit currentBPU, double distReculVoirie, boolean align, double distReculFond,
			double distReculLat, double distanceInterBati, double maximalCES, double maximalhauteur,
			boolean singleBuild, int nbcuboid, IFeatureCollection<Prescription> presc) throws Exception {
		// On appelle l'autre constructeur qui renseigne un certain nombre de
		// géométries
		this(currentBPU);
		this.currentBPU = currentBPU;
		this.distReculVoirie = distReculVoirie;
		this.distReculFond = distReculFond;
		this.distReculLat = distReculLat;
		this.distanceInterBati = distanceInterBati;
		this.maximalCES = maximalCES;
		this.maximalHauteur = maximalhauteur;
		this.singleBuild = singleBuild;
		this.prescriptions = presc;
		this.align = align;
		this.nbCuboid = nbcuboid;
	}

	// On stocke la géométrie des différentes bordures de la parcelle courante
	// Elles servent de référence pour un certain nombre de contraintes
	Geometry jtsCurveLimiteFondParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteLatParcel = null;

	Geometry surface = null;

	/**
	 * Ce constructeur initialise les géométries curveLimiteFondParcel,
	 * curveLimiteFrontParcel & curveLimiteLatParcel car elles seront utilisées pour
	 * exprimer certaines contraintes
	 * 
	 * @param bPU
	 * @throws Exception
	 */
	private PredicatePLUCities(BasicPropertyUnit bPU) throws Exception {
		super();
		this.currentBPU = bPU;

		// Pour simplifier la vérification, on extrait les différentes bordures de
		// parcelles
		IMultiCurve<IOrientableCurve> curveLimiteFondParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteFrontParcel = new GM_MultiCurve<>();
		IMultiCurve<IOrientableCurve> curveLimiteLatParcel = new GM_MultiCurve<>();

		// On parcourt les parcelles du BasicPropertyUnit (un propriétaire peut
		// avoir plusieurs parcelles)
		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			// On parcourt les limites séparatives
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

		this.surface = AdapterFactory.toGeometry(gf, bPU.getGeom());

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
	 * Cette méthode est executée à chaque fois que le système suggère une nouvelle
	 * proposition. C => contient la configuration courante (en termes de cuboids
	 * proposés) M => les modifications que l'on souhaite apporter à la
	 * configuration courante. Normalement, il n'y a jamais plus d'une naissance ou
	 * d'une mort dans M, mais là dans le code on fait comme si c'était possible,
	 * mais ça peut être simplifié
	 */
	@Override
	public boolean check(C c, M m) {

		// Il s'agit des objets de la classe Cuboid
		List<O> lO = m.getBirth();

		// On vérifie les règles sur tous les pavés droits, dès qu'il y en a un qui ne
		// respecte pas une règle, on rejette
		// On traite les contraintes qui ne concernent que les nouveux bâtiments

		
		int nbAdded = m.getBirth().size() - m.getDeath().size();
		if (c.size() + nbAdded  > 1 && singleBuild) {
			return false;
		}

//		System.out.println("taille présumé de notre collec    "+c.size());

		if (c.size() + nbAdded > nbCuboid) {
			return false;
		}
		
		for (O cuboid : lO) {

			// On vérifie que le batiment est compris dans la zone d'alignement (surfacique)

			if (prescriptions != null && align == true) {
				for (Prescription prescription : prescriptions) {
					if (prescription.type == PrescriptionType.FACADE_ALIGNMENT) {
						if (prescription.getGeom().isMultiSurface()
								&& !cuboid.toGeometry().touches(jtsCurveLimiteFrontParcel)) {
							return false;
						}

					}
				}
			}

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
			// On vérifie la contrainte de recul par rapport au front de parcelle (voirie).
			// Existe t il ?
			if (this.jtsCurveLimiteFrontParcel != null) {
				// On vérifie la distance
				if (this.jtsCurveLimiteFrontParcel.distance(cuboid.toGeometry()) < this.distReculVoirie) {
					// elle n'est pas respectée, on retourne faux
					return false;
				}
			}

			// On vérifie la contrainte de recul par rapport aux bordures de la parcelle
			// Existe t il ?
			if (jtsCurveLimiteLatParcel != null) {
				// On vérifie la distance
				if (this.jtsCurveLimiteLatParcel.distance(cuboid.toGeometry()) < this.distReculLat) {
					// elle n'est pas respectée, on retourne faux
					return false;

				}

			}
		    
		    if ((!MultipleBuildingsCuboid.ALLOW_INTERSECTING_CUBOID)
		        && (!checkDistanceInterBuildings(c, m, distanceInterBati))) {
		      return false;
		    }

		    if (MultipleBuildingsCuboid.ALLOW_INTERSECTING_CUBOID) {
		      if (!testWidthBuilding(c, m, 7.5, distanceInterBati)) {
		        return false;
		      }
		    }
		    
			// Distance between existig building and cuboid
			for (Building b : currentBPU.getBuildings()) {

				if (b.getFootprint().distance(cuboid.getFootprint()) < distanceInterBati) {
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

			if (cuboid.getHeight() > maximalHauteur) {
				return false;
			}

			if (!surface.contains(cuboid.toGeometry())) {
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

	private boolean checkDistanceInterGroups(List<List<AbstractSimpleBuilding>> lGroupes, double distanceInterBati) {
		// si un seul groupe
		if (lGroupes.size() < 2)
			return true;
		// on va stocker les hauteurs pour pas les recalculer
		double[] heights = new double[lGroupes.size()];
		for (int i = 0; i < lGroupes.size(); ++i) {
			heights[i] = getGroupeHeight(lGroupes.get(i));
		}
		for (int i = 0; i < lGroupes.size() - 1; ++i) {
			for (int j = i + 1; j < lGroupes.size(); ++j) {
				double distanceGroupes = getGroupGeom(lGroupes.get(i)).distance(getGroupGeom(lGroupes.get(j)));
				double d = Math.min(Math.max(heights[i], heights[j]) * 0.5, distanceInterBati);
				// System.out.println("max(dist groupes, heights) : " + d
				// + "---- dit inter bati : " + distanceInterBati);
				if (distanceGroupes < d)
					return false;
			}
		}
		return true;
	}

	private double getGroupeHeight(List<AbstractSimpleBuilding> g) {
		double max = -1;
		for (AbstractBuilding b : g) {
			if (((O) b).getHeight() > max)
				max = ((O) b).getHeight();
		}
		return max;
	}

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
		 * List<List<AbstractSimpleBuilding>> groupes =
		 * CuboidGroupCreation.createGroup(lBatIni, 0.5);
		 * 
		 * if (groupes.size() > 1) { return false; }
		 */

		// On récupère la superficie de la basic propertyUnit
		double airePAr = 0;
		for (CadastralParcel cP : currentBPU.getCadastralParcels()) {
			airePAr = airePAr + cP.getArea();
		}

		return ((geom.getArea() / airePAr) <= maximalCES);
	}

	private boolean testWidthBuilding(C c, M m, double widthBuffer, double distanceInterBati) {
		// On fait la liste de tous les objets après modification
		List<O> lO = new ArrayList<>();

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

			lO.add(batTemp);

		}

		// On ajoute tous les nouveaux objets
		lO.addAll(m.getBirth());

		List<List<AbstractSimpleBuilding>> lGroupes = CuboidGroupCreation.createGroup(lO, 0);

		// System.out.println("nb groupes " + lGroupes.size());
		for (List<AbstractSimpleBuilding> lAb : lGroupes) {
			// System.out.println("groupe x : " + lAb.size() + " batiments");
			if (!checkWidth(lAb, widthBuffer)) {
				return false;
			}

		}

		// Calculer la distance entre groupes
		// 1 - par rapport à distanceInterBati
		// 2 - par rapport à la moitié de la hauteur du plus haut cuboid
		if (!checkDistanceInterGroups(lGroupes, distanceInterBati))
			return false;

		// System.out.println("-------------------nb groupes " +
		// lGroupes.size());
		return true;
	}

	private Geometry getGroupGeom(List<AbstractSimpleBuilding> g) {
		Collection<Geometry> collGeom = new ArrayList<>();
		for (AbstractSimpleBuilding o : g) {
			collGeom.add(o.toGeometry()/* .buffer(0.4) */);
		}
		Geometry union = null;
		try {
			union = CascadedPolygonUnion.union(collGeom);
		} catch (Exception e) {
			return null;
		}
		/* union = TopologyPreservingSimplifier.simplify(union, 0.4); */
		return union;
	}

	private GeometryFactory gf = new GeometryFactory();
	private long c = 0;

	private boolean checkWidth(List<AbstractSimpleBuilding> lO, double widthBuffer) {

		if (lO.size() < 2)
			return true;
		Geometry union = getGroupGeom(lO);
		if (union == null)
			return false;
		// Récupérer le polygone sans le trou
		// will that do it ?
		// System.out.println(union.getClass());
		if (union instanceof Polygon) {
			// union = gf
			// .createPolygon(((Polygon)
			// union).getExteriorRing().getCoordinates())
			// .buffer(5).buffer(-5);
			union = union.buffer(5).buffer(-5);
		}
		boolean multi = false;
		if (union instanceof MultiPolygon) {
			// System.out.println("multi " + union);
			return false;
			// System.out.println("multi " + union);
			// union = union.buffer(5).buffer(-5);
			// // if it is still a multipolygon we test if we can remove too
			// small
			// ones!
			// if (union instanceof MultiPolygon) {
			// MultiPolygon mp = ((MultiPolygon) union);
			// int nbOfSmallOnes = 0;
			// int bigOneindice = -1;
			// for (int i = 0; i < mp.getNumGeometries(); ++i) {
			// if (mp.getGeometryN(i).getArea() < 5)
			// nbOfSmallOnes++;
			// else
			// bigOneindice = i;
			// }
			// if (mp.getNumGeometries() - nbOfSmallOnes > 1)
			// return false;
			// union = mp.getGeometryN(bigOneindice);
			// }
			// union = gf
			// .createPolygon(((Polygon)
			// union).getExteriorRing().getCoordinates());
			// System.out.println("multibuffered " + union);
			// multi = true;
			// au final on peut court circuiter ?
			// return false;
		}

		Geometry negativeBuffer = union.buffer(-widthBuffer);

		if (negativeBuffer.isEmpty() || negativeBuffer.getArea() < 0.001) {
			++c;
			if (c % 10000 == 0 || multi) {
				// System.out.println("**** " + multi);
				// System.out.println("**** " + union);
				// System.out.println("good width "
				// + (negativeBuffer.isEmpty() ? "empty" : negativeBuffer));
				// System.out.println("group size " + lO.size());
			}
			return true;
		}
		// System.out.println("too big");
		// System.out.println(union);
		// System.out.println(negativeBuffer);
		// System.out.println("---------------------");
		return false;

	}

	private boolean checkDistanceInterBuildings(C c, M m, double distanceInterBati) {

		// On fait la liste de tous les objets après modification
		List<O> lO = new ArrayList<>();

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

			lO.add(batTemp);

		}

		// On ajoute tous les nouveaux objets
		lO.addAll(m.getBirth());

		// EXTRA RULE : la distance entre deux bâtiments sur une même parcelle
		// est égale à la hauteur divisée par deux du bâtiment le plus haut

		int nbCuboid = lO.size();

		double hMax = -1;

		for (O o : lO) {
			hMax = Math.max(hMax, o.getHeight());
		}

		// on divise par 2 le hMax
		hMax = hMax * 0.5;

		for (int i = 0; i < nbCuboid; i++) {
			AbstractSimpleBuilding cI = lO.get(i);

			for (int j = i + 1; j < nbCuboid; j++) {
				AbstractSimpleBuilding cJ = lO.get(j);

				double distance = cI.getFootprint().distance(cJ.getFootprint());

				if (distance < Math.min(hMax, distanceInterBati)) {
					return false;
				}

			}
		}

		return true;

	}
}
