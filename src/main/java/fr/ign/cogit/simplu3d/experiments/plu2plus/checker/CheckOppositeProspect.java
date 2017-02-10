package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.experiments.plu2plus.context.SimulationcheckerContext;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class CheckOppositeProspect implements IRuleChecker {

	private double slope;
	private double hIni;
	private IMultiCurve<IOrientableCurve> ims;
	private Geometry imsJTS;

	public CheckOppositeProspect(double slope, double hIni, BasicPropertyUnit bPU) {
		super();
		this.slope = slope;
		this.hIni = hIni;
		List<ParcelBoundary> lPB = bPU.getCadastralParcels().get(0).getBoundariesByType(ParcelBoundaryType.ROAD);

		ims = new GM_MultiCurve<>();
		for (ParcelBoundary pB : lPB) {
			
			ParcelBoundary oppositeBoundary = pB.getOppositeBoundary();
			
			if(oppositeBoundary == null) continue;
			
			ims.addAll(FromGeomToLineString.convert(oppositeBoundary.getGeom()));
		}
		
		if(! ims.isEmpty()){
			try {
				imsJTS = JtsGeOxygene.makeJtsGeom(ims);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		
		
		List<AbstractBuilding> lBuildings = new ArrayList<>();

		if (context instanceof SimulationcheckerContext) {

			if (((SimulationcheckerContext) context).getNewCuboid() == null) {
				return new ArrayList<>();
			}

	
			lBuildings.add(((SimulationcheckerContext) context).getNewCuboid());

		} else {
			lBuildings.addAll(bPU.getBuildings());
		}

		return checkOppositeProspect(bPU, lBuildings, context);
		
	}
	
	
	public List<UnrespectedRule> checkOppositeProspect(BasicPropertyUnit bPU, List<AbstractBuilding> lBuildings, RuleContext context){

		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();

	
		if (lBuildings.isEmpty()) {
			return lUNR;
		}
		

		if (ims.isEmpty()) {
			return lUNR;
		}

		for (AbstractBuilding b : lBuildings) {

			boolean bool;
			
			if(b instanceof Cuboid){
				
				bool = ((Cuboid) b).prospectJTS(imsJTS, slope, hIni);
			}
			else{
				bool = b.prospect(ims, slope, hIni);
			}
			
			

			if (!bool && context.isStopOnFailure()) {
				lUNR.add(null);
				return lUNR;

			}

			if (!bool) {
				lUNR.add(new UnrespectedRule("Prospect non respecté", b.getGeom(), "Prospect"));
			}

		}

		return lUNR;
	}

}
