import java.util.*;
import java.time.LocalDateTime;


/**
 * This class manages a list of devices and provides methods to add, remove, and sort them.
 */
public class DeviceManager {
    public static List<Device> devices = new ArrayList<>();
    public static LocalDateTime setNopTime = null;

    /**
     * Adds a device to the list of devices.
     * @param device the device to be added.
     */
    public void add(Device device) {
        devices.add(device);
        sortDevices();
    }

    /**
     * Removes a device from the list of devices.
     * @param deviceName the name of the device to be removed.
     */
    public void remove(String deviceName) {
        Device device = findDeviceByName(deviceName);
        device.status = "Off";
        devices.remove(device);
        sortDevices();
    }

    /**
     * Changes the name of a device in the list of devices.
     * @param deviceName the name of the device to be renamed.
     * @param newName the new name for the device.
     * @throws Exceptions.ChangeNameException
     */
    public void changeName(String deviceName, String newName) throws Exceptions.ChangeNameException {
        Device device = findDeviceByName(deviceName);
        device.changeName(newName);
    }

    /**
     * Sets the no-operation time to the switch time of the first device in the list of devices.
     * @throws Exceptions.nopException
     */
    public static void setNopTime() throws Exceptions.nopException {
        try {
            setNopTime = devices.get(0).getSwitchTime();
            if (setNopTime == null) {
                throw new Exceptions.nopException();
            }
        }
        catch (IndexOutOfBoundsException e) {
            throw new Exceptions.nopException();
        }
    }
    /**
     * Checks if the switch time has been reached.
     * @throws erroneousCommandException
     * @throws AlreadySwitchedException
     */
    public static void checkDevices() throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException {
        for (Device device: devices) {
            if (device.getSwitchTime() != null && !device.getSwitchTime().isAfter(ProgramManager.getTime())) {
                device.setSwitchTime(null);
                if (!device.status.equals("On")) {
                    device.switchDevice("On");
                }
                else {
                    device.switchDevice("Off");
                }
                
                
            }
        }
        sortDevices();
    }

    /**
     * 
     * Sorts the list of devices by switch time.
     */
    public static void sortDevices() {
        Collections.sort(DeviceManager.devices, new Comparator<Device>() {
            @Override
            public int compare(Device d1, Device d2) {
                if (d1.getSwitchTime() == null && d2.getSwitchTime() == null) {
                    return 0;
                } else if (d1.getSwitchTime() == null) {
                    return 1;
                } else if (d2.getSwitchTime() == null) {
                    return -1;
                } else {
                    return d1.getSwitchTime().compareTo(d2.getSwitchTime());
                }
            }
        });
    }

    /**
     * Finds a device in the list of devices by its name.
     * @param deviceName the name of the device to be found.
     * @return the device with the given name, or null if no device with that name was found.
     */
    public static Device findDeviceByName (String deviceName) {
        for (Device device: devices) {
            if (device.getName().equals(deviceName)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Returns the number of devices in the list of devices.
     * @return the number of devices.
     */
    public static int numberOfDevices() {
        return devices.size();
    }

    /**
     * Prints ZReport of the devices.
     */
    public static void ZReport() {
        WriteLines.writeLineToFile("Time is:\t" + ProgramManager.timeToString(ProgramManager.getTime()));
        for (Device device: devices) {
            String printThis = device.getDeviceInfo();
            WriteLines.writeLineToFile(printThis);
        }
    }
}