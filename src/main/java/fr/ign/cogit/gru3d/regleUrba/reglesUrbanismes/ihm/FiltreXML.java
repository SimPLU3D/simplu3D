/*
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package fr.ign.cogit.gru3d.regleUrba.reglesUrbanismes.ihm;

/**
 * @author benoitpoupeau/MBrasebin
 * 
 *         Cette classe permet de ne sélectionner que des fichiers XML
 * 
 *         This class enables to select only .xml files
 * 
 */

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltreXML extends FileFilter {

  /**
   * Acceptation du fichier
   */
  @Override
  public boolean accept(File fichier) {
    if (fichier.isDirectory()) {
      return true;
    }
    // Récupération de l'extension
    String extension = this.getExtension(fichier);

    // Test pour vérifier si le fichier est du XML
    if (extension != null) {
      if (!"xml".equals(extension)) { //$NON-NLS-1$
        return false;
      }
    }
    return true;

  }

  /**
   * La description du filtre
   */
  @Override
  public String getDescription() {
    return "XML files"; //$NON-NLS-1$
  }

  /**
   * Méthode permettant d'extraire l'extension du filtre
   */
  private String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      return s.substring(i + 1).toLowerCase();
    }

    return ext;
  }
}
