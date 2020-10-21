package sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

// import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import sat.env.*;
import sat.formula.*;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    
    static String filePath = "/home/yuanhawk/50001-2D/Project-2D-starting-JunKai/sampleCNF/s8Sat.cnf";
    static String root = System.getProperty("user.dir");
    private static int numClauses=0;

	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    
	
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
    
    private static Formula makeFm(Formula f, Clause... e) {
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
    

    public static Formula formulaReader() {
        Formula output = new Formula();

        BufferedReader br = null;
    
        try {
            br = new BufferedReader(new FileReader(filePath));

            String contentLine=br.readLine();
            // change do while loop
            while (contentLine != null){
                if (contentLine.equals("")) {
                    // TODO: double newlines
                    contentLine = br.readLine();
                    if (contentLine.equals("")) {
                        contentLine = br.readLine();
                    }
                    if (contentLine == null){ break; }
                }
                Literal[] literalArray = checkList(contentLine);
                contentLine = br.readLine();

                Clause clauses = makeCl(literalArray);

                System.out.println(clauses);

                // Insert into formula
                // TODO: formula takes in an array of clauses and init at one go
                // Implement NonEmptyImList but with option of init with an array of clauses
                // Can an array of clauses even be converted to a ImList?
                output = makeFm(output, clauses);
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return output;


        // try {
        //     File f = new File(root, filePath);
        //     Scanner reader = new Scanner(f);

        //     // Skip comments
        //     String line;
        //     do {
        //         line = reader.nextLine();
        //     } while (line.charAt(0) != 'p');

        //     // Get the number of Clauses
        //     String[] pLine = line.split(" ");
        //     int numClauses = Integer.parseInt(pLine[pLine.length-1]);
            
        //     reader.useDelimiter("0");
            
        //     while (reader.hasNext()) {
        //         line = reader.next().trim();
        //         if (line.isEmpty()) break;
        //         String[] stringArray = line.split(" ");
        //         Literal[] literalArray = new Literal[stringArray.length];
                
        //         // Convert raw string to array of literal
        //         for (int i=0; i<literalArray.length; i++) {
        //             if (stringArray[i].charAt(0) == '-') {
        //                 // Negated literal
        //                 literalArray[i] = NegLiteral.make(stringArray[i].substring(1));
        //             } else {
        //                 // Positive literal
        //                 literalArray[i] = PosLiteral.make(stringArray[i]);
        //             }
        //         }

        //         // Create a Clause
        //         Clause clauses = makeCl(literalArray);


        //         System.out.println(clauses);

        //         // Insert into formula
        //         // TODO: formula takes in an array of clauses and init at one go
        //         // Implement NonEmptyImList but with option of init with an array of clauses
        //         // Can an array of clauses even be converted to a ImList?
        //         output = makeFm(output, clauses);
        //     }

        //     reader.close();
        // } catch (FileNotFoundException e) {
        //     //TODO: handle exception
        //     System.out.println("File not found.");
        //     System.out.println(root + filePath);
        //     e.printStackTrace();
        // }

        // return output;
    }

    public static void main(String[] args) {
        System.out.println(formulaReader());
    }

    private static Literal[] checkList(String contentLine) {        
        if (contentLine.charAt(0) == 'c') { return new Literal[]{}; }
        
        if (contentLine.charAt(0) == 'p') {
            String [] line = contentLine.split("\\s+");
            numClauses = Integer.parseInt(line[line.length - 1]);

            System.out.println(numClauses);
            return new Literal[]{};
        }

        
        for (String line: contentLine.split("0")){
            String[] splitStr = line.split("\\s+");
            Literal[] literalArray = new Literal[splitStr.length];
            
            for (int i=0; i<splitStr.length; i++) {
                String s = splitStr[i].strip();
                if (!s.equals("0")) {
                    if (s.charAt(0) == '-') {
                        // Negated literal
                        literalArray[i] = NegLiteral.make(s.substring(1));
                    } else {
                        // Positive literal
                        literalArray[i] = PosLiteral.make(s);
                    }
                }
            }

            return literalArray;
        }
        return new Literal[]{};
    }
    
    // Check first letter: c -comment, p -problem
    // p cnfformat #ofvariables #ofclauses
    // 1 3 2#variables -2#negatedvariables 0#endingsymbol
    // 1 4 -2 0


    // Li Yuan's code
    // public static void main(String[] args) {
        
    //     System.out.println("SAT solver starts!!!");

    //     long started = System.nanoTime();

    //     SATSolverTest sat = new SATSolverTest();
    //     sat.testSATSolver();

    //     long time = System.nanoTime();

    //     long timeTaken = time - started;

    //     System.out.println("Time:" + timeTaken/1000000.0 + "ms");
    // }

    // private void testSATSolver() {
    //     HashMap<Integer, String> map = LYCnfReader.parseCnf();

    //     System.out.println(map);

    //     for (int i = 0; i < map.size(); i++) {
    //         String[] splitStr = map.get(i).split("\\s+");

    //         for (String s: splitStr) {
    //             int var = Integer.parseInt(s);
    //             if (var < 0) {
    //                 a = PosLiteral.make(s);
    //                 na = a.getNegation();
    //             } else if (var > 0) {
    //                 b = PosLiteral.make(s);
    //             }
    //         }

    //         Environment e = SATSolver.solve(makeFm(makeCl(na, b)));
    //     }
    // }

}