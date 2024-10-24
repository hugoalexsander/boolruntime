package boolruntime.compiler;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Paths;


public class Mapper {
    private static String input;
    private static String output;
    
    public static void processor(String fileInput, String fileOutput){
        input = fileInput;
        output = fileOutput;
        clearFile(output);
        firstProcessor();
        secondProcessor();
    }

    private static void firstProcessor() {
        try(Scanner scanner = new Scanner(Paths.get(input))) {
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.matches("^\\s*$") || line.isEmpty()) {
                    continue;
                }
                String[] words = line.trim().split("\\s+");
                writeToFile(toString(words), output);
            }
        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
        }
    }

    private static void secondProcessor() {
        

    }

    private static String toString(String[] words) {
        String line = "";
        for (int i = 0; i < words.length - 1; i++) {
            
            line += words[i] + " ";
        }
        line += words[words.length - 1];
        line = line.replaceAll("\\s*\\(\\s*", "(");
        line = line.replaceAll("\\s*\\)\\s*", ")");
        line = line.replaceAll("\\s*,\\s*", ",");
        line = line.replaceAll("\\s*\\-\\s*", "-");
        line = line.replaceAll("\\s*\\.\\s*", ".");
        //line = line.replaceAll("\\s*\\_\\s*", "_");
        line += "\n";
        return line;
    }

    private static void clearFile(String output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {

        } catch (IOException e) {
            System.out.println("Erro ao limpar o arquivo de saída!");
        }
    }

    private static void writeToFile(String line, String output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            String tmp = MapperPattern.getMapping(line);
            if(tmp != null) writer.write(tmp);
            else writer.write(line);
            //writer.write(line);
        } catch (IOException e) {
            System.out.println("Erro de compilação!");
        }
    }
}