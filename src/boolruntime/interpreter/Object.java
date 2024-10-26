package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Map;

public class Object{
    private static Integer counter = 0;
    private final HashMap<String, Variable> attributes;
    private final String className;

    private Object(String className){
        this.attributes = new HashMap<>();
        this.className = className;
        for(String attribute : Model.getClassElements(className)){
            this.attributes.put(attribute, new Variable(0, Type.UNDEFINED));
        }
    }

    static Integer createObject(String className){
        Integer code = counter;
        Memory.insertObject(code, new Object(className));
        GarbageCollector.newObjectColor(code);
        counter += 1;
        return code;
    }

    String getClassName(){
        return this.className;
    }
    
    Variable getAttributeValue(String name){
        return this.attributes.get(name);
    }

    HashMap<String, Variable> getAttributes(){
        return this.attributes;
    }

    void debugPrint(){
        System.out.println("Class Name: " + this.getClassName());

        for(Map.Entry< String, Variable > entry : this.getAttributes().entrySet()){
            String attributeName = entry.getKey();
            Variable attributeVariable = entry.getValue();
            System.out.println( "- Attribute Name : " + attributeName + 
                                " - Attribute Value : " + attributeVariable.getValue() + 
                                " - Attribute Type : " + attributeVariable.getType());
        }
    }
}