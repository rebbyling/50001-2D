package sat;

public class CnfReader extends SATSolverTest{
    public static void main(String[] args) {
        Formula output = new Formula();
        try {
            File f = new File(root, filePath);
            Scanner reader = new Scanner(f);

            // Skip comments
            String line;
            do {
                line = reader.nextLine();
            } while (line.charAt(0) != 'p');

            // Get the number of Clauses
            String[] pLine = line.split(" ");
            int numClauses = Integer.parseInt(pLine[pLine.length-1]);
            
            reader.useDelimiter("0");
            
            while (reader.hasNext()) {
                line = reader.next().trim();
                if (line.isEmpty()) break;
                String[] stringArray = line.split(" ");
                Literal[] literalArray = new Literal[stringArray.length];
                
                // Convert raw string to array of literal
                for (int i=0; i<literalArray.length; i++) {
                    if (stringArray[i].charAt(0) == '-') {
                        // Negated literal
                        literalArray[i] = NegLiteral.make(stringArray[i].substring(1));
                    } else {
                        // Positive literal
                        literalArray[i] = PosLiteral.make(stringArray[i]);
                    }
                }

                // Create a Clause
                Clause clauses = makeCl(literalArray);


                System.out.println(clauses);

                // Insert into formula
                // TODO: formula takes in an array of clauses and init at one go
                // Implement NonEmptyImList but with option of init with an array of clauses
                // Can an array of clauses even be converted to a ImList?
                output = makeFm(output, clauses);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            //TODO: handle exception
            System.out.println("File not found.");
            System.out.println(root + filePath);
            e.printStackTrace();
        }

        System.out.println(output);
    }
}
