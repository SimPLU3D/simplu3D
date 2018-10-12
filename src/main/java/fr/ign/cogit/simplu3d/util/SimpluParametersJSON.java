package fr.ign.cogit.simplu3d.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimpluParametersJSON implements SimpluParameters{
    private JSONObject jsonObject;
    public SimpluParametersJSON(File f) {
        JSONParser parser = new JSONParser();
        File file = (f.getName().endsWith(".xml")) ? new File(f.getParentFile(),f.getName().replaceAll(".xml",".json")) : f;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public Object get(String name) {
        return this.jsonObject.get(name);
    }

    public String getString(String name) {
        return this.jsonObject.getOrDefault(name, "").toString();
    }

    public boolean getBoolean(String name) {
        return Boolean.parseBoolean(this.jsonObject.getOrDefault(name, false).toString());
    }

    public double getDouble(String name) {
        return Double.parseDouble(this.jsonObject.getOrDefault(name, 0.0D).toString());
    }

    public int getInteger(String name) {
        return Integer.parseInt(this.jsonObject.getOrDefault(name, 0).toString());
    }

    public float getFloat(String name) {
        return Float.parseFloat(this.jsonObject.getOrDefault(name, 0.0F).toString());
    }

    public void set(String name, Object value) {
        this.jsonObject.put(name, value);
    }
}
