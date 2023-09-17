import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;

public class Audit extends Staff {

    /** empty constructor */
    public Audit() {
        super();
    }

    /**
     * constructor with files to read
     * 
     * @param jobFile     a pathname {@code String}
     * @param profileFile a pathname {@code String}
     */
    public Audit(String jobFile, String profileFile)
            throws IOException, InvalidDataFormatException, InvalidCharacteristicException, ParseException {
        super(jobFile, profileFile);
    }

    /**
     * Used for audit statistics.
     */
    public void statistics() {
        // System.out.println("start matching");
        Map<Profile, Boolean> storage = match(jobs, profiles);
        int countOfSuccess = 0;
        float countOfSuccessAge = 0;
        float countOfSuccessWAM = 0;
        float countOfAllAge = 0;
        float countOfAllWAM = 0;
        float ageOfAll = 0;
        float ageOfSuccess = 0;
        float wamOfSuccess = 0;
        float wamOfAll = 0;
        for (int i = 0; i < profiles.size(); i++) {
            ageOfAll += profiles.get(i).getAge();
            countOfAllAge++;
            wamOfAll += profiles.get(i).getWAM();
            if (profiles.get(i).getWAM() != 0) {
                countOfAllWAM++;
            }
            if (storage.get(profiles.get(i))) {
                countOfSuccess++;
                countOfSuccessAge++;
                ageOfSuccess += profiles.get(i).getAge();
                if (profiles.get(i).getWAM() != 0) {
                    countOfSuccessWAM++;
                }
            }
        }
        // none of the applicants provide the score
        if (wamOfAll == 0) {
            System.out.printf("Number of successful matches: %d\n" +
                    "Average age: %.2f (average age of all applicants: %.2f)\n" +
                    "Average WAM: n/a (average WAM of all applicants: n/a)", countOfSuccess,
                    (float) ageOfSuccess / countOfSuccessAge, (float) ageOfAll / countOfAllAge);
        } else {
            System.out.printf("Number of successful matches: %d\n" +
                    "Average age: %.2f (average age of all applicants: %.2f)\n" +
                    "Average WAM: %.2f (average WAM of all applicants: %.2f)", countOfSuccess,
                    (float) ageOfSuccess / countOfSuccessAge, (float) ageOfAll / countOfAllAge,
                    (float) wamOfSuccess / countOfSuccessWAM,
                    (float) wamOfAll / countOfAllWAM);
        }
        System.out.println();
        genderPercentage(storage);
        degreePercentage(storage);
    }

    /** {@inheritDoc} */
    @Override
    protected void welcome() {
        System.out.println("======================================\n" +
                "# Matchmaking Audit\n" +
                "======================================");
    }

    /** {@inheritDoc} */
    @Override
    protected void command(Scanner in)
            throws ParseException, IOException, InvalidDataFormatException, InvalidCharacteristicException {
        if (jobs.size() == 0) {
            System.out.println("No jobs available for interrogation.");
            return;
        } else if (profiles.size() == 0) {
            System.out.println("No applicants available for interrogation.");
            return;
        } else {
            System.out.println("Available jobs: " + jobs.size());
            System.out.println("Total number of applicants: " + profiles.size());
            // System.exit(0);
            statistics();
        }
    }

    /** never used in Audit */
    @Override
    protected void listJobs() {
    }

    /**
     * calculate the percentage of the different degree among the matched applicants
     * 
     * @param storage a {@code Map} whose key is the {@code Profile} and the value
     *                is a {@code Boolean}. {@code Boolean} is {@code true} when the
     *                current profile is successfully matched a job
     */
    private void degreePercentage(Map<Profile, Boolean> storage) {
        int PHDTotal = 0;
        int PHDMatched = 0;
        int masterTotal = 0;
        int masterMatched = 0;
        int bachelorTotal = 0;
        int bachelorMatched = 0;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getHighestDegree().equals("PHD")) {
                PHDTotal++;
                if (storage.get(profiles.get(i))) {
                    PHDMatched++;
                }
            }
            if (profiles.get(i).getHighestDegree().equals("Master")) {
                masterTotal++;
                if (storage.get(profiles.get(i))) {
                    masterMatched++;
                }
            }
            if (profiles.get(i).getHighestDegree().equals("Bachelor")) {
                bachelorTotal++;
                if (storage.get(profiles.get(i))) {
                    bachelorMatched++;
                }
            }
        }
        float PHDRate = -1;
        float masterRate = -1;
        float bachelorRate = -1;
        if (PHDTotal != 0) {
            PHDRate = (float) PHDMatched / PHDTotal;
        }
        if (masterTotal != 0) {
            masterRate = (float) masterMatched / masterTotal;
        }
        if (bachelorTotal != 0) {
            bachelorRate = (float) bachelorMatched / bachelorTotal;
        }
        float[] order = { PHDRate, masterRate, bachelorRate };
        orderRate(order);
        for (int i = 0; i < 3; i++) {
            if (PHDRate == order[i] && PHDRate != -1) {
                System.out.printf("PHD: %.2f\n", PHDRate);
            }
            if (masterRate == order[i] && masterRate != -1) {
                System.out.printf("Master: %.2f\n", masterRate);
            }
            if (bachelorRate == order[i] && bachelorRate != -1) {
                System.out.printf("Bachelor: %.2f\n", bachelorRate);
            }
        }
    }

    /**
     * calculate the percentage of the different gender among the matched applicants
     * 
     * @param storage a {@code Map} whose key is the {@code Profile} and the value
     *                is a {@code Boolean}. {@code Boolean} is {@code true} when the
     *                current profile is successfully matched a job
     */
    private void genderPercentage(Map<Profile, Boolean> storage) {
        int maleTotal = 0;
        int maleMatched = 0;
        int femaleTotal = 0;
        int femaleMatched = 0;
        int otherTotal = 0;
        int otherMatched = 0;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getGender().equals("male")) {
                maleTotal++;
                if (storage.get(profiles.get(i))) {
                    maleMatched++;
                }
            }
            if (profiles.get(i).getGender().equals("female")) {
                femaleTotal++;
                if (storage.get(profiles.get(i))) {
                    femaleMatched++;
                }
            }
            if (profiles.get(i).getGender().equals("other")) {
                otherTotal++;
                if (storage.get(profiles.get(i))) {
                    otherMatched++;
                }
            }
        }
        float maleRate = -1;
        float femaleRate = -1;
        float otherRate = -1;
        if (maleTotal != 0) {
            maleRate = (float) maleMatched / maleTotal;
        }
        if (femaleTotal != 0) {
            femaleRate = (float) femaleMatched / femaleTotal;
        }
        if (otherTotal != 0) {
            otherRate = (float) otherMatched / otherTotal;
        }

        float[] order = { maleRate, femaleRate, otherRate };
        orderRate(order);
        for (int i = 0; i < 3; i++) {
            if (maleRate == order[i] && maleRate != -1) {
                System.out.printf("male: %.2f\n", maleRate);
            }
            if (femaleRate == order[i] && femaleRate != -1) {
                System.out.printf("female: %.2f\n", femaleRate);
            }
            if (otherRate == order[i] && otherRate != -1) {
                System.out.printf("other: %.2f\n", otherRate);
            }
        }
    }

    /**
     * auxiliary function when show the result, order the result from highest to
     * lowest
     * 
     * @param order a {@code float[]} needs to be ordered
     */
    private void orderRate(float[] order) {
        for (int i = 0; i < order.length; i++) {
            for (int j = i + 1; j < order.length; j++) {
                if (order[j] > order[i]) {
                    float temp = order[i];
                    order[i] = order[j];
                    order[j] = temp;
                }
            }
        }
    }

}
