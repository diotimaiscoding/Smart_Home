import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The ProgramManager class is responsible for managing the time and running programs on devices.
 */
public class ProgramManager {
    private static LocalDateTime currentTime;
    private static LocalDateTime nopTime;
    public static String fileName = "input1.txt";
    public static String fileNameOut = "output.txt";

    /**
     * Sets the NOP time to the device manager's NOP time and sets the current time to the NOP time.
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     * @throws Exceptions.SetTimeException
     */
    public static void setNopTime() throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException, Exceptions.SetTimeException {
        nopTime = DeviceManager.setNopTime;
        setTime(nopTime);
    }

    /**
     * Constructs a new ProgramManager object with the specified current time.
     *
     * @param currentTime the current time.
     */
    public ProgramManager(LocalDateTime currentTime) {
        ProgramManager.currentTime = currentTime;
    }

    /**
     * Sets the current time to the specified time.
     *
     * @param newTime the new time to set.
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     * @throws Exceptions.SetTimeException
     */
    public static void setTime(LocalDateTime newTime) throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException, Exceptions.SetTimeException {
        // Time cannot be reversed
        if (newTime != null && currentTime != null && newTime.isBefore(currentTime)) {
            throw new Exceptions.SetTimeException(0);
        }
        if (newTime == currentTime) {
            throw new Exceptions.SetTimeException(1);
        }
        ProgramManager.currentTime = newTime;
        DeviceManager.checkDevices();
        DeviceManager.sortDevices();
    }

    /**
     * Returns the current time.
     *
     * @return the current time.
     */
    public static LocalDateTime getTime() {
        return currentTime;
    }

    /**
     * Advances the current time by the specified number of minutes.
     *
     * @param min the number of minutes to advance the time.
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     * @throws Exceptions.SetTimeException
     * @throws Exceptions.SkipMinutesException
     */
    public static void skipMinutes(int min) throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException, Exceptions.SetTimeException, Exceptions.SkipMinutesException {
        if (min < 0) {
            throw new Exceptions.SkipMinutesException(false);
        }
        else if (min == 0) {
            throw new Exceptions.SkipMinutesException(true);
        }
        LocalDateTime toTheTime = currentTime.plusMinutes(min);
        setTime(toTheTime);
    }

    /**
     * Sets the current time to the NOP time.
     * @throws Exceptions.nopException
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     * @throws Exceptions.SetTimeException
     */
    public static void nop() throws Exceptions.nopException, Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException, Exceptions.SetTimeException {
        try {
            DeviceManager.setNopTime();
        } catch (Exceptions.nopException e) {
            throw new Exceptions.nopException();
        }
        setTime(DeviceManager.setNopTime);
    }
    public static String timeToString(LocalDateTime timeToSet) {   
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH:mm:ss");
        if (timeToSet != null) {
            return timeToSet.format(formatter);
        } else {
            return "null";
        }
    }
}