public class Exceptions {
    public static class InitialTimeException extends Exception {
        public static void notInitializedExceptionWriteError(boolean isCommandWritten) {
            String errorStat;
            if (!isCommandWritten) {
                errorStat = "First command must be set initial time";
            }
            else {
                errorStat = "Format of the initial date is wrong";
            }
            WriteLines.writeLineToFile("ERROR: " + errorStat + "! Program is going to terminate!");
            System.exit(0);
        }
    }
    public static class NoSuchDeviceException extends Exception {
        public static void noSuchDeviceExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: There is not such a device!");
        }
    }
    public static class DeviceTypeError extends Exception {
        private static String deviceClassName = "";
        public static void deviceTypeErrorWrite(String deviceClass) {
            switch(deviceClass) {
                case "SmartLamp":
                    DeviceTypeError.deviceClassName = "smart lamp";
                    break;
                case "SmartLampWithColor":
                    DeviceTypeError.deviceClassName = "smart color lamp";
                    break;
                case "SmartCamera":
                    DeviceTypeError.deviceClassName = "smart camera";
                    break;
                case "SmartPlug":
                    DeviceTypeError.deviceClassName = "smart plug";
                    break;
            }
            WriteLines.writeLineToFile(String.format("ERROR: This device is not a %s!", DeviceTypeError.deviceClassName));
        }
    }
    public static class AlreadySwitchedException extends Exception {
        private static String status;
        public AlreadySwitchedException(String status) {
            AlreadySwitchedException.status = status;
        }
        public static void AlreadySwitchedExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: This device is already switched " + status.toLowerCase() + "!");
        }
    }
    public static class ChangeNameException extends Exception{
        private static boolean existingDevice;
        public ChangeNameException(boolean existingDevice) {
            ChangeNameException.existingDevice = existingDevice;
        }
        public static void changeNameExceptionWriteError() {
            if (!existingDevice) {
                WriteLines.writeLineToFile("ERROR: Both of the names are the same, nothing changed!");
            }
            else {
                WriteLines.writeLineToFile("ERROR: There is already a smart device with same name!");
            }
        }
    }
    public static class SetTimeException extends Exception {
        private static int isBefore;
        public SetTimeException(int isBefore) {
            SetTimeException.isBefore = isBefore;
        }
        public static void setTimeExceptionWriteError() {
            if (SetTimeException.isBefore == 0) {
                WriteLines.writeLineToFile("ERROR: Time cannot be reversed!");
            }
            else if (SetTimeException.isBefore == 1) {
                WriteLines.writeLineToFile("ERROR: Time is already set to this time!");
            }
        }
    }
    public static class SkipMinutesException extends Exception {
        private static boolean isZero;
        public SkipMinutesException(boolean isZero) {
            SkipMinutesException.isZero = isZero;
        }
        public static void skipMinutesExceptionWriteError() {
            String errorMessage;
            if (SkipMinutesException.isZero) {
                errorMessage = "ERROR: There is nothing to skip!";
            }
            else {
                errorMessage = "ERROR: You cannot reverse the time!";
            }
            WriteLines.writeLineToFile(errorMessage);
        }
    }
    public static class nopException extends Exception {
        public static void nopExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: There is nothing to switch!");
        }
    }
    public static class addException extends Exception {
        public static void addExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: There is already a smart device with same name!");
        }
    }
    public static class erroneousCommandException extends Exception {
        public static void erroneousCommandWriteError() {
            WriteLines.writeLineToFile("ERROR: Erroneous command!");
        }
    }
    public static class ampereValueException extends Exception {
        public static void ampereValueExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: Ampere value must be a positive number!");
        }
    }
    public static class kelvinValueException extends Exception {
        public static void kelvinValueExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: Kelvin value must be in range of 2000K-6500K!");
        }
    }
    public static class brightnessValueException extends Exception {
        public static void brightnessValueExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: Brightness must be in range of 0%-100%!");
        }
    }
    public static class colorCodeValueException extends Exception {
        public static void colorCodeValueExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
        }
    }
    public static class megabytesValueException extends Exception {
        public static void megabytesValueExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: Megabyte value must be a positive number!");
        }
    }
    public static class plugInException extends Exception {
        public static void plugInExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: There is already an item plugged in to that plug!");
        }
    }
    public static class plugOutException extends Exception {
        public static void plugOutExceptionWriteError() {
            WriteLines.writeLineToFile("ERROR: This plug has no item to plug out from that plug!");
        }
    }
}