package fr.ign.cogit.simplu3d.io.load.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.Messages;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.AbstractDTM;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomroot.GM_Object;

public class DTMPostGISNoJava3D extends AbstractDTM {

  public int echantillonage = 1;

  public static double CONSTANT_OFFSET = 0;

  /*
   * Les paramètres du MNT xIni : il s'agit du X du coin supérieur gauche du MNT
   * yIni : il s'agit du Y du coin supérieur gauche du MNT pasX : il s'agit du
   * pas en X du MNT (echantillonnage inclus) pasY : il s'agit du pas en Y du
   * MNT (echantillonnage inclus) nX : il s'agit du nombre de mailles en X à
   * afficher nY : il s'agit du nombre de mailles en Y à afficher strip : il
   * s'agit de la liste de triangles formant le MNT exageration : il s'agi du
   * coefficient en Z que l'on applique bgeo : il s'agit de la BranchGroup
   * représentant le MNT
   */
  protected double xIni;
  protected double yIni;
  protected double zMax;
  protected double zMin;
  protected double stepX;
  protected double stepY;
  protected int nX;
  protected int nY;
  protected double noDataValue;

  private final static Logger logger = Logger
      .getLogger(DTMPostGISNoJava3D.class.getCanonicalName());

  private String host = "";
  private String port = "";
  private String base = "";
  private String tablename = "";
  private String user = "";
  private String pw = "";

  double[][] tabAlt = null;

  public DTMPostGISNoJava3D(String host, String port, String base,
      String tablename, String user, String pw)
      throws SQLException {

    super();

    this.path = null;
    this.imagePath = null;
    this.imageEnvelope = null;


    this.pw = pw;
    this.user = user;

    this.host = host;
    this.base = base;
    this.tablename = tablename;
    this.port = port;

    load();
  }

  private boolean load() throws SQLException {

    // Les informations concernant le coin supérieur gauche du MNT
    double shiftX = 0;
    double shiftY = 0;

    // Valeur indiquant l'absence de données
    this.noDataValue = -9999.0;

    // Lecture du fichier et récupèration des différentes valeurs citées
    // précédemment

    this.zMin = Double.POSITIVE_INFINITY;
    this.zMax = Double.NEGATIVE_INFINITY;

    // Liste des entités que l'on souhaite charger
    java.sql.Connection conn;

    // Création des paramètres de connexion
    String url = "jdbc:postgresql://" + host + ":" + port + "/" + base;
    logger.debug(url);

    logger.info(Messages.getString("PostGIS.Try") + url);
    conn = DriverManager.getConnection(url, user, pw);

    String requestSelect = "SELECT ST_Height(rast), ST_width(rast), ST_UpperLeftX(rast), ST_UpperLeftY(rast), ST_PixelHeight(rast), ST_PixelWidth(rast) from "
        + tablename;

    // System.out.println(requestSelect);
    logger.debug(requestSelect);

    Statement s = conn.createStatement();

    ResultSet rMeta = s.executeQuery(requestSelect);

    // Une seule ligne normalement
    rMeta.next();

    String str_height = rMeta.getString(1);
    int nrows = Integer.parseInt(str_height);

    String str_width = rMeta.getString(2);
    int ncols = Integer.parseInt(str_width);

    String str_PixelH = rMeta.getString(5);
    double cellesizeY = Double.parseDouble(str_PixelH);

    String str_PixelW = rMeta.getString(6);
    double cellesizeX = Double.parseDouble(str_PixelW);

    String str_upLeftX = rMeta.getString(3);

    shiftX = Double.parseDouble(str_upLeftX);

    String str_upLeftY = rMeta.getString(4);
    shiftY = Double.parseDouble(str_upLeftY) - cellesizeY * nrows;

    String sum = "select ST_SummaryStats(rast) from " + tablename;
    Statement s1 = conn.createStatement();

    ResultSet rStats = s1.executeQuery(sum);
    rStats.next();
    String res = rStats.getString(1);

    String[] tabPars = res.split(",");

    String strZmax = tabPars[tabPars.length - 1];
    strZmax = strZmax.substring(0, strZmax.length() - 1);

    zMax = Double.parseDouble(strZmax);
    zMin = Double.parseDouble(tabPars[tabPars.length - 2]);

    logger.debug(requestSelect);

    // On prépare le nombre de lignes
    nrows = nrows / this.echantillonage;
    ncols = ncols / this.echantillonage;

    tabAlt = new double[nrows][ncols];

    // On renseigne les différentes valeurs donnant des informations sur
    // le MNT
    this.xIni = shiftX + this.stepX/2;
    this.yIni = shiftY + this.stepY/2;

    int currentRow = 1;

    ResultSet rs = null;

    // On traite le fichier ligne par ligne
    for (int j = 0; j < nrows; j++) {

      // On charge la ligne en mémoire

      rs = getLine(currentRow, ncols, conn);

      // Pour chaque colonne que l'on souhaite récupèrer
      for (int i = 0; i < ncols; i++) {

        rs.next();

        tabAlt[j][i] = Double.parseDouble(rs.getString(2));
      }
      
    currentRow++;
      
      
    }

    conn.close();

    this.stepX = this.echantillonage * cellesizeX;
    this.stepY = this.echantillonage * cellesizeY;
    this.nX = ncols;
    this.nY = nrows;

    return true;
  }

  private ResultSet getLine(int indexLine, int nbCol, Connection conn)
      throws SQLException {

    Statement s = conn.createStatement();

    String sql = "SELECT y, ST_Value(rast, 1, y, " + indexLine
        + ",  false) As b1val FROM mnt CROSS JOIN generate_series(1, " + nbCol
        + "," + this.echantillonage + ") As y ;";
    logger.debug(sql);
 

    return s.executeQuery(sql);

  }

  /**
   * Cette fonction permet d'extraire en géométrie géoxygene les triangles du
   * MNT compris dans le rectangle formé par dpMin et dpMax en 2D
   * 
   * @param dpMin point inférieur gauche du rectangle
   * @param dpMax point supérieur droit du rectangle
   * @return une liste de polygones décrivant les géométries du MNT compris dans
   *         le rectangle formé par les 2 points en 2D
   */
  @Override
  public MultiPolygon processSurfacicGrid(double xmin, double xmax,
      double ymin, double ymax) {

    GeometryFactory fac = new GeometryFactory();

    // On récupère dans quels triangles se trouvent dpMin et dpMax
    int posxMin = (int) ((xmin - this.xIni) / (this.stepX * this.echantillonage));
    int posyMin = (int) ((ymin - this.yIni) / (this.stepY * this.echantillonage));

    int posxMax = 1 + (int) ((xmax - this.xIni) / (this.stepX * this.echantillonage));
    int posyMax = 1 + (int) ((ymax - this.yIni) / (this.stepY * this.echantillonage));

    // On récupère les sommets extérieurs de ces triangles (ceux qui
    // permettent d'englober totalement le rectangle dpMin, dpMax
    Coordinate dpOrigin = new Coordinate(posxMin * this.stepX + this.xIni,
        posyMin * this.stepY + this.yIni);
    Coordinate dpFin = new Coordinate(posxMax * this.stepX + this.xIni, posyMax
        * this.stepY + this.yIni);

    // On évalue le nombre de mailles à couvrir
    int nbInterX = Math.max(1, (int) ((dpFin.x - dpOrigin.x) / this.stepX));
    int nbInterY = Math.max(1,
        (int) ((int) ((dpFin.y - dpOrigin.y) / this.stepY)));

    Polygon[] lPolys = new Polygon[2 * nbInterX * nbInterY];
    int indPoly = 0;
    // On crée une géométrie géoxygne pour chacune de ces mailles
    // (2 triangles par maille)
    for (int i = 0; i < nbInterX; i++) {
      for (int j = 0; j < nbInterY; j++) {

        Coordinate dp1 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y
            + j * this.stepY);
        Coordinate dp2 = new Coordinate(dpOrigin.x + (i + 1) * this.stepX,
            dpOrigin.y + j * this.stepY);
        Coordinate dp3 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y
            + (j + 1) * this.stepY);

        Coordinate dp4 = new Coordinate(dpOrigin.x + (i + 1) * this.stepX,
            dpOrigin.y + (j + 1) * this.stepY);

        Coordinate[] coord = new Coordinate[4];
        coord[0] = dp1;
        coord[1] = dp2;
        coord[2] = dp4;
        coord[3] = dp1;

        LinearRing l1 = fac.createLinearRing(coord);

        Coordinate[] coord2 = new Coordinate[4];
        coord2[0] = dp1;
        coord2[1] = dp4;
        coord2[2] = dp3;
        coord2[3] = dp1;

        LinearRing l2 = fac.createLinearRing(coord2);
        lPolys[indPoly] = fac.createPolygon(l1, null);
        indPoly++;
        lPolys[indPoly] = fac.createPolygon(l2, null);
        indPoly++;

      }

    }
    // On renvoie la liste des triangles
    return fac.createMultiPolygon(lPolys);
  }

  /**
   * Cette fonction renvoie les lignes comprises dans le rectangles ayant dpMin
   * et dpMax comme points extrémités
   * 
   * @param dpMin point inférieur gauche
   * @param dpMax point supérieur droit
   * @return une liste de points correspondant aux lignes des mailles comprises
   *         entre ces 2 points
   */
  @Override
  public MultiLineString processLinearGrid(double xmin, double ymin,
      double xmax, double ymax) {
    GeometryFactory fact = new GeometryFactory();
    // On récupère l'indice des triangles contenant ces points
    int posxMin = (int) ((xmin - this.xIni) / (this.stepX * this.echantillonage));
    int posyMin = (int) ((ymin - this.yIni) / (this.stepY * this.echantillonage));

    int posxMax = 1 + (int) ((xmax - this.xIni) / (this.stepX * this.echantillonage));
    int posyMax = 1 + (int) ((ymax - this.yIni) / (this.stepY * this.echantillonage));

    // On récupère les points extrêmes appartenant à ces triangles
    Coordinate dpOrigin = new Coordinate(posxMin * this.stepX + this.xIni,
        posyMin * this.stepY + this.yIni);
    Coordinate dpFin = new Coordinate(posxMax * this.stepX + this.xIni, posyMax
        * this.stepY + this.yIni);

    // On calcule le nombre de géométries à générer
    int nbInterX = (int) ((dpFin.x - dpOrigin.x) / this.stepX);
    int nbInterY = (int) ((dpFin.y - dpOrigin.y) / this.stepY);

    nbInterX = Math.max(1, nbInterX);
    nbInterY = Math.max(1, nbInterY);

    LineString[] lineStrings = new LineString[(nbInterX ) * (nbInterY )
        * 3 + 2];
    int indL = 0;

    // On crée 3 lignes par maill du MNT
    for (int i = 0; i < nbInterX ; i++) {

      for (int j = 0; j < nbInterY ; j++) {

        Coordinate dp1 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y
            + j * this.stepY);
        Coordinate dp2 = new Coordinate(dpOrigin.x + (i + this.echantillonage) * this.stepX,
            dpOrigin.y + j * this.stepY);
        Coordinate dp3 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y
            + (j + this.echantillonage) * this.stepY);
        Coordinate dp4 = new Coordinate(dpOrigin.x + (i + this.echantillonage) * this.stepX,
            dpOrigin.y + (j + this.echantillonage) * this.stepY);

        Coordinate[] c1 = new Coordinate[2];
        Coordinate[] c2 = new Coordinate[2];
        Coordinate[] c3 = new Coordinate[2];

        c1[0] = dp1;
        c1[1] = dp2;

        LineString s1 = fact.createLineString(c1);

        c2[0] = dp1;
        c2[1] = dp3;

        LineString s2 = fact.createLineString(c2);

        c3[0] = dp1;
        c3[1] = dp4;

        LineString s3 = fact.createLineString(c3);

        lineStrings[indL] = s1;
        indL++;
        lineStrings[indL] = s2;
        indL++;
        lineStrings[indL] = s3;
        indL++;

      }

    }
    // On complète en ajoutant les bordures supérieures et inférieures (qui
    // ne sont pas parcourus par le boucle précédente
    Coordinate dpInterX = new Coordinate(dpFin.x, dpOrigin.y);
    Coordinate dpInterY = new Coordinate(dpOrigin.x, dpFin.y);

    Coordinate[] c1 = new Coordinate[2];
    c1[0] = dpInterX;
    c1[1] = dpFin;

    lineStrings[indL] = fact.createLineString(c1);
    indL++;

    Coordinate[] c2 = new Coordinate[2];
    c2[0] = dpInterY;
    c2[1] = dpFin;

    lineStrings[indL] = fact.createLineString(c2);

    // On renvoie le tout
    return fact.createMultiLineString(lineStrings);
  }

  /**
   * Renvoie le triangle qui se trouve dans la maille indexX, indexY et dans le
   * bas de la maille si inferior est true et dans le haut de celle-ci sinon
   * Renvoie null si les indices ne correspondent pas à un triangle existant
   * 
   * @param indexX l'indice de la maille en X
   * @param indexY l'indice de la maille en Y
   * @param inferior la position du triangle par rapport à la maille
   * @return une liste de points correspondants au sommet du triangle concerné
   */
  public DirectPositionList getTriangle(int indexX, int indexY, boolean inferior) {

    // Si les coordonnées ne sont pas dans le MNT, on renvoie null
    if (indexX >= (this.nX - 1) || indexX < 0 || indexY >= (this.nY - 1)
        || indexY < 0) {

      return null;

    }

    DirectPosition pt12D = new DirectPosition(indexX * stepX + this.xIni,
        indexY * stepY + this.yIni, tabAlt[indexX][indexY]);
    DirectPosition pt22D = new DirectPosition((indexX + this.echantillonage) * stepX + this.xIni,
        indexY * stepY + this.yIni, tabAlt[indexX + 1][indexY]);
    DirectPosition pt32D = new DirectPosition(indexX * stepX + this.xIni,
        (indexY + this.echantillonage) * stepY + this.yIni, tabAlt[indexX][indexY + 1]);
    DirectPosition pt42D = new DirectPosition((indexX + this.echantillonage) * stepX + this.xIni,
        (indexY + this.echantillonage) * stepY + this.yIni, tabAlt[indexX + 1][indexY + 1]);

    DirectPositionList dpl = new DirectPositionList();

    if (inferior) {

      dpl.add(pt12D);
      dpl.add(pt22D);
      dpl.add(pt32D);

    } else {

      dpl.add(pt42D);
      dpl.add(pt22D);
      dpl.add(pt32D);
    }

    return dpl;
  }

  /**
   * Cette fonction permet de projeter un point sur un MNT en lui ajoutant un
   * altitude offsetting
   * 
   * @param x ,y le point à projeter
   * @param offsetting l'altitude que l'on rajoute au point final
   * @return un point 3D ayant comme altitude Z du MNT + offesting
   */
  @Override
  public Coordinate castCoordinate(double x, double y) {

    // Etant donne que l'on a translaté le MNT
    // On fait de meme avec le vecteur

    // On recupere la maille dans laquelle se trouve le point
    int posx = (int) ((x - this.xIni) / (this.stepX * this.stepX));
    int posy = (int) ((y - this.yIni) / (this.stepY * this.stepY));

    if (posx >= (this.nX) || posx < 0 || posy >= (this.nY) || posy < 0) {

      return new Coordinate(x, y, 0);

    } else if (posx == this.nX - 1) {

      return new Coordinate(x, y, 0);

    } else if (posy == this.nY - 1) {

      return new Coordinate(x, y, 0);
    } else {

      DirectPosition pt12D = new DirectPosition(posx * stepX + this.xIni, posy
          * stepY + this.yIni, tabAlt[posx][posy]);
      DirectPosition pt22D = new DirectPosition((posx + this.echantillonage) * stepX + this.xIni,
          posy * stepY + this.yIni, tabAlt[posx + 1][posy]);
      DirectPosition pt32D = new DirectPosition(posx * stepX + this.xIni,
          (posy +this.echantillonage) * stepY + this.yIni, tabAlt[posx][posy + 1]);
      DirectPosition pt42D = new DirectPosition((posx + this.echantillonage) * stepX + this.xIni,
          (posy + this.echantillonage) * stepY + this.yIni, tabAlt[posx + 1][posy + 1]);

      DirectPosition ptloc2D = new DirectPosition(x, y);

      double d1 = ptloc2D.distance(pt12D);
      double d2 = ptloc2D.distance(pt22D);

      double d3 = ptloc2D.distance(pt32D);

      double d4 = ptloc2D.distance(pt42D);

      double zMoy = 0;
      if (d1 < 5) {

        zMoy = pt12D.getZ();
        return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
      } else if (d2 < 5) {
        zMoy = pt22D.getZ();
        return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
      } else if (d3 < 5) {
        zMoy = pt32D.getZ();
        return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
      } else if (d4 < 5) {
        zMoy = pt42D.getZ();
        return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
      }

      Vecteur v1 = new Vecteur(pt22D, pt32D);
      Vecteur v2 = new Vecteur(pt22D, ptloc2D);

      double d = v1.prodVectoriel(v2).prodScalaire(new Vecteur(0, 0, 1));

      if (d > 0) {// Triangle 1 2 3

        double xn = (pt22D.getY() - pt12D.getY())
            * (pt32D.getZ() - pt12D.getZ()) - (pt32D.getY() - pt12D.getY())
            * (pt22D.getZ() - pt12D.getZ());
        double yn = (pt32D.getX() - pt12D.getX())
            * (pt22D.getZ() - pt12D.getZ()) - (pt22D.getX() - pt12D.getX())
            * (pt32D.getZ() - pt12D.getZ());
        double zn = (pt22D.getX() - pt12D.getX())
            * (pt32D.getY() - pt12D.getY()) - (pt32D.getX() - pt12D.getX())
            * (pt22D.getY() - pt12D.getY());

        zMoy = (pt12D.getX() * xn + pt12D.getY() * yn + pt12D.getZ() * zn - x
            * xn - y * yn)
            / zn;

      } else {// Il se trouve dans le triangle 2 3 4

        double xn = (pt22D.getY() - pt42D.getY())
            * (pt32D.getZ() - pt42D.getZ()) - (pt32D.getY() - pt42D.getY())
            * (pt22D.getZ() - pt42D.getZ());
        double yn = (pt32D.getX() - pt42D.getX())
            * (pt22D.getZ() - pt42D.getZ()) - (pt22D.getX() - pt42D.getX())
            * (pt32D.getZ() - pt42D.getZ());
        double zn = (pt22D.getX() - pt42D.getX())
            * (pt32D.getY() - pt42D.getY()) - (pt32D.getX() - pt42D.getX())
            * (pt22D.getY() - pt42D.getY());

        zMoy = (pt42D.getX() * xn + pt42D.getY() * yn + pt42D.getZ() * zn - x
            * xn - y * yn)
            / zn;

      }

      return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);

    }

  }


  public Box3D get3DEnvelope() {
    if (this.emprise == null) {

      return new Box3D(this.xIni, this.yIni, this.zMin, this.xIni + this.nX
          * this.stepX, this.yIni + this.nY * this.stepY, this.zMax);
    }
    return this.emprise;
  }

  @Override
  public GM_Object getGeometryAt(double x, double y) {
    int posx = (int) ((x - this.xIni) / (this.stepX * this.echantillonage));
    int posy = (int) ((y - this.yIni) / (this.stepY * this.echantillonage));

    DirectPositionList lTri = this.getTriangle(posx, posy, true);
    GM_Triangle tri = new GM_Triangle(new GM_LineString(lTri));

    if (tri.contains(new GM_Point(new DirectPosition(x, y)))) {
      return tri;
    }
    lTri = this.getTriangle(posx, posy, false);
    return new GM_Triangle(new GM_LineString(lTri));

  }



}
