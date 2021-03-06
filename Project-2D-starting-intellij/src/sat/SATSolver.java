package sat;

import fileio.DefaultExecutorSupplier;
import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {

//    private static boolean isAssigned = false;
//    private static boolean setTrue = true;
//    private static ImList<Clause> newClause;
//    private static ImList<Clause> falseClause;
//    private static Environment newEnv;
//    private static Environment falseEnv;

    private static Environment out;
    private static ImList<Clause> newClause;
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */

    public static Environment solve(Foo foo, Formula formula) {
        // TODO: implement this.
//        ImList<Clause> clauseImList = formula.getClauses();
//
//        Environment e = null;
//        try {
//            e = solve(clauseImList, new Environment());
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//
//        return e;

        ImList<Clause> clauseImList = formula.getClauses();

        Environment e = null;
        e = solve(clauseImList, new Environment());

        return e;

//        return foo.getEnvironment();
    }

    public static class Foo implements Runnable {

        private volatile Formula formula;
        private Environment e;

        public Foo(Formula formula) {
            this.formula = formula;
        }

        @Override
        public void run() {
            e = solve(formula.getClauses(), new Environment());
        }

        public Environment getEnvironment() {
            return e;
        }
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.

        // assign falseEnv & falseClause at the start so that it can be used for computing later on

        Clause empty = new Clause();

//        int n = 100;
//        Iterator<Clause> iterator = clauses.iterator();
//        while(iterator.hasNext()) {
//            n = Math.min(iterator.next().size(), n);
//        }
//        if (clauses.isEmpty()) {
//            return env;
//        } else if (clauses.contains(empty)) {
//            return null;
//        } else {
//            Iterator<Clause> clauseIterator = clauses.iterator();
//            while (clauseIterator.hasNext()) {
//                Clause current = clauseIterator.next();
//                if (current.size() == 1) {
//                    Literal lit = current.chooseLiteral();
//
//                    env = checkLiteral(lit, env);
//
//                    newClause = substitute(clauses, lit);
//
//                    out = solve(newClause, env);
//
//                    if (out == null) {
//                        if (env.get(lit.getVariable()) == Bool.TRUE) {
//                            Literal negateLit = lit.getNegation();
//
//                            env = checkNegatedLiteral(negateLit, env);
//
//                            ImList<Clause> finalClause = substitute(clauses, negateLit);
//                            return solve(finalClause, env);
//                        } else if (env.get(lit.getVariable()) == Bool.FALSE) {
//                            return null;
//                        }
//                    } else {
//                        return out;
//                    }

//                    if ((out == null) && (env.get(lit.getVariable()) == Bool.TRUE)) {
//                        Literal negateLit = lit.getNegation();
//
//                        env = checkNegatedLiteral(negateLit, env);
//
//                        ImList<Clause> finalClause = substitute(clauses, negateLit);
//                        return solve(finalClause, env);
//                    } else if ((out == null) && (env.get(lit.getVariable()) == Bool.FALSE)) {
//                        return null;
//                    } else {
//                        return out;
//                    }
//                }
//            }
//        }

        if (clauses.isEmpty()) {
            // Case 1: No clauses, trivally satisfiable
            return env;
        } else if (clauses.contains(empty)) {
            // Case 2: Empty clause, fail and backtrack
            return null;
        } else {
            // Case 3:

            // Find smallest clause
            Clause smallest = smallestClause(clauses);


            System.out.println("smallest:\t\t" + smallest);

            // Clause size more than 1
            // First try setting the literal to TRUE, substitute for it in all the clauses, then solve() recursively.
            // If that fails, then try setting the literal to FALSE, substitute, and solve() recursively.

            // Pick a literal
            Literal lit = smallest.chooseLiteral();

            System.out.println("Literal:\t\t" + lit);

            // Set literal to true in env
            env = setLiteralTF(true, lit, env);

            System.out.println("hellooooo");

            // Substitute to reduce the clauses
            ImList<Clause> newClause = substitute(clauses, lit);

            if (smallest.size() == 1) {
                // One literal, clause would be True, continue solving
                return solve(newClause, env);
            }

            System.out.println("new Clause:\t\t" + newClause);

            // Call solve and check if it is solved
            Environment output = solve(newClause, env);
            if (output == null) {
                // Unsatisfiable, try again
                // Set literal to false in env
                setLiteralTF(false, lit, env);

                // Get negation
                Literal nlit = lit.getNegation();

                //Substitute to reduce the clauses
                newClause = substitute(clauses, nlit);

                System.out.println(lit);
                // Call solve
                return solve(newClause, env);
            } else {
                // Satisfiable, yayyy
                return output;
            }
        }
    }

    private static Environment setLiteralTF(boolean settrue, Literal lit, Environment env) {
        Variable var = lit.getVariable();
        if (lit instanceof PosLiteral) {
            // PosLiteral
            if (settrue) {
                // Sets the literal to overall True
                env = env.putTrue(var);
            } else {
                // Sets the literal to over False
                env = env.putFalse(var);
            }
        } else {
            // NegLiteral
            if (settrue) {
                // Sets the literal to overall True
                env = env.putFalse(var);
            } else {
                // Sets the literal to over False
                env = env.putTrue(var);
            }
        }
        return env;
    }

    private static Clause smallestClause(ImList<Clause> clauses) {
        int min = 100000;
        Clause minClause = new Clause();
        for (Clause c: clauses) {
            if (c.size() < min) {
                min = c.size();
                minClause = c;
            }
        }
        return minClause;
    }

    private static Environment checkNegatedLiteral(Literal negateLit, Environment env) {
        if (negateLit instanceof PosLiteral) {
            env = env.putFalse(negateLit.getVariable());
        } else {
            env = env.putTrue(negateLit.getVariable());
        }
        return env;
    }

    private static Environment checkLiteral(Literal lit, Environment env) {
        if (lit instanceof PosLiteral) {
            env = env.putTrue(lit.getVariable());
        } else {
            env = env.putFalse(lit.getVariable());
        }
        return env;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // TODO: implement this.
        ImList<Clause> output = new EmptyImList<>();
        Iterator<Clause> iter1 = clauses.iterator();

        while(iter1.hasNext()) {
            Clause clause = iter1.next();
            if(clause.contains(l) || clause.contains(l.getNegation())) {
                clause = clause.reduce(l);
            }
            if(clause!= null) {
                output= output.add(clause);
            }
        }
        return output;
    }
}
