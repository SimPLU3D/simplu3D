package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.ContextRuleCheck;
import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Building;

public class CheckHeight implements IRuleChecker {
	
	
	private double heightMax;
	
	

	public CheckHeight(double heightMax) {
		super();
		this.heightMax = heightMax;
	}



	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, ContextRuleCheck context) {

		
		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();

		List<Building> lBuildings = bPU.getBuildings();

		if (lBuildings.isEmpty()) {
			return lUNR;
		}

		for (Building b : lBuildings) {

			boolean bool = (b.height(0, 1) < heightMax);

			if (!bool & context.isStopOnFailure()) {
				lUNR.add(null);
				return lUNR;

			}

			if (!bool) {
				lUNR.add(new UnrespectedRule("Hauteur non respectée", b.getGeom(), "Hauteur maximale"));
			}

		}

		return lUNR;
		
		
	}
	
	
	

}
