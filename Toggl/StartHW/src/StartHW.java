import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point for StartHW and abstraction for high level
 * operation. holds helper functions and fields for operation.
 */
public class StartHW {
    protected String apiToken = ""; // TODO: enter Toggl API token here
    protected String wid = ""; // TODO: enter workplace ID for your desired toggl workplace

    /**
     * Meat of program, makes calls to all wanted/needed methods
     *
     * @param args [entryName, project, tag, debugmode] debug optional]
     * @throws InvalidParamException thrown when user provides invalid params
     * @throws IOException thrown by runCmd
     * @throws InterruptedException thrown by runCmd
     */
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

    /**
     * Helper function to improve abstraction.
     * Takes a cURL command as an input and runs it.
     *
     * @param cmd array of strings representing cURL command
     * @param captureResult whether the result should be captured
     * @param debugMode whether the error stream should be read
     * @return the captured output, if desired
     * @throws InterruptedException thrown by waitFor
     * @throws IOException thrown by reader and writer
     */
    public static String runCmd(String[] cmd, boolean captureResult, boolean debugMode) throws InterruptedException, IOException{
        // run given command
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmd);
        Process proc = pb.start();

        StringBuilder sb = new StringBuilder();
        // capture output
        if (captureResult) {
            BufferedReader reader;
            if (debugMode) {
                reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            }
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
