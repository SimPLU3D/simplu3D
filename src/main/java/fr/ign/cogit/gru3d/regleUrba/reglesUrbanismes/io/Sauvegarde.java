package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.io;

import java.io.FileOutputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.regles.PLU;

public class Sauvegarde {

  public static void sauvegarde(String chemin, PLU plu) {
    try {
      // Création d'une instance de la classe JAXBContext
      JAXBContext jaxbContext = JAXBContext
          .newInstance(Chargement.PATH_PACKAGE_RULES);

      // Création d'un marshaller
      Marshaller marshaller = jaxbContext.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
          new Boolean(true));

      // Définition du schéma XML associé au modéle de données
      SchemaFactory scf = SchemaFactory
          .newInstance("http://www.w3.org/2001/XMLSchema");
      URL schemaFile = Sauvegarde.class
          .getResource("/demo3D/reglesurba/reglesUrba.xsd");
      Schema schema = scf.newSchema(schemaFile);

      // Affectation du schéma au marshaller
      marshaller.setSchema(schema);

      // Test de validité du document XML
      marshaller.setEventHandler(new DefaultValidationEventHandler());

      // Messages de fin:
      System.out
          .println("Création d'un arbre de contenu et sérialisation en XML");

      System.out.println(chemin);

      FileOutputStream fo = new FileOutputStream(chemin);
      // Sérialisation des données en XML
      marshaller.marshal(plu, fo);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }// Fin du main
}
