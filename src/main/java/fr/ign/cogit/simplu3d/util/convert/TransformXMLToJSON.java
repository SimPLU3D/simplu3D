package fr.ign.cogit.simplu3d.util.convert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

import fr.ign.parameters.Parameter;
import fr.ign.parameters.ParameterComponent;
import fr.ign.parameters.Parameters;

public class TransformXMLToJSON {
    public static void main(String[] args) throws Exception {
        String folderName = TransformXMLToJSON.class.getClassLoader().getResource("scenario/").getPath();
        File folderOut = new File("JSON");
        final boolean mkdirs = folderOut.mkdirs();
        File folder = new File(folderName);
        File[] files = folder.listFiles();
        for (File fileIn: files) {
            System.out.println(fileIn);
            if (fileIn.getName().endsWith(".xml")) {
                Parameters p = Parameters.unmarshall(fileIn);
                JSONObject obj = new JSONObject();
                for (ParameterComponent c : p.entry) {
                    Parameter param = (Parameter) c;
                    obj.put(param.getKey(), param.getValue());
                }
                try (FileWriter file = new FileWriter(new File(folderOut, fileIn.getName().replaceAll(".xml", ".json")))) {
                    file.write(obj.toJSONString());
                    file.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.print(obj);
            }
        }
        //String fileName = "scenariotest.xml";
    }
}
