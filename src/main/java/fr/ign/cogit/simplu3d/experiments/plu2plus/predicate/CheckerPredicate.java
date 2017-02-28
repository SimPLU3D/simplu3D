package fr.ign.cogit.simplu3d.experiments.plu2plus.predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.model.CompositeChecker;
import fr.ign.cogit.simplu3d.experiments.plu2plus.context.SimulationcheckerContext;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

public class CheckerPredicate<O extends Cuboid, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		extends CompositeChecker implements ConfigurationModificationPredicate<C, M> {

	private BasicPropertyUnit bPU;
	private CompositeChecker check;
	private SimulationcheckerContext cRc;

	public CheckerPredicate(BasicPropertyUnit bPU, CompositeChecker check, SimulationcheckerContext cRc) {
		super();
		this.bPU = bPU;
		this.check = check;
		this.cRc = cRc;
	}

	@Override
	public boolean check(C arg0, M arg1) {
		
		
		

		List<AbstractBuilding> buildings = new ArrayList<>();

		Iterator<O> iTBat = arg0.iterator();

		while (iTBat.hasNext()) {

			O batTemp = iTBat.next();

			if (! arg1.getDeath().isEmpty() && batTemp == arg1.getDeath().get(0)) {
				continue;
			}

			buildings.add(batTemp);

		}

		Cuboid newCuboid = null;

		if (!arg1.getBirth().isEmpty()) {

			newCuboid = arg1.getBirth().get(0);
		}

		buildings.addAll(bPU.getBuildings());

		cRc.setNewCuboid(newCuboid);
		cRc.setExistingCuboid(buildings);

		

	
		
		boolean checked = check.check(bPU, cRc).isEmpty();
		


		return checked;
	}

}
