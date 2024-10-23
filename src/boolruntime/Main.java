package boolruntime;

import boolruntime.compiler.Preprocessor;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String filePath = args[0];
            Preprocessor.processor(args[0]);
        } else {
            System.out.println("Por favor, forneça o caminho do arquivo como argumento.");
        }
    }
}
