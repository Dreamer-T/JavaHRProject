import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TEST {
    public static void main(String[] args) throws ParseException {
        String s = "10/10/209";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date startDate = dateFormat.parse(s);
        System.out.println(dateFormat.format(startDate).toString());

    }
}
