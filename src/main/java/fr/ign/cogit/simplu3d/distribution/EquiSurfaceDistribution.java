package fr.ign.cogit.simplu3d.distribution;

import fr.ign.common.conversion.FromGeomToSurface;
import fr.ign.common.conversion.FromPolygonToTriangle;
import fr.ign.common.topology.Triangle;
import fr.ign.common.util.Util;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * This software is released under the licence CeCILL
 *
 * see LICENSE.TXT
 *
 * @see <a href="http://www.cecill.info/">http://www.cecill.info/</a>
 *
 *
 *

 *
 * @author Brasebin Mickaël
 *
 * @version 1.6
 *
 * @author MBrasebin
 *
 *         Classe permettant de tirer aléatoirement des points répartis
 *         équitablement sur un objet de dimension 2 ou 3 Le principe est basé
 *         sur Shape Distributions Osada and al[2002] Shape Distributions ROBERT
 *         OSADA, THOMAS FUNKHOUSER, BERNARD CHAZELLE, and DAVID DOBKIN
 *         Princeton University Class wich calculate the dissimilarity measure
 *         between 2 solids
 *
 *
 *         Class to generate random points distrubition on an object with a
 *         dimension superior than 2. The principle is based on : Shape
 *         Distributions Osada and al[2002] Shape Distributions ROBERT OSADA,
 *         THOMAS FUNKHOUSER, BERNARD CHAZELLE, and DAVID DOBKIN Princeton
 *         University Class wich calculate the dissimilarity measure between 2
 *         solids
 *
 */
public class EquiSurfaceDistribution {

    private List<Geometry> lTriangles = new ArrayList<Geometry>();

    // Il s'agit des aires cumulés des différents triangles
    // airesCumulees[i] = somme des ieme premiers triangles
    // airesCumulees[n-1] = aire de la surface enblobant le corps
    private double[] accumulatedArea;

    private static GeometryFactory gf = new GeometryFactory();

    public EquiSurfaceDistribution(Geometry geom) throws Exception {
        List<Polygon> lTriangles = FromGeomToSurface.convertGeom(geom);
        lTriangles = initTri(lTriangles);
        initSamp(lTriangles);
        this.lTriangles.addAll(lTriangles);
    }

    public boolean isValid() {
        return !lTriangles.isEmpty();
    }

    // Initialisation avant le calcul de la fonction de caractérisation
    private List<Polygon> initTri(List<Polygon> lTriangles) {
        boolean b = Util.containOnlyTriangleFaces(lTriangles);
        if (!b) {
            List<Triangle> lTrianglesTemp = FromPolygonToTriangle.convertAndTriangle(lTriangles);
            lTriangles = new ArrayList<>();
            lTriangles.addAll(lTrianglesTemp.stream().map(Triangle::getPolygon).toList());//FIXME
        }
        return lTriangles;
    }

    private void initSamp(List<Polygon> lTriangles) {
        int nbTriangles = lTriangles.size();
        // calcul des aires cumulées qui permettront de choisir les points
        // aléatoires
        accumulatedArea = new double[nbTriangles];
        for (int i = 0; i < lTriangles.size(); i++) {
            List<Coordinate> lDP = Arrays.asList(lTriangles.get(i).getCoordinates());
            Vecteur v1 = new Vecteur(lDP.get(0), lDP.get(1));
            Vecteur v2 = new Vecteur(lDP.get(0), lDP.get(2));
            Vecteur v3 = v1.prodVectoriel(v2);
            double aire = v3.norme() * 0.5;
            // Nous avons 2 points à tester, nous pouvons calculer la longeur
            if (i != 0) {
                this.accumulatedArea[i] = aire + this.accumulatedArea[i - 1];
                continue;
            }
            this.accumulatedArea[i] = aire;
        }
    }

    /**
     * Fonction permettant de tirer aléatoirement un triangle sur une surface
     * triangulée en prenant compte de l'aire
     *
     * @return
     */

    public Geometry randomTriangle(double rand) {

        // Tout d'abord on choisit aléatoirement un triangle
        // Sur la surface du volume (aléatoirement pondéré par l'aire des
        // triangles)
        int nbEleme = this.accumulatedArea.length - 1;
        double max = this.accumulatedArea[nbEleme];

        double alea = max * rand;

        int i = 0;
        // On choisit l'indice correspondant
        while (this.accumulatedArea[i] < alea) {
            i++;

        }

        double valArInf = i == 0 ? 0 : accumulatedArea[i - 1];

        corrected_rand = (alea - valArInf) / (this.accumulatedArea[i] - valArInf);

        // On retourne la valeur correspondante à cet indice
        Geometry surf = this.lTriangles.get(i);

        return surf;

    }

    private double corrected_rand = -1;

    public double getCorrectedRand() {
        return corrected_rand;
    }

    /**
     * Fonction permettant de retourner un point tiré aléatoirement sur la
     * surface d'un triangle
     *
     * @param triangle
     * @return un point tiré aléatoirement sur le triangle
     */
    private Coordinate randomPointOnTriangles(Geometry triangle, double aleaX, double aleaY) {
        Coordinate[] coord = triangle.getCoordinates();

        Coordinate p1 = coord[0];
        Coordinate p2 = coord[1];
        Coordinate p3 = coord[2];
        // Cette méthode est conseillée dans l'article dont est issue la mesure
        double sqrtAleaX = Math.sqrt(aleaX);
        Coordinate pointFinal = new Coordinate(
                p1.x * (1 - sqrtAleaX) + p2.x * sqrtAleaX * (1 - aleaY) + p3.x * aleaY * sqrtAleaX,
                p1.y * (1 - sqrtAleaX) + p2.y * sqrtAleaX * (1 - aleaY) + p3.y * aleaY * sqrtAleaX,
                p1.z * (1 - sqrtAleaX) + p2.z * sqrtAleaX * (1 - aleaY) + p3.z * aleaY * sqrtAleaX);
        return pointFinal;
    }

    public Coordinate sample() {
        return sample(Math.random(), Math.random());
    }

    public Coordinate sample(double rand1, double rand2) {
        Geometry sur = randomTriangle(rand1);
        return randomPointOnTriangles(sur, corrected_rand, rand2);
    }

    public Coordinate inversample(double x, double y) {
        for (Geometry t : lTriangles) {
            Coordinate[] coord = t.getCoordinates();
            Coordinate dp = getInverse(coord, x, y);
            if (dp.getX() <= 1 && dp.getX() >= 0 && dp.getY() <= 1 && dp.getY() >= 0) {
                return dp;
            }
        }
        return null;
    }

    private Coordinate getInverse(Coordinate[] coord, double x, double y) {
        Coordinate p1 = coord[0];
        Coordinate p2 = coord[1];
        Coordinate p3 = coord[2];
        double E0 = p2.x - p1.x;
        double E1 = p2.y - p1.y;
        double F0 = p3.x - p2.x;
        double F1 = p3.y - p2.y;
        double val2 = (E1 * (x - p1.x) - E0 * (y - p1.y)) / (F0 * E1 - F1 * E0);
        double val1 = (y - p1.y - val2 * F1) / E1;
        double eta1 = val1 * val1;
        double eta2 = val2 / val1;
        return new Coordinate(eta1, eta2);
    }
}
