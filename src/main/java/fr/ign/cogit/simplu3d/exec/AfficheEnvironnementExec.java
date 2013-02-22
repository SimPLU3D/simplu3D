package fr.ign.cogit.simplu3d.exec;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.io.load.application.LoaderSHP;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.Environnement;
import fr.ign.cogit.simplu3d.model.regle.consequences.batiment.Interdiction;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.ConstantRepresentation;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TexturedSurface;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;

public class AfficheEnvironnementExec {
  
  
  
  public static void main(String[] args){
    
    
    ConstantRepresentation.backGroundColor = new Color(156,180,193);
    
    
    String folder = "E:/mbrasebin/Donnees/Strasbourg/GTRU/Project1/";

        
   Environnement env =  LoaderSHP.load(folder);
   
   
   List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
   lTheme.add(Theme.TOIT_BATIMENT);
   lTheme.add(Theme.FACADE_BATIMENT);
   lTheme.add(Theme.FAITAGE);
   lTheme.add(Theme.PIGNON);
   lTheme.add(Theme.GOUTTIERE);
   lTheme.add(Theme.VOIRIE);
  // lTheme.add(Theme.PARCELLE);
   lTheme.add(Theme.SOUS_PARCELLE);
 //  lTheme.add(Theme.ZONE);
   lTheme.add(Theme.PAN);
   
   Theme[] tab = lTheme.toArray(new Theme[0]);
   
   List<VectorLayer> vl = RepEnvironnement.represent(env,tab );
   
   MainWindow mW = new MainWindow();
   
   for(VectorLayer l:vl){
     
     
     mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l);
   }
   
   
   
   mW.getInterfaceMap3D().removeLight(0);
   mW.getInterfaceMap3D().addLight(new Color(147,147,147), 0,0,0);
   mW.getInterfaceMap3D().moveLight(180, -15, 120,0);
   mW.getInterfaceMap3D().addLight(new Color(147,147,147), 0,0,0);
   mW.getInterfaceMap3D().moveLight(  -140, 3, 120,1);
   
   
   
   
   
   
 //  1051042.8513268954120576,6840539.0837931865826249 : 1051264.8064121364150196,6840679.2711814027279615

   
   
  double xc = ( 1051042.8513268954120576 + 1051264.8064121364150196)/2;
  double yc = (6840539.0837931865826249 + 6840679.2711814027279615) / 2 ;
    
  double z = 138;
  
  
  double longueur = 1051264.8064121364150196 - 1051042.8513268954120576;
  double largeur = 6840679.2711814027279615 - 6840539.0837931865826249;
  
  
  IDirectPositionList dpl = new DirectPositionList();
  
  IDirectPosition dp1 = new DirectPosition(xc - longueur/2, yc-largeur/2, z);
  IDirectPosition dp2 = new DirectPosition(xc + longueur/2, yc- largeur/2, z);
  IDirectPosition dp3 = new DirectPosition(xc + longueur/2, yc + largeur/2, z);
  IDirectPosition dp4 = new DirectPosition(xc - longueur/2, yc + largeur/2, z);
   
   
   
  dpl.add(dp1);
  dpl.add(dp2);
  dpl.add(dp3);
  dpl.add(dp4);      
  dpl.add(dp1);   
   
   
   
   
   IFeatureCollection<IFeature> fc = new FT_FeatureCollection<IFeature>();
   
   IFeature feat = new DefaultFeature(new GM_Polygon(new GM_LineString(dpl)));
   
   fc.add(feat);
   
   
   feat.setRepresentation(new TexturedSurface(feat, TextureManager.textureLoading( "C:/Users/mbrasebin/Desktop/Env3D/TextParcelle.png"),longueur,largeur));

   
   
   
   
   mW.getInterfaceMap3D().getCurrent3DMap().addLayer(new VectorLayer(fc,"Cool"));
   

    
  }

}
