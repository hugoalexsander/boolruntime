package boolruntime.interpreter;

enum Type{
    REFERENCE,
    NUMBER,
    BOOLEAN,
    UNDEFINED
};

public class Variable{
    private Integer value;
    private Type type;

    Variable(Integer value, Type type){
        this.value = value;
        this.type = type;
    }

    Integer getValue(){
        return this.value;
    }

    void setValue(Integer value){
        this.value = value;
    }

    Type getType(){
        return this.type;
    }

    void setType(Type type){
        this.type = type;
    }
}