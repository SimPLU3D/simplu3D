package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.impl.CESChecker;
import fr.ign.cogit.simplu3d.checker.impl.DistanceToLatLimitCheck;
import fr.ign.cogit.simplu3d.checker.impl.HeightMaxChecker;
import fr.ign.cogit.simplu3d.checker.impl.OppositeProspectChecker;
import fr.ign.cogit.simplu3d.checker.impl.ProspectToLatLimitChecker;
import fr.ign.cogit.simplu3d.checker.model.CompositeChecker;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.UrbaZone;
import fr.ign.cogit.simplu3d.model.ZoneRegulation;
import fr.ign.cogit.simplu3d.util.SimpleBandProduction;

public class CheckerGenerator {

	public static CompositeChecker generate(BasicPropertyUnit bPU) {

		UrbaZone zone = bPU.getCadastralParcels().get(0).getSubParcels().get(0).getUrbaZone();

		double dMin = deterMineDM(zone);
		double hMax = deterMinHMax(zone);

		double cesMax = determineMaxCES(zone);

		CompositeChecker check = new CompositeChecker();

		ZoneRegulation r = new ZoneRegulation();

		r.setSlopeOppositeProspect(1.0);
		r.sethIniOppositeProspect(0.0);

		r.setArt72(dMin);
		r.setArt74(1);

		r.setArt102(hMax);

		r.setBand1(200.0);
		r.setArt9(cesMax);

		new SimpleBandProduction(bPU, r, null);

		// Regle 1
		check.addChild(new OppositeProspectChecker(r));

		// Regle 4
		List<ParcelBoundaryType> lPB = new ArrayList<ParcelBoundaryType>();
		lPB.add(ParcelBoundaryType.LAT);
		lPB.add(ParcelBoundaryType.BOT);

		check.addChild(new ProspectToLatLimitChecker(r));

		// Regles 5

		if (dMin > 0) {
			check.addChild(new DistanceToLatLimitCheck(r));
		}

		// Regle 8

		check.addChild(new HeightMaxChecker(r));

		// Regle 7
		check.addChild(new CESChecker(r));

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
		// System.out.println("(1) ERROR ZONE NOT FOUND :" + uZ.getLibelle());
		return 0;

	}

	public static double deterMineDM(UrbaZone uZ) {
		String type = uZ.getLibelle();
		if (type.equals("UBg") || type.equals("UBf") || type.equals("UBg1"))
			return 0;
		return 5;

	}

	public static double deterMinHMax(UrbaZone uZ) {
		String type = uZ.getLibelle();
		if (type.equals("UBe") || type.equals("UBf") || type.equals("UBe1"))
			return 16;
		if (type.equals("UBg") || type.equals("UBg1"))
			return 22;
		if (type.equals("UEb"))
			return 16;
		if (type.equals("UEb1"))
			return 30;

		// System.out.println("(2) ERROR ZONE NOT FOUND :" + type);
		return 0;

	}

}
