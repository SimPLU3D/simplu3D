package fr.ign.cogit.simplu3d.util.merge;

import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.io.feature.FeatureType;
import fr.ign.cogit.simplu3d.model.AbstractUrbanFeature;
import fr.ign.cogit.simplu3d.model.UrbanFeature;
import fr.ign.cogit.simplu3d.util.merge.SDPCalc.GeomHeightPair;
import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.extrusion.Extrusion2DObject;
import fr.ign.common.io.ShapefileWriter;
import fr.ign.common.topo.Arc;
import fr.ign.common.topo.CarteTopo;
import fr.ign.common.topo.Face;
import org.locationtech.jts.geom.*;

/**
 * Class that enables the fusion of Cuboid Geometry in order to make consistant geomtries one by path of cuboid.
 * <p>
 * This software is released under the licence CeCILL.
 * <p>
 * see LICENSE.TXT
 * <p>
 * see <a href="http://www.cecill.info/">...</a>
 * <p>
 *
 *
 * copyright IGN
 *
 * @author Brasebin Mickaël
 *
 * @version 1.0
 **/
public class MergeCuboid {

  private static final Logger logger = Logger.getLogger(MergeCuboid.class.getName());

  // A temporary attribute to store the z of a face
  private final static String ATT_TEMP = "ZTEMP";

  // The attribute where surface value will be stored
  private final static String ATT_SURFACE = "SURFACE";

  public static void main(String[] args) throws CloneNotSupportedException, MalformedURLException {

    //DirectPosition.PRECISION = 6;
    // Fixing the Z Min
    double ZMIN = 0;

    // The shapefile to simplu3D simulation to process
    String shpeIn = "/home/mbrasebin/Documents/Donnees/IAUIDF/Resultats/ResultatChoisy/results_pchoisy/24/simul_24_true_no_demo_sampler.shp";

    // Folder in
    String strShpOut = "/tmp/tmp/";

    MergeCuboid recal = new MergeCuboid();

    Collection<UrbanFeature> fus = recal.mergeFromShapefile(shpeIn, ZMIN);
    FeatureType ft = new FeatureType("Feature");
    ft.setGeometryType("MultiPolygon");
    ft.addAttribute(ATT_TEMP, "Double");
    //FIXME Other attributes?
    ShapefileWriter.write(fus, ft, null, strShpOut + "fus.shp");
    System.out.println(recal.getSurface());
  }

  public double getSurface() {
    return surface;
  }

  private double surface = 0;

  public Collection<UrbanFeature> mergeFromShapefile(String shpIn, double zMini) throws MalformedURLException {
    return mergeFromShapefile(shpIn, zMini, 0.05, 0.001);
  }

  public Collection<UrbanFeature> mergeFromShapefile(String shpIn, double zMini, double carteTopoThreshold, double shrkiningThreshold) throws MalformedURLException {
    // Creating a partition of cuboid with height stored as ATT_TEMP attribute
    List<Collection<UrbanFeature>> featColl = this.createPartitionCollection(shpIn);
    // The results
    Collection<UrbanFeature> featCollOut = new ArrayList<>();
    // We add the feature of each groups
    for (Collection<UrbanFeature> currentCollection : featColl) {
      featCollOut.add(mergeAGroupOfCuboid(currentCollection, zMini, carteTopoThreshold, shrkiningThreshold));
    }
    return featCollOut;
  }

  /**
   *
   * @param featColl           a collection of a set of adjacing boxes
   * @param zMini              the minimum z
   * @param carteTopoThreshold the threshold for merging points in carte topo creation
   * @param shrkiningThreshold the face are shrinken by a positive value in order to avoir numeric error in order to find a point not too near from polygon boundary
   * @return a feature with agregated boxes and the surface of the feature stored as ATT_SURFACE attribute
   */
  public UrbanFeature mergeAGroupOfCuboid(Collection<UrbanFeature> featColl, double zMini, double carteTopoThreshold, double shrkiningThreshold) {
    // Creating the neighbourhood relationship with CarteTopo
    CarteTopo carteTopo = newCarteTopo(featColl, carteTopoThreshold);
    SpatialRepository<UrbanFeature> repo = new SpatialRepository<>(featColl, null, null);
    // Spatial index to speed up
    //featColl.initSpatialIndex(Tiling.class, false);
    // Surface is computed
    double surfaceGroup = 0;
    // For each group the faces are treated (normally there is only one)
    // for (Groupe g : lG) {
    List<Polygon> lOS = new ArrayList<>();
    Map<Face, Map<String, Object>> faceAttributes = new HashMap<>();
    // We prepare the list of available faces
    // For each face we look at the maximal height
    for (Face f : carteTopo.getPopFaces()) {
      // We do not treat the infinite height
      /*if (f.isInfinite()) {
        System.out.println("### INFINITE face detected ###");
        continue;
      }*/
      // We took a point into the face and look if we find it in the
      // original data
      Point p = f.getGeometrie().getInteriorPoint();
      if (!f.getGeometrie().contains(p)) {
        // This may happen for all as CarteTopo create face for them
        logger.info("Point not in polygon : " + f);
      }
      Collection<UrbanFeature> featSelect = repo.findIntersects(p);
      double zMax = Double.NEGATIVE_INFINITY;
      if (featSelect.isEmpty()) {
        zMax = 0;
        System.out.println(f.getGeometrie());
        logger.info("empty face detected");
      }
      // We get the max height of the initial data
      for (UrbanFeature feat : featSelect) {
        zMax = Math.max(zMax, Double.parseDouble(feat.getAttribute(ATT_TEMP).toString()));
      }
      // We set the z of the point of the polygon as zMax
      Polygon poly = (Polygon) f.getGeometrie().copy();
      if (!featSelect.isEmpty()) {
        lOS.add(poly);
        // We compute the surface by adding the face (the roofs)
        surfaceGroup = surfaceGroup + poly.getArea();
      }
      Map<String, Object> attributes = faceAttributes.getOrDefault(f, new HashMap<>());
      attributes.put(ATT_TEMP, zMax + zMini);
      faceAttributes.put(f, attributes);
      //AttributeManager.addAttribute(f, ATT_TEMP, zMax + zMini, "Double");
      for (Coordinate dp : poly.getCoordinates()) {
        dp.setZ(zMax + zMini);
      }
    }
    // For each edges we determine the minimal and maximal height in
    // order to generate vertical faces
    for (Arc a : carteTopo.getPopArcs()) {
      Face fd = a.getFaceDroite();
      Face fg = a.getFaceGauche();
      // No neighbour for the edge
      // This case is not supposed to happen but we make a test to avoid it
      if (fd == null && fg == null) {
        continue;
      }
      double z1 = 0;
      double z2 = 0;
      // If there is only one neighbour we make a wall until the ground (ATT_TEMP)
      if (fd == null) {
        z1 = Double.parseDouble(faceAttributes.get(fg).get(ATT_TEMP).toString());
        z2 = zMini;
      } else if (fg == null) {
        z1 = Double.parseDouble(faceAttributes.get(fd).get(ATT_TEMP).toString());
        z2 = zMini;
      } else {
        // There is two neighbour; it will be between the edges of the
        // two roofs
        z1 = Double.parseDouble(faceAttributes.get(fg).get(ATT_TEMP).toString());
        z2 = Double.parseDouble(faceAttributes.get(fd).get(ATT_TEMP).toString());
      }
      // We ensure that the heights are not the same
      double zMin = Math.min(z1, z2);
      double zMax = Math.max(z1, z2);
      if (zMax == zMini) {
        continue;
      }
      // We create the wall from the line
      LineString lineToExtrude = (LineString) a.getGeometrie().copy();
      // We update the surface by considering this new wall
      surfaceGroup = surfaceGroup + lineToExtrude.getLength() * (zMax - zMin);
      // Extrusion of the geometry according to the considered height
      Geometry geom = Extrusion2DObject.convertFromLine(lineToExtrude, zMin, zMax);
      lOS.addAll(FromGeomToSurface.convertGeom(geom));
    }
    this.surface = this.surface + surfaceGroup;
    UrbanFeature feat = new AbstractUrbanFeature();
    GeometryFactory factory =  new GeometryFactory();
    feat.setGeometry(factory.createMultiPolygon(lOS.toArray(new Polygon[0])));
    feat.setAttribute(ATT_SURFACE, surfaceGroup);
    //AttributeManager.addAttribute(feat, ATT_SURFACE, surfaceGroup, "Double");
    return feat;
  }

  /**
   *
   * @param shpIn shapefile from Cuboid simplu3D simulation
   * @return a partition with polygons that have a Z
   */
  public List<Collection<UrbanFeature>> createPartitionCollection(String shpIn) throws MalformedURLException {
    // Launching SDPCalc to get the partition as a collection of
    // GeomHeightPair
    SDPCalc sd = new SDPCalc();
    sd.process(shpIn);
    List<List<GeomHeightPair>> llGeomPair = sd.getGeometryPairByGroup();
    // Agregating the pair into a list of collection of features (one collection by group)
    // With the height stored as ATT_TEMP
    List<Collection<UrbanFeature>> featColl = new ArrayList<>();
    for (List<GeomHeightPair> lPair : llGeomPair) {
      Collection<UrbanFeature> featCollTemp = new ArrayList<>();
      for (GeomHeightPair pair : lPair) {
        Geometry geom = pair.geom;
        for (Polygon os : FromGeomToSurface.convertGeom(geom)) {
          UrbanFeature feat = new AbstractUrbanFeature();
          feat.setGeometry(os);
          feat.setAttribute(ATT_TEMP, pair.height);
          featCollTemp.add(feat);
        }
      }
      featColl.add(featCollTemp);
    }
    return featColl;
  }

  /**
   * @param collection a collection of faces that forms a partition
   * @param threshold  a threshold to merge adjacing points
   * @return the carte topo
   */
  private static CarteTopo newCarteTopo(Collection<UrbanFeature> collection, double threshold) {
    try {
      // Initialisation d'une nouvelle CarteTopo
      CarteTopo carteTopo = new CarteTopo();
      carteTopo.addFeatures(collection);
/*      carteTopo.setBuildInfiniteFace(false);
      // Récupération des arcs de la carteTopo
      IPopulation<Arc> arcs = carteTopo.getPopArcs();
      // Import des arcs de la collection dans la carteTopo
      for (IFeature feature : collection) {
        List<ILineString> lLLS = FromPolygonToLineString.convertPolToLineStrings((IPolygon) FromGeomToSurface.convertGeom(feature.getGeom()).getFirst());
        for (ILineString ls : lLLS) {
          // affectation de la géométrie de l'objet issu de la collection à l'arc de la carteTopo
          for (int i = 0; i < ls.numPoints() - 1; i++) {
            // création d'un nouvel élément
            Arc arc = arcs.nouvelElement();
            arc.setGeometrie(new GM_LineString(ls.getControlPoint(i), ls.getControlPoint(i + 1)));
            // instanciation de la relation entre l'arc créé et l'objet issu de la collection
            arc.addCorrespondant(feature);
          }
        }
      }
      if (invalid(carteTopo)) {
        logger.error("Carte Topo non valide");
        System.exit(0);
      }
      carteTopo.creeNoeudsManquants(0.0);
      if (invalid(carteTopo)) {
        logger.error("");
        System.exit(0);
      }
      carteTopo.fusionNoeuds(threshold);
      if (invalid(carteTopo)) {
        logger.error("");
        System.exit(0);
      }
      carteTopo.decoupeArcs(0.01);
      carteTopo.splitEdgesWithPoints(0.01);
      carteTopo.filtreArcsDoublons();*/
      if (invalid(carteTopo)) {
        logger.severe("Invalid CarteTopo");
        System.exit(0);
      }
      // Création de la topologie Arcs Noeuds
      //carteTopo.creeTopologieArcsNoeuds(threshold);
      // La carteTopo est rendue planaire
/*
      if (invalid(carteTopo)) {
        logger.error("");
        System.exit(0);
      }
*/
      // DEBUG.addAll(carteTopo.getListeArcs());
      // Création des faces de la carteTopo
/*
      carteTopo.creeTopologieFaces();
      if (invalid(carteTopo)) {
        logger.error("");
        System.out.println("Error 3");
      }
*/
      return carteTopo;
    } catch (Exception e) {
      logger.severe(e.getMessage());
    }
    return null;
  }

  /**
   *
   * @param ct a CarteTopo to test
   * @return true if carteTopo is Valid
   */
  private static boolean invalid(CarteTopo ct) {
    return ct.getPopArcs().stream().anyMatch(a -> a.getGeometrie().getCoordinates().length < 2);
  }
}
