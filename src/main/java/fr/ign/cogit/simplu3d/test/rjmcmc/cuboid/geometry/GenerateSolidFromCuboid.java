package fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry;

import java.util.ArrayList;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ISolid;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;
import fr.ign.cogit.simplu3d.model.application.Environnement;

public class GenerateSolidFromCuboid {

  public static ISolid generate(Cuboid c) {


    double z = c.getZmin(); 
        


    double mx = -c.ratio * c.normaly;
    double my = c.ratio * c.normalx;

    IDirectPosition p4 = new DirectPosition(c.centerx + c.normalx + mx,
        (c.centery + c.normaly + my), z + c.height);
    IDirectPosition p3 = new DirectPosition(c.centerx - c.normalx + mx,
        (c.centery - c.normaly + my), z + c.height);
    IDirectPosition p7 = new DirectPosition(c.centerx - c.normalx - mx,
        (c.centery - c.normaly - my), z + c.height);
    IDirectPosition p8 = new DirectPosition(c.centerx + c.normalx - mx,
        (c.centery + c.normaly - my), z + c.height);

    return createCube(p8, p7 , p3, p4 , z);

  }

  public static ISolid generate(CuboidSnap c){

    
    return generate(c,c.getZmin());
  }
  
  
  public static ISolid generate(CuboidSnap c, double zMin){
    
    IDirectPositionList dpl = c.getFootprint().coord();
    
    IDirectPosition dp1 = dpl.get(0);
    dp1.setZ(zMin + c.height);
    IDirectPosition dp2 = dpl.get(1);
    dp2.setZ(zMin + c.height);
    IDirectPosition dp3 = dpl.get(2);
    dp3.setZ(zMin + c.height);
    IDirectPosition dp4 = dpl.get(3);
    dp4.setZ(zMin + c.height);
    
    
  
  return createCube(dp1,dp2,dp3,dp4,zMin);
}
  
  
  public static ISolid generate(Cuboid2 c){

    
    return generate(c,c.getZmin());
  }
  
  
  public static ISolid generate(Cuboid2 c, double zMin){
    
    IDirectPositionList dpl = c.getFootprint().coord();
    
    IDirectPosition dp1 = dpl.get(0);
    dp1.setZ(zMin + c.height);
    IDirectPosition dp2 = dpl.get(1);
    dp2.setZ(zMin + c.height);
    IDirectPosition dp3 = dpl.get(2);
    dp3.setZ(zMin + c.height);
    IDirectPosition dp4 = dpl.get(3);
    dp4.setZ(zMin + c.height);
    
    
  
  return createCube(dp1,dp2,dp3,dp4,zMin);
}

  
  
  
  
  private static GM_Solid createCube(IDirectPosition p4, IDirectPosition p3,
      IDirectPosition p7, IDirectPosition p8, double zmin) {
    // On crée les 6 sommets du cube
    IDirectPosition p1 = (IDirectPosition) p4.clone();
    p1.setZ(zmin);

    IDirectPosition p2 = (IDirectPosition) p3.clone();
    p2.setZ(zmin);

    IDirectPosition p5 = (IDirectPosition) p8.clone();
    p5.setZ(zmin);

    IDirectPosition p6 = (IDirectPosition) p7.clone();
    p6.setZ(zmin);

    DirectPositionList LPoint1 = new DirectPositionList();
    DirectPositionList LPoint2 = new DirectPositionList();
    DirectPositionList LPoint3 = new DirectPositionList();
    DirectPositionList LPoint4 = new DirectPositionList();
    DirectPositionList LPoint5 = new DirectPositionList();
    DirectPositionList LPoint6 = new DirectPositionList();

    LPoint1.add(p1);
    LPoint1.add(p2);
    LPoint1.add(p3);
    LPoint1.add(p4);
    LPoint1.add(p1);

    GM_LineString ls = new GM_LineString(LPoint1);
    GM_OrientableSurface surf1 = new GM_Polygon(ls);

    LPoint2.add(p4);
    LPoint2.add(p3);
    LPoint2.add(p7);
    LPoint2.add(p8);
    LPoint2.add(p4);

    ls = new GM_LineString(LPoint2);
    GM_OrientableSurface surf2 = new GM_Polygon(ls);

    LPoint3.add(p3);
    LPoint3.add(p2);
    LPoint3.add(p6);
    LPoint3.add(p7);
    LPoint3.add(p3);

    ls = new GM_LineString(LPoint3);
    GM_OrientableSurface surf3 = new GM_Polygon(ls);
    LPoint4.add(p1);

    LPoint4.add(p5);
    LPoint4.add(p6);
    LPoint4.add(p2);

    LPoint4.add(p1);

    ls = new GM_LineString(LPoint4);
    GM_OrientableSurface surf4 = new GM_Polygon(ls);

    LPoint5.add(p1);
    LPoint5.add(p4);
    LPoint5.add(p8);
    LPoint5.add(p5);
    LPoint5.add(p1);

    ls = new GM_LineString(LPoint5);
    GM_OrientableSurface surf5 = new GM_Polygon(ls);

    LPoint6.add(p6);
    LPoint6.add(p5);
    LPoint6.add(p8);
    LPoint6.add(p7);
    LPoint6.add(p6);

    ls = new GM_LineString(LPoint6);
    GM_OrientableSurface surf6 = new GM_Polygon(ls);

    ArrayList<IOrientableSurface> LFace = new ArrayList<IOrientableSurface>();
    LFace.add(surf1);
    LFace.add(surf2);
    LFace.add(surf3);
    LFace.add(surf4);
    LFace.add(surf5);
    LFace.add(surf6);
    return new GM_Solid(LFace);

  }
}
