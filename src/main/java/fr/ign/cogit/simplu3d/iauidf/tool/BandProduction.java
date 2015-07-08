package fr.ign.cogit.simplu3d.iauidf.tool;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

public class BandProduction {

	public static List<IMultiSurface<IOrientableSurface>> getBands(
			BasicPropertyUnit bPU, Regulation r1, Regulation r2, double largBat) {

		List<IMultiSurface<IOrientableSurface>> lOut = new ArrayList<>();

		// On récupère le polygone surlequel on va faire la découpe
		IPolygon pol_BPU = bPU.getpol2D();

		// On créé la géométrie des limites donnant sur la voirie
		IMultiCurve<IOrientableCurve> iMSRoad = new GM_MultiCurve<>();
		IFeatureCollection<SpecificCadastralBoundary> lBordureVoirie = bPU
				.getCadastralParcel().get(0).getBorduresFront();
		for (SpecificCadastralBoundary sc : lBordureVoirie) {
			iMSRoad.add((IOrientableCurve) sc.getGeom());
		}

		// System.out.println("size road" + iMSRoad.size());

		double profBande = r1.getBande();
		// BANDE Profondeur de la bande principale x > 0 profondeur de la bande
		// par rapport à la voirie

		IMultiSurface<IOrientableSurface> iMSBande1 = FromGeomToSurface
				.convertMSGeom(pol_BPU.intersection(iMSRoad.buffer(profBande)));
		IMultiSurface<IOrientableSurface> iMSBande2 = null;

		if (r2 != null) {

			iMSBande2 = FromGeomToSurface.convertMSGeom(pol_BPU
					.difference(iMSRoad.buffer(profBande)));

		}

		// ART_6 Distance minimale des constructions par rapport à la voirie
		// imposée en mètre 88= non renseignable, 99= non réglementé

		// On enlève la bande de x m à partir de la voirie
		int r1_art6 = r1.getArt_6();
		if (r1_art6 != 88 && r1_art6 != 99 && r1_art6 != 0
				&& !iMSBande1.isEmpty()) {

			iMSBande1 = FromGeomToSurface.convertMSGeom(iMSBande1
					.difference(iMSRoad.buffer(r1_art6)));
		}

		// idem pour r2
		int r2_art6 = r2.getArt_6();
		if (r2_art6 != 88 && r2_art6 != 99 && r2_art6 != 0 && iMSBande2 != null
				&& !iMSBande2.isEmpty()) {

			iMSBande2 = FromGeomToSurface.convertMSGeom(iMSBande2
					.difference(iMSRoad.buffer(r2_art6)));
		}

		// ART_72 Distance minimale des constructions par rapport aux limites
		// séparatives imposée en mètre 88= non renseignable, 99= non réglementé
		// ART_73 Distance minimale des constructions par rapport à la limte
		// séparative de fond de parcelle 88= non renseignable, 99= non
		// réglementé

		// il me semble qu'on avait dit qu'il n'y avait pas de discrimination

		// On créé la géométrie des autres limites séparatives
		IMultiCurve<IOrientableCurve> iMSLim = new GM_MultiCurve<>();
		IFeatureCollection<SpecificCadastralBoundary> lBordureLat = bPU
				.getCadastralParcel().get(0).getSubParcel().get(0)
				.getBorduresLat();
		for (SpecificCadastralBoundary sc : lBordureLat) {
			iMSLim.add((IOrientableCurve) sc.getGeom());
		}

		IFeatureCollection<SpecificCadastralBoundary> lBordureFond = bPU
				.getCadastralParcel().get(0).getSubParcel().get(0)
				.getBorduresFond();
		for (SpecificCadastralBoundary sc : lBordureFond) {
			iMSLim.add((IOrientableCurve) sc.getGeom());
		}

		// On enlève la bande de x m à partir des limites séparatives
		int r1_art72 = r1.getArt_72();
		if (r1_art72 != 88 && r1_art72 != 99 && r1_art72 != 0
				&& !iMSBande1.isEmpty()) {

			iMSBande1 = FromGeomToSurface.convertMSGeom(iMSBande1
					.difference(iMSLim.buffer(r1_art72)));
		}

		// idem pour r2
		int r2_art72 = r2.getArt_72();
		if (r2_art72 != 88 && r2_art72 != 99 && r2_art72 != 0
				&& iMSBande2 != null && !iMSBande2.isEmpty()) {

			iMSBande2 = FromGeomToSurface.convertMSGeom(iMSBande2
					.difference(iMSLim.buffer(r2_art72)));
		}

		if (iMSBande1 != null && !iMSBande1.isEmpty()) {
			iMSBande1 = FromGeomToSurface.convertMSGeom(iMSBande1
					.buffer(-largBat));
		}
		if (iMSBande2 != null && !iMSBande2.isEmpty()) {
			iMSBande2 = FromGeomToSurface.convertMSGeom(iMSBande2
					.buffer(-largBat));
		}

		lOut.add(iMSBande1);
		lOut.add(iMSBande2);

		return lOut;

	}
}
