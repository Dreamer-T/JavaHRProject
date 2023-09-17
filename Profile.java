import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Profile {
    /**
     * 
     * Order the given profiles, it won't influence the original one.
     * After ordering, output the result.
     * 
     * @param profiles an {@code ArrayList} of {@code Profile} waiting for being
     *                 ordered.
     * @param field    which field is used to order
     * @return An ordered {@code ArrayList} of {@code Profile}
     */
    public static ArrayList<Profile> Order(ArrayList<Profile> profiles, String field) {
        // copy a profile arraylist, the copy one will not influence the original one
        ArrayList<Profile> orderedProfiles = new ArrayList<>(profiles);
        ArrayList<Profile> tempOrderedProfiles = new ArrayList<>();
        if (field.equals("availability")) {
            // sort the profiles with availability
            for (int i = 0; i < orderedProfiles.size(); i++) {
                for (int j = i + 1; j < orderedProfiles.size(); j++) {
                    long availability = 0;
                    long minAvailability = 0;
                    // if availability is not null
                    if (orderedProfiles.get(i).getAvailability() != null) {
                        minAvailability = orderedProfiles.get(i).getAvailability().getTime();
                    }
                    if (orderedProfiles.get(j).getAvailability() != null) {
                        availability = orderedProfiles.get(j).getAvailability().getTime();
                    }
                    // if the later one has near availability, put it first
                    if (minAvailability > availability) {
                        Profile tempProfile = new Profile(orderedProfiles.get(j));
                        orderedProfiles.set(j, orderedProfiles.get(i));
                        orderedProfiles.set(i, tempProfile);
                    }
                    // if they have the same availability, order by lastname, firstname
                    else if (minAvailability == availability) {
                        if (orderedProfiles.get(j).getLastname().charAt(0) < orderedProfiles.get(i).getLastname()
                                .charAt(0)) {
                            // those who have smaller alphabet in lastname come first
                            Profile tempProfile = new Profile(orderedProfiles.get(j));
                            orderedProfiles.set(j, orderedProfiles.get(i));
                            orderedProfiles.set(i, tempProfile);
                        }
                        // if they have same lastname alphabet at the very beginning
                        else if (orderedProfiles.get(j).getLastname().charAt(0) == orderedProfiles.get(i)
                                .getLastname()
                                .charAt(0)) {
                            // those who have smaller alphabet in first come first
                            if (orderedProfiles.get(j).getFirstname().charAt(0) < orderedProfiles.get(i).getFirstname()
                                    .charAt(0)) {
                                // those who have smaller alphabet in lastname come first
                                Profile tempProfile = new Profile(orderedProfiles.get(j));
                                orderedProfiles.set(j, orderedProfiles.get(i));
                                orderedProfiles.set(i, tempProfile);
                            }
                        }
                    }
                }
            }
            int originalSize = orderedProfiles.size();
            for (int i = 0; i < originalSize; i++) {
                if (orderedProfiles.get(i).getAvailability() == null) {
                    orderedProfiles.add(orderedProfiles.get(i));
                } else {
                    break;
                }
            }
            tempOrderedProfiles = new ArrayList<>();
            int first = 0;
            for (int i = 0; i < orderedProfiles.size(); i++) {
                if (orderedProfiles.get(i).getAvailability() != null) {
                    first = i;
                    break;
                }
            }
            if (first == 0) {
                first = orderedProfiles.size() / 2;
            }
            for (; first < orderedProfiles.size(); first++) {
                tempOrderedProfiles.add(orderedProfiles.get(first));
            }
        } else {
            tempOrderedProfiles = new ArrayList<>();
            for (int i = 0; i < orderedProfiles.size(); i++) {
                int index = 0;
                for (int j = 0; j < orderedProfiles.size(); j++) {
                    // create float numbers to compare, makes comparsion easier
                    float comparsionContext1 = transformFieldToFloat(orderedProfiles.get(index), field);
                    float comparsionContext2 = transformFieldToFloat(orderedProfiles.get(j), field);
                    // those have higher score will show at first
                    if (comparsionContext1 < comparsionContext2) {
                        index = j;
                    }
                }
                tempOrderedProfiles.add(orderedProfiles.get(index));
                orderedProfiles.remove(index);
                i = 0;
            }
            for (int i = 0; i < orderedProfiles.size(); i++) {
                tempOrderedProfiles.add(orderedProfiles.get(i));
            }
        }
        orderedProfiles = new ArrayList<>(tempOrderedProfiles);
        return orderedProfiles;
    }

    /**
     * 
     * @param profile the chosen {@code Profile}
     * @param field   the chosen field {@code String}, including
     *                createdAt/lastname/degree/wam
     * @return a {@code float} represents the field grades
     */
    private static float transformFieldToFloat(Profile profile, String field) {
        float comparsionContext1 = 0f;
        switch (field) {
            case "createdAt":
                comparsionContext1 = (float) (profile.getCreatedAt());
                break;
            case "lastname":
                // transform char to float
                // 'A' has highest score
                comparsionContext1 = (float) ('Z' - profile.getLastname().toUpperCase().charAt(0));
                break;
            case "degree":
                // transform different degree to float
                String degree = profile.getHighestDegree();
                if (degree.equals("PHD")) {
                    comparsionContext1 = 3f;
                } else if (degree.equals("Master")) {
                    comparsionContext1 = 2f;
                } else if (degree.equals("Bachelor")) {
                    comparsionContext1 = 1f;
                }
                // those who haven't provided degree will be 0
                break;
            case "wam":
                // calculate the wam
                float givenGrades = 0f;
                if (!profile.getCOMP90007().equals("")) {
                    comparsionContext1 += Float.parseFloat(profile.getCOMP90007());
                    givenGrades++;
                }
                if (!profile.getCOMP90038().equals("")) {
                    comparsionContext1 += Float.parseFloat(profile.getCOMP90038());
                    givenGrades++;
                }
                if (!profile.getCOMP90041().equals("")) {
                    comparsionContext1 += Float.parseFloat(profile.getCOMP90041());
                    givenGrades++;
                }
                if (!profile.getINFO90002().equals("")) {
                    comparsionContext1 += Float.parseFloat(profile.getINFO90002());
                    givenGrades++;
                }
                if (givenGrades != 0) {
                    comparsionContext1 = comparsionContext1 / givenGrades;
                }
                // those who haven't provided grades will be 0
                break;
        }
        return comparsionContext1;
    }

    // attributes of the Profile
    private long createdAt;
    private String lastname;
    private String firstname;
    private String careerSummary;

    private int age;
    private String gender;
    private String highestDegree;

    private String COMP90041;

    private String COMP90038;
    private String COMP90007;
    private String INFO90002;
    private String salaryExpectations;

    private Date availability;

    /** empty constructor */
    public Profile() {
    }

    /**
     * copy constuctor
     * 
     * @param profile the {@code Profile} needs to be copyed
     */
    public Profile(Profile profile) {
        this.createdAt = profile.createdAt;
        this.lastname = profile.lastname;
        this.firstname = profile.firstname;
        this.careerSummary = profile.careerSummary;
        this.age = profile.age;
        this.gender = profile.gender;
        this.highestDegree = profile.highestDegree;
        this.COMP90041 = profile.COMP90041;
        this.COMP90038 = profile.COMP90038;
        this.COMP90007 = profile.COMP90007;
        this.INFO90002 = profile.INFO90002;
        this.salaryExpectations = profile.salaryExpectations;
        this.availability = profile.availability;
    }

    /**
     * used when reading applications
     * 
     * @param createdAt          a {@code long} number represents the unix timestamp
     * @param lastname           a {@code String}, not empty
     * @param firstname          a {@code String}, not empty
     * @param careerSummary      a {@code String}, can be empty
     * @param age                a {@code int} represents the age of the applicant
     * @param gender             a {@code String}, can be empty
     * @param highestDegree      a {@code String}, can be empty
     * @param cOMP90041          a {@code String}, can be empty
     * @param cOMP90038          a {@code String}, can be empty
     * @param cOMP90007          a {@code String}, can be empty
     * @param iNFO90002          a {@code String}, can be empty
     * @param salaryExpectations a {@code String}, can be empty
     * @param availability       a {@code Date}, can be empty,
     *                           date format should be "dd/MM/yy"
     */
    public Profile(long createdAt,
            String lastname,
            String firstname,
            String careerSummary,
            int age,
            String gender,
            String highestDegree,
            String cOMP90041, String cOMP90038, String cOMP90007, String iNFO90002,
            String salaryExpectations,
            Date availability) {
        this.createdAt = createdAt;
        this.lastname = lastname;
        this.firstname = firstname;
        this.careerSummary = careerSummary;
        this.age = age;
        this.gender = gender;
        this.highestDegree = highestDegree;
        COMP90041 = cOMP90041;
        COMP90038 = cOMP90038;
        COMP90007 = cOMP90007;
        INFO90002 = iNFO90002;
        this.salaryExpectations = salaryExpectations;
        this.availability = availability;
    }

    /**
     * used when creating applications, createdAt will be the current time
     * 
     * @param lastname           a {@code String}, not empty
     * @param firstname          a {@code String}, not empty
     * @param careerSummary      a {@code String}, can be empty
     * @param age                a {@code int} represents the age of the applicant
     * @param gender             a {@code String}, can be empty
     * @param highestDegree      a {@code String}, can be empty
     * @param cOMP90041          a {@code String}, can be empty
     * @param cOMP90038          a {@code String}, can be empty
     * @param cOMP90007          a {@code String}, can be empty
     * @param iNFO90002          a {@code String}, can be empty
     * @param salaryExpectations a {@code String}, can be empty
     * @param availability       a {@code Date}, can be empty,
     *                           date format should be "dd/MM/yy"
     */
    public Profile(String lastname,
            String firstname,
            String careerSummary,
            int age,
            String gender,
            String highestDegree,
            String cOMP90041, String cOMP90038, String cOMP90007, String iNFO90002,
            String salaryExpectations,
            Date availability) {
        // transfor the currentTimeMillis to unix timestamp
        createdAt = System.currentTimeMillis() / 1000L;
        this.lastname = lastname;
        this.firstname = firstname;
        this.careerSummary = careerSummary;
        this.age = age;
        this.gender = gender;
        this.highestDegree = highestDegree;
        COMP90041 = cOMP90041;
        COMP90038 = cOMP90038;
        COMP90007 = cOMP90007;
        INFO90002 = iNFO90002;
        this.salaryExpectations = salaryExpectations;
        this.availability = availability;
    }

    // Getters
    public long getCreatedAt() {
        return createdAt;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getCareerSummary() {
        return careerSummary;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    /**
     * calculate the degree grade
     * 
     * @return A {@code Integer} indicate the degree.
     *         3 for PHD, 2 for Master, 1 for Bachelor, 0 for nothing
     */
    public int getDegreeNumber() {
        if (highestDegree.equals("PHD")) {
            return 3;
        }
        if (highestDegree.equals("Master")) {
            return 2;
        }
        if (highestDegree.equals("Bachelor")) {
            return 1;
        }
        return 0;
    }

    public String getCOMP90041() {
        return COMP90041;
    }

    public String getCOMP90038() {
        return COMP90038;
    }

    public String getCOMP90007() {
        return COMP90007;
    }

    public String getINFO90002() {
        return INFO90002;
    }

    /**
     * calculate the average grades of the given project grades
     * 
     * @return A {@code float} number, if no grades provided, return 0
     */
    public float getWAM() {
        float totalGrades = 0f;
        float count = 0f;
        if (!getCOMP90007().isEmpty()) {
            count++;
            totalGrades += Integer.parseInt(getCOMP90007());
        }
        if (!getCOMP90038().isEmpty()) {
            count++;
            totalGrades += Integer.parseInt(getCOMP90038());
        }
        if (!getCOMP90041().isEmpty()) {
            count++;
            totalGrades += Integer.parseInt(getCOMP90041());
        }
        if (!getINFO90002().isEmpty()) {
            count++;
            totalGrades += Integer.parseInt(getINFO90002());
        }
        if (count == 0) {
            return 0;
        }
        return totalGrades / count;
    }

    public String getSalaryExpectations() {
        return salaryExpectations;
    }

    public Date getAvailability() {
        return availability;
    }

    /**
     * a toString method used when storing into the file
     * 
     * @return a string used for storing in the file,
     *         with createdAt time and availability's format is "dd/mm/yy", all
     *         empty field will be empty
     */
    public String toStoreString() {
        DateFormat format = new SimpleDateFormat("dd/MM/yy");
        // two types of string
        // one with comma in the field
        if (careerSummary.contains(",")) {
            // if profile has not specified the availability
            if (availability == null) {
                return createdAt + "," + lastname + "," + firstname
                        + ",\"" + careerSummary + "\"," + age + "," + gender + ","
                        + highestDegree + "," + COMP90041 + "," + COMP90038 + "," + COMP90007
                        + "," + INFO90002 + "," + salaryExpectations + ",";
            } else {
                // if profile has specified the availability
                return createdAt + "," + lastname + "," + firstname
                        + ",\"" + careerSummary + "\"," + age + "," + gender + ","
                        + highestDegree + "," + COMP90041 + "," + COMP90038 + "," + COMP90007
                        + "," + INFO90002 + "," + salaryExpectations + "," + format.format(availability);
            }
        }
        if (availability == null) {
            // if profile has not specified the availability
            return createdAt + "," + lastname + "," + firstname
                    + "," + careerSummary + "," + age + "," + gender + ","
                    + highestDegree + "," + COMP90041 + "," + COMP90038 + "," + COMP90007
                    + "," + INFO90002 + "," + salaryExpectations + ",";
        }
        // if profile has specified the availability
        return createdAt + "," + lastname + "," + firstname
                + "," + careerSummary + "," + age + "," + gender + ","
                + highestDegree + "," + COMP90041 + "," + COMP90038 + "," + COMP90007
                + "," + INFO90002 + "," + salaryExpectations + ","
                + format.format(availability);

    }

    /**
     * a toString method used when displaying
     * 
     * @return a {@code String} used for display.
     *         No createdAt time and availability's format is "dd/mm/yyyy",
     *         all empty field will be "n/a"
     */
    public String toDisplayString() {
        String lastname = transformNull(this.lastname);
        String firstname = transformNull(this.firstname);
        String highestDegree = transformNull(this.highestDegree);
        String careerSummary = transformNull(this.careerSummary);
        String salaryExpectations = transformNull(this.salaryExpectations);

        String availability;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // if availablity is null then n/a
        if (this.availability == null) {
            availability = "n/a";
        } else {
            availability = transformNull(dateFormat.format(this.availability).toString());
        }
        return lastname + ", " + firstname + " (" + highestDegree + "): " + careerSummary + ". Salary Expectations: "
                + salaryExpectations + ". Available: " + availability + "\n";
        // <Lastname>, <Firstname> (<Highest Degree>): <Career Summary>. Salary
        // Expectations: <Salary Expectations>. Available: <Availability>.
    }

    /**
     * auxiliary function when using toDisplayString, transformed all empty
     * {@code String} into "n/a"
     * 
     * @param s a {@code String} need to be transfromed
     * @return itself if {@code String} is not empty or {@code n/a} if
     *         {@code String} is empty
     */
    private String transformNull(String s) {
        if (s.equals("")) {
            return "n/a";
        }
        return s;
    }
}
