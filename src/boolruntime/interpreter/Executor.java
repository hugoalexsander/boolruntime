package boolruntime.interpreter;
import java.util.regex.*;
import java.util.Stack;


public class Executor{
    private static String[] instructions = {
        "^\\s*(const)\\s+([0-9]+)\\s*$",
        "^\\s*(load)\\s+([a-zA-Z]+)\\s*$",
        "^\\s*(store)\\s+([a-zA-Z]+)\\s*$",
        "^\\s*(add)\\s*$",
        "^\\s*(sub)\\s*$",
        "^\\s*(mul)\\s*$",
        "^\\s*(div)\\s*$",
        "^\\s*(new)\\s+([a-zA-Z]+)\\s*$",
        "^\\s*(get)\\s+(_?[a-zA-Z]+)\\s*$",
        "^\\s*(set)\\s+(_?[a-zA-Z]+)\\s*$",
        "^\\s*(call)\\s+([a-zA-Z]+)\\s*$",
        "^\\s*(gt)\\s*$",
        "^\\s*(ge)\\s*$",
        "^\\s*(lt)\\s*$",
        "^\\s*(le)\\s*$",
        "^\\s*(eq)\\s*$",
        "^\\s*(ne)\\s*$",
        "^\\s*(pop)\\s*$",
        "^\\s*(if)\\s+([0-9]+)\\s*$",
        "^\\s*(else)\\s+([0-9]+)\\s*$",
        "^\\s*(ret)\\s*$",
        "^\\s*(prin)\\s*$"
    };
    private static Stack<Integer> pr = new Stack<>();
    private static Integer pc = 0;
    private static boolean skipElseFlag;
    private static Integer gc = 0;

    private Executor(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static void run(String inputFile){
        init(inputFile);
        while(execute(pc)){
            pc++;
            gc++;
            if(gc % 6 == 0){
                gc = 0;
                GarbageCollector.gcCall();
            }
        }
        Memory.freeMemory();
    }

    private static void init(String inputFile){
        Model.init(inputFile);
        Memory.loadProgram(inputFile);
        Memory.initStackScopes();
        pc = Model.getMainLine();
    }

    private static boolean execute(Integer line){
        String lineString = Memory.getLine(line);
        if(lineString == null || lineString.matches("^\\s*end\\s*\\n$")){
            return false;
        }
        for(String instruction : instructions){
            if(lineString.matches(instruction)){
                Pattern instructionPattern = Pattern.compile(instruction);
                Matcher instructionMatcher = instructionPattern.matcher(lineString);
                instructionMatcher.find();
                if(instructionMatcher.groupCount() == 1)
                {
                    Instructions.caller(instructionMatcher.group(1), "");
                }else{
                    Instructions.caller(instructionMatcher.group(1), instructionMatcher.group(2));
                }
                return true;
            }
        }
        return true;
    }
    
    static boolean getFlag(){
        return skipElseFlag;
    }

    static void setFlag(boolean flag){
        skipElseFlag = flag;
    }

    static Integer getPc(){
        return pc;
    }
    
    static void setPc(Integer next){
        pc = next;
    }

    static void enterScope(Integer next){
        pr.push(pc);
        pc = next;
    }

    static void exitScope(){
        pc = pr.pop();
    }

    static void debugPrint(){
        for(int i = 0; i < pr.size(); i++){
            System.out.println("Stack Value : " + pr.get(i));
        }
        System.out.println("Program Counter (pc): " + pc);
        System.out.println("Skip Else Flag : " + skipElseFlag);
    }
}