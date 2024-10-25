package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.Queue;
import java.util.LinkedList;

public class Model{ 
    private final static HashMap<String, Definition> map = new HashMap<>();

    private Model(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static void init(String input){
        Pattern classPattern = Pattern.compile("^\\s*class\\s+([a-zA-Z]+)\\s*\\n$");
        Matcher classMatcher = classPattern.matcher(input);
        Pattern methodPattern = Pattern.compile("^\\s*method\\s+([a-zA-Z]+)\\(([a-zA-Z]+(,\\s*[a-zA-Z]+)*)*\\)\\s*\\n$");
        Matcher methodMatcher = methodPattern.matcher(input);
        Pattern mainPattern = Pattern.compile("^\\s*main\\s*\\(\\)\\s*\\n$");
        Matcher mainMatcher = mainPattern.matcher(input);
        Pattern varsPattern = Pattern.compile("^\\s*vars\\s+([a-zA-Z]+(\\s*,\\s*[a-zA-Z]+)*)\\s*\\n$");
        Matcher varsMatcher = varsPattern.matcher(input);
        try(Scanner scanner = new Scanner(Paths.get(input))) {
            Integer lineInt = 0;
            String line = scanner.nextLine();
            while(scanner.hasNextLine()){
                if(classMatcher.find()){
                    List<String> attributes = new ArrayList<>();
                    Integer classLine;
                    classLine = lineInt;
                    String className = classMatcher.group(1);
                    attributes.add("_prototype");
                    while(scanner.hasNextLine() && !line.equals("^\\s*end-class\\s*\\n$")){
                        line = scanner.nextLine();
                        lineInt++;           
                        if(varsMatcher.find()){
                            for(String attribute : varsMatcher.group(1).split("\\s*,\\s*")){
                                attributes.add(attribute);
                            }
                        }else if(methodMatcher.find()){
                            List<String> metVariables = new ArrayList<>();
                            Queue<String> arguments = new LinkedList<>();
                            Integer methodLine = lineInt;
                            String methodName = methodMatcher.group(1);
                            for(String argument : methodMatcher.group(2).split("\\s*,\\s*")){
                                arguments.add(argument);
                            }
                            metVariables.add("self");
                            while(scanner.hasNextLine() && !line.equals("^\\s*begin\\s*\\n$")){
                                if(varsMatcher.find()){
                                    for(String attribute : varsMatcher.group(1).split("\\s*,\\s*")){
                                        metVariables.add(attribute);
                                    }
                                }
                            }
                            Definition method = new Definition(metVariables, arguments, methodLine);
                            map.put(className + "|" + methodName, method);
                        }
                    }
                    Definition Class = new Definition(attributes, classLine);
                    map.put(className, Class);
                }else if(mainMatcher.find()){
                    List<String> mainAttributes = new ArrayList<>();
                    Integer mainLine = lineInt;
                    while(scanner.hasNextLine() && !line.equals("^\\s*begin\\s*\\n$")){
                        line = scanner.nextLine();
                        lineInt++;           
                        if(varsMatcher.find()){
                            for(String attribute : varsMatcher.group(1).split("\\s*,\\s*")){
                                mainAttributes.add(attribute);
                            }
                        }
                    }
                    Definition Main = new Definition(mainAttributes, mainLine);
                    map.put("main", Main);
                }
            }
        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
        }        
    }

    public static List<String> getClassElements(String className){
        return map.get(className).elements;
    }

    public static Integer getClassLine(String className){
        return map.get(className).line;
    }

    public static List<String> getMethodElements(String className, String methodName){
        return map.get(className + "|" + methodName).elements;
    }

    public static Integer getMethodLine(String className, String methodName){
        return map.get(className + "|" + methodName).line;
    }

    public static Queue<String> getMethodArguments(String className, String methodName){
        return map.get(className + "|" + methodName).arguments;
    }

    private static class Definition{
        private final List<String> elements;
        private final Queue<String> arguments;
        private final Integer line;

        public Definition(List<String> variables, Integer line){
            this.elements = variables;
            arguments = new LinkedList<String>();
            this.line = line;
        }

        public Definition(List<String> variables, Queue<String> arguments, Integer line){
            this.elements = variables;
            this.arguments = arguments;
            this.line = line;
        }
    }
}