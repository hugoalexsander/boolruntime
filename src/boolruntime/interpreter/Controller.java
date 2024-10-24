package boolruntime.interpreter;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.*;


public class Controller{
    private static HashMap<String, HashMap<String, Integer>> classMap;
    private static Stack<HashMap<String, Integer>> scopeMap;
    private static Stack<Integer> stack;
    private static Integer pc;

    public static void init(String input){
        Pattern classPattern = Pattern.compile("^\\s*class\\s+[a-zA-Z]+\\s*\\n$");
        Pattern methodPatter = Pattern.compile("^\\s*method\\s+[a-zA-Z]+\\(\\)\\s*\\n$");
        Pattern methodArgPatter = Pattern.compile("^\\s*method\\s+[a-zA-Z]+\\([a-zA-Z]+(,\\s*[a-zA-Z]+)*\\)\\s*\\n$");
        Pattern mainPatter = Pattern.compile("^\\s*main\\s*\\(\\)\\s*\\n$");


    }

    public static void step(){

    }

    public static void exec(){
        
    }

}

