package fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.footprint;

import java.util.List;

import com.vividsolutions.jts.geom.Polygon;

public class SquareFactory implements IWallFactory{
	
	double[] minParameters;
	double[] maxParameters;
	
	public SquareFactory(double[]  minParameters, double[] maxParameters){
		
		this.minParameters = minParameters;
		this.maxParameters = maxParameters;
		
	}
	

	@Override
	public int getDimension() {
		return 4;
	}

	@Override
	public double[] getMinParameters() {
		// TODO Auto-generated method stub
		return minParameters;
	}

	@Override
	public double[] getMaxParameters() {
		// TODO Auto-generated method stub
		return maxParameters;
	}

	@Override
	public Polygon generateFootprint(List<Double> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
