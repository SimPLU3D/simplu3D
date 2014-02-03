package fr.ign.cogit.gtru3d.test;

import fr.ign.cogit.gru3d.regleUrba.Executor;



public class ExecutorTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    fr.ign.cogit.gru3d.regleUrba.Executor.REPERTOIRE = ExecutorTest.class.getClassLoader()
        .getResource("data3d-gtru/").getPath();
    
    try {
      Executor.main(null);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
