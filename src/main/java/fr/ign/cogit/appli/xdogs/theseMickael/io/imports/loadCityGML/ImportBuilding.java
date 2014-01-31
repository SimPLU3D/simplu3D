package fr.ign.cogit.appli.xdogs.theseMickael.io.imports.loadCityGML;

import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.calculation.Util;
import fr.ign.cogit.geoxygene.sig3d.geometry.topology.Triangle;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.sig3d.model.citygml.building.CG_AbstractBuilding;
import fr.ign.cogit.sig3d.model.citygml.building.CG_Building;
import fr.ign.cogit.sig3d.model.citygml.building.CG_BuildingPart;
import fr.ign.cogit.sig3d.model.citygml.building.CG_RoofSurface;
import fr.ign.cogit.sig3d.model.citygml.building.CG_WallSurface;
import fr.ign.cogit.sig3d.model.citygml.core.CG_CityModel;
import fr.ign.cogit.sig3d.representation.citygml.core.RP_CityObject;
import fr.ign.cogit.sig3d.topology.CarteTopo3D;

/**
 * Classe pour charger des bâtiments dans le modèle CityGML
 * @author MBrasebin
 * 
 */
public class ImportBuilding {
  
  
  public static IFeatureCollection<IFeature> lContribGeom = new FT_FeatureCollection<IFeature>();
  
  

  public static CG_CityModel importBuilding(IFeatureCollection<IFeature> featC) {

    CG_CityModel cM = new CG_CityModel();
    int count = 0;
    for (IFeature feat : featC) {

      cM.add(importBuilding(feat, ++count));

    }
    
    

    return cM;
  }

  public static CG_AbstractBuilding importBuilding(IFeature bati, int ID) {

    // On décompose le bâtiment en corps
    CarteTopo3D cT = new CarteTopo3D(bati);
    List<List<Triangle>> lGroupes = cT.getGroupes();

    CG_Building cg_B = new CG_Building();
    
    int count = 0;

    for (List<Triangle> lT : lGroupes) {

      CG_BuildingPart cBP = new CG_BuildingPart();

      // On récupère le toit
      IMultiSurface<IOrientableSurface> gTRoof = Util
          .detectNonVertical(lT, 0.2);

      // On instancie la classe surface de toit
      CG_RoofSurface cRS = new CG_RoofSurface();
      cRS.setLod2MultiSurface((IMultiSurface<IOrientableSurface>) gTRoof);
      cRS.setGeom(cRS.getLod2MultiSurface());
      // On récupère les murs
      IMultiSurface<IOrientableSurface> gTWall = Util.detectVertical(lT, 0.2);

      // On instancie la classe mur
      CG_WallSurface cWS = new CG_WallSurface();
      cWS.setLod2MultiSurface(gTWall);
      cWS.setGeom(cWS.getLod2MultiSurface());
      // On affecte ça à la partie de bâtiment
      cBP.getBoundedBySurfaces().add(cWS);
      cBP.getBoundedBySurfaces().add(cRS);
      
      
       
      
      
      AttributeManager.addAttribute(cWS, "ID_GEO", bati.getAttribute("ID_GEO"), "String");
      AttributeManager.addAttribute(cWS, "ID_3D", ID, "String");
      AttributeManager.addAttribute(cWS, "ID_PART", count, "String");
      AttributeManager.addAttribute(cWS, "ROOF", 1, "String");
      AttributeManager.addAttribute(cWS, "DATE",  bati.getAttribute("Date").toString(), "String");
      
      AttributeManager.addAttribute(cRS, "ID_GEO", bati.getAttribute("ID_GEO"), "String");
      AttributeManager.addAttribute(cRS, "ID_3D", ID, "String");
      AttributeManager.addAttribute(cRS, "ID_PART", count, "String");
      AttributeManager.addAttribute(cRS, "ROOF",  0, "String");
      AttributeManager.addAttribute(cRS, "DATE", bati.getAttribute("Date").toString(), "String");
      
      
      
      count++;
      
      
      
      if(cWS.getGeom() != null && cWS.getGeom().coord().size() != 0){
        lContribGeom.add(cWS);
      }
      if(cRS.getGeom() != null &&   cRS.getGeom().coord().size() != 0){
      lContribGeom.add(cRS);
      }

    }

    RP_CityObject.generateCityObjectRepresentation(cg_B, null);
    return cg_B;

  }

}
