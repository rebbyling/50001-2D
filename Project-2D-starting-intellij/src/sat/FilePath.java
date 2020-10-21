package sat;

import annotation.FileDir;

public interface FilePath {
    @FileDir String LINUX_FILE_IN = "/home/yuanhawk/50001-2D/Project-2D-starting-intellij/sampleCNF/largeSat.cnf";
    @FileDir String LINUX_FILE_OUT = "/home/yuanhawk/50001-2D/Project-2D-starting-intellij/sampleCNF/BoolAssignment.txt";
    String FILE_IN = "sampleCNF/largeSat.cnf";
    String FILE_OUT = "sampleCNF/BoolAssignment.txt";
}
