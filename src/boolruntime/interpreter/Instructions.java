package boolruntime.interpreter;
import java.util.function.Consumer;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class Instructions{
    private final static Map<String, Consumer<String>> callerMap = Map.ofEntries(
        Map.entry("const", Instructions::constnumber),
        Map.entry("load", Instructions::load),
        Map.entry("store", Instructions::store),
        Map.entry("add", Instructions::add),
        Map.entry("sub", Instructions::sub),
        Map.entry("mul", Instructions::mul),
        Map.entry("div", Instructions::div),
        Map.entry("new", Instructions::newclass),
        Map.entry("get", Instructions::get),
        Map.entry("set", Instructions::set),
        Map.entry("call", Instructions::call),
        Map.entry("gt", Instructions::gt),
        Map.entry("ge", Instructions::ge),
        Map.entry("lt", Instructions::lt),
        Map.entry("le", Instructions::le),
        Map.entry("eq", Instructions::eq),
        Map.entry("ne", Instructions::ne),
        Map.entry("pop", Instructions::pop),
        Map.entry("if", Instructions::ifn),
        Map.entry("else", Instructions::elsen),
        Map.entry("ret", Instructions::ret),
        Map.entry("prin", Instructions::prin)
    );

    private Instructions(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    static void caller(String instruction, String parameter){
        callerMap.get(instruction).accept(parameter);
    }

    private static void constnumber(String number){
        Memory.insert(new Variable(Integer.parseInt(number), Type.NUMBER));
    }

    private static void load(String name){
        Memory.insert(Memory.getVar(name));
    }

    private static void store(String name){
        Memory.setVar(name, Memory.remove());
    }

    private static void add(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Variable result = new Variable(top.getValue() + down.getValue(), Type.NUMBER);
        Memory.insert(result);
    }

    private static void sub(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Variable result = new Variable(top.getValue() - down.getValue(), Type.NUMBER);
        Memory.insert(result);
    }

    private static void mul(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Variable result = new Variable(top.getValue() * down.getValue(), Type.NUMBER);
        Memory.insert(result);
    }

    private static void div(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Variable result = new Variable(top.getValue() / down.getValue(), Type.NUMBER);
        Memory.insert(result);
    }

    private static void newclass(String className){
        Integer code =  Object.createObject(className);
        Memory.insert(new Variable(code, Type.REFERENCE));
    }

    private static void get(String name){
        Variable objectCode = Memory.remove();
        Object object = Memory.getObject(objectCode.getValue());
        Variable attribute = object.getAttributeValue(name);
        while(attribute == null){
            objectCode = object.getAttributeValue("_prototype");
            object = Memory.getObject(objectCode.getValue());
            attribute = object.getAttributeValue(name);
        } 
        Memory.insert(attribute);
    }

    private static void set(String name){
        Variable objectCode = Memory.remove();
        Variable value = Memory.remove();
        Object object = Memory.getObject(objectCode.getValue());
        Variable attribute = object.getAttributeValue(name);
        while(attribute == null){
            objectCode = object.getAttributeValue("_prototype");
            object = Memory.getObject(objectCode.getValue());
            attribute = object.getAttributeValue(name);
        } 
        attribute.setValue(value.getValue());
        attribute.setType(value.getType());
    }

    private static void call(String name){
        List<String> args = new ArrayList<>();
        Variable objectCode = Memory.remove();
        Variable primalObject = new Variable(objectCode.getValue(), objectCode.getType());
        Object object = Memory.getObject(objectCode.getValue());
        Integer methodLine = Model.getMethodLine(object.getClassName(), name);
        while(methodLine == null){
            objectCode = object.getAttributeValue("_prototype");
            object = Memory.getObject(objectCode.getValue());
            methodLine = Model.getMethodLine(object.getClassName(), name);
        }
        Memory.startScope(object.getClassName(), name);
        Memory.setVar("self", primalObject);
        for(String argument : Model.getMethodArguments(object.getClassName(), name)){
            args.add(argument);
        }
        for(int i = args.size() - 1; i >= 0; i--){
            Memory.setVar(args.get(i), Memory.remove());
        }
        Executor.enterScope(methodLine);
    }

    private static void gt(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Integer bool;
        if(top.getValue() > down.getValue()){
            bool = 1;
        }else{
            bool = 0;
        }
        Variable comp = new Variable(bool, Type.BOOLEAN);
        Memory.insert(comp);
    }
    
    private static void ge(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Integer bool;
        if(top.getValue() >= down.getValue()){
            bool = 1;
        }else{
            bool = 0;
        }
        Variable comp = new Variable(bool, Type.BOOLEAN);
        Memory.insert(comp);
    }
    
    private static void lt(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Integer bool;
        if(top.getValue() < down.getValue()){
            bool = 1;
        }else{
            bool = 0;
        }
        Variable comp = new Variable(bool, Type.BOOLEAN);
        Memory.insert(comp);
    }

    private static void le(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Integer bool;
        if(top.getValue() <= down.getValue()){
            bool = 1;
        }else{
            bool = 0;
        }
        Variable comp = new Variable(bool, Type.BOOLEAN);
        Memory.insert(comp);
    }
    
    private static void eq(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Integer bool;
        if(top.getValue().equals(down.getValue())){
            bool = 1;
        }else{
            bool = 0;
        }
        Variable comp = new Variable(bool, Type.BOOLEAN);
        Memory.insert(comp);
    }
    
    private static void ne(String empty){
        Variable top = Memory.remove();
        Variable down = Memory.remove();
        Integer bool;
        if(!top.getValue().equals(down.getValue())){
            bool = 1;
        }else{
            bool = 0;
        }
        Variable comp = new Variable(bool, Type.BOOLEAN);
        Memory.insert(comp);
    }

    private static void pop(String empty){
        Memory.remove();
    }
    
    private static void ifn(String number){
        if(Memory.remove().getValue() == 0){
            Executor.setFlag(false);
            Executor.setPc(Executor.getPc() + Integer.parseInt(number));
        }else{
            Executor.setFlag(true);
        }
    }

    private static void elsen(String number){
        if(Executor.getFlag()){
            Executor.setPc(Executor.getPc() + Integer.parseInt(number));
        }
    }
    
    private static void ret(String empty){
        Memory.endScope();
        Executor.exitScope();
    }

    private static void prin(String empty){
        Variable top = Memory.remove();
        System.out.println(top.getValue());
    }
}