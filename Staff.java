import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public abstract class Staff {

    // protected so that HR Applicant Audit can visit

    /** an {@code ArrayList} of {@code Job} to store the jobs available */
    protected ArrayList<Job> jobs = new ArrayList<>();

    /** an {@code ArrayList} of {@code Profile} to store the profiles received */
    protected ArrayList<Profile> profiles = new ArrayList<>();

    /** a pathname {@code String} of job file */
    protected String jobFile;

    /** a pathname {@code String} of profile file */
    protected String profileFile;

    /** empty constructor */
    public Staff() {

    }

    /**
     * constructor with file path
     * 
     * @param jobFile     a pathname {@code String}
     * @param profileFile a pathname {@code String}
     */
    public Staff(String jobFile, String profileFile) {
        welcome();
        this.jobFile = jobFile;
        this.profileFile = profileFile;
        readJobFile(jobFile);
        readProfileFile(profileFile);
    }

    /** display the welcome part of certain role */
    protected abstract void welcome();

    /**
     * the user entrance
     * 
     * @param in the source of the system input
     * @throws ParseException                 when creating job/profile, date can't
     *                                        be parsed
     * @throws IOException                    file not found
     * @throws InvalidDataFormatException     when reading job/profile, data in
     *                                        fields doesn't follow the format
     * @throws InvalidCharacteristicException when reading job/profile, data in
     *                                        fields is invalid
     */
    protected void invoke(Scanner in)
            throws ParseException, IOException, InvalidDataFormatException, InvalidCharacteristicException {
        command(in);
    }

    /**
     * execute the input command
     * 
     * @param in Source input
     * @throws ParseException                 when creating job/profile, date can't
     *                                        be parsed
     * @throws IOException                    file not found
     * @throws InvalidDataFormatException     when reading job/profile, data in
     *                                        fields doesn't follow the format
     * @throws InvalidCharacteristicException when reading job/profile, data in
     *                                        fields is invalid
     * 
     */
    protected abstract void command(Scanner in)
            throws ParseException, IOException, InvalidDataFormatException, InvalidCharacteristicException;

    /**
     * read the profile from the {@code path} and set {@code profiles}
     * 
     * @param path the name of file to read and store
     * 
     */
    protected void readProfileFile(String path) {
        // default path
        if (path.equals("")) {
            path = "applications.csv";
            profileFile = path;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            // first line doesn't contain any job
            String line = reader.readLine();

            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                // split the line and get the info
                // System.out.println(line);

                // if line contains "" means careerSummary has ,
                if (line.contains("\"")) {
                    try {
                        ArrayList<String> info = splitProfileLine(line, lineNum);
                        analyseProfileLine(info, lineNum);
                    } catch (InvalidDataFormatException | InvalidCharacteristicException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (NumberFormatException | ParseException e) {
                        System.out.println(
                                "WARNING: invalid number format in applications file in line " + lineNum);
                        continue;
                    }
                }
                // if line is standard csv
                else {
                    try {
                        // if string endswith ,
                        // split won't work
                        boolean isModified = false;
                        if (line.endsWith(",")) {
                            line = line + "1";
                            isModified = true;
                        }
                        List<String> tempInfo = Arrays.asList(line.split(","));
                        ArrayList<String> info = new ArrayList<String>(tempInfo);
                        if (info.size() != 13) {
                            throw new InvalidDataFormatException(
                                    "WARNING: invalid data format in jobs file in line " + lineNum);
                        }
                        if (isModified) {
                            info.set(12, "");
                        }
                        analyseProfileLine(info, lineNum);
                    } catch (InvalidDataFormatException | InvalidCharacteristicException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (NumberFormatException | ParseException e) {
                        System.out.println(
                                "WARNING: invalid number format in applications file in line " + lineNum);
                        continue;
                    }
                }
                // System.out.println(applications.size());
            }

        } catch (IOException e) {
        }
    }

    /**
     * add a new {@code Profile} into {@code profiles}
     * 
     * @param info    an {@code ArrayList} of {@code String} to make up the
     *                information of the profile
     * @param lineNum an {@code Integer} that shows which line is reading,
     *                used when an exception occurs
     * @throws InvalidCharacteristicExceptionwhen reading profile, data in
     *                                            fields is invalid
     * @throws ParseException                     when creating profile, date
     *                                            can't be parsed
     */
    protected void analyseProfileLine(ArrayList<String> info, int lineNum)
            throws InvalidCharacteristicException, ParseException {
        String createdAtString = info.get(0).trim();
        long createdAt = 0;
        if (createdAtString.equals("")) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in applications file in line " + lineNum);
        } else {
            createdAt = Long.parseLong(info.get(0));
        }
        String lastname = info.get(1).trim();
        // if no name is given
        if (lastname.equals("")) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in applications file in line " + lineNum);
        }
        String firstname = info.get(2).trim();
        // if no name is given
        if (firstname.equals("")) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in applications file in line " + lineNum);
        }

        String careerSummary = info.get(3).trim();
        int age = Integer.parseInt(info.get(4).trim());
        if (age <= 18 || age >= 100) {
            // check age is valid
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in applications file in line " + lineNum);
        }
        String gender = info.get(5).trim();
        // if gender is not "male"/"female"/"other"/"" throw an exception
        if (!(gender.equals("male") || gender.equals("female")
                || gender.equals("other") || gender.equals(""))) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in applications file in line " + lineNum);
        }
        String highestDegree = info.get(6).trim();
        // if highestDegree is not ""/"Bachelor"/"Master"/"PHD" throw an exception
        if (!(highestDegree.equals("") || highestDegree.equals("Bachelor")
                || highestDegree.equals("Master") || highestDegree.equals("PHD"))) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in applications file in line " + lineNum);
        }
        int COMP90041 = 0;
        String COMP90041String = info.get(7).trim();
        // if this field has data
        if (!COMP90041String.equals("")) {
            COMP90041 = Integer.parseInt(COMP90041String);
            // check the data is valid
            if (COMP90041 < 49 || COMP90041 > 100) {
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in applications file in line " + lineNum);
            }
        }
        int COMP90038 = 0;
        String COMP90038String = info.get(8).trim();
        // if this field has data
        if (!COMP90038String.equals("")) {
            COMP90038 = Integer.parseInt(COMP90038String);
            // check the data is valid
            if (COMP90038 < 49 || COMP90038 > 100) {
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in applications file in line " + lineNum);
            }
        }
        int COMP90007 = 0;
        String COMP90007String = info.get(9).trim();
        // if this field has data
        if (!COMP90007String.equals("")) {
            COMP90007 = Integer.parseInt(COMP90007String);
            // check the data is valid
            if (COMP90007 < 49 || COMP90007 > 100) {
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in applications file in line " + lineNum);
            }
        }
        int INFO90002 = 0;
        String INFO90002String = info.get(10).trim();
        // if this field has data
        if (!INFO90002String.equals("")) {
            INFO90002 = Integer.parseInt(INFO90002String);
            // check the data is valid
            if (INFO90002 < 49 || INFO90002 > 100) {
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in applications file in line " + lineNum);
            }
        }

        String salaryExpectationsString = info.get(11).trim();
        // if this field has data
        if (!salaryExpectationsString.equals("")) {
            // check the data is valid
            int salaryExpectations = Integer.parseInt(salaryExpectationsString);
            if (salaryExpectations <= 0) {
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in applications file in line " + lineNum);
            }
        }
        // transfor the string to date format in dd/mm/yy
        String date = info.get(12).trim();
        Date availability = null;
        if (!date.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            try {
                // if string is a valid date
                dateFormat.setLenient(false);
                availability = dateFormat.parse(date);
                Date now = new Date();
                now = dateFormat.parse(dateFormat.format(now));
                // if availablility is not after now
                if (availability.compareTo(now) < 0) {
                    throw new InvalidCharacteristicException(
                            "WARNING: invalid characteristic in applications file in line " + lineNum);
                }
            } catch (Exception e) {
                // if string is not a valid date
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in applications file in line " + lineNum);
            }

        }
        profiles.add(
                new Profile(createdAt, lastname, firstname, careerSummary, age, gender, highestDegree,
                        COMP90041String, COMP90038String, COMP90007String, INFO90002String,
                        salaryExpectationsString, availability));
    }

    /**
     * split a {@code String} into 13 different data field to build up a
     * {@code Profile}
     * 
     * @param line      a {@code String} contains the information of the profile
     * @param lineNuman {@code Integer} that shows which line is reading,
     *                  used when an exception occurs
     * @return an {@code ArrayList} of {@code String}, the standard size of
     *         {@code ArrayList} is 13, each {@code String} is a part of data field
     * @throws InvalidDataFormatException when reading profile, data in
     *                                    fields doesn't follow the format
     */
    protected ArrayList<String> splitProfileLine(String line, int lineNum) throws InvalidDataFormatException {
        ArrayList<String> info = new ArrayList<>();
        boolean isModified = false;
        if (line.endsWith(",")) {
            line = line + "0";
            isModified = true;
        }
        String[] originalInfo = line.split(",");
        // check if field count is less 13
        if (originalInfo.length < 13) {
            throw new InvalidDataFormatException(
                    "WARNING: invalid data format in applications file in line " + Integer.toString(lineNum));
        }
        // only the careerSummary field can contain one pair of ""
        String careerSummary = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
        // replace the part with empty
        line = line.replace("\"" + careerSummary + "\"", "");

        String[] remainInfo = line.split(",");
        // remaining parts should be 13 fields
        if (remainInfo.length != 13) {
            throw new InvalidDataFormatException(
                    "WARNING: invalid data format in applications file in line " + Integer.toString(lineNum));
        }
        for (int i = 0; i < 3; i++) {
            info.add(remainInfo[i]);
        }
        info.add(careerSummary);
        for (int i = 4; i < remainInfo.length - 1; i++) {
            info.add(remainInfo[i]);
        }
        // last one is special
        if (isModified) {
            info.add("");
        } else {
            info.add(remainInfo[remainInfo.length - 1]);
        }
        return info;
    }

    /**
     * read the job from the {@code path} and set {@code jobs}
     * 
     * @param path the name of file to read and store
     * 
     */
    protected void readJobFile(String path) {
        // default path
        if (path.equals("")) {
            path = "jobs.csv";
            jobFile = path;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            // first line doesn't contain any job
            String line = reader.readLine();

            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                // split the line and get the info
                // System.out.println(line);

                // if line contains "" means careerSummary has ,
                if (line.contains("\"")) {
                    try {
                        ArrayList<String> info = splitJobLine(line, lineNum);
                        analyseJobLine(info, lineNum);
                    } catch (InvalidDataFormatException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "WARNING: invalid number format in jobs file in line " + lineNum);
                        continue;
                    } catch (InvalidCharacteristicException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (ParseException e) {
                        System.out.println(
                                "WARNING: invalid number format in jobs file in line " + lineNum);
                        continue;
                    }
                }
                // if line is standard csv
                else {
                    try {
                        // if string endswith ,
                        // split won't work
                        boolean isModified = false;
                        if (line.endsWith(",")) {
                            line = line + "1";
                            isModified = true;
                        }
                        List<String> tempInfo = Arrays.asList(line.split(","));
                        ArrayList<String> info = new ArrayList<String>(tempInfo);
                        if (info.size() != 6) {
                            throw new InvalidDataFormatException(
                                    "WARNING: invalid data format in jobs file in line " + lineNum);
                        }
                        if (isModified) {
                            info.set(5, "");
                        }
                        analyseJobLine(info, lineNum);
                    } catch (InvalidDataFormatException | InvalidCharacteristicException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (NumberFormatException | ParseException e) {
                        System.out.println(
                                "WARNING: invalid number format in jobs file in line " + lineNum);
                        continue;
                    }
                }
                // System.out.println(jobs.size());
            }
        } catch (IOException e) {
        }
    }

    /**
     * split a {@code String} into 6 different data field to build up a job
     * 
     * @param line      a {@code String} contains the information of the job
     * @param lineNuman {@code Integer} that shows which line is reading,
     *                  used when an exception occurs
     * @return an {@code ArrayList} of {@code String}, the standard size of
     *         {@code ArrayList} is 6, each {@code String} is a part of data field
     * @throws InvalidDataFormatException when reading job, data in
     *                                    fields doesn't follow the format
     */
    protected ArrayList<String> splitJobLine(String line, int lineNum) throws InvalidDataFormatException {
        ArrayList<String> info = new ArrayList<>();
        // if string endswith ,
        // split won't work
        boolean isModified = false;
        if (line.endsWith(",")) {
            line = line + "0";
            isModified = true;
        }
        String[] originalInfo = line.split(",");
        // check if field count is less than 6
        if (originalInfo.length < 6) {
            throw new InvalidDataFormatException(
                    "WARNING: invalid data format in jobs file in line " + Integer.toString(lineNum));
        }
        // only the title field can contain one pair of ""
        String title = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
        // replace the part with empty
        line = line.replace("\"" + title + "\"", "");

        String[] remainInfo = line.split(",");
        // remaining info should be 5 part
        if (remainInfo.length != 6) {
            throw new InvalidDataFormatException(
                    "WARNING: invalid data format in jobs file in line " + Integer.toString(lineNum));
        }

        info.add(remainInfo[0]);
        info.add(title);
        for (int i = 2; i < remainInfo.length - 1; i++) {
            info.add(remainInfo[i]);
        }
        // last one is special
        if (isModified) {
            info.add("");
        } else {
            info.add(remainInfo[remainInfo.length - 1]);
        }

        return info;
    }

    /**
     * add a new {@code Job} into {@code jobs}
     * 
     * @param info    an {@code ArrayList} of {@code String} to make up the
     *                information of the job
     * @param lineNum an {@code Integer} that shows which line is reading,
     *                used when an exception occurs
     * @throws InvalidCharacteristicExceptionwhen reading job, data in
     *                                            fields is invalid
     * @throws ParseException                     when creating job, date
     *                                            can't be parsed
     */
    protected void analyseJobLine(ArrayList<String> info, int lineNum)
            throws InvalidCharacteristicException, ParseException {
        String createdAtString = info.get(0).trim();
        long createdAt = 0;
        if (createdAtString.equals("")) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in jobs file in line " + lineNum);
        } else {
            createdAt = Long.parseLong(info.get(0));
        }
        String title = info.get(1).trim();
        if (title.equals("")) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in jobs file in line " + lineNum);
        }
        String description = info.get(2).trim();
        String degree = info.get(3).trim();
        if (!(degree.equals("") || degree.equals("Bachelor")
                || degree.equals("Master") || degree.equals("PHD"))) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in jobs file in line " + lineNum);
        }
        String salaryString = info.get(4).trim();
        if (!salaryString.equals("")) {
            // check the data is valid
            int salary = Integer.parseInt(salaryString);
            if (salary <= 0) {
                throw new InvalidCharacteristicException(
                        "WARNING: invalid characteristic in jobs file in line " + lineNum);
            }
        }
        String startDateString = info.get(5).trim();
        if (!isDateValid(startDateString)) {
            throw new InvalidCharacteristicException(
                    "WARNING: invalid characteristic in jobs file in line " + lineNum);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date startDate = dateFormat.parse(startDateString);
        jobs.add(new Job(createdAt, title, description, degree, salaryString, startDate));
    }

    /**
     * check whether the given {@code startDateString} is a valid date
     * 
     * @param startDateString A {@code String} should be checked valid
     * @return {@code true} if startDateString is a valid date
     */
    protected boolean isDateValid(String startDateString) {
        boolean formatFlag = startDateString.matches("[0-9][0-9]/[0-9][0-9]/[0-9][0-9]");
        if (!formatFlag) {
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        try {
            Date startDate = dateFormat.parse(startDateString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * wirte the line into the file
     * 
     * @param filepath the pathname {@code String} of file to write
     * @param csvline  the {@code String} should be written in the file
     * @throws IOException when filepath doesn't exist
     */
    protected void writeCSV(String filepath, String csvline) throws IOException {
        File file = new File(filepath);
        // System.out.println(csvline);
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.append(csvline + "\n");
        bufferedWriter.close();
    }

    /** list the jobs available */
    protected abstract void listJobs();

    /** list the applicants in the file */
    protected void listApplicant() {
        if (profiles.size() == 0) {
            System.out.println("No applicants available.");
        } else {
            ArrayList<Profile> orderedProfiles = Profile.Order(profiles, "availability");
            for (int x = 0; x < orderedProfiles.size(); x++) {
                System.out.print("[" + (x + 1) + "] ");
                System.out.print(orderedProfiles.get(x).toDisplayString());
            }
        }
    }

    /**
     * matching algorithm for HR.
     * Priority:
     * degree -> availability -> createdAt
     * 
     * @param submission     a {@code Submission}, to get the applications and jobs
     * @param matchedHistory a {@code Map} whose key is {@code Profile} and the
     *                       value is {@code Boolean}
     * @return a {@code Map} whose key is a {@code String} built by the
     *         {@code Job} and the value is the most suitable {@code Profile} of the
     *         current {@code Job}
     */
    protected Map<String, Profile> match(Submission submission, Map<Profile, Boolean> matchedHistory) {
        Map<String, Profile> storage = new HashMap<>();
        Map<String, ArrayList<Profile>> submissionMap = new HashMap<>();
        // copy the data not the reference
        submission.getSubmissionMap().forEach((k, v) -> submissionMap.put(k, new ArrayList<>(v)));
        for (int i = 0; i < jobs.size(); i++) {
            if (submissionMap.get(jobs.get(i).toDisplayString()) == null) {
                continue;
            }
            ArrayList<Profile> candidates = submissionMap.get(jobs.get(i).toDisplayString());
            if (candidates.size() != 0) {
                // delete all the candidates who have lower degree
                for (int j = 0; j < candidates.size();) {
                    // if degree is lower than demand
                    if (jobs.get(i).getDegreeNumber() > candidates.get(j).getDegreeNumber()) {
                        candidates.remove(j);
                    } else {
                        j++;
                    }
                }
                // left at most one
                if (candidates.size() <= 1) {
                    // first apply first matched
                    if (candidates.size() != 0) {
                        storage.put(jobs.get(i).toDisplayString(), candidates.get(0));
                    }
                    continue;
                }
                // in case all candidates' availablity are null
                ArrayList<Profile> backupCandidates = new ArrayList<>();
                // remain all the candidates who have the most recent date
                for (int j = 0; j < candidates.size();) {
                    if (candidates.get(j).getAvailability() == null) {
                        backupCandidates.add(candidates.get(j));
                        candidates.remove(j);
                    } else {
                        j++;
                    }
                }
                if (candidates.size() == 0) {
                    candidates.add(backupCandidates.get(0));
                    continue;
                }
            }
            // first apply first matched
            if (candidates.size() != 0) {
                storage.put(jobs.get(i).toDisplayString(), candidates.get(0));
            }
        }
        return storage;
    }

    /**
     * Matching algorithm for {@code Audit}.
     * {@code Audit} assumes all the applicants have applied all the jobs.
     * Priority:
     * degree -> age -> date -> createAt
     * 
     * @param jobs         an {@code ArrayList} of jobs
     * @param tempProfiles an {@code ArrayList} of prorfiles
     * @return A {@code Map} which key is the profiles and value is the boolean.
     *         The size of the map is the same as profiles size.
     *         {@code true} when current profile has sucessfully matched a job.
     */
    protected Map<Profile, Boolean> match(ArrayList<Job> jobs, ArrayList<Profile> profiles) {
        Map<Profile, Boolean> storage = new HashMap<>();
        ArrayList<Profile> tempProfiles = new ArrayList<>(profiles);
        // initialize all as false (no one has successfully matched)
        for (int i = 0; i < tempProfiles.size(); i++) {
            storage.put(tempProfiles.get(i), false);
        }
        for (int indexOfJobs = 0; indexOfJobs < jobs.size(); indexOfJobs++) {
            // store the candidates in an arraylist
            ArrayList<Profile> candidates = new ArrayList<>();

            // every time looking for a new job's candidates clear the last job's candidates
            candidates.clear();
            for (int indexOfProfiles = 0; indexOfProfiles < tempProfiles.size(); indexOfProfiles++) {
                // if current profile has matched a job
                if (storage.get(tempProfiles.get(indexOfProfiles)) == true) {
                    continue;
                }
                if (jobs.get(indexOfJobs).getDegreeNumber() <= tempProfiles.get(indexOfProfiles).getDegreeNumber()) {
                    candidates.add(tempProfiles.get(indexOfProfiles));
                }
            }
            while (true) {
                // remain candidates gender the same
                boolean flagAge = false;
                for (int indexOfCandidates = 0; indexOfCandidates < candidates.size() - 1; indexOfCandidates++) {
                    if (candidates.get(indexOfCandidates).getAge() > candidates.get(indexOfCandidates + 1).getAge()) {
                        candidates.remove(indexOfCandidates);
                        flagAge = true;
                        break;
                    } else if (candidates.get(indexOfCandidates).getAge() < candidates.get(indexOfCandidates + 1)
                            .getAge()) {
                        candidates.remove(indexOfCandidates + 1);
                        flagAge = true;
                        break;
                    }
                }
                if (flagAge) {
                    continue;
                }
                // choose those who can work early
                boolean flagDate = false;
                candidates = Profile.Order(candidates, "availability");
                for (int indexOfCandidates = 0; indexOfCandidates < candidates.size() - 1; indexOfCandidates++) {
                    if (candidates.get(indexOfCandidates).getAvailability() == null) {
                        flagDate = true;
                        break;
                    }
                    if (candidates.get(indexOfCandidates + 1).getAvailability() == null) {
                        flagDate = true;
                        break;
                    }
                    if (candidates.get(indexOfCandidates).getAvailability()
                            .equals(candidates.get(indexOfCandidates + 1).getAvailability())) {
                        continue;
                    }
                }
                if (flagDate) {
                    continue;
                }
                candidates = Profile.Order(candidates, "createdAt");
                break;
            }
            if (candidates.size() != 0) {
                storage.put(candidates.get(0), true);
            }
        }
        return storage;
    }
}
