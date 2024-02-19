import java.util.ArrayList;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**s
 * This class provides a method to read lines from a file and execute the commands in them.
 */
public class ReadLines {

    /**
     * Reads lines from the file and returns them as an ArrayList.
     * @param filename the name of the file to read from
     * @return an ArrayList of Strings containing the lines from the file
     */
    private static ArrayList<String> readLinesFromFile(String filename) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("")) {
                lines.remove(i);
                i--;
            }
        }
        return lines;
    }

    /**
     * Reads the lines from the given file and executes the commands in them.
     * @param fileName the name of the file to read from
     * @param fileNameOut the name of the file to write to
     * @throws Exceptions.SetTimeException
     * @throws Exceptions.AlreadySwitchedException
     * @throws Exceptions.erroneousCommandException
     * @throws Exceptions.InitialTimeException
     * @throws Exceptions.ChangeNameException
     * @throws Exceptions.SetTimeException
     * @throws Exceptions.AlreadySwitchedException
     * @throws Exceptions.erroneousCommandException
     */
    protected static void runLines() throws Exceptions.erroneousCommandException, Exceptions.AlreadySwitchedException, Exceptions.SetTimeException {
        String fileName = ProgramManager.fileName;
        String fileNameOut = ProgramManager.fileNameOut;
        ArrayList<String> lines = readLinesFromFile(fileName);
        try {
            FileWriter clear = new FileWriter(fileNameOut);
            clear.write("");
            clear.close();
        }
        catch (IOException e) {
            WriteLines.writeLineToFile("ERROR: Illegal output file name.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-M-d_H:m:s");
        DeviceManager manager = new DeviceManager();
        String firstLine = lines.get(0);
        String[] firstLineElems = firstLine.split("\t");
        WriteLines.writeLineToFile("COMMAND: " + firstLine);
        //if command is not SetInitialTime, terminate the program.
        if (!firstLineElems[0].equals("SetInitialTime") || firstLineElems.length != 2) {
            Exceptions.InitialTimeException.notInitializedExceptionWriteError(false);
        }
        else {
            try {
                LocalDateTime timeToSet = LocalDateTime.parse(firstLineElems[1], formatter);
                ProgramManager.setTime(timeToSet);
                WriteLines.writeLineToFile("SUCCESS: Time has been set to " + ProgramManager.timeToString(timeToSet) + "!");
            }
            catch (DateTimeParseException e) {
                Exceptions.InitialTimeException.notInitializedExceptionWriteError(true);
            }
        }
        boolean isLastCommandZReport = lines.get(lines.size()-1).split("\t")[0].equals("ZReport");
        for (String line: lines.subList(1, lines.size())) {
            DeviceManager.sortDevices();
            if (line.equals("")) {
                // Nothing happens.
            }
            else{
                WriteLines.writeLineToFile("COMMAND: " + line);
                String[] lineElems = line.split("\t");
                String command = lineElems[0];
                try {
                    switch (command){
                        case "SetInitialTime":
                            // HANDLE ERROR. Initial Time is Set Once.
                            throw new Exceptions.erroneousCommandException();
                        case "SetTime":
                            LocalDateTime toTheTime = LocalDateTime.parse(lineElems[1], formatter);
                            ProgramManager.setTime(toTheTime);
                            break;
                        case "Nop":
                            ProgramManager.nop();
                            break;
                        case "SkipMinutes":
                            ProgramManager.skipMinutes(Integer.valueOf(lineElems[1]));
                            if (lineElems.length > 2) {
                                throw new Exceptions.erroneousCommandException();
                            }
                            break;
                        case "Add":
                            String deviceType = lineElems[1];
                            Device addThisDevice;
                            switch(deviceType) {
                                case "SmartLamp":
                                if (!(lineElems.length > 2 && lineElems.length < 7)) {
                                    throw new Exceptions.erroneousCommandException();
                                }
                                    addThisDevice = createSmartLamp(lineElems);
                                    break;
                                case "SmartColorLamp":
                                if (!(lineElems.length > 2 && lineElems.length < 7)) {
                                    throw new Exceptions.erroneousCommandException();
                                }
                                    addThisDevice = createSmartLampWithColor(lineElems);
                                    break;
                                case "SmartCamera":
                                    if (!(lineElems.length == 4 || lineElems.length == 5)) {
                                        throw new Exceptions.erroneousCommandException();
                                    }
                                    addThisDevice = createSmartCamera(lineElems);
                                    break;
                                case "SmartPlug":
                                if (!(lineElems.length > 2 && lineElems.length < 6)) {
                                    throw new Exceptions.erroneousCommandException();
                                }
                                    addThisDevice = createSmartPlug(lineElems);
                                    break;
                                default:
                                    throw new Exceptions.erroneousCommandException();    
                            }
                            if (DeviceManager.findDeviceByName(lineElems[2]) != null) {
                                throw new Exceptions.addException();
                            }
                            manager.add(addThisDevice);
                            break;
                        case "Remove":
                        if (!(lineElems.length == 2)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            Device device = DeviceManager.findDeviceByName(lineElems[1]);
                            String printThis = device.getDeviceInfo();
                            device.status = "Off";
                            manager.remove(lineElems[1]);
                            WriteLines.writeLineToFile("SUCCESS: Information about removed smart device is as follows:");
                            WriteLines.writeLineToFile(printThis);
                            break;
                        case "SetSwitchTime":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            Device deviceSetSwitchTime = DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSetSwitchTime.setSwitchTime(LocalDateTime.parse(lineElems[2], formatter));
                            break;
                        case "Switch":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            Device deviceSwitch = DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSwitch.switchDevice(lineElems[2]);
                            break;
                        case "ChangeName":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            if (lineElems[1].equals(lineElems[2])) {
                                throw new Exceptions.ChangeNameException(false);
                            }
                            Device deviceChangeName = DeviceManager.findDeviceByName(lineElems[1]);
                            deviceChangeName.changeName(lineElems[2]);
                            break;
                        case "PlugIn":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartPlug devicePlugIn = (SmartPlug) DeviceManager.findDeviceByName(lineElems[1]);
                            devicePlugIn.plugIn(Double.valueOf(lineElems[2]));
                            break;
                        case "PlugOut":
                        if (!(lineElems.length == 2)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartPlug devicePlugOut = (SmartPlug) DeviceManager.findDeviceByName(lineElems[1]);
                            devicePlugOut.plugOut();
                            break;
                        case "SetKelvin":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartLamp deviceSetKelvin = (SmartLamp) DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSetKelvin.setKelvin(Integer.valueOf(lineElems[2]));
                            break;
                        case "SetBrightness":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartLamp deviceSetBrightness = (SmartLamp) DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSetBrightness.setBrightness(Integer.valueOf(lineElems[2]));
                            break;
                        case "SetColorCode":
                        if (!(lineElems.length == 3)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartLampWithColor deviceSetColorCode = (SmartLampWithColor) DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSetColorCode.setColorCode(lineElems[2]);
                            break;
                        case "SetWhite":
                        if (!(lineElems.length == 4)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartLamp deviceSetWhite = (SmartLamp) DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSetWhite.setWhite(Integer.valueOf(lineElems[2]), Integer.valueOf(lineElems[3]));
                            break;
                        case "SetColor":
                        if (!(lineElems.length == 4)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            SmartLampWithColor deviceSetColor = (SmartLampWithColor) DeviceManager.findDeviceByName(lineElems[1]);
                            deviceSetColor.setColor(lineElems[2], Integer.valueOf(lineElems[3]));
                            break;
                        case "ZReport":
                        if (!(lineElems.length < 2)) {
                            throw new Exceptions.erroneousCommandException();
                        }
                            DeviceManager.ZReport();
                            break;
                        default:
                            throw new Exceptions.erroneousCommandException();
                    }
                }
                catch (Exceptions.nopException e1) {
                    Exceptions.nopException.nopExceptionWriteError();
                }
                catch (Exceptions.addException e2) {
                    Exceptions.addException.addExceptionWriteError();
                }
                catch (Exceptions.AlreadySwitchedException e2) {
                    Exceptions.AlreadySwitchedException.AlreadySwitchedExceptionWriteError();
                }
                catch (Exceptions.ChangeNameException e3) {
                    Exceptions.ChangeNameException.changeNameExceptionWriteError();
                }
                catch (Exceptions.SetTimeException e4) {
                    Exceptions.SetTimeException.setTimeExceptionWriteError();
                }
                catch (Exceptions.SkipMinutesException e5) {
                    Exceptions.SkipMinutesException.skipMinutesExceptionWriteError();
                }
                catch (Exceptions.erroneousCommandException e3) {
                    Exceptions.erroneousCommandException.erroneousCommandWriteError();
                }
                catch (Exceptions.ampereValueException e4) {
                    Exceptions.ampereValueException.ampereValueExceptionWriteError();
                }
                catch (Exceptions.kelvinValueException e5) {
                    Exceptions.kelvinValueException.kelvinValueExceptionWriteError();
                }
                catch (Exceptions.brightnessValueException e6) {
                    Exceptions.brightnessValueException.brightnessValueExceptionWriteError();
                }
                catch (Exceptions.colorCodeValueException e7) {
                    Exceptions.colorCodeValueException.colorCodeValueExceptionWriteError();
                }
                catch (Exceptions.megabytesValueException e8) {
                    Exceptions.megabytesValueException.megabytesValueExceptionWriteError();
                }
                catch (Exceptions.plugInException e9) {
                    Exceptions.plugInException.plugInExceptionWriteError();
                }
                catch (Exceptions.plugOutException e9) {
                    Exceptions.plugOutException.plugOutExceptionWriteError();
                }
                catch (NumberFormatException e10) {
                    Exceptions.erroneousCommandException.erroneousCommandWriteError();
                }
                catch (NullPointerException e11) {
                    Exceptions.NoSuchDeviceException.noSuchDeviceExceptionWriteError();
                }
                catch (ClassCastException e12) {
                    Exceptions.DeviceTypeError.deviceTypeErrorWrite(e12.getLocalizedMessage().split(" ")[5].toString());
                }
                catch (DateTimeParseException e13) {
                    WriteLines.writeLineToFile("ERROR: Time format is not correct!");
                }
            }
        }
        if (!isLastCommandZReport) {
            WriteLines.writeLineToFile("ZReport:");
            DeviceManager.ZReport();
        }
    }

    /**
     * @param lineElems
     * @return
     * @throws NumberFormatException
     * @throws erroneousCommandException
     * @throws kelvinValueException
     * @throws brightnessValueException
     */
    protected static SmartLamp createSmartLamp(String[] lineElems) throws NumberFormatException, Exceptions.erroneousCommandException, Exceptions.kelvinValueException, Exceptions.brightnessValueException {
        if (lineElems.length == 1 || lineElems.length == 2 || lineElems.length == 5 || lineElems.length > 6) {
            throw new IllegalArgumentException("ERROR");
        }
        else if (lineElems.length == 3) {
            return new SmartLamp(lineElems[2]);
        }
        else if (lineElems.length == 4) {
            return new SmartLamp(lineElems[2], lineElems[3]);
        }
        else if (lineElems.length == 6) {
            return new SmartLamp(lineElems[2], lineElems[3], Integer.valueOf(lineElems[4]), Integer.valueOf(lineElems[5]));
        }
        else {
            throw new IllegalArgumentException("ERROR");
        }
    }
    protected static SmartLampWithColor createSmartLampWithColor(String[] lineElems) throws NumberFormatException, Exceptions.erroneousCommandException, Exceptions.kelvinValueException, Exceptions.brightnessValueException, Exceptions.colorCodeValueException {
        if (lineElems.length == 1 || lineElems.length == 2 || lineElems.length == 5 || lineElems.length > 6) {
            throw new IllegalArgumentException("ERROR");
        }
        else if (lineElems.length == 3) {
            return new SmartLampWithColor(lineElems[2]);
        }
        else if (lineElems.length == 4) {
            return new SmartLampWithColor(lineElems[2], lineElems[3]);
        }
        else if (lineElems.length == 6) {
            return new SmartLampWithColor(lineElems[2], lineElems[3], lineElems[4], Integer.valueOf(lineElems[5]));
        }
        else {
            throw new IllegalArgumentException("ERROR");
        }
    }
    protected static SmartCamera createSmartCamera(String[] lineElems) throws Exceptions.erroneousCommandException, NumberFormatException, Exceptions.megabytesValueException {
        if (lineElems.length == 1 || lineElems.length == 2 || lineElems.length == 3 || lineElems.length > 5) {
            throw new Exceptions.erroneousCommandException();
        }
        else if (lineElems.length == 4) {
            return new SmartCamera(lineElems[2], Double.valueOf(lineElems[3]));
        }
        else if (lineElems.length == 5) {
            return new SmartCamera(lineElems[2], Double.valueOf(lineElems[3]), lineElems[4]);
        }
        else {
            throw new IllegalArgumentException("ERROR");
        }
    }
    protected static SmartPlug createSmartPlug(String[] lineElems) throws NumberFormatException, Exceptions.plugInException, Exceptions.ampereValueException, Exceptions.erroneousCommandException {
        if (lineElems.length == 1 || lineElems.length == 2 || lineElems.length > 5) {
            throw new IllegalArgumentException("ERROR");
        }
        else if (lineElems.length == 3) {
            return new SmartPlug(lineElems[2]);
        }
        else if (lineElems.length == 4) {
            return new SmartPlug(lineElems[2], lineElems[3]);
        }
        else if (lineElems.length == 5) {
            return new SmartPlug(lineElems[2], lineElems[3], Double.valueOf(lineElems[4]));
        }
        else {
            throw new IllegalArgumentException("ERROR");
        }
    }
}