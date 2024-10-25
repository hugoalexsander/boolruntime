package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Memory{
    private final static HashMap<Integer, Object> objects = new HashMap<>(); // Objetos criados no programa, acessados via referencia numerica.
    private final static Stack<HashMap<String, Variable>> scopes = new Stack<>(); // Tabela de simbolos.
    private final static Stack<Variable> stack = new Stack<>(); // Pilha da maquina de pilha.

    private Memory(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static void insertObject(Integer code, Object object){
        objects.put(code, object);
    }

    public static void removeObject(Integer code){
        objects.remove(code);
    }

    public static Integer getVarScope(String name){
        return scopes.peek().get(name).getValue();
    }

    public static void setVarScope(String name, Integer number, Type type){
        scopes.peek().get(name).setValue(number);
        scopes.peek().get(name).setType(type);
    }

    public static void startScope(String className, String methodName){
        scopes.add(new HashMap<>());
        for(String attribute : Model.getMethodElements(className, methodName)){
            scopes.peek().put(attribute, new Variable(0));
        }
    }

    public static void endScope(){
        scopes.pop();
    }

    public static Integer remove(){
        return stack.pop().getValue();
    }

    public static void insert(Integer number, Type type){
        stack.push(new Variable(number, type));
    }

    public static List<Variable> getAllVariables(){
        List<Variable> rt = new ArrayList<>();
        return rt;
    }
}