package fr.ign.cogit.simplu3d.util;

public interface SimpluParameters {

    Object get(String name);

    String getString(String name);

    boolean getBoolean(String name);

    double getDouble(String name);

    int getInteger(String name);

    float getFloat(String name);

    void set(String name, Object value);
}
