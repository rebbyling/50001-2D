package fileio;

import sat.FilePath;
import sat.SATSolverTest;
import sat.formula.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SatReader extends SATSolverTest {

    public static Formula formulaReader(String filePath) {
        Formula output = new Formula();

        BufferedReader br = null;

        try {
            // Using BufferedReader for fast read
            br = new BufferedReader(new FileReader(filePath));

            String multiLine = "";

            // Read the file
            while ((multiLine = br.readLine()) != null) {
                // Split according to 0
                for (String contentLine : multiLine.split(" 0")) {
                    // Remove leading and trailing whitespaces (just to be safe)
                    contentLine = contentLine.trim();

                    // Skip empty lines
                    if (contentLine.equals("")) { continue; }

                    // Skip comments and problem lines
                    switch (contentLine.charAt(0)) {
                        case 'c':
                        case 'p':
                            continue;
                    }
                    // Parse into literals and add into a clause
                    // Add clause into formula
                    Literal[] literalArray = checkList(contentLine);
                    Clause clauses = makeCl(literalArray);
                    output = makeFm(output, clauses);
                }
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
    }


    private static Literal[] checkList(String contentLine) {
        // Parse literals

        // Remove whitespaces
        String[] splitStr = contentLine.split("\\s+");

        // Initialise literalArray
        Literal[] literalArray = new Literal[splitStr.length];

        // Iterate through each letter and build literal
        for (int i = 0; i < splitStr.length; i++) {
            String s = splitStr[i];
            if (s.charAt(0) == '-') {
                // Negated literal
                literalArray[i] = NegLiteral.make(s.substring(1));
            } else {
                // Positive literal
                literalArray[i] = PosLiteral.make(s);
            }
        }
        return literalArray;
    }
}

