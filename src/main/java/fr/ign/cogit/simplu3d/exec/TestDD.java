package fr.ign.cogit.simplu3d.exec;


import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.TransferHandler;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.convert.geom.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.sig3d.analysis.RoofDetection;
import fr.ign.cogit.simplu3d.test.rjmcmc.cuboid.geometry.impl.Cuboid2;

/**
 * Dropper - show File Drop Target from Drag-n-Drop
 * 
 * @version $Id: Dropper.java,v 1.1 2004/04/10 00:12:25 ian Exp $
 */
public class TestDD extends JFrame {

  /**
   * Construct trivial GUI and connect a TransferHandler to it.
   */
public static void main(String[] args){
  
    Cuboid2 c = new Cuboid2(0, 0, 10, 123, 10, 0);
    
    
    
    IOrientableSurface ps = c.getFootprint();
    
    
    
   IGeometry geom = Extrusion2DObject.convertFromPolygon((IPolygon) ps, 0, 238);
   
   List<IOrientableSurface> llis = FromGeomToSurface.convertGeom(geom);
   
   for(IOrientableSurface ops : llis){
     System.out.println(ops);
   }
   
   
  IMultiSurface<IOrientableSurface> iOSL =  (IMultiSurface<IOrientableSurface>)RoofDetection.detectRoof(geom, 0.2, true);
   System.out.println("      ");
   
  for(IOrientableSurface ops : iOSL){
    System.out.println(ops);
  }
  
  
  System.out.println(iOSL.size());
   
    
    
    
  }
}