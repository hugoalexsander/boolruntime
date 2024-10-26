package boolruntime.interpreter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class GarbageCollector{
    private final static HashMap<Integer, Color> objectColor = new HashMap<>();
    private static Color currentColor = Color.RED;

    
    private GarbageCollector(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    static void gcCall(){
        mark();
        sweep();
    }

    static void mark(){
        List<Variable> refs = Memory.getAllVariables();
        for(Variable ref : refs){
            if(ref.getType() == Type.REFERENCE){
                objectColor.put(ref.getValue(), currentColor);
            }
        }
    }

    static void sweep(){
        List<Integer> targets = new ArrayList<>();
        for(Map.Entry<Integer, Color> entry : objectColor.entrySet()){
            if(!(currentColor.equals(entry.getValue())))
                targets.add(entry.getKey());
        }
        for(Integer target : targets){
            Memory.removeObject(target);
            objectColor.remove(target);
        }
        if(currentColor.equals(Color.RED)){
            currentColor = Color.BLACK;
        }else{
            currentColor = Color.RED;
        }
    }

    static void newObjectColor(Integer code){
        objectColor.put(code, Color.GRAY);
    }

    private enum Color{
        RED,
        GRAY,
        BLACK
    };

    static void debugPrint(){
        for(Map.Entry<Integer, Color> entry : objectColor.entrySet()){
            Integer key = entry.getKey();
            Color color = entry.getValue();

            System.out.println("- Object key : " + key + " - Color : " + color);
        }
    }
}