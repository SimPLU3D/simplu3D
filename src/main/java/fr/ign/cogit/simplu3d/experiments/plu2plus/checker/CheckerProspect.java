package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.experiments.plu2plus.context.SimulationcheckerContext;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

public class CheckerProspect implements IRuleChecker {

	private double slope;
	private double hIni;
	private IMultiCurve<IOrientableCurve> ims;

	public CheckerProspect(double slope, double hIni, List<ParcelBoundaryType> lTypes, BasicPropertyUnit bPU) {
		super();
		this.slope = slope;
		this.hIni = hIni;
		List<ParcelBoundary> lPB = bPU.getCadastralParcels().get(0).getBoundariesByTypes(lTypes);

		ims = new GM_MultiCurve<>();
		for (ParcelBoundary pB : lPB) {
			ims.addAll(FromGeomToLineString.convert(pB.getGeom()));
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

		return checkProspect(bPU, lBuildings, context);
	}

	public List<UnrespectedRule> checkProspect(BasicPropertyUnit bPU, List<AbstractBuilding> buildings,
			RuleContext context) {

		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();
		
		if(ims.isEmpty()){
			return lUNR;
		}

	
		if (buildings.isEmpty()) {
			return lUNR;
		}

		for (AbstractBuilding b : buildings) {

			boolean bool = b.prospect(ims, slope, hIni);

			if (!bool & context.isStopOnFailure()) {
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
