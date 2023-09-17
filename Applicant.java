import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Applicant extends Staff {

    /** a flag to show whether the applicant has created a profile or not */
    private boolean created = false;

    /** a {@code Profile} to store the current user's profile, empty at first */
    private Profile profile = new Profile();

    /** an {@code ArrayList} to store the chosen jobs, empty at first */
    private ArrayList<String> chosenJobs = new ArrayList<>();

    /** emtpy constructor */
    public Applicant() {
        super();
    }

    /**
     * constructor with files to read
     * 
     * @param jobFile     a pathname {@code String}
     * @param profileFile a pathname {@code String}
     */
    public Applicant(String jobFile, String profileFile) {
        super(jobFile, profileFile);
    }

    /** {@inheritDoc} */
    public void welcome() {
        System.out.println(
                "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo\n" +
                        ".d88888b           dP                                             dP          \n" +
                        "88.    \"\'          88                                             88          \n" +
                        "`Y88888b. .d8888b. 88d888b. 88d8b.d8b. .d8888b. .d8888b. .d8888b. 88 .d8888b. \n" +
                        "      `8b 88\'  `\"\" 88\'  `88 88\'`88\'`88 88\'  `88 88\'  `88 88\'  `88 88 88ooood8 \n" +
                        "d8'   .8P 88.  ... 88    88 88  88  88 88.  .88 88.  .88 88.  .88 88 88.  ... \n" +
                        " Y88888P  `88888P' dP    dP dP  dP  dP `88888P\' `88888P\' `8888P88 dP `88888P' \n" +
                        "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooo~~~~.88~oooooooooooooooooo\n" +
                        "                                                          d8888P              \n" +
                        "\n" +
                        "     _                _ _           _   _               ____            _        _ \n" +
                        "    / \\   _ __  _ __ | (_) ___ __ _| |_(_) ___  _ __   |  _ \\ ___  _ __| |_ __ _| |\n" +
                        "   / _ \\ | '_ \\| \'_ \\| | |/ __/ _` | __| |/ _ \\| '_ \\  | |_) / _ \\| \'__| __/ _` | |\n"
                        +
                        "  / ___ \\| |_) | |_) | | | (_| (_| | |_| | (_) | | | | |  __/ (_) | |  | || (_| | |\n" +
                        " /_/   \\_\\ .__/| .__/|_|_|\\___\\__,_|\\__|_|\\___/|_| |_| |_|   \\___/|_|   \\__\\__,_|_|\n"
                        + "         |_|   |_|                                                                 \n"
                        + "\n" +
                        "ooooooooooo -------- Welcome to Schmoogle's Application Portal --------  ooooooooooo\n");
    }

    /**
     * to display the status including jobs available and the applications submitted
     */
    public void status() {
        System.out
                .println(jobs.size() + " jobs available. " + chosenJobs.size() + " applications submitted.");
    }

    /** menu when profile hasn't been created */
    public void menuWithCreate() {
        System.out.println("Please enter one of the following commands to continue:\n" +
                "- create new application: [create] or [c]\n" +
                "- list available jobs: [jobs] or [j]\n" +
                "- quit the program: [quit] or [q]");
        System.out.print("> ");
    }

    /** menu when profile has been created */
    public void menuWithoutCreate() {
        System.out.println("Please enter one of the following commands to continue:\n" +
                "- list available jobs: [jobs] or [j]\n" +
                "- quit the program: [quit] or [q]");
        System.out.print("> ");
    }

    protected void command(Scanner in)
            throws ParseException, IOException, InvalidDataFormatException, InvalidCharacteristicException {
        status();
        // if the applicant has already created one profile, it will not show
        // [create]/[c] again
        if (!created) {
            menuWithCreate();
        } else {
            menuWithoutCreate();
        }
        while (true) {
            String command = in.nextLine().trim();
            // System.out.print(command);
            if ((command.equals("create") || command.equals("c")) && !created) {
                createApplication(in);
                created = true;
            } else if (command.equals("jobs") || command.equals("j")) {
                listJobs();
                if (!created) {
                    status();
                    // if the applicant has already created one profile,
                    // it will not show [create]/[c] again
                    if (!created) {
                        menuWithCreate();
                    } else {
                        menuWithoutCreate();
                    }
                    continue;
                }
                applyJobs(in);
                removeJobs();
            } else if (command.equals("quit") || command.equals("q")) {
                System.out.println();
                return;
            } else {
                System.out.print("Invalid input! Please enter a valid command to continue: \n> ");
                continue;
            }
            status();
            // if the applicant has already created one profile, it will not show
            // [create]/[c] again
            if (!created) {
                menuWithCreate();
            } else {
                menuWithoutCreate();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void listJobs() {
        for (int i = 0; i < jobs.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + jobs.get(i).toDisplayString());
        }
    }

    /**
     * remove the job after the applicant has submitted the application, according
     * to {@code chosenJobs}
     */
    private void removeJobs() {
        int i = 0;
        while (true) {
            if (jobs.size() == 0) {
                break;
            }
            if (chosenJobs.contains(jobs.get(i).toDisplayString())) {
                jobs.remove(i);
                i = 0;
                continue;
            }
            i++;
            if (i == jobs.size()) {
                break;
            }
        }
    }

    /**
     * add the chosen jobs into {@code chosenJobs}
     * 
     * @param in the source of the input
     * @throws IOException
     * @throws InvalidDataFormatException
     * @throws InvalidCharacteristicException
     * @throws ParseException
     */
    private void applyJobs(Scanner in)
            throws IOException, InvalidDataFormatException, InvalidCharacteristicException, ParseException {
        // if there is at least one job
        if (jobs.size() != 0) {
            System.out.print("Please enter the jobs you would like to apply for (multiple options are possible): ");
            String numberInput = in.nextLine().trim();
            // if the user leaves this field blank, return to the main menu
            if (numberInput.isEmpty()) {
                return;
            }
            // original strings of number
            String[] numberStrings;
            // parse strings to int
            ArrayList<Integer> numbers = new ArrayList<>();
            while (true) {
                boolean isValidInput = false;
                numbers.clear();
                // if input only contains , or numbers
                if (numberInput.matches("[[0-9]*,]*")) {
                    numberStrings = numberInput.split(",");
                    for (int i = 0; i < numberStrings.length; i++) {
                        if (!numberStrings[i].isEmpty()) {
                            numbers.add(Integer.parseInt(numberStrings[i]));
                        }
                    }
                    isValidInput = true;
                }
                // if no number input, like ",,,,,,"
                if (numbers.size() == 0) {
                    isValidInput = false;
                } else {
                    // numbers contain all the number
                    int maxNum = numbers.get(0);
                    for (int i = 0; i < numbers.size(); i++) {
                        if (maxNum < numbers.get(i)) {
                            maxNum = numbers.get(i);
                        }
                    }
                    // if max number is out of bound
                    if (maxNum > jobs.size()) {
                        isValidInput = false;
                    }
                }
                if (isValidInput) {
                    break;
                }
                System.out.print("Invalid input! Please enter a valid number to continue: ");
                numberInput = in.nextLine().trim();
            }
            // store the submission
            Submission submission = new Submission();
            for (int i = 0; i < numbers.size(); i++) {
                int index = numbers.get(i) - 1;
                submission.storeSubmission(jobs.get(index).toStoreString(), profile.toStoreString());
                chosenJobs.add(jobs.get(index).toDisplayString());
            }
        } else {
            System.out.println("No jobs available.");
        }
    }

    /**
     * create an application, and wirte the {@code Profile} into the
     * {@code profileFile}
     * 
     * @param in the source of the input
     * @throws ParseException
     * @throws IOException
     */
    private void createApplication(Scanner in) throws ParseException, IOException {
        System.out.println("# Create new Application");
        // lastname is compulsory
        System.out.print("Lastname: ");
        String lastname = in.nextLine().trim();
        while (lastname.isEmpty()) {
            System.out.print("Ooops! Lastname must be provided: ");
            lastname = in.nextLine().trim();
        }
        // firstname is compulsory
        System.out.print("Firstname: ");
        String firstname = in.nextLine();
        while (firstname.isEmpty()) {
            System.out.print("Ooops! Firstname must be provided: ");
            firstname = in.nextLine().trim();
        }
        System.out.print("Career Summary: ");
        String careerSummary = in.nextLine();
        if (careerSummary.contains(",")) {
            careerSummary = "\"" + careerSummary + "\"";
        }
        System.out.print("Age: ");
        String ageString = in.nextLine();
        // age is compulsory
        while (true) {
            // if age is not provided
            if (ageString.isEmpty()) {
                System.out.print("Ooops! Age must be provided: ");
            } else if (isStringIn(ageString, 18, 100)) {
                break;
            } else {
                // if age is not in 18-100 or not a number
                System.out.print("Ooops! A valid age between 18 and 100 must be provided: ");
            }
            ageString = in.nextLine().trim();
        }
        int age = Integer.parseInt(ageString);
        System.out.print("Gender: ");
        String gender = in.nextLine().trim();
        while (true) {
            // not provide
            if (gender.isEmpty()) {
                break;
            } else if (gender.equals("male") || gender.equals("female") || gender.equals("other")) {
                break;
            } else {
                // not a valid gender
                System.out.print("Invalid input! Please specify Gender: ");
                gender = in.nextLine().trim();
            }
        }
        System.out.print("Highest Degree: ");
        String highestDegree = in.nextLine().trim();
        while (true) {
            // not provide
            if (highestDegree.isEmpty()) {
                break;
            } else if (highestDegree.equals("PHD") || highestDegree.equals("Master")
                    || highestDegree.equals("Bachelor")) {
                break;
            } else {
                // not a valid degree
                System.out.print("Invalid input! Please specify Highest Degree: ");
                highestDegree = in.nextLine().trim();
            }
        }
        System.out.println("Coursework: ");
        System.out.print("- COMP90041: ");
        String COMP90041String = in.nextLine().trim();
        while (true) {
            if (isStringIn(COMP90041String, 48, 101) || COMP90041String.isEmpty()) {
                // not provide or valid
                break;
            } else {
                // not a valid score
                System.out.print("Invalid input! Please specify Coursework: ");
                COMP90041String = in.nextLine().trim();
            }
        }
        System.out.print("- COMP90038: ");
        String COMP90038String = in.nextLine().trim();
        while (true) {
            if (isStringIn(COMP90038String, 48, 101) || COMP90038String.isEmpty()) {
                // not provide or valid
                break;
            } else {
                // not a valid score
                System.out.print("Invalid input! Please specify Coursework: ");
                COMP90038String = in.nextLine().trim();
            }
        }
        System.out.print("- COMP90007: ");
        String COMP90007String = in.nextLine().trim();
        while (true) {
            if (isStringIn(COMP90007String, 48, 101) || COMP90007String.isEmpty()) {
                // not provide or valid
                break;
            } else {
                // not a valid score
                System.out.print("Invalid input! Please specify Coursework: ");
                COMP90007String = in.nextLine().trim();
            }
        }
        System.out.print("- INFO90002: ");
        String INFO90002String = in.nextLine().trim();
        while (true) {
            if (isStringIn(INFO90002String, 48, 101) || INFO90002String.isEmpty()) {
                // not provide or valid
                break;
            } else {
                // not a valid score
                System.out.print("Invalid input! Please specify Coursework: ");
                INFO90002String = in.nextLine().trim();
            }
        }
        System.out.print("Salary Expectations ($ per annum): ");
        String salaryExpectationsString = in.nextLine().trim();
        while (true) {
            if (salaryExpectationsString.matches("[0-9]*")) {
                if (salaryExpectationsString.isEmpty()) {
                    break;
                } else if (Integer.parseInt(salaryExpectationsString) > 0) {
                    break;
                }
            }
            System.out.print("Invalid input! Please specify Salary Expectations: ");
            salaryExpectationsString = in.nextLine().trim();
        }
        System.out.print("Availability: ");
        String availabilityString = in.nextLine().trim();
        while (true) {
            if (isInFuture(availabilityString) || availabilityString.isEmpty()) {
                break;
            } else {
                // not a valid score
                System.out.print("Invalid input! Please specify Availability: ");
                availabilityString = in.nextLine().trim();
            }
        }
        Date availability = null;
        if (!availabilityString.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            availability = dateFormat.parse(availabilityString);
        }
        profile = new Profile(lastname, firstname, careerSummary, age, gender, highestDegree,
                COMP90041String, COMP90038String, COMP90007String, INFO90002String,
                salaryExpectationsString, availability);
        // System.out.print(profile.toDisplayString());
        writeCSV(profileFile, profile.toStoreString());
    }

    /**
     * check the given {@code availabilityString} is a not a past time
     * 
     * @param availabilityString a {@code String} waiting for being analysed
     * @return {@code true} when the given {@code availabilityString} is in the
     *         future or is today
     */
    private boolean isInFuture(String availabilityString) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        try {
            Date startDate = dateFormat.parse(availabilityString);
            if (!startDate.before(new Date())) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    /***
     * auxiliary function helps analyse string in profile
     * 
     * @param s   a {@code String} to analyse
     * @param low lower bound of the section
     * @param up  upper bound of the section
     * @return true if the {@code String} can be transformed into {@code Integer}
     *         and is in the given section (low,up)
     */
    private boolean isStringIn(String s, int low, int up) {
        try {
            int number = Integer.parseInt(s);
            if (number > low && number < up) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
