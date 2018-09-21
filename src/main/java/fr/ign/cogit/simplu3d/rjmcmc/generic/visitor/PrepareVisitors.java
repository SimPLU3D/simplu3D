package fr.ign.cogit.simplu3d.rjmcmc.generic.visitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.simplu3d.model.Environnement;
import fr.ign.cogit.simplu3d.rjmcmc.generic.object.ISimPLU3DPrimitive;
import fr.ign.cogit.simplu3d.util.SimpluParameters;
import fr.ign.mpp.configuration.BirthDeathModification;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.simulatedannealing.visitor.CompositeVisitor;
import fr.ign.simulatedannealing.visitor.OutputStreamVisitor;
import fr.ign.simulatedannealing.visitor.Visitor;

public class PrepareVisitors<C extends ISimPLU3DPrimitive> {

	private List<Visitor<GraphConfiguration<C>, BirthDeathModification<C>>> lSupplementaryVisitors = new ArrayList<>();
	
	private Environnement env;
	
	public PrepareVisitors(Environnement env){
		this.env = env;
	}

	public PrepareVisitors(Environnement env,List<Visitor<GraphConfiguration<C>, BirthDeathModification<C>>> lSupplementaryVisitors) {
		this(env);
		this.lSupplementaryVisitors = lSupplementaryVisitors;
	}
	
	
	CountVisitor<GraphConfiguration<C>, BirthDeathModification<C>> countV;

	public CountVisitor<GraphConfiguration<C>, BirthDeathModification<C>> getCountV() {
		return countV;
	}

	public CompositeVisitor<GraphConfiguration<C>, BirthDeathModification<C>> prepare(SimpluParameters p, int id) {
		List<Visitor<GraphConfiguration<C>, BirthDeathModification<C>>> list = new ArrayList<>();
		
			list.addAll(lSupplementaryVisitors);
		
		if (p.getBoolean("outputstreamvisitor")) {
			Visitor<GraphConfiguration<C>, BirthDeathModification<C>> visitor = new OutputStreamVisitor<>(System.out);
			list.add(visitor);
		}

		if (p.getBoolean("shapefilewriter")) {
			ShapefileVisitor<C, GraphConfiguration<C>, BirthDeathModification<C>> shpVisitor = new ShapefileVisitor<>(
					p.get("result").toString() + "result");
			list.add(shpVisitor);
		}

		if (p.getBoolean("visitorviewer")) {
			ViewerVisitor<C, GraphConfiguration<C>, BirthDeathModification<C>> visitorViewer = new ViewerVisitor<>(env,"" + id, p);
			list.add(visitorViewer);
		}

		if (p.getBoolean("statsvisitor")) {
			StatsVisitor<C, GraphConfiguration<C>, BirthDeathModification<C>> statsViewer = new StatsVisitor<>(
					"Énergie");
			list.add(statsViewer);
		}

		if (p.getBoolean("filmvisitor")) {
			IDirectPosition dpCentre = new DirectPosition(p.getDouble("filmvisitorx"), p.getDouble("filmvisitory"),
					p.getDouble("filmvisitorz"));

			Vecteur viewTo = new Vecteur(p.getDouble("filmvisitorvectx"), p.getDouble("filmvisitorvecty"),
					p.getDouble("filmvisitorvectz")

			);

			Color c = new Color(p.getInteger("filmvisitorr"), p.getInteger("filmvisitorg"),

					p.getInteger("filmvisitorb"));

			FilmVisitor<C, GraphConfiguration<C>, BirthDeathModification<C>> visitorViewerFilmVisitor = new FilmVisitor<>(
					dpCentre, viewTo, p.getString("result"), c, p,env);
			list.add(visitorViewerFilmVisitor);
		}

		if (p.getBoolean("csvvisitorend")) {
			String fileName = p.get("result").toString() + p.get("csvfilenamend");
			CSVendStats<C, GraphConfiguration<C>, BirthDeathModification<C>> statsViewer = new CSVendStats<>(fileName);
			list.add(statsViewer);
		}
		if (p.getBoolean("csvvisitor")) {
			String fileName = p.get("result").toString() + p.get("csvfilename");
			CSVvisitor<C, GraphConfiguration<C>, BirthDeathModification<C>> statsViewer = new CSVvisitor<>(fileName);
			list.add(statsViewer);
		}

		countV = new CountVisitor<>();
		list.add(countV);
		
		
		CompositeVisitor<GraphConfiguration<C>, BirthDeathModification<C>> mVisitor = new CompositeVisitor<>(list);

		init_visitor(p, mVisitor);

		return mVisitor;
	}

	// Initialisation des visiteurs
	// nbdump => affichage dans la console
	// nbsave => sauvegarde en shapefile
	void init_visitor(SimpluParameters p, Visitor<GraphConfiguration<C>, BirthDeathModification<C>> v) {
		v.init(p.getInteger("nbdump"), p.getInteger("nbsave"));
	}
}
