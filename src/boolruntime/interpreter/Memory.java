package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Map;
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

    public static Object getObject(Integer code){
        return objects.get(code);
    }

    public static void startMainScope(){
        scopes.add(new HashMap<>());
        for(String element : Model.getClassElements("main")){
            scopes.peek().put(element, new Variable(0, Type.UNDEFINED));
        }
    }

    public static void startScope(String className, String methodName){
        scopes.add(new HashMap<>());
        for(String element : Model.getMethodElements(className, methodName)){
            scopes.peek().put(element, new Variable(0, Type.UNDEFINED));
        }
    }

    public static void endScope(){
        scopes.pop();
    }

    public static Variable getVar(String name){
        return scopes.peek().get(name);
    }

    public static void setVar(String name, Variable variable){
        scopes.peek().get(name).setValue(variable.getValue());
        scopes.peek().get(name).setType(variable.getType());
    }

    public static Variable remove(){
        return stack.pop();
    }

    public static void insert(Variable variable){
        stack.push(variable);
    }

    public static List<Variable> getAllVariables(){
        List<Variable> rt = new ArrayList<>();
        for(Variable var : stack){
            rt.add(var);
        }
        for(HashMap<String, Variable> scope : scopes){
            for(Map.Entry<String, Variable> mapEntry : scope.entrySet()){
                rt.add(mapEntry.getValue());
            }
        }
        for(Object object : objects.values()){
            for(Variable attribute : object.getAttributes().values()){
                rt.add(attribute);
            }
        }
        return rt;
    }
}