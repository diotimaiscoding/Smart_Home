/**
 * A smart lamp with color capabilities.
 */
public class SmartLampWithColor extends SmartLamp {
    private boolean colorModeOn = false;
    private int colorCode;

    /**
     * Constructs a SmartLampWithColor object with the given name, initial status, 
     * and color or kelvin value and brightness.
     *
     * @param name the name of the smart lamp
     * @param initialStatus the initial status of the smart lamp
     * @param kelvinOrColor the kelvin or color value of the lamp, depending on the format 
     *                      (integer or hex string)
     * @param brightness the brightness value of the lamp
     * @throws Exceptions.colorCodeValueException
     * @throws Exceptions.brightnessValueException
     * @throws Exceptions.kelvinValueException
     * @throws Exceptions.erroneousCommandException
     */
    public SmartLampWithColor (String name) {
        super(name);
    }
    public SmartLampWithColor (String name, String initialStatus) throws Exceptions.erroneousCommandException {
        super(name, initialStatus);
    }
    public SmartLampWithColor (String name, String initialStatus, String kelvinOrColor, int brightness) throws Exceptions.erroneousCommandException, Exceptions.kelvinValueException, Exceptions.colorCodeValueException, Exceptions.brightnessValueException {
        super(name);
        setStatus(initialStatus);
        setKelvin(toKelvin(kelvinOrColor));
        this.colorCode = toColor(kelvinOrColor);
        setBrightness(brightness);
    }

    /**
     * @param colorCode
     * @throws Exceptions.colorCodeValueException
     */
    public void setColorCode(String colorCodeString) throws Exceptions.colorCodeValueException {
        if (!colorCodeString.substring(0,2).equals("0x")) {
            throw new Exceptions.colorCodeValueException();
        }
        int colorCode = Integer.parseInt(colorCodeString.substring(2), 16);
        if (colorCode<0x000000 || colorCode>0xFFFFFF) {
            throw new Exceptions.colorCodeValueException();
        }
        this.colorCode = colorCode;
        colorModeOn = true;
    }

    /**
     * Converts the given value to kelvin format, if it is an integer.
     * If the value is in hex format, returns the default kelvin value of 4000.
     *
     * @param val the value to be converted to kelvin
     * @return the converted value in kelvin format
     */
    public int toKelvin (String valString) {
        if (valString.substring(0,2).equals("0x")) {
            this.colorModeOn = true;
            return 4000;
        }
        int val = Integer.parseInt(valString);
        return val;
    }

    /**
     * Converts the given value to color format, if it is in hex format.
     * If the value is an integer, returns 0.
     *
     * @param val the value to be converted to color
     * @return the converted value in color format
     * @throws Exceptions.colorCodeValueException
     */
    public static int toColor (String valString) throws Exceptions.colorCodeValueException {
        if (valString.substring(0,2).equals("0x")) {
            int val = Integer.parseInt(valString.substring(2), 16);
            if (val<0x000000 || val>0xFFFFFF) {
                throw new Exceptions.colorCodeValueException();
            }
            return val;
        }
        return 0;
    }

    /**
     * Sets the color of the lamp to the given color code and brightness.
     *
     * @param colorCode the color code to be set (in hex format)
     * @param brightness the brightness level to be set
     * @throws Exceptions.colorCodeValueException
     * @throws Exceptions.brightnessValueException
     */
    public void setColor(String colorCodeString, int brightness) throws Exceptions.colorCodeValueException, Exceptions.brightnessValueException {
        if (!colorCodeString.substring(0,2).equals("0x")) {
            throw new Exceptions.colorCodeValueException();
        }
        int colorCode = Integer.parseInt(colorCodeString.substring(2), 16);
        if (colorCode<0x000000 || colorCode>0xFFFFFF) {
            throw new Exceptions.colorCodeValueException();
        }
        if (brightness<0 || brightness>100) {
            throw new Exceptions.brightnessValueException();
        }
        setColorCode(colorCodeString);
        setBrightness(brightness);
    }

    /**
     * Sets the kelvin value and brightness level for the lamp to create a white light.
     *
     * @param kelvin the kelvin value to set for the white light, must be between 2500 and 6500
     * @param brightness the brightness level to set for the white light, must be between 0 and 100
     * @throws Exceptions.kelvinValueException
     * @throws Exceptions.brightnessValueException
     */
    @Override
    public void setWhite(int kelvin, int brightness) throws Exceptions.kelvinValueException, Exceptions.brightnessValueException {
        if (kelvin<2000 || kelvin>6500) {
            throw new Exceptions.kelvinValueException();
        }
        if (brightness<0 || brightness>100) {
            throw new Exceptions.brightnessValueException();
        }
        setKelvin(kelvin);
        setBrightness(brightness);
        colorModeOn = false;
    }
    public boolean isColored() {
        return this.colorModeOn;
    }
    public int getColorCode() {
        return this.colorCode;
    }
    public String getColor() {
        if (colorModeOn) {
            return "0x" + String.format("%6s", Integer.toHexString(colorCode).toUpperCase()).replace(' ', '0');
        }
        return String.valueOf(this.getKelvin()) + "K";
    }
    @Override
    public String getDeviceInfo() {
        String classOfTheDevice = "Smart Color Lamp";
        String printThis = classOfTheDevice + " " + this.getName() + " is " + this.status.toLowerCase() + " and its color value is " + this.getColor() + " with " + this.getBrightness() +"% brightness,";
        printThis += " and its time to switch its status is " + this.getSwitchTimeString() + ".";
        return printThis;
    }
}