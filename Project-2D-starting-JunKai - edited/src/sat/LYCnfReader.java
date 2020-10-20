package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.formula.Clause;
import sat.formula.Literal;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LYCnfReader {
    
        private static int count = 0;
        private static final String filePath = "C:\Users\JK\Desktop\50001 2D\50001-2D-main\Project-2D-starting\sampleCNF\s8Sat.cnf";
    
        private static HashMap<Integer, String> map = new HashMap<>();
    
        public static HashMap<Integer, String> parseCnf() {
    
            BufferedReader br = null;
    
            try {
                br = new BufferedReader(new FileReader(filePath));
    
                String contentLine = br.readLine();
    
                while (contentLine != null) {
                    checkList(contentLine);
                    contentLine = br.readLine();
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
    
            return map;
        }
    
        private static void checkList(String contentLine) {
            String[] splitStr = contentLine.split("\\s+");
    
            if (splitStr[0].equals("c") || splitStr[0].equals("p")) {
                return;
            }
    
            String output = "";
            for (String s: splitStr) {
                int var = Integer.parseInt(s);
                if (var != 0) {
                    output = output + " " + s; // -1 2
                } else {
                    map.put(count, output.strip());
                    count++;
                }
            }
        }
    
    }    
}
