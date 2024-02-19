/**
 * Represents a smart lamp device that can be controlled remotely.
 * The lamp has adjustable kelvin temperature and brightness settings.
 */
public class SmartLamp extends Device {
    private int kelvin = 4000;
    private int brightness = 100;

    /**
     * Creates a new smart lamp with the specified name and default settings.
     *
     * @param name the name of the lamp device
     */
    public SmartLamp (String name) {
        this.name = name;
    }

    /**
     * Creates a new smart lamp with the specified name and initial status.
     *
     * @param name          the name of the lamp device
     * @param initialStatus the initial status of the lamp ("On" or "Off")
     * @throws Exceptions.erroneousCommandException
     */
    public SmartLamp (String name, String initialStatus) throws Exceptions.erroneousCommandException {
        this.name = name;
        setStatus(initialStatus);
    }

    /**
     * Creates a new smart lamp with the specified name, initial status, kelvin temperature, and brightness level.
     *
     * @param name          the name of the lamp device
     * @param initialStatus the initial status of the lamp ("On" or "Off")
     * @param kelvin        the kelvin temperature setting for the lamp (2500 to 6500)
     * @param brightness    the brightness level setting for the lamp (0 to 100)
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.kelvinValueException
     * @throws Exceptions.brightnessValueException
     */
    public SmartLamp (String name, String initialStatus, int kelvin, int brightness) throws Exceptions.erroneousCommandException, Exceptions.kelvinValueException, Exceptions.brightnessValueException {
        this.name = name;
        setStatus(initialStatus);
        setKelvin(kelvin);
        setBrightness(brightness);
    }

    /**
     * Sets the kelvin temperature setting for the lamp.
     *
     * @param kelvin the kelvin temperature setting for the lamp (2500 to 6500)
     * @throws Exceptions.kelvinValueException
     */
    public void setKelvin(int kelvin) throws Exceptions.kelvinValueException {
        if (kelvin<2000 || kelvin>6500) {
            throw new Exceptions.kelvinValueException();
        }
        this.kelvin = kelvin;
    }

    /**
     * Sets the brightness level setting for the lamp.
     *
     * @param brightness the brightness level setting for the lamp (0 to 100)
     * @throws Exceptions.brightnessValueException
     */
    public void setBrightness(int brightness) throws Exceptions.brightnessValueException {
        if (brightness<0 || brightness>100) {
            throw new Exceptions.brightnessValueException();
        }
        this.brightness = brightness;
    }

    /**
     * Sets the status of the lamp to the specified value.
     *
     * @param status the new status of the lamp ("On" or "Off")
     * @throws Exceptions.erroneousCommandException
     */
    public void setStatus(String status) throws Exceptions.erroneousCommandException {
        if (!(status.equals("On") || status.equals("Off"))) {
            throw new Exceptions.erroneousCommandException();
        }
        this.status = status;
    }

    /**
     * Sets the color temperature and brightness of the smart lamp to create white light.
     * 
     * @param kelvin the color temperature value in Kelvin
     * @param brightness the brightness level, an integer between 0 and 100
     * @throws Exceptions.kelvinValueException
     * @throws Exceptions.brightnessValueException
     */
    public void setWhite(int kelvin, int brightness) throws Exceptions.kelvinValueException, Exceptions.brightnessValueException {
        setKelvin(kelvin);
        setBrightness(brightness);
    }

    /**
     * Returns the kelvin temperature setting for the lamp.
     *
     * @return the kelvin temperature setting for the lamp
     */
    public int getKelvin() {
        return kelvin;
    }

    /**
     * Returns the brightness level setting for the lamp.
     *
     * @return the brightness level setting for the lamp
     */
    public int getBrightness() {
        return brightness;
    }
    @Override
    public String getDeviceInfo() {
        String classOfTheDevice = "Smart Lamp";
        String printThis = classOfTheDevice + " " + this.getName() + " is " + this.status.toLowerCase() + " and its kelvin value is " + this.getKelvin() + "K with " + this.getBrightness();
        printThis += "% brightness, and its time to switch its status is " + this.getSwitchTimeString() + ".";
        return printThis;
    }
}