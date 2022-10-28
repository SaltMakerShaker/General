import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Instances of the Toggl class represent a time entry that can be started
 * and ended in Toggl track. It holds all the required info so that only
 * shorthand is required to make a time entry.
 */
public class Toggl extends StartHW{
    /**
     * entryName: name of new toggl entry
     * project:   shorthand for project id
     * tag:       shorthand for timer tag
     * debugMode: boolean of whether debug mode should be started
     */
    private String entryName;
    private String pid;
    private String tags;
    private String startTime;
    private int elapsedTime;
    private boolean debugMode;

    /**
     * Adds given arguments needs to create time entry to requisite fields.
     *
     * @param entryName name of new toggl entry
     * @param project shorthand for project id
     * @param tag shorthand for timer tag
     * @param debugMode boolean of whether debug mode should be started
     */
    public Toggl(String entryName, String project, String tag, boolean debugMode) {
        this.debugMode = debugMode;
        this.entryName = entryName.replaceAll("_", " ");
        try {
            this.pid = getPid(project);
            this.tags = getTag(tag);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    /**
     * startTimer() records the start time of the timer to be created.
     */
    public void startTimer() {
        this.startTime = this.now();
        this.elapsedTime = (int) System.currentTimeMillis();

    }

    /**
     * endTimer() uses the start time and fields given at construction
     * to create a time entry in Toggl.
     *
     * @throws IOException thrown by Process proc, and reader calls
     * @throws InterruptedException thrown when waitFor() fails
     */
    public void endTimer() throws IOException, InterruptedException {
        Integer elapsedTime =  (int) System.currentTimeMillis() - this.elapsedTime;
        elapsedTime /= 1000;
        System.out.println(elapsedTime);
        // builds command to run in the new command line instance
        String[] cmd = new String[]{"curl.exe", "-u",  this.apiToken + ":api_token",
                "-H", "\"Content-Type: application/json\"", "-d",
                "\"{\\\"created_with\\\":\\\"Start_hw.java\\\"," +
                        "\\\"duronly\\\":true," +
                        "\\\"pid\\\":" + this.pid + "," +
                        "\\\"tid\\\":null," +
                        "\\\"description\\\":\\\"" + this.entryName + "\\\"," +
                        "\\\"tags\\\":[\\\"" + this.tags + "\\\"]," +
                        "\\\"billable\\\":false," +
                        "\\\"wid\\\":" + this.wid + "," +
                        "\\\"start\\\":\\\"" + this.startTime + "\\\"," +
                        "\\\"duration\\\":" + elapsedTime.toString() + "," +
                        "\\\"stop\\\":null}\"",
                "-X", "POST", "https://api.track.toggl.com/api/v9/time_entries",
                "-i", "\nPAUSE"};

        String output = runCmd(cmd, this.debugMode, this.debugMode);
        if (debugMode) {
            System.out.println(output);
        }
    }

    /**
     * now() finds the current time in ISO 8601 standard,
     * more specifically a subset described in RFC 3339.
     *
     * @return current time
     */
    private String now() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    /**
     * getPid converts shorthand representations of projects
     * to PID to improve abstraction of Toggl API.
     *
     * @param shortHand shorthand representation of projects
     * @return PID of project
     * @throws InvalidParamException thrown when unknown shorthand is passed
     */
    private String getPid(String shortHand) throws InvalidParamException{
        switch (shortHand) {
            case "p": // physics
                return "184421786"; // TODO: customize to your PID for your projects
            case "m": // math
                return "184421789";
            case "c": // comp
                return "184421785";
        }
        throw new InvalidParamException("invalid shorthand for Project ID");
    }

    /**
     * getTag converts shorthand representations of tag
     * to exact tag to improve abstraction by reducing
     * spelling errors.
     *
     * @param shortHand shorthand representation of tag
     * @return exact tag
     * @throws InvalidParamException thrown when unknown shorthand is passed
     */
    private String getTag(String shortHand) throws InvalidParamException{
        switch (shortHand) {
            case "h":
                return "homework"; // TODO: customize to the tags you use
            case "p":
                return "preclass";
            case "r":
                return "reading";
            case "s":
                return "studying";
            default:
                throw new InvalidParamException("invalid shorthand for tag");
        }
    }
}
