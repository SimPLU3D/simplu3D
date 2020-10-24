package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.rjmcmc.generic.transform.SimplePolygonTransform;
import fr.ign.geometry.transform.PolygonTransform;
import fr.ign.rjmcmc.kernel.Transform;

/**
 * This software is released under the licence CeCILL see LICENSE.TXT 
 * 
 * see http://www.cecill.info/
 * 
 *
 * copyright IGN
 * @author Brasebin Mickaël
 * @version 1.0
 **/
public class TransformToSurface implements Transform {
	/**
	 * Logger.
	 */
	static Logger LOGGER = LogManager.getLogger(TransformToSurface.class.getName());
	
	
	private PolygonTransform polygonTransform;

	private double delta[];
	private double mat[];
	private double inv[];
	private double determinant;
	private double absDeterminant;
	
	private GeometryFactory factory = new GeometryFactory();

	private boolean isValid = false;

	public boolean isValid() {
		return isValid;
	}

	public TransformToSurface(double[] d, double[] v, IGeometry geom) throws Exception {
		Geometry pp = AdapterFactory.toGeometry(factory, geom);

		Iterator<Double> testedSnapping = Arrays.asList(0.1, 0.001, 0.0).iterator();

		while (testedSnapping.hasNext() && !isValid) {

			try {
				this.polygonTransform = new PolygonTransform(pp, testedSnapping.next());
				isValid = this.polygonTransform.isValid();
			} catch (Exception e) {
				//e.printStackTrace();
			}

		}

		if (!isValid) {
			this.polygonTransform = new SimplePolygonTransform(pp);
			this.isValid = polygonTransform.isValid();

		}
		
		
		this.mat = new double[d.length];
		this.delta = new double[d.length];
		this.inv = new double[d.length];
		this.determinant = 1.;
		
		for (int i = 2; i < d.length; ++i) {
			double dvalue = d[i];
			determinant *= dvalue;
			mat[i] = dvalue;
			inv[i] = 1 / dvalue;
			delta[i] = v[i];
		}
		this.absDeterminant = 1;// Math.abs(determinant);
	}



	public double getDeterminant() {
		return this.determinant;
	}

	@Override
	public double apply(boolean direct, double[] val0, double[] val1) {
		double pt = this.polygonTransform.apply(direct, val0, val1);
		if (direct) {
			/*
			if (dp == null) {
				val1[0] = 0.;
				val1[1] = 0.;
			} else {
				val1[0] = dp.getX();
				val1[1] = dp.getY();
			}*/
			for (int i = 2; i < val1.length; i++) {
				val1[i] = val0[i] * mat[i] + delta[i];
			}
			return pt * 1;
		} else {
	
			

			/*
			if (dp == null) {
				val1[0] = 0.;
				val1[1] = 0.;
			} else {
				val1[0] = dp.getX();
				val1[1] = dp.getY();
			}*/
			for (int i = 2; i < val1.length; i++) {
				val1[i] = (val0[i] - delta[i]) * inv[i];
			}
			return pt *1;
		}
	}

	// @Override
	public double getAbsJacobian(boolean direct) {
		if (direct)
			return this.absDeterminant;
		return 1 / this.absDeterminant;
	}

	@Override
	public int dimension() {
		return this.mat.length;
	}
}
