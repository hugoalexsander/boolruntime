package boolruntime.interpreter;
import java.util.regex.*;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Stack;


public class Executor{
    private static String[] instructions = {
        "^\\s*(const)\\s+([0-9]+)\\s*\\n$",
        "^\\s*(load)\\s+([a-zA-Z]+)\\s*\\n$",
        "^\\s*(store)\\s+([a-zA-Z]+)\\s*\\n$",
        "^\\s*(add)\\s*\\n$",
        "^\\s*(sub)\\s*\\n$",
        "^\\s*(mul)\\s*\\n$",
        "^\\s*(div)\\s*\\n$",
        "^\\s*(new)\\s+([a-zA-Z]+)\\s*\\n$",
        "^\\s*(get)\\s+([a-zA-Z]+)\\s*\\n$",
        "^\\s*(set)\\s+([a-zA-Z]+)\\s*\\n$",
        "^\\s*(call)\\s+([a-zA-Z]+)\\s*\\n$",
        "^\\s*(gt)\\s*\\n$",
        "^\\s*(ge)\\s*\\n$",
        "^\\s*(lt)\\s*\\n$",
        "^\\s*(le)\\s*\\n$",
        "^\\s*(eq)\\s*\\n$",
        "^\\s*(ne)\\s*\\n$",
        "^\\s*(pop)\\s*\\n$",
        "^\\s*(if)\\s+([0-9]+)\\s*\\n$",
        "^\\s*(else)\\s+([0-9]+)\\s*\\n$",
        "^\\s*(ret)\\s*\\n$",
        "^\\s*(prin)\\s*\\n$"
    };
    private static String input;
    private static Stack<Integer> pr = new Stack<>();
    private static Integer pc = 0;
    private static boolean skipElse;

    private Executor(){
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static void run(String inputFile){
        init(inputFile);
        while(execute(getLineFile()));
    }

    private static void init(String inputFile){
        input = inputFile;
        Model.init(input);
        Memory.startMainScope();
    }

    private static String getLineFile(){
        try(Scanner scanner = new Scanner(Paths.get(input))){
            String line = scanner.nextLine();
            Integer tempPc = pc;
            while (scanner.hasNextLine() && tempPc > 0) {
                line = scanner.nextLine();
                tempPc--;  
            }
            pc++;
            return line;
        } catch(Exception e) {
            System.out.println("Arquivo inviável!");
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    private static boolean execute(String line){
        if(line.matches("^\\s*end\\s*\\n$"))
        {
            return false;
        }
        for(String instruction : instructions){
            if(line.matches(instruction)){
                Pattern instructionPattern = Pattern.compile(instruction);
                Matcher instructionMatcher = instructionPattern.matcher(line);
                if(instructionMatcher.groupCount() == 1)
                {
                    Instructions.caller(instructionMatcher.group(1), "");
                }else{
                    Instructions.caller(instructionMatcher.group(1), instructionMatcher.group(2));
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean getFlag(){
        return skipElse;
    }

    public static void setFlag(boolean flag){
        skipElse = flag;
    }

    public static Integer getPc(){
        return pc;
    }
    
    public static void setPc(Integer next){
        pc = next;
    }

    public static void enterScope(Integer next){
        pr.push(pc);
        pc = next;
    }

    public static void exitScope(){
        pc = pr.pop();
    }
}