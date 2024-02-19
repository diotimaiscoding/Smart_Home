import java.time.LocalDateTime;
import java.text.DecimalFormat;
import java.time.Duration;
import java.math.BigDecimal;

/**
 * The SmartPlug class represents a smart plug device that can be turned on or off and plugged in or out.
 * It also tracks the amount of energy used while it's plugged in.
 */
public class SmartPlug extends Device {
    private double ampere;
    private boolean pluggedIn;
    private LocalDateTime lastInTime;
    private double usedEnergy;
    private final double voltage = 220;

    /**
     * Constructs a new SmartPlug object with the given name.
     *
     * @param name the name of the SmartPlug
     */
    public SmartPlug(String name) {
        this.name = name;
        this.pluggedIn = false;
        this.status = "Off";
    }

    /**
     * Constructs a new SmartPlug object with the given name and initial status.
     *
     * @param name the name of the SmartPlug
     * @param initialStatus the initial status of the SmartPlug ("On" or "Off")
     * @throws Exceptions.erroneousCommandException
     */
    public SmartPlug(String name, String initialStatus) throws Exceptions.erroneousCommandException {
        this.name = name;
        setInitialStatus(initialStatus);
    }

    /**
     * Constructs a new SmartPlug object with the given name, initial status, and ampere.
     *
     * @param name the name of the SmartPlug
     * @param initialStatus the initial status of the SmartPlug ("On" or "Off")
     * @param ampere the amount of electrical current flowing through the plug
     * @throws Exceptions.ampereValueException
     * @throws Exceptions.plugInException
     * @throws Exceptions.erroneousCommandException
     */
    public SmartPlug(String name, String initialStatus, double ampere) throws Exceptions.plugInException, Exceptions.ampereValueException, Exceptions.erroneousCommandException {
        this.name = name;
        setInitialStatus(initialStatus);
        plugIn(ampere);
    }

    /**
     * Plugs in the SmartPlug with the given ampere.
     *
     * @param ampere the amount of electrical current flowing through the plug
     * @throws Exceptions.plugInException
     * @throws Exceptions.ampereValueException
     */
    public void plugIn(double ampere) throws Exceptions.plugInException, Exceptions.ampereValueException {
        if (pluggedIn) {
            throw new Exceptions.plugInException();
        }
        if (ampere<0 || ampere == 0) {
            throw new Exceptions.ampereValueException();
        }
        this.ampere = ampere;
        pluggedIn = true;
        if (status.equals("On")) {
            lastInTime = ProgramManager.getTime();
        }
    }

    /**
     * Plugs out the SmartPlug.
     * @throws Exceptions.plugOutException
     *
     */
    public void plugOut() throws Exceptions.plugOutException {
        if (!pluggedIn) {
            throw new Exceptions.plugOutException();
        }
        pluggedIn = false;
        calculateEnergyUsed();
    }

    /**
     * Plugs out the SmartPlug.
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.AlreadySwitchedException
     *
     * @throws IllegalArgumentException if the SmartPlug is not currently plugged in
     */
    @Override
    public void switchDevice(String status) throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException {
        super.switchDevice(status);
        if (status.toLowerCase().equals("on")) {
            if (pluggedIn) {
                lastInTime = ProgramManager.getTime();
            }
        }
        else if (status.toLowerCase().equals("off")) {
            calculateEnergyUsed();
        }
    }

    /**
     * Sets the initial status of the SmartPlug to the given status ("On" or "Off").
     * 
     * @param initialStatus the initial status to set the SmartPlug to ("on" or "off").
     * @throws Exceptions.erroneousCommandException
     */
    @Override
    public void setInitialStatus(String initialStatus) throws Exceptions.erroneousCommandException {
        super.setInitialStatus(initialStatus);
        if (status.equals("On")) {
            if (pluggedIn) {
                lastInTime = ProgramManager.getTime();
            }
        }
    }

    /**
     * Calculates the amount of energy used by the SmartPlug since it was last plugged in
     * and updates the usedEnergy variable accordingly. Should be called before unplugging
     * the SmartPlug or when switching its status to "off".
     */
    public void calculateEnergyUsed() {
        BigDecimal addEnergy;
        if (lastInTime != null) {
            BigDecimal timeDiffMin = new BigDecimal(voltage * ampere * Duration.between(lastInTime, ProgramManager.getTime()).toMillis());
            BigDecimal divideBy = new BigDecimal(100000 * 60);
            addEnergy = timeDiffMin.divide(divideBy, 2, BigDecimal.ROUND_HALF_UP);
        }
        else {
            addEnergy = new BigDecimal(0);
        }
        this.usedEnergy += addEnergy.doubleValue();
    }

    /**
     * Returns the amount of energy used by the SmartPlug since it was last plugged in.
     * 
     * @return the amount of energy used by the SmartPlug in watt-hours.
     */
    public double usedEnergy() {
        return this.usedEnergy;
    }
    @Override
    public String getDeviceInfo() {
        String classOfTheDevice = "Smart Plug";
        DecimalFormat df = new DecimalFormat("#0.00");
        String printThis = classOfTheDevice + " " + this.getName() + " is " + this.status.toLowerCase() + " and consumed " + df.format(this.usedEnergy()).replace(".", ",") + "W so far (excluding current device),";
        printThis += " and its time to switch its status is " + this.getSwitchTimeString() + ".";
        return printThis;
    }
}