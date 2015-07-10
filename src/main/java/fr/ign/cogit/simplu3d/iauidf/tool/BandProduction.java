package fr.ign.cogit.simplu3d.iauidf.tool;

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
import fr.ign.cogit.simplu3d.iauidf.regulation.Regulation;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;

public class BandProduction {

	List<IMultiSurface<IOrientableSurface>> lOut = new ArrayList<>();
	IMultiCurve<IOrientableCurve> iMSRoad = new GM_MultiCurve<>();
	private IMultiCurve<IOrientableCurve> lineRoad = null;

	public BandProduction(BasicPropertyUnit bPU, Regulation r1, Regulation r2) {

		// On récupère le polygone surlequel on va faire la découpe
		IPolygon pol_BPU = bPU.getpol2D();

		// On créé la géométrie des limites donnant sur la voirie

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
				.getCadastralParcel().get(0).getSpecificCadastralBoundary();

		for (SpecificCadastralBoundary sc : lBordureLat) {

			if (sc.getType() == SpecificCadastralBoundary.LAT) {
				iMSLim.add((IOrientableCurve) sc.getGeom());
			}
		}
		IFeatureCollection<SpecificCadastralBoundary> lBordureFond = bPU
				.getCadastralParcel().get(0).getSpecificCadastralBoundary();
		for (SpecificCadastralBoundary sc : lBordureFond) {
			if (sc.getType() == SpecificCadastralBoundary.BOT) {
				iMSLim.add((IOrientableCurve) sc.getGeom());
			}
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

		/*
		 * 
		 * En fait on va réinjecter ça dans le sampler Sinon on peut pas vérifer
		 * que les boites sont bien dans les bandes
		 * 
		 * if (iMSBande1 != null && !iMSBande1.isEmpty()) { iMSBande1 =
		 * FromGeomToSurface.convertMSGeom(iMSBande1 .buffer(-largBat)); } if
		 * (iMSBande2 != null && !iMSBande2.isEmpty()) { iMSBande2 =
		 * FromGeomToSurface.convertMSGeom(iMSBande2 .buffer(-largBat)); }
		 */

		lOut.add(iMSBande1);
		lOut.add(iMSBande2);

		r1.setGeomBande(iMSBande1);

		r2.setGeomBande(iMSBande2);

		double rArt6 = r1.getArt_6();
		if (rArt6 != 99 && rArt6 != 88) {

			if (rArt6 == 0) {
				lineRoad = (IMultiCurve<IOrientableCurve>) (iMSRoad.clone());
			} else {
				lineRoad = shiftRoad(bPU, rArt6);

			}

		}

	}

	private IMultiCurve<IOrientableCurve> shiftRoad(BasicPropertyUnit bPU,
			double valShiftB) {

		IMultiCurve<IOrientableCurve> iMS = new GM_MultiCurve<>();

		IDirectPosition centroidParcel = bPU.getpol2D().centroid();

		for (IOrientableCurve oC : iMSRoad) {

			if (oC.isEmpty()) {
				continue;
			}

			IDirectPosition centroidGeom = oC.coord().get(0);
			Vecteur v = new Vecteur(centroidParcel, centroidGeom);

			Vecteur v2 = new Vecteur(oC.coord().get(0), oC.coord().get(
					oC.coord().size() - 1));
			v2.setZ(0);
			v2.normalise();

			Vecteur vOut = v2.prodVectoriel(new Vecteur(0, 0, 1));

			IGeometry geom = ((IGeometry) oC.clone());

			if (v.prodScalaire(vOut) < 0) {
				vOut = vOut.multConstante(-1);
			}

			IGeometry geom2 = geom.translate(valShiftB * vOut.getX(), valShiftB
					* vOut.getY(), 0);

			if (!geom2.intersects(bPU.getGeom())) {
				geom2 = geom.translate(-valShiftB * vOut.getX(), -valShiftB
						* vOut.getY(), 0);
			}

			iMS.addAll(FromGeomToLineString.convert(geom2));

		}

		return iMS;

	}

	public IMultiCurve<IOrientableCurve> getLineRoad() {
		return this.lineRoad;
	}

}
