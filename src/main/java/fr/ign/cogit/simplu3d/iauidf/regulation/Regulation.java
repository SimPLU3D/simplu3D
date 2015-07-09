package fr.ign.cogit.simplu3d.iauidf.regulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;

public class Regulation {

	private final static String CSV_SEPARATOR = ";";

	private static Logger log = Logger.getLogger(Regulation.class);

	// Les intitulés des colonnes
	private int code_imu, insee, date_approbation, fonctions, top_zac,
			zonage_coherent, correction_zonage, typ_bande, bande, art_5, art_6,
			art_71, art_72, art_73, art_74, art_8, art_10_top, art_101, art_13;
	private String libelle_zone, libelle_de_base, libelle_de_dul;
	private double art_9, art_102, art_12, art_14;

	public Regulation(int code_imu, String libelle_zone, int insee,
			int date_approbation, String libelle_de_base,
			String libelle_de_dul, int fonctions, int top_zac,
			int zonage_coherent, int correction_zonage, int typ_bande,
			int bande, int art_5, int art_6, int art_71, int art_72,
			int art_73, int art_74, int art_8, double art_9, int art_10_top,
			int art_101, double art_102, double art_12, int art_13,
			double art_14) {
		super();
		this.code_imu = code_imu;
		this.libelle_zone = libelle_zone;
		this.insee = insee;
		this.date_approbation = date_approbation;
		this.libelle_de_base = libelle_de_base;
		this.libelle_de_dul = libelle_de_dul;
		this.fonctions = fonctions;
		this.top_zac = top_zac;
		this.zonage_coherent = zonage_coherent;
		this.correction_zonage = correction_zonage;
		this.typ_bande = typ_bande;
		this.bande = bande;
		this.art_5 = art_5;
		this.art_6 = art_6;
		this.art_71 = art_71;
		this.art_72 = art_72;
		this.art_73 = art_73;
		this.art_74 = art_74;
		this.art_8 = art_8;
		this.art_9 = art_9;
		this.art_10_top = art_10_top;
		this.art_101 = art_101;
		this.art_102 = art_102;
		this.art_12 = art_12;
		this.art_13 = art_13;
		this.art_14 = art_14;
	}

	public Regulation(String line) {

		this(line.split(CSV_SEPARATOR));

	}

	public Regulation(String[] split) {
		this(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]),
				Integer.parseInt(split[3]), split[4], split[5], Integer
						.parseInt(split[6]), Integer.parseInt(split[7]),
				Integer.parseInt(split[8]), Integer.parseInt(split[9]), Integer
						.parseInt(split[10]), Integer.parseInt(split[11]),
				Integer.parseInt(split[12]), Integer.parseInt(split[13]),
				Integer.parseInt(split[14]), Integer.parseInt(split[15]),
				Integer.parseInt(split[16]), Integer.parseInt(split[17]),
				Integer.parseInt(split[18]), Double.parseDouble(split[19]),
				Integer.parseInt(split[20]), Integer.parseInt(split[21]),
				Double.parseDouble(split[22]), Double.parseDouble(split[23]),
				Integer.parseInt(split[24]), Double.parseDouble(split[25]));
	}

	/**
	 * Charge les règlements et les stockes dans une Map avec Integer = Code_Imu
	 * et List<Regulation> = la liste des règlements (= lignes du tableau) pour
	 * un code IMU donné
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Map<Integer, List<Regulation>> loadRegulationSet(String file)
			throws IOException {

		// On initialise la map
		Map<Integer, List<Regulation>> table = new Hashtable<>();

		// On charge le fichier CSV avec modèle IAU
		File f = new File(file);
		if (!f.exists()) {
			return table;
		}

		// On lit le fichier
		BufferedReader in = new BufferedReader(new FileReader(f));
		// On saute la première ligne car c'est une en-tête
		String line = in.readLine();
		// On traite chaque ligne
		while ((line = in.readLine()) != null) {

			log.info(line);
			// On instancier la réglementation
			Regulation r = new Regulation(line);
			// On regarde si le code imu a été rencontré auparavant
			int imu = r.getCode_imu();
			List<Regulation> lTemp = table.get(imu);
			if (lTemp == null) {
				// Ce n'est pas le cas, on ajoute une nouvelle liste à la map
				lTemp = new ArrayList<>();
				table.put(imu, lTemp);
			}
			// On ajoute la réglementation à la liste
			lTemp.add(r);

		}

		in.close();

		return table;
	}

	public int getCode_imu() {
		return code_imu;
	}

	public String getLibelle_zone() {
		return libelle_zone;
	}

	// INSEE Code de la commune
	public int getInsee() {
		return insee;
	}

	// DATE_DUL Date d'approbation du PLU
	public int getDate_approbation() {
		return date_approbation;
	}

	// LIBELLE_DE_BASE Type de zone
	public String getLibelle_de_base() {
		return libelle_de_base;
	}

	// LIBELLE_DE_DUL Type de zone
	public String getLibelle_de_dul() {
		return libelle_de_dul;
	}

	// FONCTIONS Fonction de la zone 0 : logements uniquement ; 1 : activité
	// mais possibilité de logements ; 2 : exclusivement activité
	public int getFonctions() {
		return fonctions;
	}

	// TOP_ZAC Présence d'une ZAC 0 : NON // 1 : OUI
	public int getTop_zac() {
		return top_zac;
	}

	// ZONAGE_COHERENT Zonage cohérent entre la base et le plan de zonage du DUL
	// 0 : NON // 1 : OUI
	public int getZonage_coherent() {
		return zonage_coherent;
	}

	// CORRECTION_ZONAGE Indicateur du zonage à faire prévaloir en cas
	// d'incohérence des zonages 0 : Conserver le dessin de zonage de Carto PLU
	// 1 : Numeriser le zonage du PLU analysé
	public int getCorrection_zonage() {
		return correction_zonage;
	}

	// TYP_BANDE Information concernant l'existence d'une bande principale ou
	// secondaire 0 : pas de bande // 1 : principale // 2 : secondaire
	public int getTyp_bande() {
		return typ_bande;
	}

	// BANDE Profondeur de la bande principale x > 0 profondeur de la bande par
	// rapport à la voirie
	public int getBande() {
		return bande;
	}

	// ART_5 Superficie minimale 88= non renseignable, 99= non réglementé

	public int getArt_5() {
		return art_5;
	}

	// ART_6 Distance minimale des constructions par rapport à la voirie imposée
	// en mètre 88= non renseignable, 99= non réglementé
	public int getArt_6() {
		return art_6;
	}

	// ART_71 Implantation en limite séparative 0 : non, retrait imposé (cf.72)
	// // 1 : Oui // 2 : Oui, mais sur un côté seulement
	public int getArt_71() {
		return art_71;
	}

	// ART_72 Distance minimale des constructions par rapport aux limites
	// séparatives imposée en mètre 88= non renseignable, 99= non réglementé
	public int getArt_72() {
		return art_72;
	}

	// ART_73 Distance minimale des constructions par rapport à la limte
	// séparative de fond de parcelle 88= non renseignable, 99= non réglementé
	public int getArt_73() {
		return art_73;
	}

	// ART_74 Distance minimum des constructions par rapport aux limites
	// séparatives, exprimée par rapport à la hauteur du bâtiment 0 : NON // 1 :
	// Retrait égal à la hauteur // 2 : Retrait égal à la hauteur divisé par
	// deux // 3 : Retrait égal à la hauteur divisé par trois // 4 : Retrait
	// égal à la hauteur divisé par quatre // 5 : Retrait égal à la hauteur
	// divisé par cinq // 6 : Retrait égal à la hauteur divisé par deux moins
	// trois mètres // 7 : Retrait égal à la hauteur moins trois mètres divisé
	// par deux // 8 : retrait égal à la hauteur divisé par deux moins un mètre
	// // 9 : retrait égal aux deux tiers de la hauteur // 10 : retrait égal aux
	// trois quarts de la hauteur
	public int getArt_74() {
		return art_74;
	}

	// ART_8 Distance minimale des constructions par rapport aux autres sur une
	// même propriété imposée en mètre 88= non renseignable, 99= non réglementé
	public int getArt_8() {
		return art_8;
	}

	// ART_9 Pourcentage d'emprise au sol maximum autorisé Valeur comprise de 0
	// à 1, 88= non renseignable, 99= non réglementé
	public double getArt_9() {
		return art_9;
	}

	// ART_10_TOP Indicateur de l'unité de mesure de la hauteur du batiment
	// 1_niveaux ; 2_metres du sol au point le plus haut du batiment ; 3_hauteur
	// plafond ; 4_ point le plus haut ; 5_Hauteur de façade à l'égout, 88= non
	// renseignable ; 99= non règlementé
	public int getArt_10_top() {
		return art_10_top;
	}

	// ART_101 Hauteur maximum autorisée 88= non renseignable, 99= non
	// réglementé
	public int getArt_101() {
		return art_101;
	}

	// ART_102 Hauteur maximum autorisée En mètres
	public double getArt_102() {
		return art_102;
	}

	// ART_12 Nombre de places par logement 88= non renseignable, 99= non
	// réglementé
	public double getArt_12() {
		return art_12;
	}

	// ART_13 Part minimale d'espaces libre de toute construction exprimée par
	// rapport à la surface totale de la parcelle Valeur comprise de 0 à 1, 88
	// si non renseignable, 99 si non règlementé
	public int getArt_13() {
		return art_13;
	}

	// ART_14 Coefficient d'occupation du sol 88= non renseignable, 99= non
	// réglementé
	public double getArt_14() {
		return art_14;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Regulation [code_imu=" + code_imu + ", insee=" + insee
				+ ", date_approbation=" + date_approbation + ", fonctions="
				+ fonctions + ", top_zac=" + top_zac + ", zonage_coherent="
				+ zonage_coherent + ", correction_zonage=" + correction_zonage
				+ ", typ_bande=" + typ_bande + ", bande=" + bande + ", art_5="
				+ art_5 + ", art_6=" + art_6 + ", art_71=" + art_71
				+ ", art_72=" + art_72 + ", art_73=" + art_73 + ", art_74="
				+ art_74 + ", art_8=" + art_8 + ", art_10_top=" + art_10_top
				+ ", art_101=" + art_101 + ", art_13=" + art_13
				+ ", libelle_zone=" + libelle_zone + ", libelle_de_base="
				+ libelle_de_base + ", libelle_de_dul=" + libelle_de_dul
				+ ", art_9=" + art_9 + ", art_102=" + art_102 + ", art_12="
				+ art_12 + ", art_14=" + art_14 + "]";
	}
	
	
	
	IMultiSurface<IOrientableSurface> geomBande = null;


	/**
	 * @return the geomBande
	 */
	public IMultiSurface<IOrientableSurface> getGeomBande() {
		return geomBande;
	}

	/**
	 * @param geomBande the geomBande to set
	 */
	public void setGeomBande(IMultiSurface<IOrientableSurface> geomBande) {
		this.geomBande = geomBande;
	}

	
	Geometry jtsGeometry = null;
	
	private static GeometryFactory gf = new GeometryFactory();
	
	public Geometry getJTSBand(){
		
		
		if(jtsGeometry == null){
			try {
				jtsGeometry = AdapterFactory.toGeometry(gf, this.getGeomBande());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jtsGeometry;
	}
	
}
