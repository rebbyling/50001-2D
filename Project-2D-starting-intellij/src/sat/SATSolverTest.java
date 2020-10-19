package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import read.CnfReader;
import sat.env.*;
import sat.formula.*;

import java.util.HashMap;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability

    public static void main(String[] args) {

        System.out.println("SAT solver starts!!!");

        long started = System.nanoTime();

        SATSolverTest sat = new SATSolverTest();
        sat.testSATSolver();

        long time = System.nanoTime();

        long timeTaken = time - started;

        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
    }

    private void testSATSolver() {
        HashMap<Integer, String> map = CnfReader.parseCnf();

        for (int i = 0; i < map.size(); i++) {
            String[] splitStr = map.get(i).split("\\s+");

            for (String s: splitStr) {
                int var = Integer.parseInt(s);
                if (var < 0) {
                    a = PosLiteral.make(s);
                    na = a.getNegation();
                } else if (var > 0) {
                    b = PosLiteral.make(s);
                }
            }

            Environment e = SATSolver.solve(makeFm(makeCl(na, b)));
        }
    }


    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)

    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}