import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartHW {
    protected String apiToken = ""; // TODO: enter Toggl API token here
    protected String wid = ""; // TODO: enter workplace ID for your desired toggl workplace

    public static void main(String[] args) throws InvalidParamException, IOException, InterruptedException{
        // turn arg string into usable args
        String entryName = args[0];
        String project = args[1];
        String tag = args[2];
        boolean debugMode = false;
        if (args.length > 3) {
            debugMode = args[3].equals("true");
        }

        // launch apps for given project
        HwApps.startApp(project, debugMode);

        // start timer for given project
        Toggl newTimer = new Toggl(entryName, project, tag, debugMode);
        newTimer.startTimer();

        // wait for user input to stop timer
        UserInput.waitForYes("finished?");
        newTimer.endTimer();

    }

    public static String runCmd(String[] cmd, boolean captureResult, boolean debugMode) throws InterruptedException, IOException{
        // run given command
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmd);
        Process proc = pb.start();

        StringBuilder sb = new StringBuilder();
        // capture output
        if (captureResult) {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            proc.waitFor();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            System.out.println(sb.toString());
        }
        return sb.toString();
    }
}
