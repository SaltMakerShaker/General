import java.util.Scanner;

public class UserInput {
    public static boolean waitForYes(String prompt) {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println(prompt);

        String userInput;
        do {
            System.out.println("type yes if true");
            userInput = myObj.nextLine();  // Read user input
        } while (!userInput.equals("yes"));

        return true;
    }
}
