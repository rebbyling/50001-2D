package fileio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import sat.FilePath;
import sat.env.Environment;
import sat.env.Variable;

public class SatWriter {
    public static void writer(Environment env) {
        // TODO: change filename

        try {

            FileWriter fileWriter = new FileWriter(FilePath.LINUX_FILE_OUT);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            if (env == null) {
                System.out.println("unsatisfiable");
            } else {
                System.out.println("satisfiable");
                String output = env.toString();
                // env --> Environment:[a->TRUE, b->FALSE, c->TRUE, d->FALSE]

                output = output.substring(13, output.length() - 1);
                output = output.replaceAll("->", ":");
                output = output.replaceAll(", ", "\n");
                System.out.println(output);

                printWriter.println(output);
            }
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public static void main(String[] args) {
        Environment env = new Environment();

        Variable a = new Variable("a");
        Variable b = new Variable("b");
        Variable c = new Variable("c");
        Variable d = new Variable("d");

        env = env.putTrue(a);
        env = env.putFalse(b);
        env = env.putTrue(c);
        env = env.putFalse(d);

        System.out.println(env.toString());
        writer(env);
    }
}
