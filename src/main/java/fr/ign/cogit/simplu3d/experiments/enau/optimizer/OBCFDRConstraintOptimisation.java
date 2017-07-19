package fr.ign.cogit.simplu3d.experiments.enau.optimizer;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.cogit.simplu3d.experiments.enau.energy.AlignementEnergy;
import fr.ign.cogit.simplu3d.experiments.enau.energy.HauteurEnergy;
import fr.ign.cogit.simplu3d.experiments.enau.energy.ProspectEnergy;
import fr.ign.cogit.simplu3d.experiments.enau.energy.ProspectEnergy2;
import fr.ign.cogit.simplu3d.experiments.enau.energy.ServitudeVue;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.optimizer.cuboid.BasicCuboidOptimizer;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.DifferenceVolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.IntersectionVolumeBinaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.energy.VolumeUnaryEnergy;
import fr.ign.cogit.simplu3d.rjmcmc.generic.visitor.PrepareVisitors;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.parameters.Parameters;
import fr.ign.random.Random;
import fr.ign.rjmcmc.configuration.ConfigurationModificationPredicate;
import fr.ign.rjmcmc.energy.BinaryEnergy;
import fr.ign.rjmcmc.energy.ConstantEnergy;
import fr.ign.rjmcmc.energy.MinusUnaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesBinaryEnergy;
import fr.ign.rjmcmc.energy.MultipliesUnaryEnergy;
import fr.ign.rjmcmc.energy.PlusUnaryEnergy;
import fr.ign.rjmcmc.energy.UnaryEnergy;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.SimulatedAnnealing;
import fr.ign.simulatedannealing.endtest.EndTest;
import fr.ign.simulatedannealing.schedule.Schedule;
import fr.ign.simulatedannealing.temperature.SimpleTemperature;

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
 * @version 1.0
 **/
public class OBCFDRConstraintOptimisation extends BasicCuboidOptimizer<Cuboid> {

	public GraphConfiguration<Cuboid> process(BasicPropertyUnit bpu, Parameters p, Environnement env, int id,
			ConfigurationModificationPredicate<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred,
			double distReculVoirie, double slope, double hIni, double hMax, double distReculLimi, double slopeProspect,
			double maximalCES) {
		// Géométrie de l'unité foncière sur laquelle porte la génération
		IGeometry geom = bpu.generateGeom().buffer(1);

		// Définition de la fonction d'optimisation (on optimise en décroissant)
		// relative au volume
		GraphConfiguration<Cuboid> conf = null;

		try {
			conf = create_configuration(p, geom, bpu, distReculVoirie, slope, hIni, hMax, distReculLimi, slopeProspect,
					maximalCES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Création de l'échantilloneur
		Sampler<GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> samp = create_sampler(Random.random(), p,
				bpu, pred, bpu.getGeom());
		// Température
		Schedule<SimpleTemperature> sch = create_schedule(p);

		int loadExistingConfig = p.getInteger("load_existing_config");
		if (loadExistingConfig == 1) {
			String configPath = p.get("config_shape_file").toString();
			List<Cuboid> lCuboid = LoaderCuboid.loadFromShapeFile(configPath);
			BirthDeathModification<Cuboid> m = conf.newModification();
			for (Cuboid c : lCuboid) {
				m.insertBirth(c);
			}
			conf.deltaEnergy(m);
			// conf.apply(m);
			m.apply(conf);
			System.out.println("First update OK");
		}
		// EndTest<Cuboid2, Configuration<Cuboid2>, SimpleTemperature,
		// Sampler<Cuboid2, Configuration<Cuboid2>, SimpleTemperature>> end =
		// create_end_test(p);

		EndTest end = create_end_test(p);

		PrepareVisitors<Cuboid> pv = new PrepareVisitors<>(env);

		/*
		 * < This is the way to launch the optimization process. Here, the magic
		 * happen... >
		 */
		SimulatedAnnealing.optimize(Random.random(), conf, samp, sch, end, pv.prepare(p, bpu.getId()));
		return conf;
	}

	public GraphConfiguration<Cuboid> create_configuration(Parameters p, IGeometry geom, BasicPropertyUnit bpu,
			double distReculVoirie, double slope, double hIni, double hMax, double distReculLimi, double slopeProspect,
			double maximalCES) throws Exception {

		return this.create_configuration(p, AdapterFactory.toGeometry(new GeometryFactory(), geom), bpu,
				distReculVoirie, slope, hIni, hMax, distReculLimi, slopeProspect, maximalCES);

	}

	// Création de la configuration
	/**
	 * @param p
	 *            paramètres importés depuis le fichier XML
	 * @param bpu
	 *            l'unité foncière considérée
	 * @return la configuration chargée, c'est à dire la formulation énergétique
	 *         prise en compte
	 */
	public GraphConfiguration<Cuboid> create_configuration(Parameters p, Geometry geom, BasicPropertyUnit bpu,
			double distReculVoirie, double slope, double hIni, double hMax, double distReculLimi, double slopeProspect,
			double maximalCES) {
		// Énergie constante : à la création d'un nouvel objet

		double energyCrea = Double.isNaN(this.energyCreation) ? p.getDouble("energy") : this.energyCreation;

		ConstantEnergy<Cuboid, Cuboid> energyCreation = new ConstantEnergy<Cuboid, Cuboid>(energyCrea);

		// Énergie constante : pondération de l'intersection
		ConstantEnergy<Cuboid, Cuboid> ponderationVolume = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_volume"));
		// Énergie unaire : aire dans la parcelle
		UnaryEnergy<Cuboid> energyVolume = new VolumeUnaryEnergy<Cuboid>();
		// Multiplication de l'énergie d'intersection et de l'aire
		UnaryEnergy<Cuboid> energyVolumePondere = new MultipliesUnaryEnergy<Cuboid>(ponderationVolume, energyVolume);

		// On retire de l'énergie de création, l'énergie de l'aire
		UnaryEnergy<Cuboid> u3 = new MinusUnaryEnergy<Cuboid>(energyCreation, energyVolumePondere);

		// //C1 : distance à 2 m par rapport à la voirie
		UnaryEnergy<Cuboid> u6 = new AlignementEnergy<Cuboid>(distReculVoirie, bpu);
		ConstantEnergy<Cuboid, Cuboid> ponderationC1 = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_c1"));
		UnaryEnergy<Cuboid> u6Pondere = new MultipliesUnaryEnergy<Cuboid>(u6, ponderationC1);
		UnaryEnergy<Cuboid> u7 = new MinusUnaryEnergy<Cuboid>(u3, u6Pondere);

		// //C2
		UnaryEnergy<Cuboid> u8 = new ProspectEnergy<Cuboid>(slopeProspect, hIni, bpu);
		ConstantEnergy<Cuboid, Cuboid> ponderationC2 = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_c2"));
		UnaryEnergy<Cuboid> u8Pondere = new MultipliesUnaryEnergy<Cuboid>(u8, ponderationC2);
		UnaryEnergy<Cuboid> u9 = new MinusUnaryEnergy<Cuboid>(u7, u8Pondere);

		// /C3
		UnaryEnergy<Cuboid> u10 = new HauteurEnergy<Cuboid>(hMax);
		ConstantEnergy<Cuboid, Cuboid> ponderationC3 = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_c3"));
		UnaryEnergy<Cuboid> u10pondere = new MultipliesUnaryEnergy<Cuboid>(u10, ponderationC3);
		UnaryEnergy<Cuboid> u11 = new MinusUnaryEnergy<Cuboid>(u9, u10pondere);

		// C4
		UnaryEnergy<Cuboid> u12 = new ProspectEnergy2<Cuboid>(slopeProspect, bpu);
		ConstantEnergy<Cuboid, Cuboid> ponderationC4 = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_c4"));
		UnaryEnergy<Cuboid> u12pondere = new MultipliesUnaryEnergy<Cuboid>(u12, ponderationC4);
		UnaryEnergy<Cuboid> u13 = new MinusUnaryEnergy<Cuboid>(u11, u12pondere);

		// C5
		UnaryEnergy<Cuboid> u14 = new ServitudeVue<Cuboid>(distReculLimi, bpu);
		ConstantEnergy<Cuboid, Cuboid> ponderationC5 = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_c5"));
		UnaryEnergy<Cuboid> u14pondere = new MultipliesUnaryEnergy<Cuboid>(u14, ponderationC5);
		UnaryEnergy<Cuboid> u15 = new MinusUnaryEnergy<Cuboid>(u13, u14pondere);

		// Énergie constante : pondération de la différence
		ConstantEnergy<Cuboid, Cuboid> ponderationDifference = new ConstantEnergy<Cuboid, Cuboid>(
				p.getDouble("ponderation_difference_ext"));
		// On ajoute l'énergie de différence : la zone en dehors de la parcelle
		UnaryEnergy<Cuboid> u4 = new DifferenceVolumeUnaryEnergy<Cuboid>(geom);
		UnaryEnergy<Cuboid> u5 = new MultipliesUnaryEnergy<Cuboid>(ponderationDifference, u4);
		UnaryEnergy<Cuboid> unaryEnergy = new PlusUnaryEnergy<Cuboid>(u15, u5);

		// Énergie binaire : intersection entre deux rectangles
		ConstantEnergy<Cuboid, Cuboid> c3 = new ConstantEnergy<Cuboid, Cuboid>(p.getDouble("ponderation_volume_inter"));
		BinaryEnergy<Cuboid, Cuboid> b1 = new IntersectionVolumeBinaryEnergy<Cuboid>();
		BinaryEnergy<Cuboid, Cuboid> binaryEnergy = new MultipliesBinaryEnergy<Cuboid, Cuboid>(c3, b1);

		// empty initial configuration*/
		GraphConfiguration<Cuboid> conf = new GraphConfiguration<>(unaryEnergy, binaryEnergy);
		return conf;
	}

}
