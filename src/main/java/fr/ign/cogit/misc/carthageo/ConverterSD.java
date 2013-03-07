package fr.ign.cogit.misc.carthageo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ConverterSD {

  public static void main(String[] args) throws IOException {
    String fileIn = "E:/mbrasebin/Documents/Cours/Cours 2013/M2Carthageo/Donnees/test/SD_BESA_20110319_20110602.txt";
    String fileOut = "E:/mbrasebin/Documents/Cours/Cours 2013/M2Carthageo/Donnees/test/SD_BESA_20110319_20110602.csv";

    convert(fileIn, fileOut);

  }

  public static boolean convert(String fileIn, String fileOut)
      throws IOException {

    int count = 0;

    Path path = Paths.get(fileOut);

    BufferedWriter writer = Files.newBufferedWriter(path,
        StandardCharsets.UTF_8, StandardOpenOption.CREATE);

    try (Scanner scanner = new Scanner(new File(fileIn))) {

      while (scanner.hasNextLine()) {

        count++;

        if (count % 5000 == 0) {
          System.out.println(count);
          writer.flush();
        }

        writer.write(treatLine(scanner.nextLine()));
        writer.newLine();

      }

    }

    writer.flush();
    writer.close();

    return true;
  }

  private static String treatLine(String s) {

    String[] sTab = s.split(" ");

    String strIni = "";


    int nbElem = sTab.length;
    int countAdded = 0;
    for (int i = 0; i < nbElem; i++) {

      String strTemp = sTab[i];

      if (strTemp.isEmpty()) {
        continue;
      }

      countAdded++;

      if (countAdded ==1) {

        strIni = strIni + sTab[i];
        continue;
      }

      strIni = strIni + ";" + strTemp;

    }

    return strIni;

  }

}
