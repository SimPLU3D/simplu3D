package fr.ign.cogit.simplu3d.rjmcmc.generic.energy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.util.merge.MergeCuboid;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc.GeomHeightPair;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.rjmcmc.energy.CollectionEnergy;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class CompacityCollectionEnergy<T extends ISimPLU3DPrimitive> implements CollectionEnergy<T> {

    public CompacityCollectionEnergy() {
        // TODO Auto-generated constructor stub
    }


    @Override
    public double getValue(Collection<T> lili) {
        // TODO Auto-generated method stub

        if (lili.size() == 0) {
            System.out.println("CompacityCollectionEnergy : LISTE VIDE !!!");
        }

        //merging cuboids of the list
        MergeCuboid merg = new MergeCuboid();
        //converting lili to a IfeatureCollection<Ifeature>
        List<UrbanFeature> featColl_Lili = new ArrayList<>();
        for (T l : lili) {
            featColl_Lili.add((Cuboid) l);
        }

        double surfaceExterieure = 0.0;
        if (featColl_Lili.size() > 1) {
            // merge and give value to surface attribut of merg object
            merg.mergeAGroupOfCuboid(featColl_Lili, 0, 0.05, 0.001);
            surfaceExterieure = merg.getSurface();
            System.out.println("surface externe : " + surfaceExterieure);
        } else {
            Cuboid cucu = (Cuboid) featColl_Lili.get(0);
            //surfaceExterieure = cucu.getExternalSurfaceWithoutFloor();
        }
        //pour créer la liste de paires /hauteurs
        SDPCalc sdp = new SDPCalc();
        sdp.process((List<Cuboid>) lili);
        List<List<GeomHeightPair>> pairesGeomHeight = sdp.getGeometryPairByGroup();
        double volume = 0.0;
        for (List<GeomHeightPair> geomPairs : pairesGeomHeight) {
            for (GeomHeightPair g : geomPairs) {
                Geometry jtsGeom = g.geom;
                if (jtsGeom == null || jtsGeom.isEmpty()) {
                    continue;
                }
                List<Polygon> os = FromGeomToSurface.convertGeom(jtsGeom);
                for (Polygon osTemp : os) {
                    if (osTemp.getArea() < 0.01) {
                        continue;
                    }
                    Geometry extruded = Extrusion2DObject.convertFromGeometry(osTemp, 0, g.height);
                    MultiPolygon finalOs = FromGeomToSurface.convertMSGeom(extruded);
                    UrbanFeature feat = new AbstractUrbanFeature();
                    feat.setGeometry(finalOs);
                    double contributionVolume = feat.getGeometry().getArea() * g.height;
                    volume += contributionVolume;
                }
            }
        }
        // formule approchée : 3.83 * Volume ^2/3 / Aire
        return (2 * Math.PI * Math.pow(3. / (2 * Math.PI), 2.0 / 3.0) * Math.pow(volume, 2. / 3.) / surfaceExterieure);
    }
}
