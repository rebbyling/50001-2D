package fileio;

import sat.FilePath;
import sat.SATSolverTest;
import sat.formula.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SatReader extends SATSolverTest {

    public static Formula formulaReader() {
        Formula output = new Formula();

        BufferedReader br = null;

        try {
            // File f = new File(root, filePath);
            br = new BufferedReader(new FileReader(FilePath.LINUX_FILE_IN));

            String multiLine = "";

            while ((multiLine = br.readLine()) != null) {
                for (String contentLine : multiLine.split(" 0")) {
                    contentLine = contentLine.trim();

                    if (contentLine.equals("")) { continue; }

                    switch (contentLine.charAt(0)) {
                        case 'c':
                        case 'p':
                            continue;
                    }

//                    if (contentLine.charAt(0) == 'c') { continue; }
//
//                    if (contentLine.charAt(0) == 'p') {
//                        // String[] line = contentLine.split("\\s+");
//                        // numClauses = Integer.parseInt(line[line.length - 1]);
//                        continue;
//                    }
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
        String[] splitStr = contentLine.split("\\s+");
        Literal[] literalArray = new Literal[splitStr.length];

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

