import java.io.*;

public class WriteLines {
    private static String fileName = ProgramManager.fileNameOut;
    public WriteLines() {
        try {
            FileWriter clear = new FileWriter(fileName);
            clear.write("");
            clear.close();
        }
        catch (IOException e) {
            fileName = "output.txt";
        }
        
    }
    public static void writeLineToFile(String line) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(line + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}