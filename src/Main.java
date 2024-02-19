import java.time.LocalDateTime;

public class Main{
    public static void main(String[] args) throws Exceptions.InitialTimeException, Exceptions.ChangeNameException, Exceptions.SetTimeException, ClassNotFoundException, Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException {
        ProgramManager.fileName = args[0];
        ProgramManager.fileNameOut = args[1];
        ReadLines.runLines();
    }
}