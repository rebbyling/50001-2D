package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

import java.util.Iterator;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {

    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(), new Environment());
        // return foo.getEnvironment();
    }
    
    /**
     * Threading implementation, but did not implement due to time constraint
     */
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
        Clause empty = new Clause();

        if (clauses.isEmpty()) {
            // Case 1: No clauses, trivially satisfiable
            return env;
        } else if (clauses.contains(empty)) {
            // Case 2: Empty clause, fail and backtrack
            return null;
        } else {
            /**
             * Case 3
             *
             * 3a: Set literal to TRUE
             * Simplify ImList of clauses with subsitute()
             * Call solve() on the ImList (recursive call)
             *
             * If succeed, return the env
             * else failed, move on to 3b
             *
             * 3b: Set literal to FALSE
             * Simplify ImList of clauses with subsitute()
             * Call solve() on the ImList (recursive call)
             */

            // Find smallest clause
            Clause smallest = smallestClause(clauses);

            // Pick a literal
            Literal lit = smallest.chooseLiteral();

            // Set literal to true in env
            env = setLiteralTF(true, lit, env);

            // Substitute to reduce the clauses
            ImList<Clause> newClause = substitute(clauses, lit);

            // One literal, clause would be True, continue solving other literals
            if (smallest.size() == 1) {
                return solve(newClause, env);
            }

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

                // Call solve
                return solve(newClause, env);
            } else {
                // Solved, yayyy
                return output;
            }
        }
    }

    /**
     * @param setTrue check if literal is true/false
     * @param lit check if literal is a PosLiteral or NegLiteral
     * @param env retrieves env after setting var true/false
     * @return retrieves environment
     */
    private static Environment setLiteralTF(boolean setTrue, Literal lit, Environment env) {
        Variable var = lit.getVariable();
        if (lit instanceof PosLiteral) {
            // PosLiteral
            if (setTrue) {
                // Sets the literal to overall True
                env = env.putTrue(var);
            } else {
                // Sets the literal to over False
                env = env.putFalse(var);
            }
        } else {
            // NegLiteral
            if (setTrue) {
                // Sets the literal to overall True
                env = env.putFalse(var);
            } else {
                // Sets the literal to over False
                env = env.putTrue(var);
            }
        }
        return env;
    }

    //static method to find the smallest clause by checking the number of literals in each clause
    private static Clause smallestClause(ImList<Clause> clauses) {
        int min = 100000;  // Arbitary high number to start off
        Clause minClause = new Clause();
        for (Clause c: clauses) {
            // Take the smallest clause
            if (c.size() < min) {
                min = c.size();
                minClause = c;
            }
            // Early stopping, since 1 would be the smallest clause
            if (c.size() == 1) {
                break;
            }
        }
        return minClause;
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
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {
        ImList<Clause> output = new EmptyImList<>();
        Iterator<Clause> iter1 = clauses.iterator();
        // Check the clauses in ImList and change the literal (l) to true with reduce method,
        // and as long as the clause is not null, add it into the new ImList (output)
        while (iter1.hasNext()) {
            Clause clause = iter1.next();
            if (clause.contains(l) || clause.contains(l.getNegation())) {
                clause = clause.reduce(l);
            }
            if (clause != null) {
                output= output.add(clause);
            }
        }
        return output;
    }
}
