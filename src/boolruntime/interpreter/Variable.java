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

    public Variable(Integer value){
        this.value = value;
        this.type = Type.UNDEFINED;
    }

    public Variable(Integer value, Type type){
        this.value = value;
        this.type = type;
    }

    public Integer getValue(){
        return this.value;
    }

    public void setValue(Integer value){
        this.value = value;
    }

    public Type getType(){
        return this.type;
    }

    public void setType(Type type){
        this.type = type;
    }
}