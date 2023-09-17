import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Submission extends Staff {

    /** the path of the file of the job-applicant location */
    public String PATH = "submission.txt";

    /**
     * key is jobs' {@code displayString}, value is an {@code ArrayList} profile
     */
    protected Map<String, ArrayList<Profile>> submissionMap = new HashMap<>();

    /**
     * default constructor
     * 
     * @throws IOException
     * @throws InvalidDataFormatException
     * @throws InvalidCharacteristicException
     * @throws ParseException
     */
    public Submission() throws IOException, InvalidDataFormatException, InvalidCharacteristicException, ParseException {
        readSubmission();
    }

    /**
     * @return A {@code Map}, key is a {@code String} in jobs' displayString, value
     *         is an {@code ArrayList} profile
     */
    public Map<String, ArrayList<Profile>> getSubmissionMap() {
        return submissionMap;
    }

    /**
     * read the job-applicant file, the format of the file is that
     * odd number line is job, even number line is profile,
     * line number start from 1
     * 
     * @throws IOException
     * @throws InvalidDataFormatException
     * @throws InvalidCharacteristicException
     * @throws ParseException
     */
    public void readSubmission()
            throws IOException {
        File file = new File(PATH);
        if (file.createNewFile()) {
            return;
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        // linenum start from 1
        int lineNum = 1;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            // System.out.println(lineNum);
            try {
                if (lineNum % 2 != 0) {
                    // when linenum is odd number, the line is job
                    // System.out.println(line);
                    if (line.contains("\"")) {
                        ArrayList<String> info = splitJobLine(line, lineNum);
                        analyseJobLine(info, lineNum);
                    } else {
                        boolean isModified = false;
                        if (line.endsWith(",")) {
                            line = line + "1";
                            isModified = true;
                        }
                        List<String> tempInfo = Arrays.asList(line.split(","));
                        ArrayList<String> info = new ArrayList<String>(tempInfo);
                        if (isModified) {
                            info.set(5, "");
                        }
                        analyseJobLine(info, lineNum);
                    }
                } else {
                    // when linenum is even number, the line is profile
                    // System.out.println(line);
                    if (line.contains("\"")) {
                        ArrayList<String> info = splitProfileLine(line, lineNum);
                        analyseProfileLine(info, lineNum);
                    }
                    // if line is standard csv
                    else {
                        // if string endswith ,
                        // split won't work
                        boolean isModified = false;
                        if (line.endsWith(",")) {
                            line = line + "1";
                            isModified = true;
                        }
                        List<String> tempInfo = Arrays.asList(line.split(","));
                        ArrayList<String> info = new ArrayList<String>(tempInfo);

                        if (isModified) {
                            info.set(12, "");
                        }
                        analyseProfileLine(info, lineNum);
                    }
                    ArrayList<Profile> profile = submissionMap.get(jobs.get((lineNum - 1) / 2).toDisplayString());
                    // if current job has no application before this one applies
                    if (profile == null) {
                        profile = new ArrayList<>();
                        int lastProfileIndex = profiles.size() - 1;
                        int lastJobIndex = jobs.size() - 1;
                        profile.add(profiles.get(lastProfileIndex));
                        submissionMap.put(jobs.get(lastJobIndex).toDisplayString(), profile);
                    } else {
                        int lastProfileIndex = profiles.size() - 1;
                        int lastJobIndex = jobs.size() - 1;
                        submissionMap.get(jobs.get(lastJobIndex).toDisplayString())
                                .add(profiles.get(lastProfileIndex));
                    }
                }
                lineNum++;
            } catch (Exception e) {
                lineNum++;
                continue;
            }

        }
    }

    /**
     * used when the applicant has chosen a job
     * 
     * @param job     the chosen {@code Job}
     * @param profile the applicant's {@code Profile}
     * @throws IOException
     */
    public void storeSubmission(String job, String profile) throws IOException {
        File file = new File(PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.append(job + "\n");
        bufferedWriter.append(profile + "\n");
        bufferedWriter.close();
    }

    /** never used */
    @Override
    public void welcome() {
    }

    /** never used */
    @Override
    protected void command(Scanner in) throws ParseException, IOException {
    }

    /** never used */
    @Override
    protected void listJobs() {
    }
}
