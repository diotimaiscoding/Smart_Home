import java.time.LocalDateTime;

/**
 * The abstract class for a smart device.
 */
public abstract class Device {
    protected LocalDateTime switchTime = null;
    protected String status = "Off";
    protected String name = null;
    protected LocalDateTime lastInTime;
    
    /**
     * Get the name of the device.
     *
     * @return The name of the device.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the switch time of the device.
     *
     * @param setThisSwitch The switch time to be set.
     */
    public void setSwitchTime(LocalDateTime setThisSwitch) {
        switchTime = setThisSwitch;
    }

    /**
     * Get the switch time of the device.
     *
     * @return The switch time of the device.
     */
    // Return the switch time.
    public LocalDateTime getSwitchTime() {
        return switchTime;
    }
    public String getSwitchTimeString() {
        if (switchTime != null) {
            return ProgramManager.timeToString(switchTime);
        }
        else {
            return "null";
        }
    }
    
    /**
     * Switch the device to the specified status.
     *
     * @param status The status to switch to.
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     * @throws IllegalArgumentException If the status is not "on" or "off", or if the device is already in the specified status.
     */
    public void switchDevice(String status) throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException {
        //ADD A ERROR IF STATUS IS ALREADY THE SAME.
        if (!(status.toLowerCase().equals("on") || status.toLowerCase().equals("off"))) {
            throw new Exceptions.erroneousCommandException();
        }
        if (this.status.equals(status) ) {
            if (status.equals("On")) {
                throw new Exceptions.AlreadySwitchedException("On");
            }
            else {
                throw new Exceptions.AlreadySwitchedException("Off");
            }
        }
        this.status = status;
    }

    /**
     * Set the initial status of the device.
     *
     * @param initialStatus The initial status to be set.
     * @throws Exceptions.erroneousCommandException
     * @throws IllegalArgumentException If the initial status is not "on" or "off".
     */
    public void setInitialStatus(String initialStatus) throws Exceptions.erroneousCommandException {
        if (!(status.toLowerCase().equals("on") || status.toLowerCase().equals("off"))) {
            throw new Exceptions.erroneousCommandException();
        }
        this.status = initialStatus;
    }
    // Change the device name.
    /**
     * Change the name of the device.
     *
     * @param newName The new name to be set.
     * @throws Exceptions.ChangeNameException
     */
    public void changeName(String newName) throws Exceptions.ChangeNameException {
        if (this.name.equals(newName)) {
            throw new Exceptions.ChangeNameException(false);
        }
        if (DeviceManager.findDeviceByName(newName) != null) {
            throw new Exceptions.ChangeNameException(true);
        }
        this.name = newName;
    }
    /**
     * Abstract method to get the device information.
     * @return
     */
    public abstract String getDeviceInfo();
}