package fr.ign.cogit.simplu3d.experiments.herault.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

public class PredicateHerault<O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
        implements ConfigurationModificationPredicate<C, M> {

    protected BasicPropertyUnit currentBPU;
    Geometry jtsCurveLimiteFondParcel = null;
    Geometry jtsCurveLimiteFrontParcel = null;
    Geometry jtsCurveLimiteLatParcel = null;
    IFeatureCollection<Prescription> forbiddenZones;

    private int nbMaxBox;
    
    double art_6, art_7_1, art_7_2, art_8, art_9, art_10;
    
    
    

    @Override
    public String toString() {
        return "PredicateHerault [art_6=" + art_6 + ", art_7_1=" + art_7_1
                + ", art_7_2=" + art_7_2 + ", art_8=" + art_8 + ", art_9="
                + art_9 + ", art_10=" + art_10 + "]";
    }

    public PredicateHerault(BasicPropertyUnit bPU,
            IFeatureCollection<Prescription> forbiddenZones, IFeature zoneplu, int nbMaxBox)
            throws Exception {
        super();
        this.currentBPU = bPU;
        this.nbMaxBox = nbMaxBox;
        this.forbiddenZones = forbiddenZones;

        // On lit toutes les informations utiles pour vérifier les règles
        art_6 = Double.parseDouble(zoneplu.getAttribute("ART_6").toString());
        art_7_1 = Double
                .parseDouble(zoneplu.getAttribute("ART_7_1").toString());
        art_7_2 = Double
                .parseDouble(zoneplu.getAttribute("ART_7_2").toString());
        art_8 = Double.parseDouble(zoneplu.getAttribute("ART_8").toString());
        art_9 = Double.parseDouble(zoneplu.getAttribute("ART_9").toString());
        art_10 = Double.parseDouble(zoneplu.getAttribute("ART_10").toString());

        init();

    }

    /**
     * Initialisation : stocke les limites séparatives dans les géométries jts
     * jtsCurveLimiteSepParcel et jtsCurveLimiteFrontParcel.
     * 
     * @throws Exception
     */
    private void init() throws Exception {
        // Pour simplifier la vérification, on extrait les différentes bordures
        // de
        // parcelles
        IMultiCurve<IOrientableCurve> curveLimiteFondParcel = new GM_MultiCurve<>();
        IMultiCurve<IOrientableCurve> curveLimiteFrontParcel = new GM_MultiCurve<>();
        IMultiCurve<IOrientableCurve> curveLimiteLatParcel = new GM_MultiCurve<>();

        // On parcourt les parcelles du BasicPropertyUnit (un propriétaire peut
        // avoir plusieurs parcelles)
        for (CadastralParcel cP : currentBPU.getCadastralParcels()) {

            // On parcourt les limites séparaticves
            for (ParcelBoundary sCB : cP.getBoundaries()) {

                // En fonction du type on ajoute à telle ou telle géométrie

                // Fond de parcel
                if (sCB.getType() == ParcelBoundaryType.BOT) {

                    IGeometry geom = sCB.getGeom();

                    if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
                        curveLimiteFondParcel.add((IOrientableCurve) geom);

                    } else {
                        System.out.println(
                                "Classe SamplePredicate : quelque chose n'est pas un ICurve : "
                                        + geom.getClass());
                    }

                }

                // Limite latérale
                if (sCB.getType() == ParcelBoundaryType.LAT) {

                    IGeometry geom = sCB.getGeom();

                    if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
                        curveLimiteLatParcel.add((IOrientableCurve) geom);

                    } else {
                        System.out.println(
                                "Classe SamplePredicate : quelque chose n'est pas un ICurve : "
                                        + geom.getClass());
                    }

                }

                // Limite front
                if (sCB.getType() == ParcelBoundaryType.ROAD) {

                    IGeometry geom = sCB.getGeom();

                    if (geom instanceof IOrientableCurve && !geom.isEmpty()) {
                        curveLimiteFrontParcel.add((IOrientableCurve) geom);

                    } else {
                        System.out.println(
                                "Classe SamplePredicate : quelque chose n'est pas un ICurve : "
                                        + geom.getClass());
                    }

                }

            }

        }

        GeometryFactory gf = new GeometryFactory();

        if (!curveLimiteFondParcel.isEmpty()) {
            this.jtsCurveLimiteFondParcel = AdapterFactory.toGeometry(gf,
                    curveLimiteFondParcel);
        }

        if (!curveLimiteFrontParcel.isEmpty()) {
            this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf,
                    curveLimiteFrontParcel);
        }

        if (!curveLimiteLatParcel.isEmpty()) {
            this.jtsCurveLimiteLatParcel = AdapterFactory.toGeometry(gf,
                    curveLimiteLatParcel);
        }

    }

    @Override
    public boolean check(C c, M m) {
        
   
 
        
        // Il s'agit des objets de la classe Cuboid
        List<O> lO = m.getBirth();

        // On vérifie les règles sur tous les pavés droits, dès qu'il y en a un
        // qui
        // ne respecte pas une règle, on rejette
        // On traite les contraintes qui ne concernent que les nouveux bâtiments
        for (O cuboid : lO) {

            // On vérifie la contrainte de recul par rapport au fond de parcelle
            // Existe t il ?
            if (jtsCurveLimiteFondParcel != null) {
                Geometry geom = cuboid.toGeometry();
                if (geom == null) {
                    System.out.println("Nullll");
                }
                // On vérifie la distance (on récupère le foot
                if (this.jtsCurveLimiteFondParcel
                        .distance(geom) < this.art_7_2) {
                    // elle n'est pas respectée, on retourne faux
                    return false;

                }
                
                
                
                if(!  cuboid.prospectJTS(jtsCurveLimiteFondParcel, art_7_1, 0)){
                    return false;
                }
                

            }
            
       
            // On vérifie la contrainte de recul par rapport au front de
            // parcelle
            // (voirie)
            // Existe t il ?
            if (this.jtsCurveLimiteFrontParcel != null) {
                // On vérifie la distance
                if (this.jtsCurveLimiteFrontParcel
                        .distance(cuboid.toGeometry()) < this.art_6) {
                    // elle n'est pas respectée, on retourne faux
                    return false;

                }

            }

            // On vérifie la contrainte de recul par rapport aux bordures de la
            // parcelle
            // Existe t il ?
            if (jtsCurveLimiteLatParcel != null) {
                // On vérifie la distance
                if (this.jtsCurveLimiteLatParcel
                        .distance(cuboid.toGeometry()) < this.art_7_2) {
                    // elle n'est pas respectée, on retourne faux
                    return false;

                }
                
                
                
                if(!  cuboid.prospectJTS(jtsCurveLimiteLatParcel, art_7_1, 0)){
                    return false;
                }

            }

            // La hauteur des cuboides
            if (cuboid.height > art_10) {
                return false;
            }

            
            
            // On vérifie que le cuboid est dans la parcelle
            if (this.forbiddenZones != null) {
                for (IFeature feat : this.forbiddenZones) {
                    if (feat.getGeom().intersects(cuboid.getFootprint())) {
                        return false;
                    }
                }
            }

        }
        
        


        // Pour produire des boîtes séparées et vérifier que la distance inter
        // bâtiment est respectée
        try {
            if (!checkDistanceInterBuildings(c, m)) {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Pour vérifier que le CES (surface bâti) est respecté
        if (!respectMaximalBuiltArea(c, m)) {
            return false;
        }

        // On a réussi tous les tests, on renvoie vrai
        return checkNumberOfBoxes(c,m);

    }
    
    
    private boolean checkNumberOfBoxes(C c, M m){
        // On fait la liste de tous les objets après modification
        List<O> lCuboid = new ArrayList<>();

        // On ajoute tous les nouveaux objets
        lCuboid.addAll(m.getBirth());

        // On récupère la boîte (si elle existe) que l'on supprime lors de la
        // modification
        O cuboidDead = null;

        if (!m.getDeath().isEmpty()) {
            cuboidDead = m.getDeath().get(0);
        }

        // On parcourt les objets existants moins celui qu'on supprime
        Iterator<O> iTBat = c.iterator();
        while (iTBat.hasNext()) {

            O cuboidTemp = iTBat.next();

            // Si c'est une boîte qui est amenée à disparaître après
            // modification,
            // elle n'entre pas en jeu dans les vérifications
            if (cuboidTemp == cuboidDead) {
                continue;
            }

            lCuboid.add(cuboidTemp);

        }

        return lCuboid.size() <= this.nbMaxBox;
    }

    /**
     * Code pour vérifier que la distance entre bâtiments est vérifiée
     * 
     * @param c
     * @param m
     * @return
     * @throws Exception
     */
    private boolean checkDistanceInterBuildings(C c, M m) throws Exception {
        GeometryFactory gf = new GeometryFactory();
        // On récupère les objets ajoutées lors de la proposition
        List<O> lO = m.getBirth();

        // On récupère la boîte (si elle existe) que l'on supprime lors de la
        // modification
        O batDeath = null;

        if (!m.getDeath().isEmpty()) {
            batDeath = m.getDeath().get(0);
        }

        // On regarde la distance entre les bâtiments existants
        // et les boîtes que l'on ajoute
        for (Building b : currentBPU.getBuildings()) {
            for (O ab : lO) {
                Geometry geomBat = AdapterFactory.toGeometry(gf,
                        b.getFootprint());
                if (geomBat.distance(ab.toGeometry()) < art_8) {
                    return false;
                }
            }
        }

        // On parcourt les boîtes existantes dans la configuration courante
        // (avant
        // d'appliquer la modification)
        Iterator<O> iTBat = c.iterator();
        while (iTBat.hasNext()) {

            O batTemp = iTBat.next();

            // Si c'est une boîte qui est amenée à disparaître après
            // modification,
            // elle n'entre pas en jeu dans les vérifications
            if (batTemp == batDeath) {
                continue;
            }

            // On parcourt les boîtes que l'on ajoute
            for (O ab : lO) {

                // On regarde si la distance entre les boîtes qui restent et
                // celles que
                // l'on ajoute
                // respecte la distance entre boîtes
                if (batTemp.toGeometry().distance(ab.toGeometry()) < art_8) {
                    return false;
                }

            }

        }
        return true;

    }

    /**
     * Vérification du nom dépassement du CES
     * 
     * @param c
     * @param m
     * @return
     */
    private boolean respectMaximalBuiltArea(C c, M m) {
        // On fait la liste de tous les objets après modification
        List<O> lCuboid = new ArrayList<>();

        // On ajoute tous les nouveaux objets
        lCuboid.addAll(m.getBirth());

        // On récupère la boîte (si elle existe) que l'on supprime lors de la
        // modification
        O cuboidDead = null;

        if (!m.getDeath().isEmpty()) {
            cuboidDead = m.getDeath().get(0);
        }

        // On parcourt les objets existants moins celui qu'on supprime
        Iterator<O> iTBat = c.iterator();
        while (iTBat.hasNext()) {

            O cuboidTemp = iTBat.next();

            // Si c'est une boîte qui est amenée à disparaître après
            // modification,
            // elle n'entre pas en jeu dans les vérifications
            if (cuboidTemp == cuboidDead) {
                continue;
            }

            lCuboid.add(cuboidTemp);

        }

        // C'est vide la règle est respectée
        if (lCuboid.isEmpty()) {
            return true;
        }

        // On calcule la surface couverte par l'ensemble des cuboid
        int nbElem = lCuboid.size();

        Geometry geom = lCuboid.get(0).toGeometry();

        for (int i = 1; i < nbElem; i++) {

            geom = geom.union(lCuboid.get(i).toGeometry());

        }

        List<AbstractSimpleBuilding> lBatIni = new ArrayList<>();
        for (ISimPLU3DPrimitive s : lCuboid) {
            lBatIni.add((AbstractSimpleBuilding) s);
        }

        /*
         * List<List<AbstractSimpleBuilding>> groupes =
         * CuboidGroupCreation.createGroup(lBatIni, 0.5);
         * 
         * if (groupes.size() > 1) { return false; }
         */

        // On récupère la superficie de la basic propertyUnit
        double airePAr = 0;
        for (CadastralParcel cP : currentBPU.getCadastralParcels()) {
            airePAr = airePAr + cP.getArea();
        }

        return ((geom.getArea() / airePAr) <= art_9);
    }
}