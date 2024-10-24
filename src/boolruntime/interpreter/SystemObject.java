package boolruntime.interpreter;
import java.util.HashMap;
import java.util.List;


enum Color{
    RED,
    GRAY,
    BLACK
}

public class SystemObject{
    private HashMap<String, Integer> attributes;
    private static List<SystemObject> objects;
    private String className;
    private Color color;
}