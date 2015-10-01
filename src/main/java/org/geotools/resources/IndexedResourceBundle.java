
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.resources;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
// import org.opengis.util.InternationalString;

/**
 * {@link ResourceBundle} implementation using integers instead of strings for
 * resource keys. Because it doesn't use strings, this implementation avoids
 * adding all those string constants to {@code .class} files and runtime images.
 * Developers still have meaningful labels in their code (e.g.
 * {@code DIMENSION_MISMATCH}) through a set of constants defined in interfaces.
 * This approach furthermore gives the benefit of compile-time safety. Because
 * integer constants are inlined right into class files at compile time, the
 * declarative interface is never loaded at run time. This class also provides
 * facilities for string formatting using {@link MessageFormat}.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class IndexedResourceBundle extends ResourceBundle {
	/**
	 * Maximum string length for text inserted into another text. This parameter
	 * is used by {@link #summarize}. Resource strings are never cut to this
	 * length. However, text replacing "{0}" in a string like
	 * "Parameter name is {0}." will be cut to this length.
	 */
	private static final int MAX_STRING_LENGTH = 200;

	/**
	 * The resource name of the binary file containing resources. It may be a
	 * file name or an entry in a JAR file.
	 */
	private final String filename;

	/**
	 * The array of resources. Keys are an array index. For example, the value
	 * for key "14" is {@code values[14]}. This array will be loaded only when
	 * first needed. We should not load it at construction time, because some
	 * {@code ResourceBundle} objects will never ask for values. This is
	 * particularly the case for ancestor classes of {@code Resources_fr_CA},
	 * {@code Resources_en}, {@code Resources_de}, etc., which will only be used
	 * if a key has not been found in the subclass.
	 */
	private String[] values;

	/**
	 * The locale for formatting objects like number, date, etc. There are two
	 * possible Locales we could use: default locale or resource bundle locale.
	 * If the default locale uses the same language as this ResourceBundle's
	 * locale, then we will use the default locale. This allows dates and
	 * numbers to be formatted according to user conventions (e.g. French
	 * Canada) even if the ResourceBundle locale is different (e.g. standard
	 * French). However, if languages don't match, then we will use
	 * ResourceBundle locale for better coherence.
	 */
	private transient Locale locale;

	/**
	 * The object to use for formatting messages. This object will be
	 * constructed only when first needed.
	 */
	private transient MessageFormat format;

	/**
	 * The key of the last resource requested. If the same resource is requested
	 * multiple times, knowing its key allows us to avoid invoking the costly
	 * {@link MessageFormat#applyPattern} method.
	 */
	private transient int lastKey;

	/**
	 * Constructs a new resource bundle. The resource filename will be inferred
	 * from the fully qualified classname of this {@code IndexedResourceBundle}
	 * subclass.
	 */
	protected IndexedResourceBundle() {
		filename = getClass().getSimpleName() + ".utf";
	}

	/**
	 * Constructs a new resource bundle.
	 *
	 * @param filename
	 *            The resource name containing resources. It may be a filename
	 *            or an entry in a JAR file.
	 */
	protected IndexedResourceBundle(final String filename) {
		this.filename = filename;
	}

	/**
	 * Returns the locale to use for formatters.
	 */
	private Locale getFormatLocale() {
		if (locale == null) {
			locale = Locale.getDefault();
			final Locale resourceLocale = getLocale();
			if (!locale.getLanguage().equalsIgnoreCase(resourceLocale.getLanguage())) {
				locale = resourceLocale;
			}
		}
		return locale;
	}

	/**
	 * Returns the name of the package.
	 */
	private String getPackageName() {
		final String name = getClass().getName();
		final int index = name.lastIndexOf('.');
		return (index >= 0) ? name.substring(0, index) : "org.geotools";
	}

	/**
	 * Lists resources to the specified stream. If a resource has more than one
	 * line, only the first line will be written. This method is used mostly for
	 * debugging purposes.
	 *
	 * @param out
	 *            The destination stream.
	 * @throws IOException
	 *             if an output operation failed.
	 */
	public final void list(final Writer out) throws IOException {
		// Synchronization performed by 'ensureLoaded'
		list(out, ensureLoaded(null));
	}

	/**
	 * Lists resources to the specified stream. If a resource has more than one
	 * line, only the first line will be written. This method is used mostly for
	 * debugging purposes.
	 *
	 * @param out
	 *            The destination stream.
	 * @param values
	 *            The resources to list.
	 * @throws IOException
	 *             if an output operation failed.
	 */
	private static void list(final Writer out, final String[] values) throws IOException {
		final String lineSeparator = System.getProperty("line.separator", "\n");
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (value == null) {
				continue;
			}
			int indexCR = value.indexOf('\r');
			if (indexCR < 0)
				indexCR = value.length();
			int indexLF = value.indexOf('\n');
			if (indexLF < 0)
				indexLF = value.length();
			final String number = String.valueOf(i);
			out.write(Utilities.spaces(5 - number.length()));
			out.write(number);
			out.write(":\t");
			out.write(value.substring(0, Math.min(indexCR, indexLF)));
			out.write(lineSeparator);
		}
	}

	/**
	 * Ensures that resource values are loaded. If they are not, load them
	 * immediately.
	 *
	 * @param key
	 *            Key for the requested resource, or {@code null} if all
	 *            resources are requested. This key is used mostly for
	 *            constructing messages.
	 * @return The resources.
	 * @throws MissingResourceException
	 *             if this method failed to load resources.
	 */
	private String[] ensureLoaded(final String key) throws MissingResourceException {

		//System.out.println("J'entre dans le ensure Loaded");

		LogRecord record = null;
		try {
			String[] values;
			synchronized (this) {
				values = this.values;
				if (values != null) {
					return values;
				}
				/*
				 * Prepares a log record. We will wait for successful loading
				 * before posting this record. If loading fails, the record will
				 * be changed into an error record. Note that the message must
				 * be logged outside the synchronized block, otherwise there is
				 * dead locks!
				 */
				record = new LogRecord(Level.FINER, "Loaded resources for {0} from bundle \"{1}\".");
				record.setSourceClassName(getClass().getName());
				record.setSourceMethodName((key != null) ? "getObject" : "getKeys");
				/*
				 * Loads resources from the UTF file.
				 */
				InputStream in;
				String name = filename;

			//	System.out.println("Le filename est : " + filename);
				//	System.out.println("La classe courante est : " + getClass().getName());

				in = getClass().getResourceAsStream(name);

			

				if (in == null) {
					in = getClass().getClassLoader().getResourceAsStream(name);

					
				}

				if (in == null) {
					in = getClass().getClassLoader().getResourceAsStream("/" + name);

				

				}

				if (in == null) {
					in = getClass().getResourceAsStream("/" + name);

					
				}

				if (in == null) {
					in = getClass().getResourceAsStream("./" + name);

				
				}

				if (in == null) {
					in = org.geotools.renderer.i18n.Logging.class.getResourceAsStream("./" + name);

				}

				if (in == null) {
					URL url = getClass().getResource("/" + name);

					if (url != null) {
						in = url.openStream();

					}

				
				}

				if (in == null) {
					URL url = getClass().getResource("./" + name);

					if (url != null) {
						in = url.openStream();

					}

				
				}

				/*
				 * while ((in =
				 * getClass().getClassLoader().getResourceAsStream(name)) ==
				 * null) { final int ext = name.lastIndexOf('.'); final int lang
				 * = name.lastIndexOf('_', ext-1); if (lang <= 0) { throw new
				 * FileNotFoundException(filename); } name = name.substring(0,
				 * lang) + name.substring(ext); }
				 */

				if (in != null) {
				//	System.out.println("On a bien un input stream avec : " + name);
					final DataInputStream input = new DataInputStream(new BufferedInputStream(in));

					this.values = values = new String[input.readInt()];
					for (int i = 0; i < values.length; i++) {
						values[i] = input.readUTF();
						if (values[i].length() == 0)
							values[i] = null;
					}

					input.close();
				}


				if (filename.contains("Errors")) {
					//System.out.println("Je modifie l'erreur");
					values = errors;
				}

				if (filename.equals("Loggings_fr.utf")) {
					//System.out.println("Je modifie le loggings");
					values = loggingFR;
				}

				if (filename.equals("Vocabulary.utf") || filename.equals("Vocabulary_fr.utf")
						|| filename.equals("Vocabulary_en.utf")) {

					//System.out.println("Je modifie le vocabulaire");
					values = vocabulary;
				}

				/*
				 * Now, log the message. This message is not localized.
				 */
				String language = getLocale().getDisplayName(Locale.US);
				if (language == null || language.length() == 0) {
					language = "<default>";
				}
				record.setParameters(new String[] { language, getPackageName() });
			}
			final Logger logger = Logging.getLogger(IndexedResourceBundle.class);
			record.setLoggerName(logger.getName());
			logger.log(record);
			return values;
		} catch (IOException exception) {

			record.setLevel(Level.WARNING);
			record.setMessage(exception.getLocalizedMessage());
			record.setThrown(exception);
			final Logger logger = Logging.getLogger(IndexedResourceBundle.class);
			record.setLoggerName(logger.getName());
			logger.log(record);
			final MissingResourceException error = new MissingResourceException(exception.getLocalizedMessage(),
					getClass().getName(), key);
			error.initCause(exception);
			throw error;
		}
	}

	private final String[] errors = { "La coordonnée \"{0}\" n''est pas valide.", "Saisie invalide",
			"Rectangle vide ou invalide: {0}", "Les longueurs des axes sont ambiguës.", "L''angle {0} est trop élevé.",
			"Les latitudes {0} et {1} sont aux antipodes.", "L''azimut {0} est en dehors des limites permises (±180°).",
			"Le numéro de bande {0} n''est pas valide.", "Le coefficient {0}={1} ne doit pas être NaN ou infini.",
			"La plage d''index [{1} .. {2}] de la dimension {0} n''est pas valide.",
			"Données invalides à la ligne {1} du fichier \"{0}\".", "Code de langue inconu: {0}",
			"Le paramètre \"{0}\" ne doit pas avoir la valeur \"{1}\".",
			"Le paramètre \"{0}\" ne peut pas être du type ''{1}''.", "La plage [{0} .. {1}] n''est pas valide.",
			"Transformation de type \"{0}\" illégale.",
			"La multiplication ou division des {0} par des {1} n''est pas permise.",
			"Les unités {1} ne peuvent pas être élevées à la puissance {0}.",
			"Les paramètres de Bursa Wolf sont requis.", "La dérivé ne peut pas être calculée.",
			"Les transformations \"{0}\" et \"{1}\" ne peuvent pas être combinées.",
			"Échec lors de la connexion à la base de données {0}.",
			"La valeur ne peut pas être convertie à partir du type ''{0}''.",
			"Échec lors de la création d''une fabrique de type \"{0}\".",
			"Un objet de type ''{0}'' ne peut pas être créé à partir d''un texte.",
			"Une erreur s'est produite durant le rognage de l'image.",
			"La valeur à la coordonnée ({0}) ne peut pas être évaluée.",
			"La source de données \"{0}\" n''a pas pu être obtenue.", "Échec lors de la lecture du fichier \"{0}\".",
			"Le système de coordonnées \"{0}\" ne peut pas être réduit à deux dimensions.",
			"Ne peut pas reprojeter la couverture \"{0}\".", "Ne peut pas séparer le SRC \"{0}\".",
			"Ne peut pas définir une valeur pour le paramètre \"{0}\".", "Ne peut pas transformer l'enveloppe.",
			"Des points qui devraient être valides n'ont pas pu être transformés.",
			"Le graphique \"{0}\" appartient à un autre afficheur.", "Les axes {0} et {1} sont colinéaires.",
			"La couverture retournée par ''{0}'' est déjà une vue de la couverture \"{1}\".",
			"Échec lors de la création d''un objet ''{0}'' pour le code \"{1}\".",
			"La date {0} est en dehors de la plage de temps des données disponibles.",
			"La destination n'a pas été définie.", "La direction n'a pas été définie.", "La fabrique a été fermée.",
			"La distance {0} est en dehors des limites permises ({1} à {2} {3})",
			"Plusieurs valeurs correspondent au code \"{0}\".", "Projection elliptique non-supportée.",
			"Le tableau doit contenir au moins un élément.",
			"L'enveloppe doit avoir au moins deux dimensions et ne pas être vide.", "Fin de fichier inatendu.",
			"Aucune fabrique de type \"{0}\" n''a été trouvée.",
			"Le fichier n''a pas été trouvé ou ne peut pas être lu: {0}", "Le fichier contient trop peu de données.",
			"Les extensions de Geotools sont requises pour l''opération \"{0}\".",
			"Les latitudes et longitudes ne sont pas espacées de façon régulière.",
			"En-tête d''une longueur inatendu: {0}", "Le trou n'est pas à l'intérieur du polygone.",
			"Le modèle \"{0}\" n''est pas valide pour lire et écrire des angles.",
			"Valeur illégale pour l''argument \"{0}\".", "L''argument \"{0}={1}\" est illegal.",
			"La longueur du tableau n''est pas valide pour des points de dimension {0}.",
			"Un axe ne peut pas être orienté \"{0}\" dans les systèmes de coordonnées de classe \"{1}\".",
			"La classe ''{0}'' est illégale. Il doit s''agir d''une classe ''{1}'' ou dérivée.",
			"Système de référence de coordonnées illégal.",
			"Le système de coordonnées de type ''{0}'' est incompatible avec le SRC de type ''{1}''.",
			"Le système de coordonnées ne peut pas avoir {0} dimensions.",
			"Descripteur illégal pour le paramètre \"{0}\".", "Les ordonnées de la dimension {0} ne sont pas valides.",
			"\"{0}\" n''est pas un identifiant valide.", "L''instruction \"{0}\" n''est pas reconnue.",
			"Clé illégale: {0}", "La dimension de la matrice est illégale.",
			"Le paramètre \"{0}\" apparaît {1} fois, alors que le nombre d''occurences attendu était [{2}..{3}].",
			"Cette opération ne peut pas s''appliquer aux valeurs de classe ''{0}''.",
			"Type de système de coordonnées incompatible.",
			"Le paramètre de projection \"{0}\" est incompatible avec l''ellipsoïde \"{1}\".",
			"Les géométries des grilles ne sont pas compatibles.", "L''unité \"{0}\" n''est pas compatible.",
			"La direction \"{1}\" n''est pas cohérente avec l''axe \"{0}\".",
			"La valeur de la propriété \"{0}\" n''est pas cohérente avec celles des autres propriétés.",
			"L''index {0} est en dehors des limites permises.", "La valeur {0} est infinie.",
			"La transformation n'est pas séparable.", "{0} points étaient spécifiés, alors que {1} sont requis.",
			"Cet objet de type \"{0}\" est trop complexe pour la syntaxe WKT.", "Erreur dans \"{0}\":",
			"La latitude {0} est en dehors des limites permises (±90°).",
			"La ligne contient {0} colonnes alors qu''on n''en attendait que {1}. Le texte \"{2}\" est en trop.",
			"La ligne ne contient que {0} colonnes alors qu''on en attendait {1}.",
			"La longitude {0} est en dehors des limites permises (±180°).", "L'enveloppe présente des incohérences.",
			"Toutes les lignes n'ont pas la même longueur.", "Les tableaux n'ont pas la même longueur.",
			"Le système de référence des coordonnées doit être le même pour tous les objets.",
			"Les dimensions des objets ne concordent pas: ils ont {0}D et {1}D.",
			"L''argument \"{0}\" a {1} dimensions, alors qu''on en attendait {2}.",
			"L''enveloppe utilise un système de référence des coordonnées incompatible, soit \"{1}\" alors qu''on attendait \"{0}\".",
			"Aucune autorité n''a été spécifiée pour le code \"{0}\". Le format attendu est habituellement \"AUTORITÉ:NOMBRE\".",
			"Un caractère ''{0}'' était attendu.", "Cette opération requiert le module \"{0}\".",
			"Le paramètre \"{0}\" est manquant.", "Il manque la valeur du paramètre \"{0}\".",
			"Définition WKT manquante.",
			"Des catégories géophysiques sont mélangées avec des catégories non-géophysiques.",
			"Le numéro de colonne des \"{0}\" ({1}) ne doit pas être négatif.",
			"La transformation affine pour redimensionner l'image n'est pas invertible.",
			"La transformation n'est pas invertible.",
			"La transformation de la grille vers le système de coordonnées doit être affine.",
			"\"{0}\" n''est pas une unité d''angles.", "Le système de coordonnées \"{0}\" n''est pas cartésien.",
			"Les données exprimées en {1} ne peuvent pas être converties en {0}.",
			"Les parenthèses ne sont pas équilibrées dans \"{0}\". Il manque ''{1}''.",
			"Certaines catégories utilisent des valeurs qui ne sont pas entières.", "La relation n'est pas linéaire.",
			"\"{0}\" n''est pas une unité de longueurs.",
			"La conversion des unités \"{0}\" vers \"{1}\" n''est pas linéaire.",
			"Les directions {0} et {1} ne sont pas perpendiculaires.", "\"{0}\" n''est pas une unité d''échelle.",
			"\"{0}\" n''est pas une unité de temps.", "La transformation n'est pas affine.",
			"La classe \"{0}\" n''est pas celle d''un angle.", "Le nombre \"{0}\" n''est pas en entier valide.",
			"Les points ne semblent pas être distribués sur une grille régulière.",
			"Un nombre réel était attendu. \"{0}\" n''est pas valide.", "La classe {0} n''est pas comparable.",
			"Le nombre {0} n''est pas valide. On attendait un nombre réel non-nul.",
			"Le nombre {0} n''est pas valide. On attendait un nombre positif non-nul.",
			"Le système de coordonnées n'est pas tri-dimensionel.",
			"Un objet à {0} dimensions ne peut pas être ramené à 2 dimensions.",
			"Aucune catégorie n''est définie pour la valeur {0}.", "La transformation ne converge pas.",
			"Le calcul ne converge pas pour les points {0} et {1}.", "Aucune source de données n'a été trouvée.",
			"Aucune entrée n'a été spécifiée pour lire l'image.",
			"Aucune sortie n'a été spécifiée pour écrire l'image.",
			"Aucun décodeur d'image n'a été trouvé pour cette source.",
			"Aucun encodeur d'image n'a été trouvé pour cette destination.", "Aucun axe source ne correspond à {0}.",
			"Aucun objet de type \"{0}\" n''a été trouvé pour le code \"{1}\".",
			"Aucun code \"{0}\" de l''autorité \"{1}\" n''a été trouvé pour un objet de type \"{2}\".",
			"Aucune transformation à deux dimensions n'est disponible pour cette géométrie.",
			"Aucune transformation du système \"{0}\" vers \"{1}\" n''est disponible.",
			"Aucune transformation de classe \"{0}\" n''est définie.", "Des unités doivent être spécifiées.",
			"L''argument \"{0}\" ne doit pas être nul.", "L''attribut \"{0}\" ne doit pas être nul.",
			"Le format #{0} (sur {1}) n''est pas défini.", "Le paramètre \"{0}\" doit être non-nul et du type \"{1}\".",
			"Dans la table \"{2}\", la colonne \"{1}\" de l''enregistrement \"{0}\" ne devrait pas contenir de valeur nulle.",
			"Le nombre de bandes de l''image ({0}) ne correspond pas au nombre d''objets ''{2}'' spécifiés ({1}).",
			"Un tableau de longueur paire était attendu. La longueur {0} n''est pas valide.",
			"L''opération \"{0}\" est déjà définie.", "L''opération \"{0}\" est déjà déclarée dans ce processeur.",
			"L''opération \"{0}\" n''est pas gérée par ce processeur.",
			"Possible usage de la projection \"{0}\" en dehors de sa région de validité.",
			"Le nom ou un alias pour le paramètre \"{0}\" à l''index {1} duplique le nom \"{2}\" à l''index {3}.",
			"Le texte \"{0}\" n''a pas pu être interprété. Vérifiez les caractères \"{1}\".",
			"La coordonnée ({0}) est en dehors des limites permises.", "Des points sont en dehors de la grille.",
			"Point en dehors de l'hémisphere de projection.", "La latitude {0} est trop proche d''un pôle.",
			"On ne peut pas ajouter des points à un polygone déjà fermé.",
			"Le résultat de la projection pourrait se trouver à {0} mètres de la position attendue. Êtes-vous certain que les coordonnées sont dans la région de validité de cette projection cartographique? Le point est situé à {1} du méridien central et à {2} de la latitude d''origine. La projection est \"{3}\".",
			"Les plages [{0}..{1}] et [{2}..{3}] se chevauchent.",
			"Appels recursifs lors de la création d''un objet ''{0}''.",
			"Appels recursifs lors de la création d''un objet ''{0}'' pour le code \"{1}\".",
			"La valeur RGB {0} est en dehors des limites attendues.", "L'exécution par une machine distante a échouée.",
			"On attendait {0}={1} mais on a obtenu {2}.", "Tolérance dépassée.",
			"Trop d''occurences de \"{0}\". Il en existe déjà {1}.", "Propriété non-définie.",
			"La propriété \"{0}\" n''est pas définie.", "Argument innatendu pour l''opération \"{0}\".",
			"Dimension innatendue pour un système de coordonnées de type \"{0}\".", "Fin de texte inatendue.",
			"L'image n'a pas la taille attendue.", "Le paramètre \"{0}\" est en trop.",
			"La ligne {0} de la matrice a une longueur de {1}, alors qu''on attendait {2}.",
			"La transformation n'a pas produit les valeurs attendues.", "Le paramètre \"{0}\" n''a pas d''unité.",
			"L''autorité \"{0}\" n''est pas connue ou ne correspond pas aux indices spécifiés. Le fichier JAR qui la définie n''est peut-être pas accessible?",
			"La direction \"{0}\" n''est pas connue pour un axe.", "Le format d''image \"{0}\" n''est pas reconnu.",
			"L''interpolation \"{0}\" n''est pas reconnue.", "Paramètre inconnu: {0}", "Nom de paramètre inconu: {0}",
			"Type de projection inconnu.", "Le type \"{0}\" n''est pas reconnu dans ce contexte.",
			"Cette transformation affine n'est pas modifiable.", "La géométrie n'est pas modifiable.",
			"La méta-donnée n'est pas modifiable.", "Ne peut pas décoder \"{0}\" comme un nombre.",
			"Ne peut pas décoder \"{0}\" car le texte \"{1}\" n''est pas reconnu.",
			"Le système de référence des coordonnées n'a pas été spécifié.",
			"La taille de l'image n'a pas été spécifiée.", "La transformation de coordonnées n'a pas été spécifiée.",
			"Le système de coordonnées \"{0}\" n''est pas supporté.",
			"Le système de référence des coordonnées \"{0}\" n''est pas supporté.",
			"Ce type de données n'est pas pris en charge.", "Le type de données \"{0}\" n''est pas pris en charge.",
			"Type de fichier non-supporté: {0} ou {1}", "La valeur {0} est en dehors de la plage [{1}..{2}].",
			"La valeur numérique tend vers l'infini.",
			"Aucune variable \"{0}\" n''a été trouvée dans le fichier \"{1}\".",
			"La valeur {1} est en dehors du domaine de la couverture \"{0}\".", "null", "Longitude sphérique",
			"Date de début", "Projection stéréographique", "Système", "Destination", "SRC destination",
			"Point destination", "Tâches", "Temporel", "Taille des tuiles", "Capacité de la cache des tuiles: {0} Mo",
			"{0}×{1} tuiles de {2} × {3} pixels", "Temps", "Heure", "Plage de temps", "Fuseau horaire",
			"Transformation", "Précision de la transformation", "Transparence", "Projection Mercator transverse",
			"VRAI", "Julien tronqué", "Type", "Non-défini", "Inconnu", "Entier non-signé sur {0} bits ({1} bits/pixel)",
			"Sans-titre", "Haut", "Utiliser la meilleure résolution disponible", "Heure universelle (UTC)", "Valeur",
			"Vendeur", "Version {0}", "Version \"{0}\"", "Vertical", "Composante verticale", "Avertissement", "Ouest",
			"Chemin ouest", "Jaune", "Zoom avant", "Zoom rapproché", "Zoom arrière", "null", "null", "null", "Inconnu",
			"J'entre dans le ensure Loaded", "J'entre dans le ensure Loaded", "Le filename est : Errors_fr.utf",
			"La classe courante est : org.geotools.resources.i18n.Errors_fr", "0",
			"On a bien un input stream avec : Errors_fr.utf", "La coordonnée \"{0}\" n''est pas valide.",
			"Saisie invalide", "Rectangle vide ou invalide: {0}", "Les longueurs des axes sont ambiguës.",
			"L''angle {0} est trop élevé.", "Les latitudes {0} et {1} sont aux antipodes.",
			"L''azimut {0} est en dehors des limites permises (±180°).", "Le numéro de bande {0} n''est pas valide.",
			"Le coefficient {0}={1} ne doit pas être NaN ou infini.",
			"La plage d''index [{1} .. {2}] de la dimension {0} n''est pas valide.",
			"Données invalides à la ligne {1} du fichier \"{0}\".", "Code de langue inconu: {0}",
			"Le paramètre \"{0}\" ne doit pas avoir la valeur \"{1}\".",
			"Le paramètre \"{0}\" ne peut pas être du type ''{1}''.", "La plage [{0} .. {1}] n''est pas valide.",
			"Transformation de type \"{0}\" illégale.",
			"La multiplication ou division des {0} par des {1} n''est pas permise.",
			"Les unités {1} ne peuvent pas être élevées à la puissance {0}.",
			"Les paramètres de Bursa Wolf sont requis.", "La dérivé ne peut pas être calculée.",
			"Les transformations \"{0}\" et \"{1}\" ne peuvent pas être combinées.",
			"Échec lors de la connexion à la base de données {0}.",
			"La valeur ne peut pas être convertie à partir du type ''{0}''.",
			"Échec lors de la création d''une fabrique de type \"{0}\".",
			"Un objet de type ''{0}'' ne peut pas être créé à partir d''un texte.",
			"Une erreur s'est produite durant le rognage de l'image.",
			"La valeur à la coordonnée ({0}) ne peut pas être évaluée.",
			"La source de données \"{0}\" n''a pas pu être obtenue.", "Échec lors de la lecture du fichier \"{0}\".",
			"Le système de coordonnées \"{0}\" ne peut pas être réduit à deux dimensions.",
			"Ne peut pas reprojeter la couverture \"{0}\".", "Ne peut pas séparer le SRC \"{0}\".",
			"Ne peut pas définir une valeur pour le paramètre \"{0}\".", "Ne peut pas transformer l'enveloppe.",
			"Des points qui devraient être valides n'ont pas pu être transformés.",
			"Le graphique \"{0}\" appartient à un autre afficheur.", "Les axes {0} et {1} sont colinéaires.",
			"La couverture retournée par ''{0}'' est déjà une vue de la couverture \"{1}\".",
			"Échec lors de la création d''un objet ''{0}'' pour le code \"{1}\".",
			"La date {0} est en dehors de la plage de temps des données disponibles.",
			"La destination n'a pas été définie.", "La direction n'a pas été définie.", "La fabrique a été fermée.",
			"La distance {0} est en dehors des limites permises ({1} à {2} {3})",
			"Plusieurs valeurs correspondent au code \"{0}\".", "Projection elliptique non-supportée.",
			"Le tableau doit contenir au moins un élément.",
			"L'enveloppe doit avoir au moins deux dimensions et ne pas être vide.", "Fin de fichier inatendu.",
			"Aucune fabrique de type \"{0}\" n''a été trouvée.",
			"Le fichier n''a pas été trouvé ou ne peut pas être lu: {0}", "Le fichier contient trop peu de données.",
			"Les extensions de Geotools sont requises pour l''opération \"{0}\".",
			"Les latitudes et longitudes ne sont pas espacées de façon régulière.",
			"En-tête d''une longueur inatendu: {0}", "Le trou n'est pas à l'intérieur du polygone.",
			"Le modèle \"{0}\" n''est pas valide pour lire et écrire des angles.",
			"Valeur illégale pour l''argument \"{0}\".", "L''argument \"{0}={1}\" est illegal.",
			"La longueur du tableau n''est pas valide pour des points de dimension {0}.",
			"Un axe ne peut pas être orienté \"{0}\" dans les systèmes de coordonnées de classe \"{1}\".",
			"La classe ''{0}'' est illégale. Il doit s''agir d''une classe ''{1}'' ou dérivée.",
			"Système de référence de coordonnées illégal.",
			"Le système de coordonnées de type ''{0}'' est incompatible avec le SRC de type ''{1}''.",
			"Le système de coordonnées ne peut pas avoir {0} dimensions.",
			"Descripteur illégal pour le paramètre \"{0}\".", "Les ordonnées de la dimension {0} ne sont pas valides.",
			"\"{0}\" n''est pas un identifiant valide.", "L''instruction \"{0}\" n''est pas reconnue.",
			"Clé illégale: {0}", "La dimension de la matrice est illégale.",
			"Le paramètre \"{0}\" apparaît {1} fois, alors que le nombre d''occurences attendu était [{2}..{3}].",
			"Cette opération ne peut pas s''appliquer aux valeurs de classe ''{0}''.",
			"Type de système de coordonnées incompatible.",
			"Le paramètre de projection \"{0}\" est incompatible avec l''ellipsoïde \"{1}\".",
			"Les géométries des grilles ne sont pas compatibles.", "L''unité \"{0}\" n''est pas compatible.",
			"La direction \"{1}\" n''est pas cohérente avec l''axe \"{0}\".",
			"La valeur de la propriété \"{0}\" n''est pas cohérente avec celles des autres propriétés.",
			"L''index {0} est en dehors des limites permises.", "La valeur {0} est infinie.",
			"La transformation n'est pas séparable.", "{0} points étaient spécifiés, alors que {1} sont requis.",
			"Cet objet de type \"{0}\" est trop complexe pour la syntaxe WKT.", "Erreur dans \"{0}\":",
			"La latitude {0} est en dehors des limites permises (±90°).",
			"La ligne contient {0} colonnes alors qu''on n''en attendait que {1}. Le texte \"{2}\" est en trop.",
			"La ligne ne contient que {0} colonnes alors qu''on en attendait {1}.",
			"La longitude {0} est en dehors des limites permises (±180°).", "L'enveloppe présente des incohérences.",
			"Toutes les lignes n'ont pas la même longueur.", "Les tableaux n'ont pas la même longueur.",
			"Le système de référence des coordonnées doit être le même pour tous les objets.",
			"Les dimensions des objets ne concordent pas: ils ont {0}D et {1}D.",
			"L''argument \"{0}\" a {1} dimensions, alors qu''on en attendait {2}.",
			"L''enveloppe utilise un système de référence des coordonnées incompatible, soit \"{1}\" alors qu''on attendait \"{0}\".",
			"Aucune autorité n''a été spécifiée pour le code \"{0}\". Le format attendu est habituellement \"AUTORITÉ:NOMBRE\".",
			"Un caractère ''{0}'' était attendu.", "Cette opération requiert le module \"{0}\".",
			"Le paramètre \"{0}\" est manquant.", "Il manque la valeur du paramètre \"{0}\".",
			"Définition WKT manquante.",
			"Des catégories géophysiques sont mélangées avec des catégories non-géophysiques.",
			"Le numéro de colonne des \"{0}\" ({1}) ne doit pas être négatif.",
			"La transformation affine pour redimensionner l'image n'est pas invertible.",
			"La transformation n'est pas invertible.",
			"La transformation de la grille vers le système de coordonnées doit être affine.",
			"\"{0}\" n''est pas une unité d''angles.", "Le système de coordonnées \"{0}\" n''est pas cartésien.",
			"Les données exprimées en {1} ne peuvent pas être converties en {0}.",
			"Les parenthèses ne sont pas équilibrées dans \"{0}\". Il manque ''{1}''.",
			"Certaines catégories utilisent des valeurs qui ne sont pas entières.", "La relation n'est pas linéaire.",
			"\"{0}\" n''est pas une unité de longueurs.",
			"La conversion des unités \"{0}\" vers \"{1}\" n''est pas linéaire.",
			"Les directions {0} et {1} ne sont pas perpendiculaires.", "\"{0}\" n''est pas une unité d''échelle.",
			"\"{0}\" n''est pas une unité de temps.", "La transformation n'est pas affine.",
			"La classe \"{0}\" n''est pas celle d''un angle.", "Le nombre \"{0}\" n''est pas en entier valide.",
			"Les points ne semblent pas être distribués sur une grille régulière.",
			"Un nombre réel était attendu. \"{0}\" n''est pas valide.", "La classe {0} n''est pas comparable.",
			"Le nombre {0} n''est pas valide. On attendait un nombre réel non-nul.",
			"Le nombre {0} n''est pas valide. On attendait un nombre positif non-nul.",
			"Le système de coordonnées n'est pas tri-dimensionel.",
			"Un objet à {0} dimensions ne peut pas être ramené à 2 dimensions.",
			"Aucune catégorie n''est définie pour la valeur {0}.", "La transformation ne converge pas.",
			"Le calcul ne converge pas pour les points {0} et {1}.", "Aucune source de données n'a été trouvée.",
			"Aucune entrée n'a été spécifiée pour lire l'image.",
			"Aucune sortie n'a été spécifiée pour écrire l'image.",
			"Aucun décodeur d'image n'a été trouvé pour cette source.",
			"Aucun encodeur d'image n'a été trouvé pour cette destination.", "Aucun axe source ne correspond à {0}.",
			"Aucun objet de type \"{0}\" n''a été trouvé pour le code \"{1}\".",
			"Aucun code \"{0}\" de l''autorité \"{1}\" n''a été trouvé pour un objet de type \"{2}\".",
			"Aucune transformation à deux dimensions n'est disponible pour cette géométrie.",
			"Aucune transformation du système \"{0}\" vers \"{1}\" n''est disponible.",
			"Aucune transformation de classe \"{0}\" n''est définie.", "Des unités doivent être spécifiées.",
			"L''argument \"{0}\" ne doit pas être nul.", "L''attribut \"{0}\" ne doit pas être nul.",
			"Le format #{0} (sur {1}) n''est pas défini.", "Le paramètre \"{0}\" doit être non-nul et du type \"{1}\".",
			"Dans la table \"{2}\", la colonne \"{1}\" de l''enregistrement \"{0}\" ne devrait pas contenir de valeur nulle.",
			"Le nombre de bandes de l''image ({0}) ne correspond pas au nombre d''objets ''{2}'' spécifiés ({1}).",
			"Un tableau de longueur paire était attendu. La longueur {0} n''est pas valide.",
			"L''opération \"{0}\" est déjà définie.", "L''opération \"{0}\" est déjà déclarée dans ce processeur.",
			"L''opération \"{0}\" n''est pas gérée par ce processeur.",
			"Possible usage de la projection \"{0}\" en dehors de sa région de validité.",
			"Le nom ou un alias pour le paramètre \"{0}\" à l''index {1} duplique le nom \"{2}\" à l''index {3}.",
			"Le texte \"{0}\" n''a pas pu être interprété. Vérifiez les caractères \"{1}\".",
			"La coordonnée ({0}) est en dehors des limites permises.", "Des points sont en dehors de la grille.",
			"Point en dehors de l'hémisphere de projection.", "La latitude {0} est trop proche d''un pôle.",
			"On ne peut pas ajouter des points à un polygone déjà fermé.",
			"Le résultat de la projection pourrait se trouver à {0} mètres de la position attendue. Êtes-vous certain que les coordonnées sont dans la région de validité de cette projection cartographique? Le point est situé à {1} du méridien central et à {2} de la latitude d''origine. La projection est \"{3}\".",
			"Les plages [{0}..{1}] et [{2}..{3}] se chevauchent.",
			"Appels recursifs lors de la création d''un objet ''{0}''.",
			"Appels recursifs lors de la création d''un objet ''{0}'' pour le code \"{1}\".",
			"La valeur RGB {0} est en dehors des limites attendues.", "L'exécution par une machine distante a échouée.",
			"On attendait {0}={1} mais on a obtenu {2}.", "Tolérance dépassée.",
			"Trop d''occurences de \"{0}\". Il en existe déjà {1}.", "Propriété non-définie.",
			"La propriété \"{0}\" n''est pas définie.", "Argument innatendu pour l''opération \"{0}\".",
			"Dimension innatendue pour un système de coordonnées de type \"{0}\".", "Fin de texte inatendue.",
			"L'image n'a pas la taille attendue.", "Le paramètre \"{0}\" est en trop.",
			"La ligne {0} de la matrice a une longueur de {1}, alors qu''on attendait {2}.",
			"La transformation n'a pas produit les valeurs attendues.", "Le paramètre \"{0}\" n''a pas d''unité.",
			"L''autorité \"{0}\" n''est pas connue ou ne correspond pas aux indices spécifiés. Le fichier JAR qui la définie n''est peut-être pas accessible?",
			"La direction \"{0}\" n''est pas connue pour un axe.", "Le format d''image \"{0}\" n''est pas reconnu.",
			"L''interpolation \"{0}\" n''est pas reconnue.", "Paramètre inconnu: {0}", "Nom de paramètre inconu: {0}",
			"Type de projection inconnu.", "Le type \"{0}\" n''est pas reconnu dans ce contexte.",
			"Cette transformation affine n'est pas modifiable.", "La géométrie n'est pas modifiable.",
			"La méta-donnée n'est pas modifiable.", "Ne peut pas décoder \"{0}\" comme un nombre.",
			"Ne peut pas décoder \"{0}\" car le texte \"{1}\" n''est pas reconnu.",
			"Le système de référence des coordonnées n'a pas été spécifié.",
			"La taille de l'image n'a pas été spécifiée.", "La transformation de coordonnées n'a pas été spécifiée.",
			"Le système de coordonnées \"{0}\" n''est pas supporté.",
			"Le système de référence des coordonnées \"{0}\" n''est pas supporté.",
			"Ce type de données n'est pas pris en charge.", "Le type de données \"{0}\" n''est pas pris en charge.",
			"Type de fichier non-supporté: {0} ou {1}", "La valeur {0} est en dehors de la plage [{1}..{2}].",
			"La valeur numérique tend vers l'infini.",
			"Aucune variable \"{0}\" n''a été trouvée dans le fichier \"{1}\".",
			"La valeur {1} est en dehors du domaine de la couverture \"{0}\".", null };

	private final String[] vocabulary = { "A propos de", "Transformation de Molodenski abrégée",
			"Transformation affine", "Projection Albers équivalent", "Tous", "Altitude", "Autorité",
			"Ajustements d'axes", "Azimuth", "Bande", "Altitude barométrique", "Noir", "Bleu", "Annuler", "Cartésien",
			"Cartésien 2D", "Cartésien 3D", "Catégorie", "Classe", "Classique", "Fermer", "Code", "Couleurs",
			"{0} couleurs", "Modèle de couleurs", "Colonne", "Colonnes", "Compare avec", "Conversion",
			"Conversion et transformation", "Sélection des coordonnées", "Format des coordonnées",
			"Système de référence des coordonnées", "Cyan", "Projection Mercator cylindrique",
			"Moteur de base de données", "URL de la base de données", "Base de données {0} version {1} sur moteur {2}.",
			"Type de données", "{0,choice,0.0#Entier non-signé|1.0#Entier signé|2.0#Nombre réel} sur {1} bits",
			"Changement de référentiel", "Débug", "Décodeurs", "Valeur par défaut", "Profondeur", "Dérivé de {0}",
			"Description", "Discontinu", "Affichage", "Distance", "Bas", "Julien Dublin", "Valeur dupliquée",
			"Modèle gravitationnel terrestre", "Est", "Chemin est", "Ellipsoïde", "Ellipsoïdal", "Hauteur ellipsoïdale",
			"Changement d'ellipsoïde", "Encodeurs", "Date de fin", "Projection équidistante cylindrique", "Erreur",
			"Erreur - {0}", "Filtres d'erreurs", "Journal des événements", "Exception", "Exponentiel", "Fabrique",
			"FAUX", "Fichier {0}", "Ligne {1} dans le fichier {0}", "Format", "Futur", "Cartésien générique 2D",
			"Cartésien générique 3D", "Géocentrique", "Rayon géocentrique", "Transformation géocentrique",
			"X géocentrique", "Y géocentrique", "Z géocentrique", "Géodésique 2D", "Géodésique 3D",
			"Latitude géodésique", "Longitude géodésique", "Coordonnées géographiques", "Geoïdal",
			"Géoïde dérivé d'un modèle", "Géophysique", "Heure de Greenwich (GMT)", "Masques de gradients",
			"Hauteur par gravité", "Gris", "Vert", "Grille", "Cacher", "Caché", "Horizontal", "Composante horizontale",
			"Teinte", "Identité", "Images", "Image de classe \"{0}\"", "Taille de l'image",
			"{0} × {1} pixels × {2} bandes", "Implémentations", "Index", "Indexé", "Informations", "Intérieur",
			"{0} inverse", "Opération inverse", "Transformation inverse", "{0}", "Java version {0}", "Julien",
			"Matrice", "Projection Lambert conique conforme", "Latitude", "Gauche", "Niveau", "Brillance", "Lignes",
			"Ligne {0}", "Chargement de {0}...", "Chargement des images {0} et {1}", "Chargement de l''image {0}",
			"Local", "Logarithmique", "Enregistreur", "Longitude", "Magenta", "Loupe", "Transformation mathématique",
			"Maximum", "Mémoire réservée: {0} Mo", "Réservation utilisée à {0,number,percent}", "Message", "Méthode",
			"Minimum", "Julien modifié", "Transformation de Molodenski", "... {0} de plus...", "Transformation NADCOM",
			"Nom", "Absence de données", "Normal", "Nord", "Chemin nord", "Note",
			"{0} (il n''y a pas d''autre information sur cette erreur).", "Aucun doublon trouvé.",
			"Projection de Mercator oblique", "Ok", "Opérations", "Opération \"{0}\"", "Ordre",
			"Distance orthodromique", "Projection orthographique", "Orthométrique", "Système {0}",
			"Version {0} pour {1}", "Autre", "Autres", "Extérieur", "Palette", "Paramètre {0}", "Passé", "Personnalisé",
			"Pixels", "{0} points sur une grille de {1} × {2}.", "Matrices prédéfinies",
			"Résolution approximative désirée", "Aperçu", "État d'avancement", "Projeté", "Propriétés",
			"Nombre réel sur {0} bits", "Région: x=[{0} .. {1}], y=[{2} .. {3}]", "Rouge", "Rétablir", "Droite",
			"Valeur RMS de l'erreur.", "Rotation gauche", "Rotation droite", "Ligne", "Tâches en cours",
			"Modèle de données", "Saturation", "Enregistrement de {0}...", "Échelle 1:{0} (approximatif)", "Chercher",
			"Décimer à la résolution spécifiée", "Afficher la loupe", "Entier signé sur {0} bits", "Taille",
			"{0} × {1}", "(en minutes d'angle)", "SRC source", "Point source", "Sud", "Chemin sud", "Sphérique",
			"Latitude sphérique", "Longitude sphérique", "Date de début", "Projection stéréographique", "Système",
			"Destination", "SRC destination", "Point destination", "Tâches", "Temporel", "Taille des tuiles",
			"Capacité de la cache des tuiles: {0} Mo", "{0}×{1} tuiles de {2} × {3} pixels", "Temps", "Heure",
			"Plage de temps", "Fuseau horaire", "Transformation", "Précision de la transformation", "Transparence",
			"Projection Mercator transverse", "VRAI", "Julien tronqué", "Type", "Non-défini", "Inconnu",
			"Entier non-signé sur {0} bits ({1} bits/pixel)", "Sans-titre", "Haut",
			"Utiliser la meilleure résolution disponible", "Heure universelle (UTC)", "Valeur", "Vendeur",
			"Version {0}", "Version \"{0}\"", "Vertical", "Composante verticale", "Avertissement", "Ouest",
			"Chemin ouest", "Jaune", "Zoom avant", "Zoom rapproché", "Zoom arrière", "null", "null", "null", "Inconnu",
			"J'entre dans le ensure Loaded", "J'entre dans le ensure Loaded", "Le filename est : Errors_fr.utf",
			"La classe courante est : org.geotools.resources.i18n.Errors_fr", "0",
			"On a bien un input stream avec : Errors_fr.utf", "La coordonnée \"{0}\" n''est pas valide.",
			"Saisie invalide", "Rectangle vide ou invalide: {0}", "Les longueurs des axes sont ambiguës.",
			"L''angle {0} est trop élevé.", "Les latitudes {0} et {1} sont aux antipodes.",
			"L''azimut {0} est en dehors des limites permises (±180°).", "Le numéro de bande {0} n''est pas valide.",
			"Le coefficient {0}={1} ne doit pas être NaN ou infini.",
			"La plage d''index [{1} .. {2}] de la dimension {0} n''est pas valide.",
			"Données invalides à la ligne {1} du fichier \"{0}\".", "Code de langue inconu: {0}",
			"Le paramètre \"{0}\" ne doit pas avoir la valeur \"{1}\".",
			"Le paramètre \"{0}\" ne peut pas être du type ''{1}''.", "La plage [{0} .. {1}] n''est pas valide.",
			"Transformation de type \"{0}\" illégale.",
			"La multiplication ou division des {0} par des {1} n''est pas permise.",
			"Les unités {1} ne peuvent pas être élevées à la puissance {0}.",
			"Les paramètres de Bursa Wolf sont requis.", "La dérivé ne peut pas être calculée.",
			"Les transformations \"{0}\" et \"{1}\" ne peuvent pas être combinées.",
			"Échec lors de la connexion à la base de données {0}.",
			"La valeur ne peut pas être convertie à partir du type ''{0}''.",
			"Échec lors de la création d''une fabrique de type \"{0}\".",
			"Un objet de type ''{0}'' ne peut pas être créé à partir d''un texte.",
			"Une erreur s'est produite durant le rognage de l'image.",
			"La valeur à la coordonnée ({0}) ne peut pas être évaluée.",
			"La source de données \"{0}\" n''a pas pu être obtenue.", "Échec lors de la lecture du fichier \"{0}\".",
			"Le système de coordonnées \"{0}\" ne peut pas être réduit à deux dimensions.",
			"Ne peut pas reprojeter la couverture \"{0}\".", "Ne peut pas séparer le SRC \"{0}\".",
			"Ne peut pas définir une valeur pour le paramètre \"{0}\".", "Ne peut pas transformer l'enveloppe.",
			"Des points qui devraient être valides n'ont pas pu être transformés.",
			"Le graphique \"{0}\" appartient à un autre afficheur.", "Les axes {0} et {1} sont colinéaires.",
			"La couverture retournée par ''{0}'' est déjà une vue de la couverture \"{1}\".",
			"Échec lors de la création d''un objet ''{0}'' pour le code \"{1}\".",
			"La date {0} est en dehors de la plage de temps des données disponibles.",
			"La destination n'a pas été définie.", "La direction n'a pas été définie.", "La fabrique a été fermée.",
			"La distance {0} est en dehors des limites permises ({1} à {2} {3})",
			"Plusieurs valeurs correspondent au code \"{0}\".", "Projection elliptique non-supportée.",
			"Le tableau doit contenir au moins un élément.",
			"L'enveloppe doit avoir au moins deux dimensions et ne pas être vide.", "Fin de fichier inatendu.",
			"Aucune fabrique de type \"{0}\" n''a été trouvée.",
			"Le fichier n''a pas été trouvé ou ne peut pas être lu: {0}", "Le fichier contient trop peu de données.",
			"Les extensions de Geotools sont requises pour l''opération \"{0}\".",
			"Les latitudes et longitudes ne sont pas espacées de façon régulière.",
			"En-tête d''une longueur inatendu: {0}", "Le trou n'est pas à l'intérieur du polygone.",
			"Le modèle \"{0}\" n''est pas valide pour lire et écrire des angles.",
			"Valeur illégale pour l''argument \"{0}\".", "L''argument \"{0}={1}\" est illegal.",
			"La longueur du tableau n''est pas valide pour des points de dimension {0}.",
			"Un axe ne peut pas être orienté \"{0}\" dans les systèmes de coordonnées de classe \"{1}\".",
			"La classe ''{0}'' est illégale. Il doit s''agir d''une classe ''{1}'' ou dérivée.",
			"Système de référence de coordonnées illégal.",
			"Le système de coordonnées de type ''{0}'' est incompatible avec le SRC de type ''{1}''.",
			"Le système de coordonnées ne peut pas avoir {0} dimensions.",
			"Descripteur illégal pour le paramètre \"{0}\".", "Les ordonnées de la dimension {0} ne sont pas valides.",
			"\"{0}\" n''est pas un identifiant valide.", "L''instruction \"{0}\" n''est pas reconnue.",
			"Clé illégale: {0}", "La dimension de la matrice est illégale.",
			"Le paramètre \"{0}\" apparaît {1} fois, alors que le nombre d''occurences attendu était [{2}..{3}].",
			"Cette opération ne peut pas s''appliquer aux valeurs de classe ''{0}''.",
			"Type de système de coordonnées incompatible.",
			"Le paramètre de projection \"{0}\" est incompatible avec l''ellipsoïde \"{1}\".",
			"Les géométries des grilles ne sont pas compatibles.", "L''unité \"{0}\" n''est pas compatible.",
			"La direction \"{1}\" n''est pas cohérente avec l''axe \"{0}\".",
			"La valeur de la propriété \"{0}\" n''est pas cohérente avec celles des autres propriétés.",
			"L''index {0} est en dehors des limites permises.", "La valeur {0} est infinie.",
			"La transformation n'est pas séparable.", "{0} points étaient spécifiés, alors que {1} sont requis.",
			"Cet objet de type \"{0}\" est trop complexe pour la syntaxe WKT.", "Erreur dans \"{0}\":",
			"La latitude {0} est en dehors des limites permises (±90°).",
			"La ligne contient {0} colonnes alors qu''on n''en attendait que {1}. Le texte \"{2}\" est en trop.",
			"La ligne ne contient que {0} colonnes alors qu''on en attendait {1}.",
			"La longitude {0} est en dehors des limites permises (±180°).", "L'enveloppe présente des incohérences.",
			"Toutes les lignes n'ont pas la même longueur.", "Les tableaux n'ont pas la même longueur.",
			"Le système de référence des coordonnées doit être le même pour tous les objets.",
			"Les dimensions des objets ne concordent pas: ils ont {0}D et {1}D.",
			"L''argument \"{0}\" a {1} dimensions, alors qu''on en attendait {2}.",
			"L''enveloppe utilise un système de référence des coordonnées incompatible, soit \"{1}\" alors qu''on attendait \"{0}\".",
			"Aucune autorité n''a été spécifiée pour le code \"{0}\". Le format attendu est habituellement \"AUTORITÉ:NOMBRE\".",
			"Un caractère ''{0}'' était attendu.", "Cette opération requiert le module \"{0}\".",
			"Le paramètre \"{0}\" est manquant.", "Il manque la valeur du paramètre \"{0}\".",
			"Définition WKT manquante.",
			"Des catégories géophysiques sont mélangées avec des catégories non-géophysiques.",
			"Le numéro de colonne des \"{0}\" ({1}) ne doit pas être négatif.",
			"La transformation affine pour redimensionner l'image n'est pas invertible.",
			"La transformation n'est pas invertible.",
			"La transformation de la grille vers le système de coordonnées doit être affine.",
			"\"{0}\" n''est pas une unité d''angles.", "Le système de coordonnées \"{0}\" n''est pas cartésien.",
			"Les données exprimées en {1} ne peuvent pas être converties en {0}.",
			"Les parenthèses ne sont pas équilibrées dans \"{0}\". Il manque ''{1}''.",
			"Certaines catégories utilisent des valeurs qui ne sont pas entières.", "La relation n'est pas linéaire.",
			"\"{0}\" n''est pas une unité de longueurs.",
			"La conversion des unités \"{0}\" vers \"{1}\" n''est pas linéaire.",
			"Les directions {0} et {1} ne sont pas perpendiculaires.", "\"{0}\" n''est pas une unité d''échelle.",
			"\"{0}\" n''est pas une unité de temps.", "La transformation n'est pas affine.",
			"La classe \"{0}\" n''est pas celle d''un angle.", "Le nombre \"{0}\" n''est pas en entier valide.",
			"Les points ne semblent pas être distribués sur une grille régulière.",
			"Un nombre réel était attendu. \"{0}\" n''est pas valide.", "La classe {0} n''est pas comparable.",
			"Le nombre {0} n''est pas valide. On attendait un nombre réel non-nul.",
			"Le nombre {0} n''est pas valide. On attendait un nombre positif non-nul.",
			"Le système de coordonnées n'est pas tri-dimensionel.",
			"Un objet à {0} dimensions ne peut pas être ramené à 2 dimensions.",
			"Aucune catégorie n''est définie pour la valeur {0}.", "La transformation ne converge pas.",
			"Le calcul ne converge pas pour les points {0} et {1}.", "Aucune source de données n'a été trouvée.",
			"Aucune entrée n'a été spécifiée pour lire l'image.",
			"Aucune sortie n'a été spécifiée pour écrire l'image.",
			"Aucun décodeur d'image n'a été trouvé pour cette source.",
			"Aucun encodeur d'image n'a été trouvé pour cette destination.", "Aucun axe source ne correspond à {0}.",
			"Aucun objet de type \"{0}\" n''a été trouvé pour le code \"{1}\".",
			"Aucun code \"{0}\" de l''autorité \"{1}\" n''a été trouvé pour un objet de type \"{2}\".",
			"Aucune transformation à deux dimensions n'est disponible pour cette géométrie.",
			"Aucune transformation du système \"{0}\" vers \"{1}\" n''est disponible.",
			"Aucune transformation de classe \"{0}\" n''est définie.", "Des unités doivent être spécifiées.",
			"L''argument \"{0}\" ne doit pas être nul.", "L''attribut \"{0}\" ne doit pas être nul.",
			"Le format #{0} (sur {1}) n''est pas défini.", "Le paramètre \"{0}\" doit être non-nul et du type \"{1}\".",
			"Dans la table \"{2}\", la colonne \"{1}\" de l''enregistrement \"{0}\" ne devrait pas contenir de valeur nulle.",
			"Le nombre de bandes de l''image ({0}) ne correspond pas au nombre d''objets ''{2}'' spécifiés ({1}).",
			"Un tableau de longueur paire était attendu. La longueur {0} n''est pas valide.",
			"L''opération \"{0}\" est déjà définie.", "L''opération \"{0}\" est déjà déclarée dans ce processeur.",
			"L''opération \"{0}\" n''est pas gérée par ce processeur.",
			"Possible usage de la projection \"{0}\" en dehors de sa région de validité.",
			"Le nom ou un alias pour le paramètre \"{0}\" à l''index {1} duplique le nom \"{2}\" à l''index {3}.",
			"Le texte \"{0}\" n''a pas pu être interprété. Vérifiez les caractères \"{1}\".",
			"La coordonnée ({0}) est en dehors des limites permises.", "Des points sont en dehors de la grille.",
			"Point en dehors de l'hémisphere de projection.", "La latitude {0} est trop proche d''un pôle.",
			"On ne peut pas ajouter des points à un polygone déjà fermé.",
			"Le résultat de la projection pourrait se trouver à {0} mètres de la position attendue. Êtes-vous certain que les coordonnées sont dans la région de validité de cette projection cartographique? Le point est situé à {1} du méridien central et à {2} de la latitude d''origine. La projection est \"{3}\".",
			"Les plages [{0}..{1}] et [{2}..{3}] se chevauchent.",
			"Appels recursifs lors de la création d''un objet ''{0}''.",
			"Appels recursifs lors de la création d''un objet ''{0}'' pour le code \"{1}\".",
			"La valeur RGB {0} est en dehors des limites attendues.", "L'exécution par une machine distante a échouée.",
			"On attendait {0}={1} mais on a obtenu {2}.", "Tolérance dépassée.",
			"Trop d''occurences de \"{0}\". Il en existe déjà {1}.", "Propriété non-définie.",
			"La propriété \"{0}\" n''est pas définie.", "Argument innatendu pour l''opération \"{0}\".",
			"Dimension innatendue pour un système de coordonnées de type \"{0}\".", "Fin de texte inatendue.",
			"L'image n'a pas la taille attendue.", "Le paramètre \"{0}\" est en trop.",
			"La ligne {0} de la matrice a une longueur de {1}, alors qu''on attendait {2}.",
			"La transformation n'a pas produit les valeurs attendues.", "Le paramètre \"{0}\" n''a pas d''unité.",
			"L''autorité \"{0}\" n''est pas connue ou ne correspond pas aux indices spécifiés. Le fichier JAR qui la définie n''est peut-être pas accessible?",
			"La direction \"{0}\" n''est pas connue pour un axe.", "Le format d''image \"{0}\" n''est pas reconnu.",
			"L''interpolation \"{0}\" n''est pas reconnue.", "Paramètre inconnu: {0}", "Nom de paramètre inconu: {0}",
			"Type de projection inconnu.", "Le type \"{0}\" n''est pas reconnu dans ce contexte.",
			"Cette transformation affine n'est pas modifiable.", "La géométrie n'est pas modifiable.",
			"La méta-donnée n'est pas modifiable.", "Ne peut pas décoder \"{0}\" comme un nombre.",
			"Ne peut pas décoder \"{0}\" car le texte \"{1}\" n''est pas reconnu.",
			"Le système de référence des coordonnées n'a pas été spécifié.",
			"La taille de l'image n'a pas été spécifiée.", "La transformation de coordonnées n'a pas été spécifiée.",
			"Le système de coordonnées \"{0}\" n''est pas supporté.",
			"Le système de référence des coordonnées \"{0}\" n''est pas supporté.",
			"Ce type de données n'est pas pris en charge.", "Le type de données \"{0}\" n''est pas pris en charge.",
			"Type de fichier non-supporté: {0} ou {1}", "La valeur {0} est en dehors de la plage [{1}..{2}].",
			"La valeur numérique tend vers l'infini.",
			"Aucune variable \"{0}\" n''a été trouvée dans le fichier \"{1}\".",
			"La valeur {1} est en dehors du domaine de la couverture \"{0}\".", null };

	private final String[] loggingFR = { "La géométrie de la grille a été ajustée pour la couverture \"{0}\".",
			"Ambiguïté entre l'aplatissement et la longueur du semi-axe mineur. Utilise l'aplatissement.",
			"{3,choice,0.0#Applique} l''opération \"{1}\" sur l''image \"{0}\" avec l''interpolation \"{2}\".",
			"Échantillonne la couverture \"{0}\" du système de coordonnées \"{1}\" (pour une image de taille {2}×{3}) vers le système \"{4}\" (image de taille {5}×{6}). L''opération d''image utilisée est \"{7}\" avec l''interpolation \"{9}\" sur les valeurs {8,choice,0.0#compactes|1.0#géophysiques} des pixels. La valeur d''arrière-plan est ({10}).",
			"Conversion de \"{0}\" des unités \"{1}\" vers \"{2}\". Nous supposons que ce sont les unités attendues aux fins de calculs.",
			"Échec lors de l''enregistrement de l''entrée \"{0}\".",
			"Échec lors de la création d''une transformation de coordonnées selon l''autorité \"{0}\".",
			"Échec lors de la libération des ressources.",
			"Échec lors de l''initialisation d''un service de catégorie \"{0}\". La cause est \"{1}\".",
			"Échec lors de la lecture de \"{0}\".",
			"L''opération JAI \"{0}\" n''a pas pu être enregistrée. Des traitements d''images ne vont peut-être pas fonctionner.",
			"Le système de coordonnées de l'afficheur a été changé. La cause est:",
			"Fermeture de la connexion à la base de données EPSG.",
			"Connecté à la base de données EPSG \"{0}\" sur \"{1}\".",
			"Création de l''opération \"{0}\" pour le SRC source \"{1}\" et le SRC destination \"{2}\".",
			"Une entrée \"{0}\" a été créée dans le système de noms.",
			"Création d''une image enregistrable pour la couverture \"{0}\" en utilisant l''encodage \"{1}\".",
			"Création de la base de données EPSG version {0}. Cette opération peut prendre quelques minutes...",
			"Traçage de la tuile ({0},{1}) reporté.", "Consommation excessive de la mémoire.",
			"La capacité de la cache des tuiles excède la quantité de mémoire allouée au Java ({0} Mo).",
			"Implémentations des fabriques de catégorie {0}:",
			"Échec dans la fabrique primaire: {0} La fabrique secondaire sera interrogée.",
			"Trouvé {0} systèmes de références dans {1} éléments. Le plus fréquent apparait {2} fois et le moins fréquent apparait {3} fois.",
			"L''indice \"{0}\" a été ignoré.", "Initialise une transformation de {0} vers {1}.",
			"Pilote JDBC \"{0}\" version {1}.{2} chargé.", "Chargement des alias des référentiels à partir de \"{0}\".",
			"Des textes ont été ignorés pour certaines langues.",
			"Il n''y a pas d''opérations allant de \"{0}\" vers \"{1}\" parce que ces derniers sont associés à deux fabriques différentes.",
			"Le type de l''objet demandé ne correspond pas au type de l''URN \"{0}\".",
			"Accélération native {1,choice,0.0#désactivée} pour l''opération \"{0}\".",
			"Le traçage dans un buffer a échoué pour la couche \"{0}\". Trace sans buffer.",
			"Traçage de \"{0}\" complété en {1} secondes.",
			"Polygones tracés avec {0,number,percent} des points disponibles, dont {1,number,percent} proviennent de la cache (résolution: {2} {3}).",
			"Échec lors de la tentative d''allouer {0} Mo de mémoire. Une nouvelle tentative sera effectuée.",
			"Le journal des événements est redirigé vers \"commons-logging\" de Apache.",
			"Chargement des extensions de Geotools aux opérations de JAI.",
			"Sélectionne une image de \"{0}\" décimée au niveau {1} sur {2}.",
			"Crée une vue {1,choice,0.0#compacte|1.0#géophysique|2.0#photographique} de la couverture \"{0}\" en utilisant l''opération \"{2}\".",
			"La couche \"{0}\" demande à redessiner toute la fenêtre.",
			"La couche \"{0}\" demande à redessiner les pixels x=[{1}..{2}] et y=[{3}..{4}] de la fenêtre.",
			"La fabrique n''est pas disponible pour l''autorité \"{0}\".",
			"Tentative de récupération après une exception inatendue.",
			"Les unités \"{0}\" sont inattendues dans ce contexte. L''échelle de la carte sera peut-être imprécise.",
			"Un paramètre inconnu a été ignoré: \"{0}\" = {1} {2}.",
			"Les styles de classe {0} ne sont pas gérés. La géométrie \"{1}\" ignorera donc ses informations de style.",
			"Le type d''échelle \"{0}\" est inconnu. Une échelle linéaire sera utilisée à la place.",
			"Mise à jour de la cache pour la couche \"{0}\".", "Utilise \"{0}\" comme fabrique {1}" };

	/**
	 * Returns an enumeration of the keys.
	 */
	public final Enumeration<String> getKeys() {
		// Synchronization performed by 'ensureLoaded'
		final String[] values = ensureLoaded(null);
		return new Enumeration<String>() {
			private int i = 0;

			public boolean hasMoreElements() {
				while (true) {
					if (i >= values.length)
						return false;
					if (values[i] != null)
						return true;
					i++;
				}
			}

			public String nextElement() {
				while (true) {
					if (i >= values.length)
						throw new NoSuchElementException();
					if (values[i] != null)
						return String.valueOf(i++);
					i++;
				}
			}
		};
	}

	/**
	 * Gets an object for the given key from this resource bundle. Returns null
	 * if this resource bundle does not contain an object for the given key.
	 *
	 * @param key
	 *            the key for the desired object
	 * @exception NullPointerException
	 *                if {@code key} is {@code null}
	 * @return the object for the given key, or null
	 */
	protected final Object handleGetObject(final String key) {
		// Synchronization performed by 'ensureLoaded'
		final String[] values = ensureLoaded(key);
		final int keyID;
		try {
			keyID = Integer.parseInt(key);
		} catch (NumberFormatException exception) {
			return null;
		}
		return (keyID >= 0 && keyID < values.length) ? values[keyID] : null;
	}

	/**
	 * Makes sure that the {@code text} string is not longer than
	 * {@code maxLength} characters. If {@code text} is not longer, it is
	 * returned unchanged (except for trailing blanks, which are removed). If
	 * {@code text} is longer, it will be cut somewhere in the middle. This
	 * method tries to cut between two words and replace the missing words with
	 * "(...)". For example, the following string:
	 *
	 * <blockquote> "This sentence given as an example is way too long to be
	 * included in a message." </blockquote>
	 *
	 * May be "summarized" by something like this:
	 *
	 * <blockquote>
	 * "This sentence given (...) included in a message." </blockquote>
	 *
	 * @param text
	 *            The sentence to summarize if it is too long.
	 * @param maxLength
	 *            The maximum length allowed for {@code text}. If {@code text}
	 *            is longer, it will be summarized.
	 * @return A sentence not longer than {@code maxLength}.
	 */
	private static String summarize(String text, int maxLength) {
		text = text.trim();
		final int length = text.length();
		if (length <= maxLength) {
			return text;
		}
		/*
		 * Computes maximum length for one half of the string. Take into account
		 * the space needed for inserting the " (...) " string.
		 */
		maxLength = (maxLength - 7) >> 1;
		if (maxLength <= 0) {
			return text;
		}
		/*
		 * We will remove characters from 'break1' to 'break2', both exclusive.
		 * We try to adjust 'break1' and 'break2' in such a way that the first
		 * and last characters to be removed will be spaces or punctuation
		 * characters. Constants 'lower' and 'upper' are limit values. If we
		 * don't find values for 'break1' and 'break2' inside those limits, we
		 * will give up.
		 */
		int break1 = maxLength;
		int break2 = length - maxLength;
		for (final int lower = (maxLength >> 1); break1 >= lower; break1--) {
			if (!Character.isUnicodeIdentifierPart(text.charAt(break1))) {
				while (--break1 >= lower && !Character.isUnicodeIdentifierPart(text.charAt(break1)))
					;
				break;
			}
		}
		for (final int upper = length - (maxLength >> 1); break2 < upper; break2++) {
			if (!Character.isUnicodeIdentifierPart(text.charAt(break2))) {
				while (++break2 < upper && !Character.isUnicodeIdentifierPart(text.charAt(break2)))
					;
				break;
			}
		}
		return (text.substring(0, break1 + 1) + " (...) " + text.substring(break2)).trim();
	}

	/**
	 * Returns {@code arguments} as an array. If {@code arguments} is already an
	 * array, this array or a copy of this array will be returned. If
	 * {@code arguments} is not an array, it will be placed in an array of
	 * length 1. In any case, all the array's elements will be checked for
	 * {@link String} objects. Any strings of length greater than
	 * {@link #MAX_STRING_LENGTH} will be reduced using the {@link #summarize}
	 * method.
	 *
	 * @param arguments
	 *            The object to check.
	 * @return {@code arguments} as an array.
	 */
	private Object[] toArray(final Object arguments) {
		Object[] array;
		if (arguments instanceof Object[]) {
			array = (Object[]) arguments;
		} else {
			array = new Object[] { arguments };
		}
		for (int i = 0; i < array.length; i++) {
			final Object element = array[i];
			if (element instanceof CharSequence) {
				final String s0;
				// if (element instanceof InternationalString) {
				// s0 = ((InternationalString)
				// element).toString(getFormatLocale());
				// } else {
				s0 = element.toString();
				// }
				final String s1 = summarize(s0, MAX_STRING_LENGTH);
				if (s0 != s1 && !s0.equals(s1)) {
					if (array == arguments) {
						array = new Object[array.length];
						System.arraycopy(arguments, 0, array, 0, array.length);
					}
					array[i] = s1;
				}
			} else if (element instanceof Throwable) {
				String message = ((Throwable) element).getLocalizedMessage();
				if (message == null) {
					message = Classes.getShortClassName(element);
				}
				array[i] = message;
			} else if (element instanceof Class) {
				array[i] = Classes.getShortName((Class<?>) element);
			}
		}
		return array;
	}

	/**
	 * Gets a string for the given key and appends "..." to it. This method is
	 * typically used for creating menu items.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @return The string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getMenuLabel(final int key) throws MissingResourceException {
		return getString(key) + "...";
	}

	/**
	 * Gets a string for the given key and appends ": " to it. This method is
	 * typically used for creating labels.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @return The string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getLabel(final int key) throws MissingResourceException {
		return getString(key) + ": ";
	}

	/**
	 * Gets a string for the given key from this resource bundle or one of its
	 * parents.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @return The string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getString(final int key) throws MissingResourceException {
		return getString(String.valueOf(key));
	}

	/**
	 * Gets a string for the given key and formats it with the specified
	 * argument. The message is formatted using {@link MessageFormat}. Calling
	 * this method is approximately equivalent to calling:
	 *
	 * <blockquote>
	 * 
	 * <pre>
	 * String pattern = getString(key);
	 * Format f = new MessageFormat(pattern);
	 * return f.format(arg0);
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * If {@code arg0} is not already an array, it will be placed into an array
	 * of length 1. Using {@link MessageFormat}, all occurrences of "{0}", \
	 * "{1}\", "{2}" in the resource string will be replaced by {@code arg0[0]},
	 * {@code arg0[1]}, {@code arg0[2]}, etc.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @param arg0
	 *            A single object or an array of objects to be formatted and
	 *            substituted.
	 * @return The string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 *
	 * @see #getString(String)
	 * @see #getString(int,Object,Object)
	 * @see #getString(int,Object,Object,Object)
	 * @see MessageFormat
	 */
	public final String getString(final int key, final Object arg0) throws MissingResourceException {
		final String pattern = getString(key);
		final Object[] arguments = toArray(arg0);
		synchronized (this) {
			if (format == null) {
				/*
				 * Constructs a new MessageFormat for formatting the arguments.
				 */
				format = new MessageFormat(pattern, getFormatLocale());
			} else if (key != lastKey) {
				/*
				 * Method MessageFormat.applyPattern(...) is costly! We will
				 * avoid calling it again if the format already has the right
				 * pattern.
				 */
				format.applyPattern(pattern);
				lastKey = key;
			}
			return format.format(arguments);
		}
	}

	/**
	 * Gets a string for the given key and replaces all occurrences of "{0}",
	 * \"{1}\", with values of {@code arg0}, {@code arg1}, etc.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @param arg0
	 *            Value to substitute for "{0}".
	 * @param arg1
	 *            Value to substitute for \"{1}\".
	 * @return The formatted string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getString(final int key, final Object arg0, final Object arg1) throws MissingResourceException {
		return getString(key, new Object[] { arg0, arg1 });
	}

	/**
	 * Gets a string for the given key and replaces all occurrences of "{0}",
	 * \"{1}\", with values of {@code arg0}, {@code arg1}, etc.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @param arg0
	 *            Value to substitute for "{0}".
	 * @param arg1
	 *            Value to substitute for \"{1}\".
	 * @param arg2
	 *            Value to substitute for "{2}".
	 * @return The formatted string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getString(final int key, final Object arg0, final Object arg1, final Object arg2)
			throws MissingResourceException {
		return getString(key, new Object[] { arg0, arg1, arg2 });
	}

	/**
	 * Gets a string for the given key and replaces all occurrences of "{0}",
	 * \"{1}\", with values of {@code arg0}, {@code arg1}, etc.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @param arg0
	 *            Value to substitute for "{0}".
	 * @param arg1
	 *            Value to substitute for \"{1}\".
	 * @param arg2
	 *            Value to substitute for "{2}".
	 * @param arg3
	 *            Value to substitute for "{3}".
	 * @return The formatted string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getString(final int key, final Object arg0, final Object arg1, final Object arg2,
			final Object arg3) throws MissingResourceException {
		return getString(key, new Object[] { arg0, arg1, arg2, arg3 });
	}

	/**
	 * Gets a string for the given key and replaces all occurrences of "{0}",
	 * \"{1}\", with values of {@code arg0}, {@code arg1}, etc.
	 *
	 * @param key
	 *            The key for the desired string.
	 * @param arg0
	 *            Value to substitute for "{0}".
	 * @param arg1
	 *            Value to substitute for \"{1}\".
	 * @param arg2
	 *            Value to substitute for "{2}".
	 * @param arg3
	 *            Value to substitute for "{3}".
	 * @param arg4
	 *            Value to substitute for "{4}".
	 * @return The formatted string for the given key.
	 * @throws MissingResourceException
	 *             If no object for the given key can be found.
	 */
	public final String getString(final int key, final Object arg0, final Object arg1, final Object arg2,
			final Object arg3, final Object arg4) throws MissingResourceException {
		return getString(key, new Object[] { arg0, arg1, arg2, arg3, arg4 });
	}

	/**
	 * Gets a localized log record.
	 *
	 * @param level
	 *            The log record level.
	 * @param key
	 *            The resource key.
	 * @return The log record.
	 */
	public LogRecord getLogRecord(final Level level, final int key) {
		return getLogRecord(level, key, null);
	}

	/**
	 * Gets a localized log record.
	 *
	 * @param level
	 *            The log record level.
	 * @param key
	 *            The resource key.
	 * @param arg0
	 *            The parameter for the log message, or {@code null}.
	 * @return The log record.
	 */
	public LogRecord getLogRecord(final Level level, final int key, final Object arg0) {
		final LogRecord record = new LogRecord(level, String.valueOf(key));
		record.setResourceBundle(this);
		if (arg0 != null) {
			record.setParameters(toArray(arg0));
		}
		return record;
	}

	/**
	 * Gets a localized log record.
	 *
	 * @param level
	 *            The log record level.
	 * @param key
	 *            The resource key.
	 * @param arg0
	 *            The first parameter.
	 * @param arg1
	 *            The second parameter.
	 * @return The log record.
	 */
	public LogRecord getLogRecord(final Level level, final int key, final Object arg0, final Object arg1) {
		return getLogRecord(level, key, new Object[] { arg0, arg1 });
	}

	/**
	 * Gets a localized log record.
	 *
	 * @param level
	 *            The log record level.
	 * @param key
	 *            The resource key.
	 * @param arg0
	 *            The first parameter.
	 * @param arg1
	 *            The second parameter.
	 * @param arg2
	 *            The third parameter.
	 * @return The log record.
	 */
	public LogRecord getLogRecord(final Level level, final int key, final Object arg0, final Object arg1,
			final Object arg2) {
		return getLogRecord(level, key, new Object[] { arg0, arg1, arg2 });
	}

	/**
	 * Gets a localized log record.
	 *
	 * @param level
	 *            The log record level.
	 * @param key
	 *            The resource key.
	 * @param arg0
	 *            The first parameter.
	 * @param arg1
	 *            The second parameter.
	 * @param arg2
	 *            The third parameter.
	 * @param arg3
	 *            The fourth parameter.
	 * @return The log record.
	 */
	public LogRecord getLogRecord(final Level level, final int key, final Object arg0, final Object arg1,
			final Object arg2, final Object arg3) {
		return getLogRecord(level, key, new Object[] { arg0, arg1, arg2, arg3 });
	}

	/**
	 * Localize and format the message string from a log record. This method
	 * performs a work similar to
	 * {@link java.util.logging.Formatter#formatMessage}, except that the work
	 * will be delegated to {@link #getString(int, Object)} if the
	 * {@linkplain LogRecord#getResourceBundle record resource bundle} is an
	 * instance of {@code IndexedResourceBundle}.
	 *
	 * @param record
	 *            The log record to format.
	 * @return The formatted message.
	 */
	public static String format(final LogRecord record) {
		String message = record.getMessage();
		final ResourceBundle resources = record.getResourceBundle();
		if (resources instanceof IndexedResourceBundle) {
			int key = -1;
			try {
				key = Integer.parseInt(message);
			} catch (NumberFormatException e) {
				unexpectedException(e);
			}
			if (key >= 0) {
				final Object[] parameters = record.getParameters();
				return ((IndexedResourceBundle) resources).getString(key, parameters);
			}
		}
		if (resources != null) {
			try {
				message = resources.getString(message);
			} catch (MissingResourceException e) {
				unexpectedException(e);
			}
			final Object[] parameters = record.getParameters();
			if (parameters != null && parameters.length != 0) {
				final int offset = message.indexOf('{');
				if (offset >= 0 && offset < message.length() - 1) {
					// Uses a more restrictive check than
					// Character.isDigit(char)
					final char c = message.charAt(offset);
					if (c >= '0' && c <= '9')
						try {
							return MessageFormat.format(message, parameters);
						} catch (IllegalArgumentException e) {
							unexpectedException(e);
						}
				}
			}
		}
		return message;
	}

	/**
	 * Invoked when an unexpected exception occured in the {@link #format}
	 * method.
	 */
	private static void unexpectedException(final RuntimeException exception) {
		Logging.unexpectedException(IndexedResourceBundle.class, "format", exception);
	}

	/**
	 * Returns a string representation of this object. This method is for
	 * debugging purposes only.
	 */
	@Override
	public synchronized String toString() {
		final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this));
		buffer.append('[');
		if (values != null) {
			int count = 0;
			for (int i = 0; i < values.length; i++) {
				if (values[i] != null)
					count++;
			}
			buffer.append(count);
			buffer.append(" values");
		}
		buffer.append(']');
		return buffer.toString();
	}
}
