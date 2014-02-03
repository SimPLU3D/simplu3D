package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.io;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.PLU;

public class Chargement {

  public static String PATH_PACKAGE_RULES = "fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles";

  public static PLU chargementFichier(String nomFichier) {

    File fichier = new File(nomFichier);

    System.out.println(fichier.getAbsolutePath());
    /**
     * Comment créer un arbre de contenu, y ajouter des objets et le sérialiser
     * dans un document XML
     */
    try {
      /*
       * Création d'une instance de la classe JAXBContext: on fournit au
       * constructeur une liste de packages contenant les classes générées par
       * le compilateur XJC
       */
      JAXBContext jc = JAXBContext.newInstance(Chargement.PATH_PACKAGE_RULES);
      // Création d'un rassembleur de données ("unmarshaller")
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      // Définition du schéma XML associé au modéle de données
      SchemaFactory scf = SchemaFactory
          .newInstance("http://www.w3.org/2001/XMLSchema");

      URL schemaFile = Chargement.class
          .getResource("/demo3D/reglesurba/reglesUrba.xsd");

      System.out.println(schemaFile.getPath());

      Schema schema = scf.newSchema(schemaFile);

      /*
       * Affectation du schéma au rassembleur en cours: une validation est
       * effectuée par défaut sans appel au ValidationEventHandler: seules les
       * erreurs les plus graves provoquent un arrét du processus. Pour plus de
       * rigeur, on peut créer son propre ValidationEventHandler et l'appeler
       * aprés la ligne ci-dessous: unmarshaller.setEventHandler(new
       * MyValidationEventHandler())
       */
      unmarshaller.setSchema(schema);

      // Conversion XML2Java
      PLU plu = (PLU) unmarshaller.unmarshal(fichier);
      System.out.println("Conversion XML2Java réussie...");

      return plu;

    }// Fin du bloc try
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
