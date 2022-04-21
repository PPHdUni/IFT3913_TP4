import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Proto {

    private static int nc,loc,cloc,weight;

    /**
     * Entry point of the program, asks the user for the path, and then start the analysis on the code. Outputs the metrics in csv files.
     * @param args The command line arguments.
     * @throws java.io.IOException if we can't read/write a file
     **/
    public static void main(String[] args) throws IOException {

        nc = 0;
        loc = 0;
        cloc = 0;
        weight = 0;

        File f = new File("protodepot");
        searchDirectory(f);

        float mWMC, mcBC;
        if (nc!=0) {
            mWMC = ((float)weight/(float)nc);
            mcBC = (((float)cloc/(float)loc)/mWMC);
        }
        else {
            mWMC = 0;
            mcBC = 0;
        }

        FileWriter writer = new FileWriter("proto.csv", true);
        writer.write(args[0]+";"+nc+";"+mWMC+";"+mcBC+"\n");
        writer.close();
    }

    /**
     * Recursively searches a file's directory to find other java files in sub-folders
     * @param f {@link java.io.File} from which the search will start
     * @throws java.io.IOException if we can't read/write a file
     * */
    public static void searchDirectory(File f) throws IOException {
        File[] directory = f.listFiles();
        assert directory != null;
        for (File file: directory)
        {
            if(file.isDirectory()) {
                searchDirectory(file);
            }
            else {
                parseClass(file.getAbsolutePath());
            }
        }
    }

    /**
     * Parses a class file and computes metrics on it. Updates global list of metrics for classes
     * @param path {@link String} containing the path of the class
     * @throws java.io.IOException if we can't read/write a file
     * */
    public static void parseClass(String path) throws IOException {

        if(!path.endsWith(".java")) return;
        nc++;

        int classWeight = -1;
        boolean commentBlock = false, commentLine;
        File javaFile = new File(path);

        Scanner reader = new Scanner(javaFile);
        String line;

        while(reader.hasNextLine()) {
            commentLine = false;
            line = reader.nextLine();
            if(line.trim().equals("")) continue;

            if (line.contains("//")) commentLine = true;
            if (line.contains("/*")) commentBlock = true;
            if (commentBlock) commentLine = true;
            if (line.contains("*/")) commentBlock = false;

            if(line.contains("{")&&!commentBlock) classWeight++;

            loc++;
            if(commentLine) cloc++;
        }

        reader.close();

        if(classWeight <= 0) classWeight = 1;
        weight += classWeight;

    }
}
