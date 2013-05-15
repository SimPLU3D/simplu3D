package fr.ign.cogit.simplu3d.generation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.sig3d.semantic.DTM;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.algo.JtsUtil;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.calculation.StoreyCalculation;
import fr.ign.cogit.simplu3d.generation.emprise.GenerateEmprise;
import fr.ign.cogit.simplu3d.generation.facade.GenerationFacade;
import fr.ign.cogit.simplu3d.generation.toit.GenerationToit;
import fr.ign.cogit.simplu3d.model.application.Batiment;
import fr.ign.cogit.simplu3d.model.application.EmpriseBatiment;
import fr.ign.cogit.simplu3d.model.application.Facade;
import fr.ign.cogit.simplu3d.model.application.Materiau;

public class BatimentProcedural extends Batiment {

  TopologieBatiment tB;
  double largeur;
  double hauteur;
  double largeur2;
  double hauteur2;
  double zGouttiere;
  double zMax;
  Materiau materiauToit;
  List<Materiau> materiauFacades;
  boolean[] facadesNonAveugles;
  IDirectPosition centre;
  double angleToit;

  private DTM dtm = null;

  public BatimentProcedural(TopologieBatiment tB, double largeur,
      double hauteur, double largeur2, double hauteur2, double zGouttiere,
      double zMax, Materiau materiauToit, List<Materiau> materiauFacades,
      boolean[] facadesNonAveugles, IDirectPosition centre, double angle, double angleToit) {
    this(tB, largeur, hauteur, largeur2, hauteur2, zGouttiere, zMax,
        materiauToit, materiauFacades, facadesNonAveugles, centre, angle, null,angleToit);

  }

  public BatimentProcedural(TopologieBatiment tB, double largeur,
      double hauteur, double largeur2, double hauteur2, double zGouttiere,
      double zMax, Materiau materiauToit, List<Materiau> materiauFacades,
      boolean[] facadesNonAveugles, IDirectPosition centre, double angle,
      DTM mnt,  double angleToit) {
    super();
    this.tB = tB;
    this.largeur = largeur;
    this.hauteur = hauteur;
    this.largeur2 = largeur2;
    this.hauteur2 = hauteur2;
    this.zGouttiere = zGouttiere;
    this.zMax = zMax;
    this.materiauToit = materiauToit;
    this.materiauFacades = materiauFacades;
    this.facadesNonAveugles = facadesNonAveugles;
    this.centre = centre;
    this.angle = angle;
    this.dtm = mnt;
    this.angleToit = angleToit;

  }

  public boolean generationEmprise() {

    // 1 on génère l'emprise:
    IPolygon emprise = GenerateEmprise.generateEmprise(tB.getfE(), largeur,
        hauteur, largeur2, hauteur2);

    // 2 on tourne l'emprise
    try {
      emprise = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(JtsUtil.rotation(
          (Polygon) JtsGeOxygene.makeJtsGeom(emprise), angle));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // 3 on translate l'emprise
    emprise = (IPolygon) emprise.translate(centre.getX(), centre.getY(), 0);

    // 4 On affecte l'emprise au bâtiment
    EmpriseBatiment empriseBat = new EmpriseBatiment();
    empriseBat.setGeom(emprise);

    IMultiSurface<IOrientableSurface> mS = new GM_MultiSurface<IOrientableSurface>();
    mS.add(emprise);

    empriseBat.setLod2MultiSurface(mS);
    this.setEmprise(empriseBat);

    // 5 si il y a un MNT, on détermine le Z
    if (dtm != null) {
      double z = determineZ();
      this.centre.setZ(z);
      emprise = (IPolygon) emprise.translate(0, 0, z);
    }

    return true;

  }

  public double determineZ() {

    double z = this.centre.getZ();

    try {
      IGeometry geom = dtm.mapGeom(this.getEmprise().getGeom(), 0, true, true);
      Box3D b = new Box3D(geom);
      z = b.getURDP().getZ();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return z;

  }

  public boolean generationToit() {

    // On génère le toit et les façades
    ToitProcedural t = GenerationToit.generationToit(tB, this.centre.getZ()
        + zGouttiere, this.centre.getZ() + zMax, materiauToit,
        this.getEmprise(),angleToit);

    this.setToit(t);

    return true;
  }

  public boolean generationFacade() {

    List<FacadeProcedural> lF = GenerationFacade.generate(this.getToit(),
        materiauFacades, this.centre.getZ(), facadesNonAveugles);

    IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<IOrientableSurface>();
    iMS.addAll(this.getToit().getLod2MultiSurface());

    for (Facade f : lF) {
      iMS.addAll(f.getLod2MultiSurface());
    }

    this.setFacade(lF);
    this.setGeom(iMS);
    this.setLod2MultiSurface(iMS);

    return true;

  }

  double angle;

  public TopologieBatiment gettB() {
    return tB;
  }

  public double getLargeur() {
    return largeur;
  }

  public double getHauteur() {
    return hauteur;
  }

  public double getLargeur2() {
    return largeur2;
  }

  public double getHauteur2() {
    return hauteur2;
  }

  public double getzGouttiere() {
    return zGouttiere;
  }

  public double getzMax() {
    return zMax;
  }

  public Materiau getMateriauToit() {
    return materiauToit;
  }

  public List<Materiau> getMateriauFacades() {
    return materiauFacades;
  }

  public boolean[] getFacadesNonAveugles() {
    return facadesNonAveugles;
  }

  public IDirectPosition getCentre() {
    return centre;
  }

  public double getAngle() {
    return angle;
  }

  public Set<IDirectPosition> coord() {
    Set<IDirectPosition> set = new HashSet<IDirectPosition>();
    set.addAll(this.getGeom().coord());
    set.addAll(this.getToit().getFaitage().coord());
    set.addAll(this.getToit().getGouttiere().coord());
    set.addAll(this.getEmprise().getGeom().coord());

    return set;

  }

  public void translate(double x, double y) {
    // / System.out.println("Ca translate en tique");

    Set<IDirectPosition> set = null;
    
    if (x != 0 || y != 0) {

      
      set = this.coord();

      for (IDirectPosition dp : set) {

        dp.move(x, y, 0);

      }
      
      this.centre.move(x, y);

    }

    if (dtm != null) {
      


      double oldZ = this.centre.getZ();

      double newZ = determineZ();
      
      System.out.println("Old Z " + oldZ + "  newZ  " + newZ);

      if (newZ != oldZ) {
        
        if(set == null){
          set = this.coord();
        }
        
        this.centre.setZ(newZ);
        for (IDirectPosition dp : set) {

          dp.move(0,0, newZ - oldZ);

        }
        



      }

    }


  }

  /**
   * Angle en d°
   * @param d
   */
  public void rotate(double d) {

    Set<IDirectPosition> set = this.coord();

    double angleRad = Math.PI * d / 180;
    for (IDirectPosition dp : set) {

      Vecteur v = new Vecteur(centre, dp);

      double x = v.getX() * Math.cos(angleRad) - v.getY() * Math.sin(angleRad);
      double y = v.getX() * Math.sin(angleRad) + v.getY() * Math.cos(angleRad);

      dp.setX(centre.getX() + x);
      dp.setY(centre.getY() + y);

    }

    if (dtm != null) {

      double oldZ = this.centre.getZ();

      double newZ = determineZ();

      if (newZ != oldZ) {

        this.translate(0, 0);

      }

    }

    this.angle = angle + angleRad;
  }

  public void changeFormeToit(TopologieBatiment.FormeToitEnum e) {

    this.tB.setfT(e);

    if (this.tB.getlIndArret().size() == 0) {
      this.tB.getlIndArret().add(1);
    }

    this.generationToit();
    this.generationFacade();

  }

  public void changeFormeEmprise(TopologieBatiment.FormeEmpriseEnum e) {

    this.tB.setfE(e);
    this.generationEmprise();
    this.generationToit();
    this.generationFacade();

  }

  public void moveGouttiere(double zGouttiere) {
    this.zGouttiere = zGouttiere;
    this.generationToit();
    this.generationFacade();

  }

  public void moveZMax(double zMax) {
    this.zMax = zMax;
    this.generationToit();
    this.generationFacade();

  }

  public void changeHauteur(double d) {
    this.hauteur = d;
    this.generationEmprise();
    this.generationToit();
    this.generationFacade();
  }

  public void changeLargeur(double d) {
    this.largeur = d;
    this.generationEmprise();
    this.generationToit();
    this.generationFacade();

  }

  public void changeHauteur2(double d) {
    this.hauteur2 = d;
    this.generationEmprise();
    this.generationToit();
    this.generationFacade();
  }

  public void changeLargeur2(double d) {
    this.largeur2 = d;
    this.generationEmprise();
    this.generationToit();
    this.generationFacade();
  }

  public void changeTextureToit(Materiau materiau) {
    this.getToit().setMat(materiau);
    this.materiauToit = materiau;
  }

  public void changeTextureFacade(Materiau materiau, int ind) {
    int nbFacade = this.getFacade().size();
    if (nbFacade > ind) {
      this.getFacade().get(ind).setMat(materiau);

      int nbText = this.materiauFacades.size();

      if (nbText < nbFacade) {

        while (this.materiauFacades.size() < nbFacade) {
          this.materiauFacades.add(this.materiauFacades.get(nbText - 1));
        }

      }

      this.materiauFacades.set(ind, materiau);

    }

  }

  public void changePignon(List<Integer> arrayList) {

    this.tB.setlIndArret(arrayList);
    this.generationToit();
    this.generationFacade();

  }

  public double getAngleToit() {
       return this.angleToit;
  }

  public void changeAngleToit(double d) {
    this.angleToit = d;
    
    System.out.println(d);
    
    this.generationToit();
    this.generationFacade();

  }
  
  
  @Override
  public int getStoreysAboveGround() {
      return StoreyCalculation.process(this);

  }

}
