import java.time.LocalDateTime;
import java.text.DecimalFormat;
import java.time.Duration;

/**
 * A class that represents a smart camera device, which can record videos and store them on its internal storage.
 */
public class SmartCamera extends Device {
    private double megabytesPerRecord;
    private double usedStorage;

    /**
     * Constructs a SmartCamera object with the given name and the specified amount of storage per recorded video.
     *
     * @param name The name of the smart camera.
     * @param megabytesPerRecord The amount of storage space required to record one video (in megabytes).
     * @throws Exceptions.megabytesValueException if megabytesPerRecord is negative.
     */
    public SmartCamera(String name, double megabytesPerRecord) throws Exceptions.megabytesValueException {
        this.name = name;
        if (megabytesPerRecord<=0) {
            throw new Exceptions.megabytesValueException();
        }
        this.megabytesPerRecord = megabytesPerRecord;
    }

    /**
     * Constructs a SmartCamera object with the given name, the specified amount of storage per recorded video,
     * and an initial status.
     *
     * @param name The name of the smart camera.
     * @param megabytesPerRecord The amount of storage space required to record one video (in megabytes).
     * @param initialStatus The initial status of the device.
     * @throws Exceptions.megabytesValueException if megabytesPerRecord is negative.
     * @throws Exceptions.erroneousCommandException
     */
    public SmartCamera(String name, double megabytesPerRecord, String initialStatus) throws Exceptions.megabytesValueException, Exceptions.erroneousCommandException {
        this.name = name;
        if (megabytesPerRecord<=0) {
            throw new Exceptions.megabytesValueException();
        }
        this.megabytesPerRecord = megabytesPerRecord;
        setInitialStatus(initialStatus);
        if (initialStatus.equals("On")) {
            lastInTime = ProgramManager.getTime();
        }
    }

    /**
     * Switches the status of the device to the specified status.
     *
     * @param status The new status of the device ("on" or "off").
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     */
    public void switchDevice(String status) throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException {
        super.switchDevice(status);
        if (status.toLowerCase().equals("on")) {
            lastInTime = ProgramManager.getTime();
        }
        else if (status.toLowerCase().equals("off")) {
            calculateStorageUsed();
        }
    }

    /**
     * Calculates the amount of storage used by the smart camera, based on the time elapsed since the last time it was switched on.
     */
    public void calculateStorageUsed() {
        double timeDiffMin = Duration.between(lastInTime, ProgramManager.getTime()).toMinutes();
        double addStorage = timeDiffMin * megabytesPerRecord;
        this.usedStorage += addStorage;
    }

    /**
     * Returns the amount of storage currently being used by the smart camera (in megabytes).
     *
     * @return The amount of storage currently being used by the smart camera.
     */
    public double usedStorage() {
        return usedStorage;
    }
    @Override
    public String getDeviceInfo() {
        String classOfTheDevice = "Smart Camera";
        DecimalFormat df = new DecimalFormat("#0.00");
        String printThis = classOfTheDevice + " " + this.getName() + " is " + this.status.toLowerCase() + " and used " + df.format(this.usedStorage()).replace(".", ",") + " MB of storage so far (excluding current status),";
        printThis += " and its time to switch its status is " + this.getSwitchTimeString() + ".";
        return printThis;
    }
}