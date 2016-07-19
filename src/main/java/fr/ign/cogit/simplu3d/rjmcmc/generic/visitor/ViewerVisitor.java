package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.feature.DefaultFeature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.gui.MainWindow;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TextureManager;
import fr.ign.cogit.geoxygene.sig3d.representation.texture.TexturedSurface;
import fr.ign.cogit.geoxygene.sig3d.semantic.VectorLayer;
import fr.ign.cogit.geoxygene.sig3d.util.ColorRandom;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.util.attribute.AttributeManager;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement;
import fr.ign.cogit.simplu3d.representation.RepEnvironnement.Theme;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.mpp.configuration.AbstractBirthDeathModification;
import fr.ign.mpp.configuration.AbstractGraphConfiguration;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.mpp.configuration.GraphVertex;
import fr.ign.parameters.Parameters;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * 
 * @copyright IGN
 * 
 * @author Brasebin Mickaël
 * 
 * @version 1.7
 **/
public class ViewerVisitor<O extends ISimPLU3DPrimitive, C extends AbstractGraphConfiguration<O, C, M>, M extends AbstractBirthDeathModification<O, C, M>>
		implements Visitor<C, M> {

	private static MainWindow mW = null;
	private int save;
	private int iter;

	private final static String PREFIX_NAME_STRING = "Étape";
	private String prefix = "";
	private static int MIN_LAYER = 3;

	private GraphConfiguration<ISimPLU3DPrimitive> bestConfig = null;
	private double bestValue = Double.POSITIVE_INFINITY;

	public ViewerVisitor(Environnement env,String prefixe, Parameters p) {
		prefix = prefixe;
		if (mW == null) {
			mW = new MainWindow();
			// mW.getMainMenuBar().add(new IOToolBar(mW));
			represent(env, mW, p);
			MIN_LAYER = mW.getInterfaceMap3D().getCurrent3DMap().getLayerList()
					.size();
		}

	}

	@Override
	public void init(int dump, int save) {
		this.iter = 0;
		this.save = save;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void visit(C config, Sampler<C, M> sampler, Temperature t) {
		++iter;

		if (config.getEnergy() < bestValue) {
			bestValue = config.getEnergy();
			bestConfig = (GraphConfiguration<ISimPLU3DPrimitive>) config;

		}

		if ((save > 0) && (iter % save == 0)) {
			this.addInformationToMainWindow((GraphConfiguration<ISimPLU3DPrimitive>) config);
		}
	}

	@Override
	public void begin(C config, Sampler<C, M> sampler, Temperature t) {
	}

	@Override
	public void end(C config, Sampler<C, M> sampler, Temperature t) {

		this.addInformationToMainWindow(bestConfig);
	}

	private void addInformationToMainWindow(
			GraphConfiguration<ISimPLU3DPrimitive> config) {

		IFeatureCollection<IFeature> feat = new FT_FeatureCollection<>();

		for (GraphVertex<ISimPLU3DPrimitive> v : config.getGraph()
				.vertexSet()) {

			IGeometry geom = null;

			geom = v.getValue().generated3DGeom();

			if (geom == null) {
				continue;
			}

			DefaultFeature df = new DefaultFeature(geom);
			AttributeManager
					.addAttribute(df, "Energy", v.getEnergy(), "Double");
			feat.add(df);

		}

		if (!feat.isEmpty()) {
			VectorLayer vl = new VectorLayer(feat, PREFIX_NAME_STRING + prefix
					+ " : " + iter, ColorRandom.getRandomColor());

			int nbLayer = mW.getInterfaceMap3D().getCurrent3DMap()
					.getLayerList().size();

			if (nbLayer > MIN_LAYER) {
				mW.getInterfaceMap3D().getCurrent3DMap().getLayerList()
						.get(nbLayer - 1).setVisible(false);
			}

			mW.getInterfaceMap3D().getCurrent3DMap().addLayer(vl);
		}

	}

	private static void represent(Environnement env, MainWindow mW, Parameters p) {
		List<Theme> lTheme = new ArrayList<RepEnvironnement.Theme>();
		// lTheme.add(Theme.TOIT_BATIMENT);
		lTheme.add(Theme.VOIRIE);
		// lTheme.add(Theme.FAITAGE);
		// lTheme.add(Theme.PIGNON);
		// lTheme.add(Theme.GOUTTIERE);
		// lTheme.add(Theme.VOIRIE);
		lTheme.add(Theme.PARCELLE);
		lTheme.add(Theme.BORDURE);
		lTheme.add(Theme.ZONE);
		// lTheme.add(Theme.PAN);

		Theme[] tab = lTheme.toArray(new Theme[0]);

		List<VectorLayer> vl = RepEnvironnement.represent(env, tab);

		for (VectorLayer l : vl) {

			mW.getInterfaceMap3D().getCurrent3DMap().addLayer(l);
		}

		mW.getInterfaceMap3D().removeLight(0);
		mW.getInterfaceMap3D().addLight(new Color(147, 147, 147), 0, 0, 0);
		mW.getInterfaceMap3D().moveLight(180, -15, 120, 0);
		mW.getInterfaceMap3D().addLight(new Color(147, 147, 147), 0, 0, 0);
		mW.getInterfaceMap3D().moveLight(-140, 3, 120, 1);

		if (p.getBoolean("showbackground")) {
			return;
		}

		double z = p.getDouble("z");

		double xmin = p.getDouble("xminbg");
		double xmax = p.getDouble("xmaxbg");
		double ymin = p.getDouble("yminbg");
		double ymax = p.getDouble("ymaxbg");

		//
		// 1051042.8513268954120576,6840539.0837931865826249 :
		// 1051264.8064121364150196,6840679.2711814027279615
		// Projet 1
		IDirectPosition dpLL = new DirectPosition(xmin, ymin, z);
		IDirectPosition dpUR = new DirectPosition(xmax, ymax, z);

		// Projet 3
		// IDirectPosition dpLL = new DirectPosition(1051157, 6840727, z);
		// IDirectPosition dpUR = new DirectPosition(1051322, 6840858, z);

		IDirectPositionList dpl = new DirectPositionList();

		IDirectPosition dp2 = new DirectPosition(dpUR.getX(), dpLL.getY(), z);

		IDirectPosition dp4 = new DirectPosition(dpLL.getX(), dpUR.getY(), z);

		dpl.add(dpLL);
		dpl.add(dp2);
		dpl.add(dpUR);
		dpl.add(dp4);
		dpl.add(dpLL);

		IFeatureCollection<IFeature> fc = new FT_FeatureCollection<IFeature>();

		IFeature feat = new DefaultFeature(new GM_Polygon(
				new GM_LineString(dpl)));

		fc.add(feat);

		// feat.setRepresentation(new TexturedSurface(feat, TextureManager
		// .textureLoading(folder + "Env3D_86.png"), dpUR.getX()-dpLL.getX(),
		// dpUR.getY()-dpLL.getY()));

		String background = p.getString("background_img").toString();

		feat.setRepresentation(new TexturedSurface(feat, TextureManager
				.textureLoading(env.folder + background), dpUR.getX()
				- dpLL.getX(), dpUR.getY() - dpLL.getY()));

		mW.getInterfaceMap3D().getCurrent3DMap()
				.addLayer(new VectorLayer(fc, "Fond"));

	}
}
