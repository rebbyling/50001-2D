package sat;/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import fileio.SatReader;
import fileio.SatWriter;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

//import java.io.File;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // private static String filePath = "sampleCNF/largeSat.cnf";
    // private static String root = System.getProperty("user.dir");
    // private static int numClauses=0;


    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability

    public static void main(String[] args) throws InterruptedException {
        // Read the cnf file
        Formula f2 = SatReader.formulaReader();

        SATSolver.Foo foo = new SATSolver.Foo(f2);
//        Thread thread = new Thread(foo);
//        thread.start();

        // Start timer
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();

//        thread.join();

        // Solve for satisfiability

        Environment e = SATSolver.solve(foo, f2);
        if (e == null) {
            System.out.println("unsatisfiable");
        } else {
            System.out.println("satisfiable");
        }

        // Stop timer
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time:" + timeTaken / 1000000.0 + "ms");

        // Write env to BoolAssignment.txt
        if (e!=null) { SatWriter.writer(e); }

    }

//    public void testSATSolver1(){
//    	// (a v b)
//    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
///*
//    	assertTrue( "one of the literals should be set to true",
//    			Bool.TRUE == e.get(a.getVariable())
//    			|| Bool.TRUE == e.get(b.getVariable())	);
//
//*/
//    }
//
//
//    public void testSATSolver2(){
//    	// (~a)
//
//    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
///*
//    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
//*/
//    }

    protected static Formula makeFm(Formula f, Clause... e) {
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    protected static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }

}