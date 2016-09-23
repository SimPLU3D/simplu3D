package fr.ign.cogit.simplu3d.experiments.plu2plus.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.checker.CompositeChecker;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.UrbaZone;

public class CheckerGenerator {

	public static CompositeChecker generate(BasicPropertyUnit bPU) {
		CompositeChecker check = new CompositeChecker();


		UrbaZone zone = bPU.getCadastralParcels().get(0).getSubParcels().get(0).getUrbaZone();
		
		//Regle 1
		check.addChild(new CheckOppositeProspect(1, 0, bPU));
		
		
		//Regle 4
		List<ParcelBoundaryType> lPB = new ArrayList<ParcelBoundaryType>();
		lPB.add(ParcelBoundaryType.LAT);
		lPB.add(ParcelBoundaryType.BOT);
	
		check.addChild(new CheckerProspect(1, 2, lPB, bPU));
		
		//Regles 5
		double dMin = deterMineDM(zone); 
		if(dMin > 0){
			check.addChild(new CheckerDistanceParcelBoundaries(dMin, lPB, bPU));
		}

		//Regle 7
		double cesMax = determineMaxCES(zone);
		check.addChild(new CheckCES(cesMax, bPU));
		
		//Regle 8
		double hMax = deterMinHMax(zone);
		check.addChild(new CheckHeight(hMax));
		



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
		System.out.println("ERROR ZONE NOT FOUND :" + uZ.getLibelle());
		return 0;

	}
	
	public static double deterMineDM(UrbaZone uZ) {
		String type = uZ.getLibelle();
		if(type.equals("UBg") || type.equals("UBf") || type.equals("UBg1")  ) return 0;
		return 5;
 		
	}
	
	public static double deterMinHMax(UrbaZone uZ){
		String type = uZ.getLibelle();
		if(type.equals("UBe") || type.equals("UBf") || type.equals("UBe1")  ) return 16;
		if(type.equals("UBg") || type.equals("UBg1")  ) return 22;
		System.out.println("ERROR ZONE NOT FOUND :" + type);
		return 0;
		
		
		
	}

		
		

}
