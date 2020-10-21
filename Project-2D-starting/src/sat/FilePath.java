package sat;

import annotation.FileDir;

// FilePath annotations
public interface FilePath {
    @FileDir String FILE_IN = "sampleCNF/test_2020.cnf";
    @FileDir String FILE_OUT = "sampleCNF/BoolAssignment.txt";
}
