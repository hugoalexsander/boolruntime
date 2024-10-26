package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;


public class Memory{
    private final static HashMap<Integer, Object> objects = new HashMap<>();
    private final static Stack<HashMap<String, Variable>> scopes = new Stack<>();
    private final static Stack<Variable> stack = new Stack<>();
    private static List<String> program = new ArrayList<>();

    private Memory(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    static void freeMemory(){
        objects.clear();
        while(!scopes.isEmpty()) scopes.pop().clear();
        stack.clear();
    }

    static void loadProgram(String input){
        try(Scanner scanner = new Scanner(Paths.get(input))){
            program.add(scanner.nextLine());
            while (scanner.hasNextLine()) {
                program.add(scanner.nextLine());
            }
        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
            System.exit(0);
        }
    }

    static String getLine(Integer line){
        if((program.size() - 1) < line){
            return null;
        }
        return program.get(line);
    }
    
    static void insertObject(Integer code, Object object){
        objects.put(code, object);
    }

    static void removeObject(Integer code){
        objects.remove(code);
    }

    static Object getObject(Integer code){
        return objects.get(code);
    }

    static void initStackScopes(){
        scopes.add(new HashMap<>());
        Variable builtIn = new Variable(Object.createObject("Output"), Type.REFERENCE);
        scopes.peek().put("io", builtIn);
        scopes.add(new HashMap<>());
        for(String element : Model.getClassElements("main")){
            scopes.peek().put(element, new Variable(0, Type.UNDEFINED));
        }
    }

    static void startScope(String className, String methodName){
        scopes.add(new HashMap<>());
        for(String element : Model.getMethodElements(className, methodName)){
            scopes.peek().put(element, new Variable(0, Type.UNDEFINED));
        }
        for(String arguments : Model.getMethodArguments(className, methodName)){
            scopes.peek().put(arguments, new Variable(0, Type.UNDEFINED));
        }

    }

    static void endScope(){
        scopes.pop();
    }

    static Variable getVar(String name){
        if(scopes.peek().containsKey(name)) return scopes.peek().get(name);
        else if(scopes.get(0).containsKey(name)) return scopes.get(0).get(name);
        else return null;
    }

    static void setVar(String name, Variable variable){
        scopes.peek().get(name).setValue(variable.getValue());
        scopes.peek().get(name).setType(variable.getType());
    }

    static Variable remove(){
        return stack.pop();
    }

    static void insert(Variable variable){
        stack.push(variable);
    }

    static List<Variable> getAllVariables(){
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

    static void debugPrint(){
        System.out.println("Objects:");
        for (Map.Entry<Integer, Object> entry : objects.entrySet()) {
            Integer code = entry.getKey();
            Object obj = entry.getValue();
            System.out.println("Object Code: " + code + ", Class: " + obj.getClassName());
            for (Map.Entry<String, Variable> attribute : obj.getAttributes().entrySet()) {
                System.out.println(" - Attribute Name: " + attribute.getKey() + ", Value: " + attribute.getValue().getValue() + ", Type: " + attribute.getValue().getType());
            }
        }
        System.out.println();
        for (int i = 0; i < scopes.size(); i++) {
            System.out.println("Scope Level " + i + ":");
            HashMap<String, Variable> scope = scopes.get(i);
            for (Map.Entry<String, Variable> entry : scope.entrySet()) {
                String varName = entry.getKey();
                Variable var = entry.getValue();
                System.out.println(" - Variable Name: " + varName + ", Value: " + var.getValue() + ", Type: " + var.getType());
            }
        }
        System.out.println();
        System.out.println("Pilha:");
        for (int i = 0; i < stack.size(); i++) {
            Variable var = stack.get(i);
            System.out.println(" - Stack Position " + i + ": Value: " + var.getValue() + ", Type: " + var.getType());
        }
        System.out.println();
    }

    static void debugPrintObjects(){
        for(Map.Entry<Integer, Object> entry : objects.entrySet()){
            Integer code = entry.getKey();
            Object obj = entry.getValue();
            System.out.println("Object Code: " + code + ", Class: " + obj.getClassName());
            for (Map.Entry<String, Variable> attribute : obj.getAttributes().entrySet()) {
                System.out.println(" - Attribute Name: " + attribute.getKey() + ", Value: " + attribute.getValue().getValue() + ", Type: " + attribute.getValue().getType());
            }
            System.out.println();
        }
    }

    static void debugPrintScopes(){
        for (int i = 0; i < scopes.size(); i++) {
            System.out.println("Scope Level " + i + ":");
            HashMap<String, Variable> scope = scopes.get(i);
            for (Map.Entry<String, Variable> entry : scope.entrySet()) {
                String varName = entry.getKey();
                Variable var = entry.getValue();
                System.out.println(" - Variable Name: " + varName + ", Value: " + var.getValue() + ", Type: " + var.getType());
            }
        }
    }    

    static void debugPrintStack(){
        System.out.println("Pilha:");
        for (int i = 0; i < stack.size(); i++) {
            Variable var = stack.get(i);
            System.out.println(" - Stack Position " + i + ": Value: " + var.getValue() + ", Type: " + var.getType());
        }
        System.out.println();
    }

    static void debugPrintProgram(){
        System.out.println("Programa:");
        for(String line : program){
            System.out.println(line);
        }
        System.out.println();
    }
}