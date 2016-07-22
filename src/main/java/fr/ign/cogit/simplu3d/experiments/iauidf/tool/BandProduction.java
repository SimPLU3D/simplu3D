package fr.ign.cogit.simplu3d.experiments.iauidf.tool;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToLineString;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.experiments.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.model.SpecificCadastralBoundaryType;

public class BandProduction {

	List<IMultiSurface<IOrientableSurface>> lOut = new ArrayList<>();
	IMultiCurve<IOrientableCurve> iMSRoad = new GM_MultiCurve<>();
	private IMultiCurve<IOrientableCurve> lineRoad = null;

	@SuppressWarnings("unchecked")
	public BandProduction(BasicPropertyUnit bPU, Regulation r1, Regulation r2) {

		// On récupère le polygone surlequel on va faire la découpe
		IPolygon pol_BPU = bPU.getpol2D();

		// On créé la géométrie des limites donnant sur la voirie

		List<SpecificCadastralBoundary> lBordureVoirie = bPU.getCadastralParcels().get(0).getBoundariesByType(SpecificCadastralBoundaryType.ROAD);
		for (SpecificCadastralBoundary sc : lBordureVoirie) {
			iMSRoad.add((IOrientableCurve) sc.getGeom());
		}

		// System.out.println("size road" + iMSRoad.size());

		double profBande = r1.getBande();
		// BANDE Profondeur de la bande principale x > 0 profondeur de la bande
		// par rapport à la voirie
		IMultiSurface<IOrientableSurface> iMSBande1;
		if (profBande == 0) {
			iMSBande1 = FromGeomToSurface.convertMSGeom(pol_BPU);
		} else {
			iMSBande1 = FromGeomToSurface.convertMSGeom(pol_BPU.intersection(iMSRoad.buffer(profBande)));
		}

		IMultiSurface<IOrientableSurface> iMSBande2 = null;

		// ART_6 Distance minimale des constructions par rapport à la voirie
		// imposée en mètre 88= non renseignable, 99= non réglementé

		// On enlève la bande de x m à partir de la voirie
		double r1_art6 = r1.getArt_6();
		if (r1_art6 != 88.0 && r1_art6 != 99.0 && r1_art6 != 0.0 && !iMSBande1.isEmpty()) {

			iMSBande1 = FromGeomToSurface.convertMSGeom(iMSBande1.difference(iMSRoad.buffer(r1_art6)));
		}

		// ART_72 Distance minimale des constructions par rapport aux limites
		// séparatives imposée en mètre 88= non renseignable, 99= non réglementé
		// ART_73 Distance minimale des constructions par rapport à la limte
		// séparative de fond de parcelle 88= non renseignable, 99= non
		// réglementé

		// il me semble qu'on avait dit qu'il n'y avait pas de discrimination

		// On créé la géométrie des autres limites séparatives
		IMultiCurve<IOrientableCurve> iMSLim = new GM_MultiCurve<>();
		List<SpecificCadastralBoundary> lBordureLat = bPU.getCadastralParcels().get(0)
				.getBoundaries();

		for (SpecificCadastralBoundary sc : lBordureLat) {

			if (sc.getType() == SpecificCadastralBoundaryType.LAT) {
				iMSLim.add((IOrientableCurve) sc.getGeom());
			}
		}
		List<SpecificCadastralBoundary> lBordureFond = bPU.getCadastralParcels().get(0).getBoundaries();
		for (SpecificCadastralBoundary sc : lBordureFond) {
			if (sc.getType() == SpecificCadastralBoundaryType.BOT) {
				iMSLim.add((IOrientableCurve) sc.getGeom());
			}
		}

		// On enlève la bande de x m à partir des limites séparatives
		double r1_art72 = r1.getArt_72();
		if (r1_art72 != 88.0 && r1_art72 != 99.0 && r1_art72 != 0.0 && !iMSBande1.isEmpty() && (r1.getArt_71() != 2)) {

			iMSBande1 = FromGeomToSurface.convertMSGeom(iMSBande1.difference(iMSLim.buffer(r1_art72)));
		}

		// Idem s'il y a un règlement de deuxième bande
		if (r2 != null) {

			iMSBande2 = FromGeomToSurface.convertMSGeom(pol_BPU.difference(iMSRoad.buffer(profBande)));

			// idem pour r2
			double r2_art6 = r2.getArt_6();
			if (r2_art6 != 88.0 && r2_art6 != 99.0 && r2_art6 != 0.0 && iMSBande2 != null && !iMSBande2.isEmpty()) {

				iMSBande2 = FromGeomToSurface.convertMSGeom(iMSBande2.difference(iMSRoad.buffer(r2_art6)));
			}

			// idem pour r2
			double r2_art72 = r2.getArt_72();
			if (r2_art72 != 88.0 && r2_art72 != 99.0 && r2_art72 != 0.0 && iMSBande2 != null && !iMSBande2.isEmpty()
					&& (r2.getArt_71() != 2)) {

				iMSBande2 = FromGeomToSurface.convertMSGeom(iMSBande2.difference(iMSLim.buffer(r2_art72)));
			}

			r2.setGeomBande(iMSBande2);

		}

		// 2 bandes
		lOut.add(iMSBande1);
		lOut.add(iMSBande2);

		r1.setGeomBande(iMSBande1);

		// Si l'article 6 demande qu'un alignementsoit respecté, on l'active
		double rArt6 = r1.getArt_6();
		if (rArt6 != 99.0 && rArt6 != 88.0) {

			if (rArt6 == 0) {
				// Soit le long de la limite donnant sur la voirie
				lineRoad = (IMultiCurve<IOrientableCurve>) (iMSRoad.clone());
			} else {
				// Soit en appliquant un petit décalage
				lineRoad = shiftRoad(bPU, rArt6);
			}
		} else {

		}
	}

	/**
	 * Méthode permettant de produire une multicurve en reculant vers
	 * l'intérieur de l'unité foncière (bPU) les limites donnant sur la voirie
	 * d'une distance (valShiftB)
	 * 
	 * @param bPU
	 * @param valShiftB
	 * @return
	 */
	private IMultiCurve<IOrientableCurve> shiftRoad(BasicPropertyUnit bPU, double valShiftB) {

		IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();

		IDirectPosition centroidParcel = bPU.getpol2D().centroid();

		for (IOrientableCurve oC : iMSRoad) {

			if (oC.isEmpty()) {
				continue;
			}

			IDirectPosition centroidGeom = oC.coord().get(0);
			Vecteur v = new Vecteur(centroidParcel, centroidGeom);

			Vecteur v2 = new Vecteur(oC.coord().get(0), oC.coord().get(oC.coord().size() - 1));
			v2.setZ(0);
			v2.normalise();

			Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));

			IGeometry geom = ((IGeometry) oC.clone());

			if (v.prodScalaire(vOut) < 0) {
				vOut = vOut.multConstante(-1);
			}

			IGeometry geom2 = geom.translate(valShiftB * vOut.getX(), valShiftB * vOut.getY(), 0);

			if (!geom2.intersects(bPU.getGeom())) {
				geom2 = geom.translate(-valShiftB * vOut.getX(), -valShiftB * vOut.getY(), 0);
			}

			iMS.addAll(FromGeomToLineString.convert(geom2));

		}

		return iMS;

	}

	public IMultiCurve<IOrientableCurve> getLineRoad() {
		return this.lineRoad;
	}

}
