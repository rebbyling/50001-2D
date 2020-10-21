package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import fileio.SatReader;
import fileio.SatWriter;
import sat.env.*;
import sat.formula.*;


public class SATSolverTest {

    public static void main(String[] args) {
        // Read the cnf file
        Formula f2 = SatReader.formulaReader(args[0]);

        /**
         * Threader stuff
         * SATSolver.Foo foo = new SATSolver.Foo(f2);
         * Thread thread = new Thread(foo);
         * thread.start();
         */

        // Start timer
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();

        // thread.join();

        // Solve for satisfiability
        Environment e = SATSolver.solve(f2);

        // Print out answers
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

    //static method that makes formula from clause
    //protected as SatReader extends it
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

    //static method that makes a clause from literal
    protected static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }

}