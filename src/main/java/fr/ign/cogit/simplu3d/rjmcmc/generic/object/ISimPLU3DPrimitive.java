package fr.ign.cogit.simplu3d.rjmcmc.generic.object;

import fr.ign.geometry.Primitive;
import org.locationtech.jts.geom.Geometry;

public interface ISimPLU3DPrimitive extends Primitive {

	public double getHeight();

	public double getVolume();

	public Geometry generated3DGeom();
}
