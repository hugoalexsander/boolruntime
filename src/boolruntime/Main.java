package boolruntime;
import boolruntime.compiler.Mapper;
import boolruntime.interpreter.Executor;


// O que pode dar errado?...
public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            Mapper.processor(args[0], args[1]);
            Executor.run(args[1]);
        } else {
            System.out.println("Por favor, forneça o caminho do arquivo como argumento.");
        }
    }
}