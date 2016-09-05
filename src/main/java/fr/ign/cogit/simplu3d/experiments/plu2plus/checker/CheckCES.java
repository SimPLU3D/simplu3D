package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;

public class CheckCES implements IRuleChecker {
	
	public final static String CODE_CES = "CES";

	private double cesMax;
	private BasicPropertyUnit bPU;

	public CheckCES(double cesMax, BasicPropertyUnit bPU) {
		super();
		this.cesMax = cesMax;
		this.bPU = bPU;

	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU,RuleContext checker) {
	

		List<Building> lBuildings = bPU.getBuildings();

		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();
		
		if(lBuildings.isEmpty()) {return lUNR;}

		int nbElem = lBuildings.size();

		IGeometry geom = lBuildings.get(0).getFootprint();

		for (int i = 1; i < nbElem; i++) {

			geom = geom.union(lBuildings.get(i).getFootprint());

		}

		double airePAr = this.bPU.getGeom().area();

		double ces = (geom.area() / airePAr);

		if (ces > cesMax) {
			lUNR.add(new UnrespectedRule("CES dépassé : " + ces + "> " + cesMax, geom, CODE_CES));
		}

		return lUNR;
	}

}
