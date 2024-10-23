package boolruntime.compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Paths;

public class Preprocessor {

    public static void processor(String file) {
        try(Scanner scanner = new Scanner(Paths.get(file))) {
            clearOutputFile();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.isEmpty()) {
                    continue;
                }
                //line = line.replaceAll("\\s+", " ");
                //System.out.println(line);
                String[] words = line.trim().split("\\s+");
                String processedLine = toString(words);
                writeToFile(processedLine);
            }
        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
        }
    }

    private static String toString(String[] words) {
        String line = "";
        for (int i = 0; i < words.length; i++) {
            line += words[i] + " ";
        }
        line += "\n";
        return line;
    }

    private static void clearOutputFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

        } catch (IOException e) {
            System.out.println("Erro ao limpar o arquivo de saída!");
        }
    }

    private static void writeToFile(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            writer.write(line);
        } catch (IOException e) {
            System.out.println("Erro de compilação!");
        }
    }
}