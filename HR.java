import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HR extends Staff {

    /** a {@code Submission} storing all the jobs and their applicants */
    private Submission submission;

    /**
     * a {@code HashMap} whose key is the {@code Profile} and the value is the
     * {@code Boolean}. {@code true} when the {@code Profile} has successfully
     * matched a job
     */
    Map<Profile, Boolean> matchedHistory = new HashMap<>();

    /** empty constructor */
    public HR() {
    }

    /**
     * constructor with file path
     * 
     * @param jobFile     a pathname {@code String}
     * @param profileFile a pathname {@code String}
     */
    public HR(String jobFile, String profileFile) {
        super(jobFile, profileFile);
        try {
            submission = new Submission();
        } catch (IOException e) {
        } catch (InvalidDataFormatException e) {
        } catch (InvalidCharacteristicException e) {
        } catch (ParseException e) {
        }
    }

    /** {@inheritDoc} */
    public void welcome() {
        System.out.println(
                "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo\n" +
                        ".d88888b           dP                                             dP          \n"
                        + "88.    \"\'          88                                             88          \n" +
                        "`Y88888b. .d8888b. 88d888b. 88d8b.d8b. .d8888b. .d8888b. .d8888b. 88 .d8888b. \n" +
                        "      `8b 88\'  `\"\" 88\'  `88 88\'`88\'`88 88\'  `88 88\'  `88 88\'  `88 88 88ooood8 \n" +
                        "d8'   .8P 88.  ... 88    88 88  88  88 88.  .88 88.  .88 88.  .88 88 88.  ... \n" +
                        " Y88888P  `88888P' dP    dP dP  dP  dP `88888P\' `88888P\' `8888P88 dP `88888P' \n" +
                        "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooo~~~~.88~oooooooooooo\n"
                        + "                                                          d8888P              \n" +
                        " _   _ _____      ____    ____   ____  _   ____  _____  ____   __  _  _____ \n" +
                        "| |_| || () )    / () \\  (_ (_` (_ (_`| | (_ (_`|_   _|/ () \\ |  \\| ||_   _|\n" +
                        "|_| |_||_|\\_\\   /__/\\__\\.__)__).__)__)|_|.__)__)  |_| /__/\\__\\|_|\\__|  |_|  \n" +
                        "\n" +
                        "ooooooooo -------- Welcome to Schmoogle's Hiring Assistant --------  ooooooooo\n");
    }

    /**
     * to display the status, including the applications received
     */
    public void status() {
        int count = 0;
        if (submission.submissionMap == null) {
            count = 0;
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                if (submission.submissionMap.get(jobs.get(i).toDisplayString()) == null) {
                    continue;
                }
                count += submission.submissionMap.get(jobs.get(i).toDisplayString()).size();
            }
        }
        System.out.println(count + " applications received.");
    }

    /** display the menu */
    public void menu() {
        System.out.println("Please enter one of the following commands to continue:\n" +
                "- create new job: [create] or [c]\n" +
                "- list available jobs: [jobs] or [j]\n" +
                "- list applicants: [applicants] or [a]\n" +
                "- filter applications: [filter] or [f]\n" +
                "- matchmaking: [match] or [m]\n" +
                "- quit the program: [quit] or [q]");
        System.out.print("> ");
    }

    /** {@inheritDoc} */
    @Override
    protected void command(Scanner in) throws ParseException, IOException {
        status();
        menu();
        while (true) {
            String command = in.nextLine().trim();
            // System.out.print(command);
            if (command.equals("create") || command.equals("c")) {
                createJob(in);
            } else if (command.equals("jobs") || command.equals("j")) {
                listJobs();
            } else if (command.equals("applicants") || command.equals("a")) {
                listApplicant();
            } else if (command.equals("filter") || command.equals("f")) {
                filterInterface(in);
            } else if (command.equals("match") || command.equals("m")) {
                if (jobs.size() == 0) {
                    System.out.println("No jobs available.");
                } else if (profiles.size() == 0) {
                    System.out.println("No applicants available.");
                } else {
                    Map<String, Profile> storage = match(submission, matchedHistory);
                    listJobs(storage);
                }
            } else if (command.equals("quit") || command.equals("q")) {
                System.out.println();
                return;
            } else {
                System.out.print("Invalid input! Please enter a valid command to continue: \n> ");
                continue;
            }
            status();
            menu();
        }
    }

    /**
     * List the jobs followed by applications.
     * Applications ordered by submission time
     * 
     */
    protected void listJobs() {
        // if no job
        if (jobs.size() == 0) {
            System.out.println("No jobs available.");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                // list job info first
                System.out.println("[" + (i + 1) + "] " + jobs.get(i).toDisplayString());
                // if current job has applications
                if (submission.submissionMap.get(jobs.get(i).toDisplayString()) != null) {
                    // applcations ordered by submission time
                    // submission read it in submission time applications in submission are already
                    // ordered
                    for (int j = 0; j < submission.submissionMap.get(jobs.get(i).toDisplayString()).size(); j++) {
                        String orderString = "";
                        // give current application a code
                        // start from 'a' to 'z'
                        // and add number behind char
                        char currentOrder = (char) (j % 26 + 'a');
                        if (j / 26 == 0) {
                            orderString = currentOrder + "";
                        } else {
                            orderString = currentOrder + Integer.toString(j / 26);
                        }
                        // print the applications
                        System.out.print("   [" + orderString + "] " +
                                submission.submissionMap.get(jobs.get(i).toDisplayString()).get(j)
                                        .toDisplayString());
                    }
                }
            }
        }

    }

    /**
     * list the jobs after the matching
     * 
     * @param storage a {@code Map} store whose key is the {@code Profile} and the
     *                value is the {@code Boolean}. {@code true} when the
     *                {@code Profile} has successfully matched a job
     */
    private void listJobs(Map<String, Profile> storage) {
        for (int i = 0; i < jobs.size(); i++) {
            if (storage.get(jobs.get(i).toDisplayString()) != null) {
                // display job info first
                System.out.println("[" + (i + 1) + "] " + jobs.get(i).toDisplayString());
                System.out.print("    Applicant match: " +
                        storage.get(jobs.get(i).toDisplayString()).toDisplayString());
            }
        }
    }

    /**
     * the user interface of the filter
     * 
     * @param in the source of the system input
     */
    private void filterInterface(Scanner in) {
        System.out.print("Filter by: [lastname], [degree] or [wam]: ");
        String field = in.nextLine().trim();
        while (true) {
            if (field.equals("wam") || field.equals("degree") || field.equals("lastname")) {
                break;
            } else {
                System.out.print("Invalid input! Please specify filter: ");
                field = in.nextLine().trim();
            }
        }
        // System.out.println("start filtering");
        ArrayList<Profile> orderedProfiles = Profile.Order(profiles, field);
        for (int x = 0; x < orderedProfiles.size(); x++) {
            System.out.print("[" + (x + 1) + "] ");
            System.out.print(orderedProfiles.get(x).toDisplayString());
        }
    }

    /**
     * Create a job and store it in the current job file path
     * 
     * @param in Source of input
     * @throws ParseException if date cannot be parsed
     * @throws IOException    if the job file missing
     * 
     */
    private void createJob(Scanner in) throws ParseException, IOException {
        System.out.println("# Create new Job");

        System.out.print("Position Title: ");
        String title = in.nextLine().trim();
        // compulsory data field
        while (title.equals("")) {
            System.out.print("Ooops! Position Title must be provided: ");
            title = in.nextLine().trim();
        }

        System.out.print("Position Description: ");
        String description = in.nextLine().trim();

        System.out.print("Minimum Degree Requirement: ");
        String degree = in.nextLine().trim();
        while (true) {
            // if degree is empty or valid
            if (degree.equals("Bachelor") || degree.equals("Master")
                    || degree.equals("PHD") || degree.isEmpty()) {
                break;
            }
            System.out.print("Invalid input! Please specify Minimum Degree Requirement: ");
            degree = in.nextLine().trim();
        }

        System.out.print("Salary ($ per annum): ");
        String salaryString = in.nextLine().trim();
        while (true) {
            // if salary is emtpy or greater than 0
            if (salaryString.matches("[0-9]*")) {
                if (salaryString.isEmpty()) {
                    break;
                } else {
                    try {
                        int salary = Integer.parseInt(salaryString);
                        if (salary > 0) {
                            break;
                        }
                    } catch (Exception e) {
                    }

                }
            }
            System.out.print("Invalid input! Please specify Salary: ");
            salaryString = in.nextLine().trim();
        }

        System.out.print("Start Date: ");
        String startDateString;
        while (true) {
            startDateString = in.nextLine().trim();
            // compulsory data field
            if (startDateString.isEmpty()) {
                System.out.print("Ooops! Start Date must be provided: ");
                continue;
            }
            // if date is invalid
            if (isDateValid(startDateString)) {
                break;
            }
            System.out.print("Invalid input! Please specify Start Date: ");
        }

        // format the date with given format "dd/MM/yy"
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date startDate = dateFormat.parse(startDateString);

        // create a new job
        Job job = new Job(title, description, degree, salaryString, startDate);

        // add this new job to current job list
        jobs.add(job);

        // permenant store the job
        writeCSV(jobFile, job.toStoreString());
    }

}
