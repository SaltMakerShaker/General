import java.io.IOException;

public class HwApps extends StartHW{
    public static void startApp(String className, boolean debugMode) throws IOException, InterruptedException, InvalidParamException{
        String[] cmd = new String[]{};
        switch (className) {
            case "p": // physics
                cmd = new String[]{"C:/"}; // TODO: change to absolute path of the application you want
                runCmd(cmd, false, debugMode);
                cmd = new String[]{"cmd", "/c", "start", "chrome", "www.keltonk.com"}; // TODO: change to url you want
                runCmd(cmd, false, debugMode);
                break;

            case "m": // math
                cmd = new String[]{"C:/"}; // TODO: change to absolute path of the application you want
                runCmd(cmd, false, debugMode);
                cmd = new String[]{"cmd", "/c", "start", "chrome", "www.google.com"}; // TODO: change to url you want
                runCmd(cmd, false, debugMode);
                break;

            default:
                throw new InvalidParamException("invalid class shorthand: " + className);
        }
    }
}