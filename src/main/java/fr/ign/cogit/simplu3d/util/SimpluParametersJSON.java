package fr.ign.cogit.simplu3d.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimpluParametersJSON implements SimpluParameters {
  private JSONObject jsonObject;

  public SimpluParametersJSON(File f) {
    JSONParser parser = new JSONParser();
    File file = (f.getName().endsWith(".xml")) ? new File(f.getParentFile(), f.getName().replaceAll(".xml", ".json")) : f;
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

  public SimpluParametersJSON(SimpluParametersJSON p) {
    jsonObject = new JSONObject();
    for (Object o : p.jsonObject.entrySet()) {
      Entry e = (Entry) o;
      jsonObject.put(e.getKey(), e.getValue());
    }
  }

  public SimpluParametersJSON(List<File> lf) {
    jsonObject = new JSONObject();
    JSONParser parser = new JSONParser();
    for (File f : lf) {
      File file = (f.getName().endsWith(".xml")) ? new File(f.getParentFile(), f.getName().replaceAll(".xml", ".json")) : f;
      try {
        JSONObject jsonObjectForFile = (JSONObject) parser.parse(new FileReader(file));
        jsonObject.putAll(jsonObjectForFile);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  public void add(SimpluParametersJSON p) {
    jsonObject.putAll(p.jsonObject);
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
  
  public String toString() {
    return this.jsonObject.toJSONString();
  }
}
