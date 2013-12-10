package fr.ign.cogit.simplu3d.exec.stability;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RenderStability {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    String fileIn = "C:/Users/mbrasebin/Desktop/Exp1/100iterations/temp/inflboucle2e.csv";
    
    
   double du =  (-21953 + 20716) / 5;
   
   for(int i=1; i<6;i++){
     System.out.println(-20716  
         + du * i);
   }
   
   

    int count = 0;
    double[] energy = new double[100];
    int[] boxes = new int[100];

    try {
      FileReader fR = new FileReader(new File(fileIn));

      BufferedReader br = new BufferedReader(fR);

      String s;

      br.readLine();

      while ((s = br.readLine()) != null) {

        String[] tabS = s.split(";");

        energy[count] = Double.parseDouble(tabS[1]);
        boxes[count] = Integer.parseInt(tabS[2]);

        count++;

      }

      fR.close();

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;

    int minBox = 9999;
    int maxBox = 0;

    for (int i = 0; i < 100; i++) {
      min = Math.min(min, energy[i]);
      max = Math.max(max, energy[i]);

      minBox = Math.min(minBox, boxes[i]);
      maxBox = Math.max(maxBox, boxes[i]);
    }

    double pas = (max - min) / 10;

    List<List<Integer>> tabFinal = new ArrayList<>();

    for (int i = 0; i < maxBox - minBox + 1; i++) {

      List<Integer> lInteger = new ArrayList<>();
      for (int j = 0; j < 10; j++) {
        lInteger.add(0);
      }

      tabFinal.add(lInteger);
    }
    
    
    for (int i = 0; i < 100; i++) {
      double d = energy[i];
      
      
      
      if(d > -20963.458891831135 && d < -20839.736157648214  ){
        
      System.out.println("test");
    }
    
    }
    
    for (int i = 0; i < 100; i++) {

      double d = energy[i];

      int index = 10;
      
      
      
      if(d > -20963.458891831135 && d < -20839.736157648214  ){
        
      System.out.println("test");
    }

      for (int j = 1; j < 10; j++) {

        if (d < min + pas * j) {

          index = j;

          break;
        }

      }

      index = index -1;
      
      int nbBox = boxes[i];

      tabFinal.get(nbBox - minBox).set(index,
          tabFinal.get(nbBox - minBox).get(index) + 1);

    }

    
    int nbCount = 0;


    for (int j = 0; j < 10; j++) {
      
      System.out.println(min + (j +0.5) * pas + "; ");
    }
    
    System.out.println("");


    for (int i = 0; i < maxBox - minBox + 1; i++) {
      
      for (int j = 0; j < 10; j++) {
        
        Integer elem  = tabFinal.get(i).get(j);
        nbCount = nbCount + elem;
        
        
        System.out.print(elem+ ";");
        
      }
      
      System.out.println("");

    }
    
    
    System.out.println(nbCount);

  }

}
