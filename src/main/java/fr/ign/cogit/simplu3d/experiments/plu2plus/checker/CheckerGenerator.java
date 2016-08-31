package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import fr.ign.cogit.simplu3d.checker.CompositeChecker;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.UrbaZone;

public class CheckerGenerator {

	public static CompositeChecker generate(BasicPropertyUnit bPU) {
		CompositeChecker check = new CompositeChecker();
		check.setStopOnFailure(true);

		UrbaZone zone = bPU.getCadastralParcels().get(0).getSubParcels().get(0).getUrbaZone();

		double cesMax = determineMaxCES(zone);
		check.addChild(new CheckCES(cesMax, bPU));

		return check;
	}

	public static double determineMaxCES(UrbaZone uZ) {

		switch (uZ.getLibelle()) {
		case "UBe":
		case "UBf":
		case "UEb":
		case "UEb1":
			return 0.5;
		case "UBe1":
			return 0.6;
		case "UBg":
		case "UBg1":
			return 1;

		}
		System.out.println("ERROR ZONE NOT FOUND");
		return 0;

	}

}
