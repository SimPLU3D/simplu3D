package fr.ign.cogit.simplu3d.experiments.plu2plus.predicate;

import java.util.List;

import fr.ign.cogit.simplu3d.checker.CompositeChecker;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;

public class CheckerPredicate<O extends Cuboid, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		extends CompositeChecker implements ConfigurationModificationPredicate<C, M> {

	private BasicPropertyUnit bPU;
	private CompositeChecker check;

	public CheckerPredicate(BasicPropertyUnit bPU, CompositeChecker check) {
		super();
		this.bPU = bPU;
		this.check = check;
	}

	@Override
	public boolean check(C arg0, M arg1) {

		List<O> births = arg1.getBirth();
		List<O> deaths = arg1.getDeath();

		
		if (births != null) {
			bPU.getBuildings().addAll(births);

		}

		if (deaths != null) {
			bPU.getBuildings().removeAll(births);

		}

		boolean checked = check.check(bPU).isEmpty();
		
		

		if (births != null) {
			bPU.getBuildings().removeAll(births);

		}

		if (deaths != null) {
			bPU.getBuildings().addAll(births);

		}

		return checked;
	}

}
