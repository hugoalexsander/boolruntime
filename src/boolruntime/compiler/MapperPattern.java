package boolruntime.compiler;
import java.util.function.Function;
import java.util.regex.*;


public class MapperPattern{
    private static final Rule rules[] = {
        new Rule("^\\s*[a-zA-Z]+\\._prototype\\s*=\\s*[a-zA-Z]+\\n$", MapperPattern::prototypeEqVar),
        new Rule("^\\s*[a-zA-Z]+\\s*=\\s*[0-9]+\\n$", MapperPattern::varEqNum),
        new Rule("^\\s*[a-zA-Z]+\\s*=\\s*[a-zA-Z]+\\n$", MapperPattern::varEqVar),
        new Rule("^\\s*[a-zA-z]+\\s*=\\s*[a-zA-Z]+\\.[a-zA-z]+\\n$", MapperPattern::varEqAtt),
        new Rule("^\\s*[a-zA-Z]+\\s*=\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\)\\n$", MapperPattern::varEqMet),
        new Rule("^\\s*[a-zA-Z]+\\s*=\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\s*[a-zA-Z]+(,\\s*[a-zA-Z]+)*\\)\\n$", MapperPattern::varEqMetArg),
        new Rule("^\\s*[a-zA-Z]+\\s*=\\s*new\\s*[a-zA-Z]+\\n$", MapperPattern::varEqObj),
        new Rule("^\\s*[a-zA-Z]+\\s*=\\s*[a-zA-Z]+\\s*[+\\-\\*/]\\s*[a-zA-Z]+\\n$", MapperPattern::varEqOpe),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*=\\s*[0-9]+\\n$", MapperPattern::attEqNum),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-z]+\\s*=\\s*[a-zA-Z]+\\n$", MapperPattern::attEqVar),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-z]+\\s*=\\s*[a-zA-Z]+\\.[a-zA-z]+\\n$", MapperPattern::attEqAtt),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*=\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\)\\n$", MapperPattern::attEqMet),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*=\\s*[a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\s*[a-zA-Z]+(,\\s*[a-zA-Z]+)*\\)\\n$", MapperPattern::attEqMetArg),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-z]+\\s*=\\s*new\\s*[a-zA-Z]+\\n$", MapperPattern::attEqObj),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-z]+\\s*=\\s*[a-zA-Z]+\\s*[+\\-\\*/]\\s*[a-zA-Z]+\\n$", MapperPattern::attEqOpe),
        new Rule("^\\s*new\\s*[a-zA-Z]+\\n$", MapperPattern::objCreation),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-z]+\\s*\\(\\)\\n$", MapperPattern::methodCall),
        new Rule("^\\s*[a-zA-Z]+\\.[a-zA-z]+\\s*\\(\\s*[a-zA-Z]+(,\\s*[a-zA-Z]+)*\\)\\n$", MapperPattern::methodArgumentCall),
        new Rule("^\\s*if\\s+[a-zA-Z]+\\s+(eq|ne|gt|ge|lt|le)\\s+[a-zA-Z]+\\s+then\\n", MapperPattern::ifCase),
        new Rule("^\\s*else\\n$", MapperPattern::elseCase),
        new Rule("^\\s*return\\s*[a-zA-Z]+\\n$", MapperPattern::returnCase)
    };

    private MapperPattern(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    static String getMapping(String input){
        for (Rule rule : rules) {
            if (input.matches(rule.pattern)) {
                return rule.mapper.apply(input);
            }
        }
        return null;
    }

    private static String prototypeEqVar(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\._prototype\\s*=\\s*([a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " +  matcher.group(2) + "\n";
        rt += "load " + matcher.group(1) + "\n";
        rt += "set _prototype\n";
        return rt;
    }

    private static String varEqNum(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\s*=\\s*([0-9]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "const " +  matcher.group(2) + "\n";
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String varEqVar(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " +  matcher.group(2) + "\n";
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String varEqAtt(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-z]+)\\s*=\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " +  matcher.group(2) + "\n";
        rt += "get " + matcher.group(3) + "\n";
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String varEqMet(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\)\\n)$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += methodCall(matcher.group(2));
        rt = rt.replaceAll("pop\\n", "");
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String varEqMetArg(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\s*[a-zA-Z]+(,\\s*[a-zA-Z]+)*\\s*\\)\\n)$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += methodArgumentCall(matcher.group(2));
        rt = rt.replaceAll("pop\\n", "");
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String varEqObj(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\s*=\\s*(new\\s*[a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += objCreation(matcher.group(2) + "\n");
        rt = rt.replaceAll("pop\\n", "");
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String varEqOpe(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z]+)\\s*([+\\-\\*/])\\s*([a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(2) + "\n";
        rt += "load " + matcher.group(4) + "\n";
        if((matcher.group(3)).equals("+")){
            rt += "add\n";
        } else if((matcher.group(3)).equals("-")){
            rt += "sub\n";
        } else if((matcher.group(3)).equals("*")){
            rt += "mul\n";
        } else{
            rt += "div\n";
        }
        rt += "store " + matcher.group(1) + "\n";
        return rt;
    }

    private static String attEqNum(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-Z]+)\\s*=\\s*([0-9]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "const " + matcher.group(3) + "\n";
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }

    private static String attEqVar(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\s*=\\s*([a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(3) + "\n";
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }

    private static String attEqAtt(String input){  
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\s*=\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(3) + "\n";
        rt += "get " + matcher.group(4) + "\n";
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }

    private static String attEqMet(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\s*=\\s*([a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\)\\n)$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += methodCall(matcher.group(3));
        rt = rt.replaceAll("pop\\n", "");
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }

    private static String attEqMetArg(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\s*=\\s*([a-zA-Z]+\\.[a-zA-Z]+\\s*\\(\\s*[a-zA-Z]+(,\\s*[a-zA-Z]+)*\\s*\\)\\n)$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += methodArgumentCall(matcher.group(3));
        rt = rt.replaceAll("pop\\n", ""); // Amem, irmaos
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }

    private static String attEqObj(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\s*=\\s*(new\\s*[a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += matcher.group(3) + "\n";
        rt = rt.replaceAll("pop\\n", "");
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }
    
    private static String attEqOpe(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*([a-zA-Z]+)\\.([a-zA-z]+)\\s*=\\s*([a-zA-Z]+)\\s*([+\\-\\*/])\\s*([a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(3) + "\n";
        rt += "load " + matcher.group(5) + "\n";
        if((matcher.group(4)).equals("+")){
            rt += "add\n";
        } else if((matcher.group(4)).equals("-")){
            rt += "sub\n";
        } else if((matcher.group(4)).equals("*")){
            rt += "mul\n";
        } else{
            rt += "div\n";
        }
        rt += "load " + matcher.group(1) + "\n";
        rt += "set " + matcher.group(2) + "\n";
        return rt;
    }

    private static String objCreation(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("new\\s*([a-zA-Z]+)\\n");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "new " + matcher.group(1) + "\n";
        rt += "pop\n";
        return rt;
    }

    private static String methodCall(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^([a-zA-Z]+)\\.([a-zA-Z]+)\\s*\\(\\)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(1) + "\n";
        rt += "call " + matcher.group(2) + "\n";
        rt += "pop\n";
        return rt;
    }

    private static String methodArgumentCall(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^([a-zA-Z]+)\\.([a-zA-Z]+)\\s*\\(\\s*([a-zA-Z]+(,\\s*[a-zA-Z]+)*)\\)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        Pattern nameList = Pattern.compile("([a-zA-Z]+)");
        Matcher listMatcher = nameList.matcher(matcher.group(3));
        while (listMatcher.find()) {
            rt += "load " + listMatcher.group(1) + "\n";
        }
        rt += "load " + matcher.group(1) + "\n";
        rt += "call " + matcher.group(2) + "\n";
        rt += "pop\n";
        return rt;
    }

    private static String ifCase(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*if\\s+([a-zA-Z]+)\\s+(eq|ne|gt|ge|lt|le)\\s+([a-zA-Z]+)\\s+then\\s*\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(1) + "\n";
        rt += "load " + matcher.group(3) + "\n";
        rt += matcher.group(2) + "\n";
        rt += "if <n>\n";
        return rt;
    }

    private static String elseCase(String input){
        return "else <n>\n";
    }
    
    private static String returnCase(String input){
        String rt = "";
        Pattern pattern = Pattern.compile("^\\s*return\\s*([a-zA-Z]+)\\n$");
        Matcher matcher = pattern.matcher(input);
        matcher.matches();
        rt += "load " + matcher.group(1) + "\n";
        rt += "ret\n";
        return rt;
    }

    private static class Rule{
        private final String pattern;
        private final Function<String, String> mapper;
        
        public Rule(String pattern, Function<String, String> mapper){
            this.pattern = pattern;
            this.mapper = mapper;
        }
    }
}
