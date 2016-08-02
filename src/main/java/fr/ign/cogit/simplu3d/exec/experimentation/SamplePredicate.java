package fr.ign.cogit.simplu3d.exec.experimentation;

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
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;


// @TODO : displace prospect to make this class really generic
public class SamplePredicate<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	// Des valeurs pour les différentes contraintes
	private double distReculVoirie = 0.0;
	private double distReculFond = 0.0;
	private double distReculLat = 0.0;
	private double maximalCES = 0.0;
	private double hIniRoad = 0.0;
	private double slopeRoad = 0.0;

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
	 */
	public SamplePredicate(BasicPropertyUnit currentBPU,
			double distReculVoirie, double distReculFond, double distReculLat,
			double maximalCES, double hIniRoad, double slopeRoad) {
		// On appelle l'autre connstructeur qui renseigne un certain nombre de
		// géométries
		this(currentBPU);
		this.currentBPU = currentBPU;
		this.distReculVoirie = distReculVoirie;
		this.distReculFond = distReculFond;
		this.distReculLat = distReculLat;
		this.hIniRoad = hIniRoad;
		this.slopeRoad = slopeRoad;
		this.maximalCES = maximalCES;
	}

	// On stocke la géométrie des différentes bordures de la parcelle courante
	// Elles servent de référence pour un certain nombre de contraintes
	Geometry jtsCurveLimiteFondParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;
	Geometry jtsCurveLimiteLatParcel = null;

	/**
	 * Ce constructeur initialise les géométries curveLimiteFondParcel,
	 * curveLimiteFrontParcel & curveLimiteLatParcel car elles seront utilisées
	 * pour exprimer certaines contraintes
	 * 
	 * @param bPU
	 */
	private SamplePredicate(BasicPropertyUnit bPU) {
		super();
		this.currentBPU = bPU;
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		// On parcourt les parcelles du BasicPropertyUnit (un propriétaire peut
		// avoir plusieurs parcelles)
		for (CadastralParcel cP : currentBPU.getCadastralParcels()) {

			// On parcourt les limites séparaticves
			for (ParcelBoundary sCB : cP
					.getBoundaries()) {

				// En fonction du type on ajoute à telle ou telle géométrie

				// Fond de parcel
				if (sCB.getType() == ParcelBoundaryType.BOT) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
						curveLimiteFondParcel.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe SamplePredicate : quelque chose n'est pas un ICurve : "
										+ geom.getClass());
					}

				}

				// Limite latérale
				if (sCB.getType() == ParcelBoundaryType.LAT) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
						curveLimiteLatParcel.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe SamplePredicate : quelque chose n'est pas un ICurve : "
										+ geom.getClass());
					}

				}

				// Limite front
				if (sCB.getType() == ParcelBoundaryType.ROAD) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
						curveLimiteFrontParcel.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe SamplePredicate : quelque chose n'est pas un ICurve : "
										+ geom.getClass());
					}

				}

			}

		}

		GeometryFactory gf = new GeometryFactory();

		if (!curveLimiteFondParcel.isEmpty()) {
			this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf,
					curveLimiteFondParcel);
		}

		if (!curveLimiteFrontParcel.isEmpty()) {
			this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf,
					curveLimiteFrontParcel);
		}

		if (!curveLimiteLatParcel.isEmpty()) {
			this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf,
					curveLimiteLatParcel);
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

			if (this.jtsCurveLimiteFondParcel != null
					&& this.jtsCurveLimiteFondParcel.distance(cuboid
							.toGeometry()) < this.distReculFond) {
				// elle n'est pas respectée, on retourne faux
				return false;

			}

			if (this.jtsCurveLimiteFrontParcel != null
					&& this.jtsCurveLimiteFrontParcel.distance(cuboid
							.toGeometry()) < this.distReculVoirie) {
				// elle n'est pas respectée, on retourne faux
				return false;

			}

			if (this.jtsCurveLimiteLatParcel != null
					&& this.jtsCurveLimiteLatParcel.distance(cuboid
							.toGeometry()) < this.distReculLat) {
				// elle n'est pas respectée, on retourne faux
				return false;

			}

			if (this.jtsCurveLimiteFrontParcel != null
					&& !cuboid.prospectJTS(this.jtsCurveLimiteFrontParcel,
							slopeRoad, this.hIniRoad)) {
				return false;
			}

		}

		List<O> lBatIni = new ArrayList<>();
		Iterator<O> iTBat = c.iterator();

		O batDeath = null;

		if (!m.getDeath().isEmpty()) {
			batDeath = m.getDeath().get(0);
		}

		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			if (batTemp == batDeath) {
				continue;
			}

			lBatIni.add(batTemp);

		}

		lBatIni.addAll(lO);

		// Pour vérifier que le CES (surface bâti) est respecté
		if (!respectBuildArea(lBatIni)) {
			return false;
		}

		// On a réussi tous les tests, on renvoie vrai
		return true;

	}

	private boolean respectBuildArea(List<O> lBatIni) {

		if (lBatIni.isEmpty()) {
			return true;
		}

		int nbElem = lBatIni.size();

		Geometry geom = lBatIni.get(0).toGeometry();

		for (int i = 1; i < nbElem; i++) {

			geom = geom.union(lBatIni.get(i).toGeometry());

		}

		double airePAr = this.currentBPU.getCadastralParcels().get(0).getArea();

		return ((geom.getArea() / airePAr) <= this.maximalCES);
	}
}
