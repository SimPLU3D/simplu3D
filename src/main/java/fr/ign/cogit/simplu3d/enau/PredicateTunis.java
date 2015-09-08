package fr.ign.cogit.simplu3d.enau;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.application.CadastralParcel;
import fr.ign.cogit.simplu3d.model.application.SpecificCadastralBoundary;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.0
 **/
public class PredicateTunis<O extends Cuboid, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements ConfigurationModificationPredicate<C, M> {

	Geometry jtsCurveLimiteSepParcel = null;
	Geometry jtsCurveLimiteFrontParcel = null;

	// C1
	private double distReculVoirie = 2;

	// C2
	private double slope = 1;
	private double hIni = 45;

	// C3
	private double hMax = 17;

	// C4
	private double distReculLimi = 5.4;
	private double slopeProspect = 2;

	// C7
	private double maximalCES = 0.5;

	private BasicPropertyUnit bPU;

	GeometryFactory gf = new GeometryFactory();

	public PredicateTunis(double distReculVoirie, double slope, double hIni,
			double hMax, double distReculLimi, double slopeProspect,
			double maximalCES, BasicPropertyUnit bPU) {
		super();
		this.distReculVoirie = distReculVoirie;
		this.slope = slope;
		this.hIni = hIni;
		this.hMax = hMax;
		this.distReculLimi = distReculLimi;
		this.slopeProspect = slopeProspect;
		this.maximalCES = maximalCES;
		this.bPU = bPU;
		init();
	}

	private void init() {

		List<IOrientableCurve> lCurveVoirie = new ArrayList<>();

		List<IOrientableCurve> lCurveLatBot = new ArrayList<>();

		for (CadastralParcel cP : bPU.getCadastralParcel()) {
			// for (SubParcel sB : cP.getSubParcel()) {

			for (SpecificCadastralBoundary sCB : cP
					.getSpecificCadastralBoundary()) {

				if (sCB.getType() == SpecificCadastralBoundary.ROAD) {

					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve) {

						lCurveVoirie.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
					}

				} else if (sCB.getType() != SpecificCadastralBoundary.INTRA) {
					IGeometry geom = sCB.getGeom();

					if (geom instanceof IOrientableCurve) {

						lCurveLatBot.add((IOrientableCurve) geom);

					} else {
						System.out
								.println("Classe UB14PredicateFull : quelque chose n'est pas un ICurve");
					}

				}

				// }

			}

		}

		// System.out.println("NB voirie : " + lCurveVoirie.size());

		// System.out.println("NB other : " + lCurveLatBot.size());

		System.out.println(lCurveVoirie.size());

		try {
			if (!lCurveVoirie.isEmpty()) {
				this.jtsCurveLimiteFrontParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveVoirie));
			}

			if (!lCurveLatBot.isEmpty()) {
				this.jtsCurveLimiteSepParcel = AdapterFactory.toGeometry(gf,
						new GM_MultiCurve<>(lCurveLatBot));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean check(C c, M m) {
		O birth = null;

		//On récupère le nouvel objet s'il existe
		if (!m.getBirth().isEmpty()) {
			birth = m.getBirth().get(0);
		}
		

		if (birth != null) {
			///////////// Contrainte C1
			if(birth.toGeometry().distance(this.jtsCurveLimiteFrontParcel) < this.distReculVoirie){
				return false;
			}
			
			///////////// Contrainte C2
			if(! birth.prospectJTS(this.jtsCurveLimiteFrontParcel,this.slope, this.hIni)){
				return false;
			}
			
			/////// Contrainte C4
			if(birth.toGeometry().distance(this.jtsCurveLimiteSepParcel) < this.distReculLimi){
				return false;
			}
			
			if(! birth.prospectJTS(this.jtsCurveLimiteSepParcel,this.slopeProspect, 0)){
				return false;
			}
			
		}
		

	//	if(true) return true;
		
		
		//On récupère l'objet mort s'il existe
		O batDeath = null;

		if (!m.getDeath().isEmpty()) {

			batDeath = m.getDeath().get(0);

		}

		//On récupère la liste des bâtiments actuels
		List<O> lBatIni = new ArrayList<>();
		Iterator<O> iTBat = c.iterator();

		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			if (batTemp == batDeath) {
				continue;
			}

			lBatIni.add(batTemp);

		}

		
		if (birth != null) {
			///////////// Contrainte C7
			
			lBatIni.add(birth);
			//On vérifie le CES sur les bâtiments actuels
			if (!respectBuildArea(lBatIni)) {
				return false;
			}

			
			////Contrainte C4 pour un groupe la distance doit être inférieure à 7,4 m
			List<List<O>> listGroup = createGroupe(lBatIni);
			
			bouclegroupe : for(List<O> l : listGroup){
				
				for(O bat : l){
					
					if(bat.toGeometry().distance(jtsCurveLimiteSepParcel) < 7.4){
						continue bouclegroupe;
					}
					
					
				}
				
				return false;
				
			}
			
			
			
		}

		return true;
	}
	
	
	  private List<List<O>> createGroupe(List<O> lBatParam) {

		    List<O> lBatIn = new ArrayList<>();
		    lBatIn.addAll(lBatParam);

		    List<List<O>> listGroup = new ArrayList<>();

		    while (!lBatIn.isEmpty()) {

		      O batIni = lBatIn.remove(0);

		      List<O> currentGroup = new ArrayList<>();
		      currentGroup.add(batIni);

		      int nbElem = lBatIn.size();

		      bouclei: for (int i = 0; i < nbElem; i++) {

		        for (O batTemp : currentGroup) {

		          if (lBatIn.get(i).getFootprint().distance(batTemp.getFootprint()) < 0.5) {

		            currentGroup.add(lBatIn.get(i));
		            lBatIn.remove(i);
		            i = -1;
		            nbElem--;
		            continue bouclei;

		          }
		        }

		      }

		      listGroup.add(currentGroup);
		    }

		    return listGroup;
		  }

	private boolean respectBuildArea(List<O> lBatIni) {

		if (lBatIni.isEmpty()) {
			return true;
		}

		int nbElem = lBatIni.size();

		Geometry geom = lBatIni.get(0).toGeometry();

		for (int i = 1; i < nbElem; i++) {

			geom = geom.union(lBatIni.get(i).toGeometry());

		}

		double airePAr = this.bPU.getCadastralParcel().get(0).getArea();

		return ((geom.getArea() / airePAr) <= this.maximalCES);
	}

}