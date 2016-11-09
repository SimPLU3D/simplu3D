package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.IRuleChecker;
import fr.ign.cogit.simplu3d.checker.RuleContext;
import fr.ign.cogit.simplu3d.checker.UnrespectedRule;
import fr.ign.cogit.simplu3d.experiments.plu2plus.context.SimulationcheckerContext;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;

public class CheckHeight implements IRuleChecker {

	private double heightMax;

	public CheckHeight(double heightMax) {
		super();
		this.heightMax = heightMax;
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {

		List<AbstractBuilding> lBuildings = new ArrayList<>();

		if (context instanceof SimulationcheckerContext) {

			if (((SimulationcheckerContext) context).getNewCuboid() == null) {
				return new ArrayList<>();
			}

			lBuildings.addAll(((SimulationcheckerContext) context).getExistingCuboid());


		} else {
			lBuildings.addAll(bPU.getBuildings());
		}

		return checkHeight(bPU, lBuildings, context);

	}

	public List<UnrespectedRule> checkHeight(BasicPropertyUnit bPU, List<AbstractBuilding> lBuildings,
			RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<UnrespectedRule>();

		if (lBuildings.isEmpty()) {
			return lUNR;
		}

		for (AbstractBuilding b : lBuildings) {

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
