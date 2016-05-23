package fr.ign.cogit.simplu3d.rjmcmc.generic.object;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.geometry.Primitive;

public interface ISimPLU3DPrimitive extends Primitive {

	public double getHeight();

	public double getVolume();

	public IGeometry generated3DGeom();
}
