package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

import java.io.*;
import java.util.Iterator;

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
        // ImList<Clause> clauseImList = formula.getClauses();

        // Environment e = null;
        // try {
        //     e = solve(clauseImList, new Environment());
        // } catch (IOException ioException) {
        //     ioException.printStackTrace();
        // }

        //return e;
        return solve(formula.getClauses(), new Environment());
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
        // noClause(clauses);
        Clause empty = new Clause();

        int n = 100;
        Iterator<Clause> iterator =clauses.iterator();
        while(iterator.hasNext()) {
            n = Math.min(iterator.next().size(), n);
        }

        // assign falseEnv & falseClause at the start so that it can be used for computing later on

        // if (!isAssigned) {
        //     falseEnv = env;
        //     falseClause = clauses;
        //     isAssigned = true;
        // }

        if(clauses.isEmpty()) {
            return env;
        } else if (clauses.contains(empty)) {
            return null;
        } else {
            Iterator<Clause> clauseIterator = clauses.iterator();
            while (clauseIterator.hasNext()) {
                Clause current = clauseIterator.next();
                if (current.size() == 1) {   //TODO: what's this for actually?
                    Literal lit = current.chooseLiteral();
                    if (lit instanceof PosLiteral) {
                        env = env.putTrue(lit.getVariable());
                    }
                    else {
                        env = env.putFalse(lit.getVariable());
                    }

                    ImList<Clause> newClause = substitute(clauses, lit);

                    Environment out = solve(newClause, env);

                    if ((out == null) && (env.get(lit.getVariable())==Bool.TRUE)) {
                        Literal negateLit = lit.getNegation();
                        if (lit instanceof PosLiteral) {
                            env = env.putFalse(lit.getVariable());
                        }
                        else {
                            env = env.putTrue(lit.getVariable());
                        }

                        ImList<Clause> finalClause = substitute(clauses, negateLit);
                        return solve(finalClause, env);
                    }
                    else if ((out ==null) && (env.get(lit.getVariable())==Bool.FALSE)) {
                        return null;
                    }
                    else {
                        return out;
                    }
                }
            }

        }
        return null;
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
        ImList<Clause> output = new EmptyImList<>;
        Iterator<Clause> iter1 = clauses.iterator();

        while(iter1.hasNext()) {
            Clause clause = (Clause) iter1.next();
            boolean containL = false;
            boolean containNegL = false;
            Iterator<Literal> iter2 = clause.iterator();

            while(iter1.hasNext()) {
                Literal literal = iter2.next();

                if (literal == l)
                    containL = true;
                else
                    containNegL = true;
            }

            if (!containL) {
                if (containNegL) {
                    clause = clause.reduce(l);
                output = output.add(clause);
            }

        }
        return output;
    }

}
