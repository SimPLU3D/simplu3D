package fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ign.common.util.Vecteur;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.locationtech.jts.geom.*;

import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.util.AssignZ;
import fr.ign.geometry.Primitive;

public class LBuildingWithRoof extends AbstractSimpleBuilding {

	private double hgutter;

	private double h1, h2, l1, l2, shift;

	
	/**
	 * 
	 * @param centerx x coordinate of the shape center
	 * @param centery  y coordinate of the shape center
	 * @param l1 total length of the shape without the length of the L bar
	 * @param l2 length of the bar of the L
	 * @param h1 total height of the shape without the length of the L bar
	 * @param h2 height of the L bar
	 * @param height maximal height of the shape
	 * @param orientation azimut of the shape
	 * @param hgutter gutter height of the shape
	 * @param shift slope at the extremities of the L
	 */
	public LBuildingWithRoof(double centerx, double centery, double l1, double l2, double h1, double h2, double height,
			double orientation, double hgutter, double shift) {
		super();
		this.setNew(true);
		this.centerx = centerx;
		this.centery = centery;
		this.h1 = h1;
		this.h2 = h2;
		this.l1 = l1;
		this.l2 = l2;
		this.height = height;
		this.orientation = orientation;
		this.hgutter = hgutter;
		this.shift = shift;

	}

	@Override
	public double[] toArray() {
		return new double[] { this.centerx, this.centery, this.l1, this.l2, this.h1, this.h2, this.height,
				this.orientation, this.hgutter, this.shift };
	}

	@Override
	public int size() {
		return 10;
	}

	@Override
	public void set(List<Double> list) {
		this.centerx = list.get(0);
		this.centery = list.get(1);
		this.l1 = list.get(2);
		this.l2 = list.get(3);
		this.h1 = list.get(4);
		this.h2 = list.get(5);
		this.height = list.get(6);
		this.orientation = list.get(7);
		this.hgutter = list.get(8);
		this.shift = list.get(9);
		this.setNew(true);;

	}

	@Override
	public Object[] getArray() {
		return new Object[] { this.centerx, this.centery, this.l1, this.l2, this.h1, this.h2, this.height,
				this.orientation, this.hgutter, this.shift };
	}

	@Override
	public double getHeight() {
		return this.height + this.hgutter;
	}

	@Override
	public double getVolume() {
		return this.getArea() * this.hgutter + 	
				+ (this.shift * this.height * this.l1 / 3) //volume of two extremities
				+ (this.shift * this.height * this.h1 / 3)
				+ ((1-this.shift) * (this.l1 + this.l2/2) * h2 * height  )
				+ ((1-this.shift) * (this.h1 + this.h2/2) * l2 * height  )
				;
	}

	@Override
	public double intersectionArea(Primitive p) {

		return this.toGeometry().intersection(p.toGeometry()).getArea();
	}

	@Override
	public double getArea() {
		return (this.l1 + this.l2) * (this.h1 + this.h2);
	}

	private Polygon geomJTS;

	@Override
	public Polygon toGeometry() {
		if (geomJTS == null) {
			Polygon pol = new GeometryFactory().createPolygon(new Coordinate[]{
					new Coordinate(centerx, centery),
					new Coordinate(centerx, centery + h1),
					new Coordinate(centerx - l2, centery + h1),
					new Coordinate(centerx - l2, centery - h2),
					new Coordinate(centerx + l1, centery - h2),
					new Coordinate(centerx + l1, centery),
					new Coordinate(centerx, centery)
			});
			AffineTransform affineTransform = AffineTransform.getRotateInstance(this.orientation, centerx, centery);
			MathTransform mathTransform = new AffineTransform2D(affineTransform);
			//pol = CommonAlgorithms.rotation(pol, new Coordinate(centerx, centery), this.orientation);
            try {
                pol = (Polygon) JTS.transform(pol, mathTransform);//FIXME check that
            } catch (TransformException e) {
				throw new RuntimeException(e);
            }

            try {
				this.geomJTS = pol;
				if (this.geomJTS == null) {
					System.out.println("null ? ");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.geomJTS;
	}

	Polygon polyGeox = null;

	@Override
	public Polygon getFootprint() {
		if (polyGeox == null) {
			try {
				polyGeox = this.toGeometry();//FIXME REMOVE DUPLICATE?
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return polyGeox;
	}

	private final static GeometryFactory geomF = new GeometryFactory();

	@Override
	public boolean prospectJTS(Geometry geom, double slope, double hIni) {
		Coordinate[] coord = this.toGeometry().getCoordinates();
		// We first check poits from exterior footprint
        for (Coordinate c : coord) {
            Point p = geomF.createPoint(c);
            double distance = p.distance(geom);
            if (distance * slope + hIni < this.hgutter) {
                return false;
            }
        }
		// We check the two inner point
		Coordinate dp = this.getInnerPoint1();
		Coordinate c1 = new Coordinate(dp.getX(), dp.getY(), dp.getZ());

		Point p1 = geomF.createPoint(c1);

		double distance = p1.distance(geom);

		if (distance * slope + hIni < this.getHeight()) {
			return false;
		}

		Coordinate dp2 = this.getInnerPoint2();
		Coordinate c2 = new Coordinate(dp2.getX(), dp2.getY(), dp2.getZ());

		Point p2 = geomF.createPoint(c2);

		distance = p2.distance(geom);

		if (distance * slope + hIni < this.getHeight()) {
			return false;
		}

		return true;
	}

	double zMin = Double.NaN;

	public double getZmin() {
		if (Double.isNaN(zMin)) {
			Environnement env = Environnement.createEnvironnement();
			if (env.getTerrain() != null) {
				zMin = env.getTerrain().castCoordinate(this.centerx, this.centery).z;
			} else {
				System.out.println("No terrain Cuboid ZMin set to 0");//FIXME use logger
				zMin = AssignZ.DEFAULT_Z;
			}

		}
		// TODO Auto-generated method stub
		return zMin;
	}

	public Geometry getGeom() {
		return generated3DGeom();
	}

	public boolean prospect(Geometry geom, double slope, double hIni) {
		try {
			return prospectJTS(geom, slope, hIni);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	Geometry geometry3d = null;

	private Coordinate getInnerPoint1() {
		List<Coordinate> dpl = Arrays.asList(this.getFootprint().getCoordinates());
		Coordinate dp4 = dpl.get(4);
		dp4.setZ(this.getZmin() + this.hgutter);
		Coordinate dp5 = dpl.get(5);
		dp5.setZ(this.getZmin() + this.hgutter);
		Vecteur vz = new Vecteur(0, 0, 1);
		Coordinate dpc2 = new Coordinate(dp4.getX() / 2 + dp5.getX() / 2, dp4.getY() / 2 + dp5.getY() / 2, 0);
		Vecteur v2 = new Vecteur(dp4, dp5);
		v2.normalise();
		Vecteur vt2 = vz.prodVectoriel(v2);
		vt2 = vt2.multConstante(this.shift * (this.l1 + this.l2 / 2));
		Coordinate dp6 = vt2.translate(dpc2);
		dp6.setZ(this.getZmin() + this.height + this.hgutter);
		return dp6;
	}

	private Coordinate getInnerPoint2() {
		List<Coordinate> dpl = Arrays.asList(this.getFootprint().getCoordinates());
		Coordinate dp1 = dpl.get(1);
		dp1.setZ(this.getZmin() + this.hgutter);
		Coordinate dp2 = dpl.get(2);
		dp2.setZ(this.getZmin() + this.hgutter);
		Vecteur vz = new Vecteur(0, 0, 1);
		Coordinate dpc1 = new Coordinate(dp1.getX() / 2 + dp2.getX() / 2, dp1.getY() / 2 + dp2.getY() / 2, 0);
		Vecteur v1 = new Vecteur(dp1, dp2);
		v1.normalise();
		Vecteur vt = vz.prodVectoriel(v1);
		vt = vt.multConstante(this.shift * (this.h1 + this.h2 / 2));
		Coordinate dp7 = vt.translate(dpc1);
		dp7.setZ(this.getZmin() + this.height + this.hgutter);
		return dp7;
	}

	@Override
	public Geometry generated3DGeom() {
		if (geometry3d == null) {
			List<Coordinate> dpl = Arrays.asList(this.getFootprint().getCoordinates());
			Coordinate dp0 = dpl.get(0);
			dp0.setZ(this.getZmin() + this.hgutter);
			Coordinate dp1 = dpl.get(1);
			dp1.setZ(this.getZmin() + this.hgutter);
			Coordinate dp2 = dpl.get(2);
			dp2.setZ(this.getZmin() + this.hgutter);
			Coordinate dp3 = dpl.get(3);
			dp3.setZ(this.getZmin() + this.hgutter);
			Coordinate dp4 = dpl.get(4);
			dp4.setZ(this.getZmin() + this.hgutter);
			Coordinate dp5 = dpl.get(5);
			dp5.setZ(this.getZmin() + this.hgutter);
			Coordinate dp6 = this.getInnerPoint1();
			Coordinate dp7 = this.getInnerPoint2();
			Coordinate dp8 = new Coordinate(dp0.getX() / 2 + dp3.getX() / 2, dp0.getY() / 2 + dp3.getY() / 2);
			dp8.setZ(this.getZmin() + this.height + this.hgutter);
			Coordinate dp0Bas = dp0.copy();
			dp0Bas.setZ(this.getZmin());
			Coordinate dp1Bas = dp1.copy();
			dp1Bas.setZ(this.getZmin());
			Coordinate dp2Bas = dp2.copy();
			dp2Bas.setZ(this.getZmin());
			Coordinate dp3Bas = dp3.copy();
			dp3Bas.setZ(this.getZmin());
			Coordinate dp4Bas = dp4.copy();
			dp4Bas.setZ(this.getZmin());
			Coordinate dp5Bas = dp5.copy();
			dp5Bas.setZ(this.getZmin());
			Coordinate dp7Bas = dp7.copy();
			dp7Bas.setZ(this.getZmin());
			Coordinate dp6Bas = dp6.copy();
			dp6Bas.setZ(this.getZmin());
			Coordinate dp8Bas = dp8.copy();
			dp8Bas.setZ(this.getZmin());
			CoordinateList dpl1 = new CoordinateList();
			dpl1.add(dp3);
			dpl1.add(dp4);
			dpl1.add(dp6);
			dpl1.add(dp8);
			dpl1.add(dp3);
			CoordinateList dpl2 = new CoordinateList();
			dpl2.add(dp4);
			dpl2.add(dp5);
			dpl2.add(dp6);
			dpl2.add(dp4);
			CoordinateList dpl3 = new CoordinateList();
			dpl3.add(dp5);
			dpl3.add(dp0);
			dpl3.add(dp8);
			dpl3.add(dp6);
			dpl3.add(dp5);
			CoordinateList dpl4 = new CoordinateList();
			dpl4.add(dp0);
			dpl4.add(dp1);
			dpl4.add(dp7);
			dpl4.add(dp8);
			dpl4.add(dp0);
			CoordinateList dpl5 = new CoordinateList();
			dpl5.add(dp1);
			dpl5.add(dp2);
			dpl5.add(dp7);
			dpl5.add(dp1);
			CoordinateList dpl6 = new CoordinateList();
			dpl6.add(dp2);
			dpl6.add(dp3);
			dpl6.add(dp8);
			dpl6.add(dp7);
			dpl6.add(dp2);
			CoordinateList dpl7 = new CoordinateList();
			dpl7.add(dp0);
			dpl7.add(dp1);
			dpl7.add(dp1Bas);
			dpl7.add(dp0Bas);
			dpl7.add(dp0);
			CoordinateList dpl8 = new CoordinateList();
			dpl8.add(dp1);
			dpl8.add(dp2);
			dpl8.add(dp2Bas);
			dpl8.add(dp1Bas);
			dpl8.add(dp1);
			CoordinateList dpl9 = new CoordinateList();
			dpl9.add(dp2);
			dpl9.add(dp3);
			dpl9.add(dp3Bas);
			dpl9.add(dp2Bas);
			dpl9.add(dp2);
			CoordinateList dpl10 = new CoordinateList();
			dpl10.add(dp3);
			dpl10.add(dp4);
			dpl10.add(dp4Bas);
			dpl10.add(dp3Bas);
			dpl10.add(dp3);
			CoordinateList dpl11 = new CoordinateList();
			dpl11.add(dp4);
			dpl11.add(dp5);
			dpl11.add(dp5Bas);
			dpl11.add(dp4Bas);
			dpl11.add(dp4);
			CoordinateList dpl12 = new CoordinateList();
			dpl12.add(dp5);
			dpl12.add(dp0);
			dpl12.add(dp0Bas);
			dpl12.add(dp5Bas);
			dpl12.add(dp5);
			List<Polygon> ims = new ArrayList<>();
			GeometryFactory fact = new GeometryFactory();
			ims.add(fact.createPolygon(dpl1.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl2.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl3.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl4.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl5.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl6.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl7.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl8.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl9.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl10.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl11.toCoordinateArray()));
			ims.add(fact.createPolygon(dpl12.toCoordinateArray()));
			geometry3d = fact.createMultiPolygon(ims.toArray(new Polygon[0]));
		}
		return geometry3d;
	}

	@Override
	public void setCoordinates(double[] val1) {

		val1[0] = this.centerx;
		val1[1] = this.centery;
		val1[2] = this.l1;
		val1[3] = this.l2;
		val1[4] = this.h1;
		val1[5] = this.h2;
		val1[6] = this.height;
		val1[7] = this.orientation;
		val1[8] = this.hgutter;
		val1[9] = this.shift;
	}

	@Override
	public String toString() {
		return "LBuildingWithRoof [hgutter=" + hgutter + ", h1=" + h1 + ", h2=" + h2 + ", l1=" + l1 + ", l2=" + l2
				+ ", shift=" + shift + ", geomJTS=" + geomJTS + ", polyGeox=" + polyGeox + ", zMin=" + zMin + "]";
	}
}
