import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Job {
    // basic attribution of the job
    private String title;
    private String description;
    private String degree;
    private String salary;
    private Date startDate;
    private long createdAt;

    /**
     * default constructor, current time is the created time
     * 
     * @param title       a job title {@code String}, can not be {@code null}
     * @param description a description {@code String} of the job
     * @param degree      the degree requirement {@code String} of the job,
     *                    only allows
     *                    {@code PHD},{@code PHD},{@code Master},{@code Bachelor}
     * @param salary      a {@code String} represents the salary, must be able to
     *                    transformed into an {@code Integer}
     * @param startDate   {@code Date} when the job start, can not be {@code null}
     */
    public Job(String title,
            String description,
            String degree,
            String salary,
            Date startDate) {
        createdAt = System.currentTimeMillis() / 1000L;
        this.title = title;
        this.description = description;
        this.degree = degree;
        this.salary = salary;
        this.startDate = startDate;
    }

    /**
     * a kind of copy constructor
     * 
     * @param createdAt   a {@code long} represents unix timestamp
     * @param title       a job title {@code String}, can not be {@code null}
     * @param description a description {@code String} of the job
     * @param degree      the degree requirement {@code String} of the job,
     *                    only allows
     *                    {@code PHD},{@code PHD},{@code Master},{@code Bachelor}
     * @param salary      a {@code String} represents the salary, must be able to
     *                    transformed into an {@code Integer}
     * @param startDate   {@code Date} when the job start, can not be {@code null}
     */
    public Job(long createdAt,
            String title,
            String description,
            String degreee,
            String salary,
            Date startDate) {
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.degree = degreee;
        this.salary = salary;
        this.startDate = startDate;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDegree() {
        return degree;
    }

    /**
     * 
     * @return A {@code Integer} indicate the degree grades.
     *         3 for PHD, 2 for Master, 1 for Bachelor, 0 for nothing
     */
    public int getDegreeNumber() {
        if (degree.equals("PHD")) {
            return 3;
        }
        if (degree.equals("Master")) {
            return 2;
        }
        if (degree.equals("Bachelor")) {
            return 1;
        }
        return 0;
    }

    public String getSalary() {
        return salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * a toString method used when displaying
     * 
     * @return a {@code String} used for display.
     *         No createdAt time and startDate's format is "dd/MM/yyyy",
     *         all empty field will be "n/a"
     */
    public String toDisplayString() {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String description = transformNull(this.description);
        String degree = transformNull(this.degree);
        String salary = transformNull(this.salary);

        return title + " (" + description + "). " + degree + ". Salary: " + salary
                + ". Start Date: " + format.format(startDate);
    }

    /**
     * a toString method used when storing into the file
     * 
     * @return a string used for storing in the file,
     *         with createdAt time and startDate's format is "dd/MM/yy", all
     *         empty field will be empty
     */
    public String toStoreString() {
        DateFormat format = new SimpleDateFormat("dd/MM/yy");
        // two types of string
        // one with comma in the field
        long createAt = System.currentTimeMillis() / 1000L;
        if (description.contains(",")) {
            // if profile has specified the startDate
            return createAt + "," + title + ",\"" + description + "\"," + degree + "," + salary
                    + "," + format.format(startDate);

        }
        return createAt + "," + title + "," + description + "," + degree + "," + salary
                + "," + format.format(startDate);
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
