package fr.ign.cogit.simplu3d.experiments.openmole.paris.simulation;

import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;


//Règles instanciées :
// Parcelles sans bordure : 
// http://pluenligne.paris.fr/plu/sites-plu/site_statique_38/documents/796_Plan_Local_d_Urbanisme_de_P/802_Reglement/809_Atlas_general___Planches_au/C_D_10-V04.pdf
// Légende : http://www.v1.paris.fr/commun/v2asp/fr/Urbanisme/PLU/Atlas_PLU/LEGAG.PDF
// Donc les règles sont :
// http://pluenligne.paris.fr/plu/sites-plu/site_statique_38/documents/796_Plan_Local_d_Urbanisme_de_P/802_Reglement/805_Tome_1___Figures/C_REG1FIG-V06.pdf
// Hauteur < P + 4 + 3
// Prospect : P + 4 avec pente de 0.5
// Zone verte à protéger dans la zone
//Réglement UG :
// http://pluenligne.paris.fr/plu/sites-plu/site_statique_37/documents/772_Plan_Local_d_Urbanisme_de_P/778_Reglement/780_Tome_1___Reglements_par_zon/C_REG1UG-V13.pdf
// Construction à l'alignement


public class ParisPredicate <O extends AbstractSimpleBuilding, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
implements ConfigurationModificationPredicate<C, M> {
	
	
	
	
	public double p_value = 0;
	IGeometry bandeE;
	
	BasicPropertyUnit bP;
	public ParisPredicate(BasicPropertyUnit bP) {
		super();
		this.bP = bP;
		this.determineP();
		this.generateBandeE();
		System.out.println("P VAlue : " + p_value);
	}
	
	
	

	private void generateBandeE() {
		List<ParcelBoundary> lLimitesFront = this.bP.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD);
		
		IMultiSurface<IOrientableSurface> bandeETemp = new GM_MultiSurface<>();
		
		for(ParcelBoundary limit : lLimitesFront) {
			bandeETemp.addAll(FromGeomToSurface.convertGeom(limit.getGeom().buffer(15)));
		}
		bandeE = bandeETemp.buffer(0.5);
		
	}




	@Override
	public boolean check(C c, M m) {
	    O birth = null;

	    if (!m.getBirth().isEmpty()) {
	      birth = m.getBirth().get(0);
	    }
	    
	    if(birth == null) {
	    	return true;
	    }

	    
	    if(!bandeE.contains(birth.getFootprint())) {
	    	return false;
	    }
	 
	    ////Test de la hauteur
	    if(birth.getHeight() >  this.p_value + 4 + 3) {
	    	return false;
	    }
	    
	    for(ParcelBoundary bP  : this.bP.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD)) {
			if(bP.getGeom() == null) {
				System.out.println(this.bP.getCadastralParcels().get(0).getId());
				continue;
			}
	        if (! birth.prospect(bP.getGeom(), 0.5,  this.p_value + 4)){
	        	return false;
	        }
	    }
	 
	 	    
	    if(! this.bP.getPol2D().contains(birth.getFootprint())) {
	    	return false;
	    }
	   


	    
	    
		return true;
	}

	private void determineP() {
		List<ParcelBoundary> lLimitesFront = this.bP.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD);
		
		double currentP = Double.POSITIVE_INFINITY;
		
		for(ParcelBoundary bP : lLimitesFront) {
			ParcelBoundary bPBoundary = bP.getOppositeBoundary();
			
			if(bPBoundary==null||bPBoundary.getGeom() == null ) {
				System.out.println(this.bP.getCadastralParcels().get(0).getId());
				continue;
			}
			
			currentP = Math.min(bP.getGeom().distance(bPBoundary.getGeom()), currentP);
		}
		
		p_value = currentP;
	}
	
	
	
	
	
	
	

	
	

}
