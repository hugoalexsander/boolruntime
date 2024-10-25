package boolruntime.interpreter;
import java.util.HashMap;

public class Object{
    private static Integer counter;
    private final HashMap<String, Variable> attributes;
    private final String className;

    private Object(String className){
        this.attributes = new HashMap<>();
        this.className = className;
        for(String attribute : Model.getClassElements(className)){
            this.attributes.put(attribute, new Variable(0, Type.UNDEFINED));
        }
    }

    public static Integer createObject(String className){
        Integer code = counter;
        Memory.insertObject(code, new Object(className));
        counter++;
        return code;
    }

    public String getClassName(){
        return this.className;
    }
    
    public Integer getAttributeValue(String name){
        return this.attributes.get(name).getValue();
    }
}