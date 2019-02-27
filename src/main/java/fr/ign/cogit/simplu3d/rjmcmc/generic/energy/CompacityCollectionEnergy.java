package fr.ign.cogit.simplu3d.rjmcmc.generic.energy;

import java.util.Collection;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.building.CG_Building;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.util.JTS;
import fr.ign.cogit.simplu3d.util.merge.MergeCuboid;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc.GeomHeightPair;
import fr.ign.rjmcmc.energy.CollectionEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;
 

public class CompacityCollectionEnergy<T extends ISimPLU3DPrimitive> implements CollectionEnergy<T> {

  public  CompacityCollectionEnergy() {
    // TODO Auto-generated constructor stub
  }
  

    @Override
    public double getValue(Collection<T> lili) {
      // TODO Auto-generated method stub
      
      if(lili.size()==0) {
        System.out.println("CompacityCollectionEnergy : LISTE VIDE !!!");
      }
      
      //merging cuboids of the list
      MergeCuboid merg = new MergeCuboid();
      //converting lili to a IfeatureCollection<Ifeature>
      IFeatureCollection<IFeature> featColl_Lili = new FT_FeatureCollection<>();
      for(T l:lili) {
        featColl_Lili.add((Cuboid)l);
      }
      
      Double surfaceExterieure = 0.0 ;
      if(featColl_Lili.size()>1) {
      // merge and give value to surface attribut of merg object
       merg.mergeAGroupOfCuboid(featColl_Lili,  0, 0.05, 0.001); 
       surfaceExterieure = merg.getSurface();
       System.out.println("surface externe : " + surfaceExterieure);
       
      }
      else {
        
            Cuboid cucu = (Cuboid)featColl_Lili.get(0);
      }
      
      
      //pour créer la liste de paires /hauteurs 
      SDPCalc sdp = new SDPCalc();
      sdp.process((List<Cuboid>)lili);
      List<List<GeomHeightPair>> pairesGeomHeight = sdp.getGeometryPairByGroup();
      
      
      Double volume = 0.0 ;

      for (List<GeomHeightPair> geomPairs : pairesGeomHeight) {
          for (GeomHeightPair g : geomPairs) {

              IGeometry jtsGeom = JTS.fromJTS(g.geom);

              if (jtsGeom == null || jtsGeom.coord().isEmpty()) {
                  continue;
              }

              IMultiSurface<IOrientableSurface> os = FromGeomToSurface.convertMSGeom(jtsGeom);

              for (IOrientableSurface osTemp : os) {
                  if (osTemp.area() < 0.01) {
                      continue;
                  }
                  IGeometry extruded = Extrusion2DObject.convertFromGeometry(osTemp, 0, g.height);
                  IMultiSurface<IOrientableSurface> finalOs = FromGeomToSurface.convertMSGeom(extruded);
                  IFeature feat = new DefaultFeature(finalOs);
                  Double contributionVolume = feat.getGeom().area() * g.height; 
                  volume += contributionVolume ;
              }

          }

      }
      
     
      
        // formule approchée : 3.83 * Volume ^2/3 / Aire
        return (2 * Math.PI * Math.pow(3. / (2 * Math.PI), 2.0 / 3.0) * Math.pow(volume, 2. / 3.) / surfaceExterieure);
      }

}
