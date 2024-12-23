package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;

public class Model{ 
    private final static HashMap<String, Definition> map = new HashMap<>();

    private Model(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    static void init(String input){
        try(Scanner scanner = new Scanner(Paths.get(input))) {
            Integer lineInt = 0;
            String line = scanner.nextLine();
            while(scanner.hasNextLine()){  
                if(mainLineFlag(line)){
                    Integer mainLine = lineInt;
                    List<String> mainElements = new ArrayList<>();
                    if(scanner.hasNextLine()) {
                        line = scanner.nextLine();
                        lineInt++;
                        if((varsLineFlag(line))){
                            mainElements = varsExtract(line);
                        }
                    }
                    map.put("main", new Definition(mainElements, mainLine));
                }else if(classLineFlag(line)){
                    List<String> classAtributes = new ArrayList<>();
                    String className = classNameExtract(line);
                    if(scanner.hasNextLine() && !line.matches("\\s*end-class\\s*")){
                        line = scanner.nextLine();
                        lineInt++;
                        if(varsLineFlag(line)){
                            classAtributes = varsExtract(line);
                        }
                    }
                    while(scanner.hasNextLine() && !line.matches("\\s*end-class\\s*")){
                        if(methodLineFlag(line)){
                            Integer methodLine = lineInt;
                            String methodName = methodNameExtract(line);
                            Queue<String> arguments = argumentsExtract(line);
                            List<String> elements = new ArrayList<>();
                            if(scanner.hasNextLine() && !line.matches("\\s*end-method\\s*")){
                                line = scanner.nextLine();
                                lineInt++;
                                if(varsLineFlag(line)){
                                    elements = varsExtract(line);
                                }
                            }
                            elements.add("self");
                            map.put(className + "|" + methodName, new Definition(elements, arguments, methodLine));
                            
                        }
                        line = scanner.nextLine();
                        lineInt++;
                    }                        
                    classAtributes.add("_prototype");
                    map.put(className, new Definition(classAtributes));
                }
                line = scanner.nextLine();
                lineInt++;
            }
        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
        }
    }

    private static boolean classLineFlag(String line){
        Pattern pattern = Pattern.compile("^\\s*class\\s+[a-zA-Z]+\\s*$");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private static boolean methodLineFlag(String line){
        Pattern pattern = Pattern.compile("^\\s*method\\s+[a-zA-Z]+\\s*\\(\\s*([a-zA-Z]+(\\s*,\\s*[a-zA-Z]+)*)?\\s*\\)$");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private static boolean mainLineFlag(String line){
        Pattern pattern = Pattern.compile("^\\s*main\\s*\\(\\s*\\)$");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private static boolean varsLineFlag(String line){
        Pattern pattern = Pattern.compile("^vars\\s+([a-zA-Z]+(?:,\\s*[a-zA-Z]+)*)$");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private static String classNameExtract(String line){
        Pattern pattern = Pattern.compile("^\\s*class\\s+([a-zA-Z]+)\\s*$");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group(1);
    }

    private static String methodNameExtract(String line){
        Pattern pattern = Pattern.compile("^\\s*method\\s+([a-zA-Z]+)\\s*\\(");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group(1);
    }

    private static List<String> varsExtract(String line){
        List<String> variables = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\s*vars\\s+([a-zA-Z]+(\\s*,\\s*[a-zA-Z]+)*)?\\s*$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String varPart = matcher.group(1);
            String[] vars = varPart.split("\\s*,\\s*");
            for (String var : vars) {
                variables.add(var.trim());
            }
        }
        return variables;
    }

    private static Queue<String> argumentsExtract(String line){
        Queue<String> arguments = new LinkedList<>();
        Pattern pattern = Pattern.compile("^\\s*method\\s+[a-zA-Z]+\\s*\\(\\s*([a-zA-Z]*(\\s*,\\s*[a-zA-Z]*)*)?\\s*\\)$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String argPart = matcher.group(1);
            String[] args = argPart.split("\\s*,\\s*");
            for (String arg : args) {
                if (!arg.isEmpty()) {
                    arguments.add(arg.trim());
                }
            }
        }
        return arguments;
    }

    static Integer getMainLine(){
        return map.get("main").line;
    }

    static List<String> getClassElements(String className){
        if(map.containsKey(className)) return map.get(className).elements;
        else return null;
    }

    static Integer getClassLine(String className){
        if(map.containsKey(className)) return map.get(className).line;
        else return null;
    }

    static List<String> getMethodElements(String className, String methodName){
        if(map.containsKey(className + "|" + methodName)) return map.get(className + "|" + methodName).elements;
        else return null;
    }

    static Integer getMethodLine(String className, String methodName){
        if(map.containsKey(className + "|" + methodName)) return map.get(className + "|" + methodName).line;
        else return null;
    }

    static Queue<String> getMethodArguments(String className, String methodName){
        if(map.containsKey(className + "|" + methodName)) return map.get(className + "|" + methodName).arguments;
        else return null;
    }

    static void debugPrint(){
        for(Map.Entry<String, Definition> entryMap : map.entrySet()){
            System.out.println(entryMap.getKey());
            System.out.print("Elementos: ");
            for(String a : entryMap.getValue().elements){
                System.out.print(a + " ");
            }
            System.out.print("\nArgumentos: ");
            for(String a : entryMap.getValue().arguments){
                System.out.print(a + " ");
            }
            System.out.println("\n" + entryMap.getValue().line + "\n");
        }
    }

    private static class Definition{
        private final List<String> elements;
        private final Queue<String> arguments;
        private final Integer line;

        public Definition(List<String> elements){
            this.elements = elements;
            arguments = new LinkedList<String>();
            this.line = -1;
        }

        public Definition(List<String> elements, Integer line){
            this.elements = elements;
            this.arguments = new LinkedList<>();
            this.line = line;
        }

        public Definition(List<String> elements, Queue<String> arguments, Integer line){
            this.elements = elements;
            this.arguments = arguments;
            this.line = line;
        }
    }
}