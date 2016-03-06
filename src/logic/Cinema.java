package logic;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author PIX
 */
public class Cinema {
    private final String name;
    private final ArrayList<CinemaFunction> functions;

    public Cinema(String name) {
        this.name = name;
        functions = new ArrayList<>();
    }
    
    public void addFunction(CinemaFunction newFunction){
        functions.add(newFunction);
    }
    
    public Iterator<CinemaFunction> getFunctions(){
        return functions.iterator();
    }
    
    public void printAllFunctionsInformation(){
        for(CinemaFunction function : functions){
            System.out.println("------------------------");
            function.printFunctionInformation();
            System.out.println("------------------------");
        }
    }
}
