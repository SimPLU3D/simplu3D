package fr.ign.cogit.gtru3d.test;

import junit.framework.Assert;

import org.junit.Test;

import fr.ign.cogit.gru3d.regleUrba.Executor;



public class ExecutorTest {

  /**
   * @param args
   */
  public static void main(String[] args) {

    (new ExecutorTest()).testLoader();
  }
  
  
  @Test
  public void testLoader(){
    
    
    fr.ign.cogit.gru3d.regleUrba.Executor.REPERTOIRE = ExecutorTest.class.getClassLoader()
        .getResource("data3d-gtru/").getPath();
    
    try {
      Executor.main(null);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Assert.assertTrue(true);
  }

  

}
