package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class GarbageCollector{
    private final static HashMap<Integer, Color> objectColor = new HashMap<>();
    private static Color currentColor = Color.RED;

    
    private GarbageCollector(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public void mark(){
        List<Variable> refs = Memory.getAllVariables();
        for(Variable ref : refs){
            if(ref.getType() == Type.REFERENCE){
                objectColor.put(ref.getValue(), currentColor);
            }
        }
    }

    public void sweep(){
        for(Map.Entry<Integer, Color> entry : objectColor.entrySet()){
            if(!(currentColor.equals(entry.getValue())))
                Memory.removeObject(entry.getKey());
                objectColor.remove(entry.getKey());
        }
        if(currentColor.equals(Color.RED)){
            currentColor = Color.BLACK;
        }else{
            currentColor = Color.RED;
        }
    }

    public void newObjectColor(Integer code){
        objectColor.put(code, Color.GRAY);
    }

    private enum Color{
        RED,
        GRAY,
        BLACK
    };
}