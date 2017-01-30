package fr.ign.cogit.simplu3d.rjmcmc.paramshapes.impl;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.footprint.IWallFactory;
import fr.ign.cogit.simplu3d.rjmcmc.paramshapes.factory.roof.IRoofFactory;
import fr.ign.geometry.Primitive;

public class ParametricBuilding extends AbstractSimpleBuilding implements ISimPLU3DPrimitive {

	private IRoofFactory roofFactory;
	private IWallFactory wallFactory;

	public ParametricBuilding(IWallFactory wallFactory, IRoofFactory roofFactory,  double[] parameters) {
		int size = parameters.length;
		for (int i = 0; i < size; i++) {
			this.parameters.add(parameters[i]);
		}
		this.roofFactory = roofFactory;
		this.wallFactory = wallFactory;
	}

	Polygon pol = null;

	@Override
	public Polygon toGeometry() {
		if (pol == null) {
			pol = generateFootprint();

		}
		return pol;
	}

	double area = Double.NaN;

	@Override
	public double getArea() {
		if (Double.isNaN(area)) {
			area = this.toGeometry().getArea();
		}
		return area;
	}

	@Override
	public double intersectionArea(Primitive arg0) {
		return (arg0.toGeometry().intersection(this.toGeometry()).getArea());
	}

	List<Double> parameters = new ArrayList<Double>();

	@Override
	public void set(List<Double> arg0) {
		arg0 = parameters;
	}

	@Override
	public Object[] getArray() {
		return parameters.toArray();
	}

	@Override
	public int size() {
		return parameters.size();
	}

	@Override
	public double[] toArray() {
		double list[] = new double[parameters.size()];

		int count = 0;
		for (Double d : parameters) {
			list[count] = d;
			count++;
		}

		return list;
	}

	double height = -1;

	@Override
	public double getHeight() {

		if (height == -1) {
			height = (new Box3D(this.generated3DGeom())).getHeight();
		}
		return height;
	}


	//////////////// TODO
	private Polygon generateFootprint() {
		
		//On enlève les paramètres qui sont dus au wall surface
		return null;
	}
	
	

	@Override
	public double getVolume() {

		return 0;
	}

	@Override
	public boolean prospectJTS(Geometry geom, double slope, double hIni) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IGeometry generated3DGeom() {
		// TODO Auto-generated method stub
		return null;
	}

}
