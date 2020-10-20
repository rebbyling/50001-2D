package sat;

import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

import java.io.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {

    private static boolean isAssigned = false;
    private static boolean setTrue = true;
    private static ImList<Clause> newClause;
    private static ImList<Clause> falseClause;
    private static Environment newEnv;
    private static Environment falseEnv;
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
        // TODO: implement this.
        ImList<Clause> clauseImList = formula.getClauses();

        Environment e = null;
        try {
            e = solve(clauseImList, new Environment());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return e;
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
    private static Environment solve(ImList<Clause> clauses, Environment env) throws IOException {
        // TODO: implement this.
        noClause(clauses);

        // assign falseEnv & falseClause at the start so that it can be used for computing later on

        if (!isAssigned) {
            falseEnv = env;
            falseClause = clauses;
            isAssigned = true;
        }

        // newEnv & newClause changes every recursion
        newEnv = env;
        newClause = clauses;

        for (Clause c: newClause) {
            /**
             * if empty clause, use empty clause to denote a clause evaulated to FALSE based on the variable binding in
             * the environment
             */
            if (c.isEmpty())
                newEnv.putFalse(new Variable(c.toString()));
            else {

                /**
                 * If the clause has only 1 literal, bind its variable in the environment so that the clause is satisfied,
                 * substitute for the variable in all the other clauses (using suggested substitute() method), and recursively
                 * call solve()
                 */

                if (c.size() == 1) {
                    newEnv.putTrue(new Variable(c.toString()));
                    newEnv = solve(substitute(newClause.remove(c), c.chooseLiteral()), newEnv);
                }

                else {
                    /**
                     * Pick an arbitrary literal from this small clause:
                     * Try setting the literal to TRUE, substitute for the variable in all the clauses, then solve() recursively
                     */

                    if (setTrue) {
                        newEnv.putTrue(new Variable(c.chooseLiteral().toString()));
                        newEnv = solve(substitute(newClause.remove(c), c.chooseLiteral()), newEnv);

                        /**
                         * If the previous fails, set the literal to FALSE, substitute, and solve() recursively
                         */

                        if (!clauses.isEmpty()) {
                            setTrue = false;
                            newEnv = solve(substitute(falseClause, c.chooseLiteral()), falseEnv);
                        }

                    } else {
                        newEnv.putFalse(new Variable(c.chooseLiteral().toString()));
                        newEnv = solve(substitute(falseClause.remove(c), c.chooseLiteral()), newEnv);
                    }
                }
            }
        }

        return newEnv;
    }

    private static void noClause(ImList<Clause> clauses) throws IOException {
        /**
         * If no clause, the formula is trivially satisfiable:
         * Print out "satisfiable"
         * Output variable assignments to a result file "BoolAssignment.txt" with format of one var per line:
         * <variable>:<assignment>
         * 1:TRUE
         * 2:FALSE
         */

        FileOutputStream out = null;

        if (clauses.isEmpty()) {
            try {
                out = new FileOutputStream("/home/yuanhawk/50001-2D/Project-2D-starting-intellij/sampleCNF/BoolAssignment.txt");
                String result = "satisfiable";
                out.write(result.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null)
                    out.close();
            }
        }
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

        ImList<Clause> newClause = clauses;
        for (Clause c: newClause) {
            c.reduce(l);
        }

        return newClause;
    }

}
