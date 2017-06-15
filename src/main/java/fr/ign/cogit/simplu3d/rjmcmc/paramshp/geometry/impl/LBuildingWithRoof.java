package fr.ign.cogit.simplu3d.rjmcmc.paramshp.geometry.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.algo.CommonAlgorithms;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.geometry.Primitive;

public class LBuildingWithRoof extends AbstractSimpleBuilding {

	private double hgutter;

	private double h1, h2, l1, l2, shift;

	/**
	 * Crée une forme de L.
	 * 
	 * @param centre
	 *            centre de la forme
	 * @param largeur1
	 *            largeur totale de la forme
	 * @param hauteur1
	 *            hauteur totale de la forme
	 * @param largeur2
	 *            largeur de la barre du L
	 * @param hauteur2
	 *            hauteur de la base du L
	 * @return un L centré sur le centre en paramètre
	 */

	public LBuildingWithRoof(double centerx, double centery, double l1, double l2, double h1, double h2, double height,
			double orientation, double hgutter, double shift) {
		super();
		this.generated = true;
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

		this.generated = true;

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
		return this.getArea() * this.getHeight() - (this.shift * this.height * this.l1 / 3)
				- (this.shift * this.height * this.h1 / 3);
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

			IPolygon pol = new GM_Polygon(new GM_LineString(new DirectPositionList(Arrays.asList(

					(IDirectPosition) new DirectPosition(centerx, centery),
					(IDirectPosition) new DirectPosition(centerx, centery + h1),
					(IDirectPosition) new DirectPosition(centerx - l2, centery + h1),
					(IDirectPosition) new DirectPosition(centerx - l2, centery - h2),
					(IDirectPosition) new DirectPosition(centerx + l1, centery - h2),
					(IDirectPosition) new DirectPosition(centerx + l1, centery),
					(IDirectPosition) new DirectPosition(centerx, centery)))));

			pol = CommonAlgorithms.rotation(pol, new DirectPosition(centerx, centery), this.orientation);

			try {
				this.geomJTS = (Polygon) JtsGeOxygene.makeJtsGeom(pol);

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

	IPolygon polyGeox = null;

	@Override
	public IOrientableSurface getFootprint() {
		if (polyGeox == null) {
			try {
				polyGeox = (IPolygon) JtsGeOxygene.makeGeOxygeneGeom(this.toGeometry());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return polyGeox;
	}

	@Override
	public boolean prospectJTS(Geometry geom, double slope, double hIni) {

		try {
			return prospect(JtsGeOxygene.makeGeOxygeneGeom(geom), slope, hIni);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	double zMin = Double.NaN;

	public double getZmin() {
		if (Double.isNaN(zMin)) {
			Environnement env = Environnement.getInstance();
			if (env != null && env.getTerrain() != null) {
				zMin = env.getTerrain().castCoordinate(this.centerx, this.centery).z;
			} else {
				logger.warn("No terrain Cuboid ZMin set to 0");
				zMin = 0;
			}

		}
		// TODO Auto-generated method stub
		return zMin;
	}

	public IGeometry getGeom() {
		return generated3DGeom();
	}

	public boolean prospect(IGeometry geom, double slope, double hIni) {

		double distance = this.getFootprint().distance(geom);

		if (distance * slope + hIni < shift + this.getHeight()) {
			return false;
		}

		return true;
	}

	@Override
	public IGeometry generated3DGeom() {

		IDirectPositionList dpl = this.getFootprint().coord();

		IDirectPosition dp0 = dpl.get(0);
		dp0.setZ(this.getZmin() + this.hgutter);
		IDirectPosition dp1 = dpl.get(1);
		dp1.setZ(this.getZmin() + this.hgutter);
		IDirectPosition dp2 = dpl.get(2);
		dp2.setZ(this.getZmin() + this.hgutter);
		IDirectPosition dp3 = dpl.get(3);
		dp3.setZ(this.getZmin() + this.hgutter);
		IDirectPosition dp4 = dpl.get(4);
		dp4.setZ(this.getZmin() + this.hgutter);
		IDirectPosition dp5 = dpl.get(5);
		dp5.setZ(this.getZmin() + this.hgutter);

		Vecteur vz = new Vecteur(0, 0, 1);

		IDirectPosition dpc1 = new DirectPosition(dp1.getX() / 2 + dp2.getX() / 2, dp1.getY() / 2 + dp2.getY() / 2, 0);
		Vecteur v1 = new Vecteur(dp1, dp2);
		v1.normalise();
		Vecteur vt = vz.prodVectoriel(v1);
		vt = vt.multConstante(this.shift * (this.h1 + this.h2 / 2));
		IDirectPosition dp7 = vt.translate(dpc1);
		dp7.setZ(this.getZmin() + this.height + this.hgutter);

		IDirectPosition dpc2 = new DirectPosition(dp4.getX() / 2 + dp5.getX() / 2, dp4.getY() / 2 + dp5.getY() / 2, 0);
		Vecteur v2 = new Vecteur(dp4, dp5);
		v2.normalise();
		Vecteur vt2 = vz.prodVectoriel(v2);
		vt2 = vt2.multConstante(this.shift * (this.l1 + this.l2 / 2));
		IDirectPosition dp6 = vt2.translate(dpc2);
		dp6.setZ(this.getZmin() + this.height + this.hgutter);

		IDirectPosition dp8 = new DirectPosition(dp0.getX() / 2 + dp3.getX() / 2, dp0.getY() / 2 + dp3.getY() / 2);
		dp8.setZ(this.getZmin() + this.height + this.hgutter);

		IDirectPosition dp0Bas = (IDirectPosition) dp0.clone();
		dp0Bas.setZ(this.getZmin());
		IDirectPosition dp1Bas = (IDirectPosition) dp1.clone();
		dp1Bas.setZ(this.getZmin());
		IDirectPosition dp2Bas = (IDirectPosition) dp2.clone();
		dp2Bas.setZ(this.getZmin());
		IDirectPosition dp3Bas = (IDirectPosition) dp3.clone();
		dp3Bas.setZ(this.getZmin());
		IDirectPosition dp4Bas = (IDirectPosition) dp4.clone();
		dp4Bas.setZ(this.getZmin());
		IDirectPosition dp5Bas = (IDirectPosition) dp5.clone();
		dp5Bas.setZ(this.getZmin());

		IDirectPosition dp7Bas = (IDirectPosition) dp7.clone();
		dp7Bas.setZ(this.getZmin());
		IDirectPosition dp6Bas = (IDirectPosition) dp6.clone();
		dp6Bas.setZ(this.getZmin());
		IDirectPosition dp8Bas = (IDirectPosition) dp8.clone();
		dp8Bas.setZ(this.getZmin());

		IDirectPositionList dpl1 = new DirectPositionList();
		dpl1.add(dp3);
		dpl1.add(dp4);
		dpl1.add(dp6);
		dpl1.add(dp8);
		dpl1.add(dp3);

		IDirectPositionList dpl2 = new DirectPositionList();
		dpl2.add(dp4);
		dpl2.add(dp5);
		dpl2.add(dp6);
		dpl2.add(dp4);

		IDirectPositionList dpl3 = new DirectPositionList();
		dpl3.add(dp5);
		dpl3.add(dp0);
		dpl3.add(dp8);
		dpl3.add(dp6);
		dpl3.add(dp5);

		IDirectPositionList dpl4 = new DirectPositionList();
		dpl4.add(dp0);
		dpl4.add(dp1);
		dpl4.add(dp7);
		dpl4.add(dp8);
		dpl4.add(dp0);

		IDirectPositionList dpl5 = new DirectPositionList();
		dpl5.add(dp1);
		dpl5.add(dp2);
		dpl5.add(dp7);
		dpl5.add(dp1);

		IDirectPositionList dpl6 = new DirectPositionList();
		dpl6.add(dp2);
		dpl6.add(dp3);
		dpl6.add(dp8);
		dpl6.add(dp7);
		dpl6.add(dp2);

		IDirectPositionList dpl7 = new DirectPositionList();
		dpl7.add(dp0);
		dpl7.add(dp1);
		dpl7.add(dp1Bas);
		dpl7.add(dp0Bas);
		dpl7.add(dp0);

		IDirectPositionList dpl8 = new DirectPositionList();
		dpl8.add(dp1);
		dpl8.add(dp2);
		dpl8.add(dp2Bas);
		dpl8.add(dp1Bas);
		dpl8.add(dp1);

		IDirectPositionList dpl9 = new DirectPositionList();
		dpl9.add(dp2);
		dpl9.add(dp3);
		dpl9.add(dp3Bas);
		dpl9.add(dp2Bas);
		dpl9.add(dp2);

		IDirectPositionList dpl10 = new DirectPositionList();
		dpl10.add(dp3);
		dpl10.add(dp4);
		dpl10.add(dp4Bas);
		dpl10.add(dp3Bas);
		dpl10.add(dp3);

		IDirectPositionList dpl11 = new DirectPositionList();
		dpl11.add(dp4);
		dpl11.add(dp5);
		dpl11.add(dp5Bas);
		dpl11.add(dp4Bas);
		dpl11.add(dp4);

		IDirectPositionList dpl12 = new DirectPositionList();
		dpl12.add(dp5);
		dpl12.add(dp0);
		dpl12.add(dp0Bas);
		dpl12.add(dp5Bas);
		dpl12.add(dp5);

		List<IOrientableSurface> ims = new ArrayList<>();
		ims.add(new GM_Polygon(new GM_LineString(dpl1)));
		ims.add(new GM_Polygon(new GM_LineString(dpl2)));
		ims.add(new GM_Polygon(new GM_LineString(dpl3)));
		ims.add(new GM_Polygon(new GM_LineString(dpl4)));
		ims.add(new GM_Polygon(new GM_LineString(dpl5)));
		ims.add(new GM_Polygon(new GM_LineString(dpl6)));
		ims.add(new GM_Polygon(new GM_LineString(dpl7)));
		ims.add(new GM_Polygon(new GM_LineString(dpl8)));
		ims.add(new GM_Polygon(new GM_LineString(dpl9)));
		ims.add(new GM_Polygon(new GM_LineString(dpl10)));
		ims.add(new GM_Polygon(new GM_LineString(dpl11)));
		ims.add(new GM_Polygon(new GM_LineString(dpl12)));

		return new GM_MultiSurface<>(ims);
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
