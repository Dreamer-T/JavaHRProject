import java.util.Scanner;

public class HRAssistant {

    /**
     * main part of the system
     * 
     * @param args {@code String[]} from terminal
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        HRAssistant hrAssistant = new HRAssistant();
        try {
            hrAssistant.commandline(args, input);
            // for(int i=0;i<args.length;i++){
            // System.out.print(args[i]+" ");
            // }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            hrAssistant.help();
        }
    }

    /**
     * execute the commands one by one
     * execution order -a -j -r
     * 
     * @param args  a {@code String[]} of the commands
     * @param input the source of input
     * @throws Exception
     */
    public void commandline(String[] args, Scanner input) throws Exception {
        // no command line show the help and exit
        if (args.length == 0) {
            help();
            return;
        }
        // if -h or --help is in command line, just show the help
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--help") || args[i].equals("-h")) {
                help();
                return;
            }
        }

        // if any exception occurs
        boolean exception = false;

        // get the application file path first
        String applicationFile = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--applications") || args[i].equals("-a")) {
                // if -a has a valid argument
                if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                    applicationFile = args[i + 1];
                    break;
                } else {
                    exception = true;
                }
            }
        }
        if (exception) {
            help();
            return;
        }
        // reset the flag
        exception = false;

        // get the job file path
        String jobFile = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--jobs") || args[i].equals("-j")) {
                // if -a has a valid argument
                if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                    jobFile = args[i + 1];
                    break;
                } else {
                    exception = true;
                }
            }
        }
        if (exception) {
            help();
            return;
        }

        // if staff is applicant, num=0;
        // if staff is hr, num=1;
        // if staff is audit, num=2;
        int staffNum = -1;
        String staff = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--role") || args[i].equals("-r")) {
                // if -r has a valid argument
                if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                    staff = args[i + 1];
                } else {
                    throw new Exception("ERROR: no role defined.");
                }
            }
        }
        switch (staff) {
            case "applicant":
                staffNum = 0;
                break;
            case "hr":
                staffNum = 1;
                break;
            case "audit":
                staffNum = 2;
                break;
            default:
                staffNum = -1;
                break;
        }
        // System.out.println(jobFile);
        switch (staffNum) {
            case 0:
                Applicant applicant = new Applicant(jobFile, applicationFile);
                applicant.invoke(input);
                break;
            case 1:
                HR hr = new HR(jobFile, applicationFile);
                hr.invoke(input);
                break;
            case 2:
                Audit audit = new Audit(jobFile, applicationFile);
                audit.invoke(input);
                break;
            case -1:
                throw new Exception("ERROR: " + staff + " is not a valid role.");
        }
    }

    /** help command result */
    public void help() {
        System.out.println("HRAssistant - COMP90041 - Final Project\n");
        System.out.println("Usage: java HRAssistant [arguments]\n");
        System.out.println("Arguments:");
        System.out.println("    -r or --role            Mandatory: determines the user's role");
        System.out.println("    -a or --applications    Optional: path to applications file");
        System.out.println("    -j or --jobs            Optional: path to jobs file");
        System.out.println("    -h or --help            Optional: print Help (this message) and exit");
    }
}
