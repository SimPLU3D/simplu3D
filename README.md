[![Build Status](https://travis-ci.org/IGNF/simplu3D.svg?branch=master)](https://travis-ci.org/IGNF/simplu3D)

Simplu3D
============

A library to automatically general built configuration that respect urban regulations and optimize a utility function.


Introduction
---------------------

This research library is developed as part of [COGIT laboratory](http://recherche.ign.fr/labos/cogit/accueilCOGIT.php) researches concerning processing of urban regulation. 

It provides an implementation of multi-dimensionnal simulated annealing algorithm to produce built configuration from a set of boxes constrained by urban regulation that optimizes a utility function. 

The project is developed over 3D GIS Open-Source library [GeOxygene](http://oxygene-project.sourceforge.net) concerning geometric operators and 3D visualization, [librjmcmc4j](https://github.com/IGNF/librjmcmc4j) for simulated annealing implementation and [simplu3d-rules](https://github.com/IGNF/simplu3d-rules) for geographical model and regulation management.

Conditions for use
---------------------
This software is free to use under CeCILL license. However, if you use this library in a research paper, you are kindly requested to acknowledge the use of this software.

Furthermore, we are interested in every feedbacks about this library if you find it useful, if you want to contribute or if you have some suggestions to improve it.

Library installation
---------------------
The project is build with Maven and is coded in Java (JDK 1.7 or higher is required), it has been tested in most common OS. If you are not familiar with Maven, we suggest installing developer tools and versions as described in [GeOxygene install guide](http://oxygene-project.sourceforge.net/documentation/developer/install.html).

Vidéo and illustration
---------------------
[Generating a building with n boxes](https://www.youtube.com/watch?v=dH9woKexsVw)

[Generating n buildings with 1 box](https://www.youtube.com/watch?v=LwsPW0rcB44)

![Different generations with various parcels](https://github.com/IGNF/simplu3D/blob/master/readme_images/simParc.png)

Test class
---------------------
fr.ign.cogit.simplu3d.exec.BasicSimulator class using predefined resource  files is runnable. It generates a built composed by a set of intersecting boxes.

```Java
public static void main(String[] args) throws Exception {

	// Loading of configuration file that contains sampling space
	// information and simulated annealing configuration
	String folderName = BasicSimulator.class.getClassLoader().getResource("scenario/").getPath();
	String fileName = "building_parameters_project_expthese_3.xml";
	Parameters p = Parameters.unmarshall(new File(folderName + fileName));

	// Load default environment (data are in resource directory)
	Environnement env = LoaderSHP.load(new File(
			LoadDefaultEnvironment.class.getClassLoader().getResource("fr/ign/cogit/simplu3d/data/").getPath()));

	// Select a parcel on which generation is proceeded
	BasicPropertyUnit bPU = env.getBpU().get(8);

	// Instantiation of the sampler
	OptimisedBuildingsCuboidFinalDirectRejection oCB = new OptimisedBuildingsCuboidFinalDirectRejection();

	// Rules parameters
	// Distance to road
	double distReculVoirie = 0.0;
	// Distance to bottom of the parcel
	double distReculFond = 2;
	// Distance to lateral parcel limits
	double distReculLat = 4;
	// Distance between two buildings of a parcel
	double distanceInterBati = 5;
	// Maximal ratio built area
	double maximalCES = 2;

	// Instantiation of the rule checker
	SamplePredicate<Cuboid, GraphConfiguration<Cuboid>, BirthDeathModification<Cuboid>> pred = new SamplePredicate<>(
			bPU, distReculVoirie, distReculFond, distReculLat, distanceInterBati, maximalCES);

	// Run of the optimisation on a parcel with the predicate
	GraphConfiguration<Cuboid> cc = oCB.process(bPU, p, env, 1, pred);

	// Witting the output
	IFeatureCollection<IFeature> iFeatC = new FT_FeatureCollection<>();
	// For all generated boxes
	for (GraphVertex<Cuboid> v : cc.getGraph().vertexSet()) {


		//Output feature with generated geometry
		IFeature feat = new DefaultFeature(v.getValue().generated3DGeom());

		// We write some attributes
		AttributeManager.addAttribute(feat, "Longueur", Math.max(v.getValue().length, v.getValue().width),
				"Double");
		AttributeManager.addAttribute(feat, "Largeur", Math.min(v.getValue().length, v.getValue().width), "Double");
		AttributeManager.addAttribute(feat, "Hauteur", v.getValue().height, "Double");
		AttributeManager.addAttribute(feat, "Rotation", v.getValue().orientation, "Double");

		iFeatC.add(feat);

	}

	// A shapefile is written as output
	// WARNING : 'out' parameter from configuration file have to be change
	ShapefileWriter.write(iFeatC, p.get("result").toString() + "out.shp");

	System.out.println("That's all folks");

}

```

Documentation and publications
---------------------
For more information about this code, the generation process and the underlying models are described in the PhD of Mickael Brasebin (French document) :

[Brasebin, M. (2014) Les données géographiques 3D pour simuler l'impact de la réglementation urbaine sur la morphologie du bâti, Thèse de doctorat, spécialité Sciences et Technologies de l'Information Géographique, Université Paris-Est, apr 2014](http://recherche.ign.fr/labos/cogit/publiCOGITDetail.php?idpubli=5016)

Contact for feedbacks
---------------------
[Mickaël Brasebin](http://recherche.ign.fr/labos/cogit/cv.php?nom=Brasebin) & [Julien Perret](http://recherche.ign.fr/labos/cogit/cv.php?prenom=Julien&nom=Perret)
[Cogit Laboratory](http://recherche.ign.fr/labos/cogit/accueilCOGIT.php)


Users
--------------------
[IAUIDF for land price assessment : Note de conjecture ORF](http://www.orf.asso.fr/uploads/attachements/orf_nc7_ok_lg.pdf)

Acknowledgments
---------------------

+ This research is supported by the French National Mapping Agency ([IGN](http://www.ign.fr))
+ It is partially funded by the FUI TerraMagna project and by Île-de-France
Région in the context of [e-PLU projet](www.e-PLU.fr)
+ Mickaël Borne for improvements in the code and for refactoring

