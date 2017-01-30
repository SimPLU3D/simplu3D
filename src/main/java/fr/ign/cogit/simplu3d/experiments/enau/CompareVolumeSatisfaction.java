package fr.ign.cogit.simplu3d.experiments.enau;

import java.awt.Color;
import java.io.File;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.util.conversion.ShapefileReader;
import fr.ign.cogit.simplu3d.experiments.enau.calculation.ENAUMorphoIndicators;
import fr.ign.cogit.simplu3d.experiments.enau.geometry.DeformedCuboid;
import fr.ign.cogit.simplu3d.io.nonStructDatabase.shp.LoaderSHP;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.util.AssignZ;

public class CompareVolumeSatisfaction {

	public static void main(String[] args) throws Exception {
		String shapeFile = "C:/Users/mbrasebin/Desktop/Alia/couche_formeAlia_trans.shp";

		String shapeFileM = "C:/Users/mbrasebin/Desktop/Alia/simul5.shp";

		

		IFeatureCollection<IFeature> featC = ShapefileReader.read(shapeFile);
		
		IMultiSurface<IOrientableSurface> ims = FromGeomToSurface.convertMSGeom(featC.get(0).getGeom());
		 ims.addAll(FromGeomToSurface.convertMSGeom(featC.get(1).getGeom()));

		DeformedCuboid dc = retrieveCuboid(ims, Math.PI * 216 / 180, false);

		IFeature feat = new DefaultFeature(dc.generated3DGeom());
		
		IFeatureCollection<IFeature> featCRep = new FT_FeatureCollection<>();
		featCRep.add(feat);
		
		
		
		
		
		IFeatureCollection<IFeature> featCM = ShapefileReader.read(shapeFileM);
		
		IMultiSurface<IOrientableSurface> imsM = FromGeomToSurface.convertMSGeom(featCM.get(0).getGeom());
		// imsM.addAll(FromGeomToSurface.convertMSGeom(featCM.get(1).getGeom()));
		

			DeformedCuboid dcM = retrieveCuboid(imsM,Math.PI +  Math.PI * 216 / 180, false);
			
			IFeature featM = new DefaultFeature(dcM.generated3DGeom());
			
			IFeatureCollection<IFeature> featCRepM = new FT_FeatureCollection<>();
			featCRepM.add(featM);
			
		
		MainWindow mW = new MainWindow();
		
		VectorLayer vl = new VectorLayer(featCRep, "Couche1",Color.red);
		VectorLayer v2 = new VectorLayer(featC, "Couche2",Color.green);
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vl);
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(v2);
		
		
		VectorLayer v3 = new VectorLayer(featCRepM, "Couche1M",Color.red);
		VectorLayer v4 = new VectorLayer(featCM, "Couche2M",Color.green);
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(v3);
		mW.getInterfaceMap3D().getCurrent3DMap().addLayer(v4);
		
		
		
		// Chargement du fichier de configuration
		String folderName = "C:/Users/mbrasebin/Desktop/Alia/";

//		String fileName = "building_parameters_project_expthese_3.xml";

		//CadastralParcelLoader.ATT_ID_PARC = "id_parcell";
		AssignZ.DEFAULT_Z = 0;

		// Chargement de l'environnement
		Environnement env = LoaderSHP.loadNoDTM(new File(folderName));
		
		// On trouve la parcelle qui a l'identifiant numéro 4
		BasicPropertyUnit bPU = null;
		for (BasicPropertyUnit bPUTemp : env.getBpU()) {
			// Identiant numéro 4
			if (bPUTemp.getId() == 3) {
				bPU = bPUTemp;
				break;
			}

		}

		////// Deformed 1
		ENAUMorphoIndicators ind1 = new ENAUMorphoIndicators(bPU, dc);
		
		
		// Valeurs de règles à saisir
		// C1
		double distReculVoirie = 2;

		// C2
		double slope = 1;
		double hIni = 45;

		// C3
		double hMax = 17;


		// C4
		double distReculLimi = 5.4;
		double slopeProspectLimit = 2;

		// C7
		double maximalCES = 0.5;
		
		System.out.println("NomForme;Volume;C1;C7;C3;C4;C2;C5");
		System.out.println("Forme Alia;"+ind1.volume()+";"+
		ind1.getSatisfactionAlignement(distReculVoirie)+";"+
		ind1.getSatisfactionCES(maximalCES)+";"+
		ind1.getSatisfactionHauteur(hMax)+";"+
		ind1.getSatisfactionProspectLimSep(slopeProspectLimit, 0 )+";"+
		ind1.getSatisfactionProspectRoute(slope, hIni)+";"+
		ind1.getSatisfactionServitudeVue(distReculLimi)+";"
		);
		
		
		
		////// Deformed 2
		ENAUMorphoIndicators ind2 = new ENAUMorphoIndicators(bPU, dcM);
		System.out.println("Forme M;"+ind2.volume()+";"+
				ind2.getSatisfactionAlignement(distReculVoirie)+";"+
				ind2.getSatisfactionCES(maximalCES)+";"+
				ind2.getSatisfactionHauteur(hMax)+";"+
				ind2.getSatisfactionProspectLimSep(slopeProspectLimit, 0 )+";"+
				ind2.getSatisfactionProspectRoute(slope, hIni)+";"+
				ind2.getSatisfactionServitudeVue(distReculLimi)+";"
				);
		
		
		
		
	}

	private static DeformedCuboid retrieveCuboid(IGeometry geom, double orientation, boolean goodSens) {

		IDirectPosition dp = geom.centroid();

		OrientedBoundingBox oBB = new OrientedBoundingBox(geom);

		if(goodSens){
			return new DeformedCuboid(dp.getX(), dp.getY(),  	oBB.getLength(),oBB.getWidth(),
					

					geom.coord().get(0).getZ(), geom.coord().get(1).getZ(), geom
							.coord().get(2).getZ(), geom.coord().get(3).getZ(),
							orientation);
		}
		
		return new DeformedCuboid(dp.getX(), dp.getY(),  oBB.getWidth(),	oBB.getLength(),
				

				geom.coord().get(0).getZ(), geom.coord().get(1).getZ(), geom
						.coord().get(2).getZ(), geom.coord().get(3).getZ(),
						orientation);
	}

}
