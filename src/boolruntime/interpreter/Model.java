package boolruntime.interpreter;
import java.util.HashMap;
import java.util.List;


public class Model{ 
    private final static HashMap<String, Definition> map = new HashMap<>();

    private Model(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static void init(String input){
        
        /*
        Pattern classPattern = Pattern.compile("^\\s*class\\s+([a-zA-Z]+)\\s*\\n$");
        Matcher classMatcher = classPattern.matcher(input);
        Pattern methodPattern = Pattern.compile("^\\s*method\\s+([a-zA-Z]+)\\(([a-zA-Z]+(,\\s*[a-zA-Z]+)*)*\\)\\s*\\n$");
        Matcher methodMatcher = classPattern.matcher(input);
        Pattern mainPattern = Pattern.compile("^\\s*main\\s*\\(\\)\\s*\\n$");
        Matcher mainMatcher = classPattern.matcher(input);
        Pattern varsPattern = Pattern.compile("^\\s*vars\\s+([a-zA-Z]+(\\s*,\\s*[a-zA-Z]+)*)\\s*\\n$");
        Matcher varsMatcher = varsPattern.matcher(input);
        
        try(Scanner scanner = new Scanner(Paths.get(input))) {
            Integer lineInt = 0;
            String line = scanner.nextLine();

            while(scanner.hasNextLine()){
                if(classMatcher.find()){
                    String name = classMatcher.group(1);
                    HashMap<String, MethodModel> methodMap = new HashMap<>();
                    List<String> attrs = new ArrayList<>();
                    while (scanner.hasNextLine() && !line.matches("^\\s*end-class\\s*\\n$")) {
                        line = scanner.nextLine();
                        lineInt++;
                        if(varsMatcher.find()){
                            for(String banana : varsMatcher.group(1).split(",")){
                                attrs.add(banana);
                            }
                        }else if(methodMatcher.find() ) {
                            List<String> attrsMet = new ArrayList<>();
                            Integer metInt = lineInt;
                            while (scanner.hasNextLine() && !line.matches("^\\s*end-method\\s*\\n$")) {
                                line = scanner.nextLine();
                                lineInt++;
                                if(varsMatcher.find()){
                                    for(String banana : varsMatcher.group(1).split(",")){
                                        attrsMet.add(banana);
                                    }
                                }
                            }
                            MethodModel model = new MethodModel(attrsMet, metInt);
                            methodMap.put(methodMatcher.group(1), model);
                        }
                    }
                    ClassModel classModel = new ClassModel(attrs, methodMap);
                    classMap.put(name, classModel);
                }
            }
            
            

        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
        }
        */
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

    private static class Definition{
        private final List<String> elements;
        private final Integer line;

        public Definition(List<String> elements, Integer line){
            this.elements = elements;
            this.line = line;
        }
    }
}