package boolruntime.compiler;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


public class Mapper {
    private static String input;
    private static String output;
    
    private Mapper(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static void processor(String fileInput, String fileOutput) {
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

    private static void secondProcessor(){
        try(Scanner scanner = new Scanner(Paths.get(output));){
            List<String> lines = new ArrayList<>();
            while(scanner.hasNextLine()){
                lines.add(scanner.nextLine());
            }
            Pattern pattern = Pattern.compile("if <n>");
            for (int i = 0; i < lines.size(); i++) {
                Matcher matcher = pattern.matcher(lines.get(i));
                if (matcher.matches()) {
                    int countIf = countLinesIf(lines, i);
                    String line = lines.get(i).replaceAll("<n>", String.valueOf(countIf));
                    lines.set(i, line);  
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                for (String line : lines) {
                    if(line.matches("^\\s*$") || line.isEmpty()) {
                        continue;
                    }
                    writer.write(line);
                    writer.newLine();  
                }
            }
        } catch (Exception e) {
            System.out.println("Erro de compilação: " + e.getMessage());
        }
    }

    private static int countLinesIf(List<String> lines, int position){
        int i = position + 1;
        int countIf = 0;
        while (!lines.get(i).matches("end-if") && !lines.get(i).matches("else <n>")) {
            countIf++;
            i++;
        }
        if (lines.get(i).matches("else <n>"))
            countLinesElse(lines, i);
        else if (lines.get(i).matches("end-if")) {
            lines.set(i, "");
        }
        return countIf;
    }

    private static int countLinesElse(List<String> lines, int position) {
        int i = position + 1;
        int countElse = 0;

        while (!lines.get(i).matches("end-if")) {
            countElse++;
            i++;
        }

        String line = lines.get(position).replaceAll("<n>", String.valueOf(countElse));
        lines.set(position, line);  

        lines.set(i, ""); 
        return countElse;
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
            writer.write("class io\nvars rt\nmethod print(x)\nconst 0\nstore rt\nload x\nprin\nload rt\nret\nend-method\nend-class\n");
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