package fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.footprint;

import java.util.List;

import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 * @author mickael brasebin
 *
 */
public interface IWallFactory {
	
	
	public int getDimension();	

	public double[] getMinParameters();
	public double[] getMaxParameters();

	
	public Polygon generateFootprint(List<Double> parameters);
}
