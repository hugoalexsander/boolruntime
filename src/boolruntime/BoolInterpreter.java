package boolruntime;
import boolruntime.interpreter.Executor;


// O que pode dar errado?...
public class BoolInterpreter{
    public static void main(String[] args) {
        if (args.length > 0) {
            Executor.run(args[0]);
        } else {
            System.out.println("Por favor, forneça o caminho do arquivo como argumento.");
        }
    }
}