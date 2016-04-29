package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;

public class CuboidGroupCreation {
	/**
	 * The create group function that separates cuboid into list of near cuboid
	 * 
	 * @param lBatIn
	 * @param connexionDistance
	 *            : minimal distance to consider 2 boxes as connected
	 * @return
	 */
	public static List<List<? extends Cuboid>> createGroup(List<? extends Cuboid> lBatIn, double connexionDistance) {

		List<List<? extends Cuboid>> listGroup = new ArrayList<>();

		while (!lBatIn.isEmpty()) {

			Cuboid batIni = lBatIn.remove(0);

			List<Cuboid> currentGroup = new ArrayList<>();
			currentGroup.add(batIni);

			int nbElem = lBatIn.size();

			bouclei: for (int i = 0; i < nbElem; i++) {

				for (Cuboid batTemp : currentGroup) {

					if (lBatIn.get(i).getFootprint().distance(batTemp.getFootprint()) < connexionDistance) {

						currentGroup.add(lBatIn.get(i));
						lBatIn.remove(i);
						i = -1;
						nbElem--;
						continue bouclei;

					}
				}

			}

			listGroup.add(currentGroup);
		}


		return listGroup;

	}
}
